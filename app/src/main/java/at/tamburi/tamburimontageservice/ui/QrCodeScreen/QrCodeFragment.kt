package at.tamburi.tamburimontageservice.ui.QrCodeScreen

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import at.tamburi.tamburimontageservice.R
import at.tamburi.tamburimontageservice.ui.ViewModels.MontageWorkflowViewModel
import at.tamburi.tamburimontageservice.ui.ViewModels.QrCodeScannerState
import at.tamburi.tamburimontageservice.ui.theme.TamburiMontageServiceTheme
import at.tamburi.tamburimontageservice.utils.Constants

private const val TAG = "QrCodeFragment"

@RequiresApi(Build.VERSION_CODES.M)
class QrCodeFragment : Fragment() {
    val viewModel: MontageWorkflowViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                TamburiMontageServiceTheme {
                    Surface(color = MaterialTheme.colors.background) {
                        var code by remember {
                            mutableStateOf("")
                        }
                        val context = LocalContext.current
                        val lifecycleOwner = LocalLifecycleOwner.current
                        val cameraProviderFuture = remember {
                            ProcessCameraProvider.getInstance(context)
                        }
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            AndroidView(
                                modifier = Modifier.weight(1f),
                                factory = { context ->
                                    val previewView = PreviewView(context)
                                    val preview = Preview.Builder().build()
                                    val selector = CameraSelector.Builder()
                                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                                        .build()
                                    preview.setSurfaceProvider(previewView.surfaceProvider)
                                    val imageAnalysis = ImageAnalysis.Builder()
                                        .setTargetResolution(
                                            Size(
                                                previewView.width,
                                                previewView.height
                                            )
                                        )
                                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                        .build()
                                    imageAnalysis.setAnalyzer(
                                        ContextCompat.getMainExecutor(context),
                                        QrCodeAnalyzer { result ->
                                            code = result
                                        }
                                    )
                                    try {
                                        cameraProviderFuture.get().bindToLifecycle(
                                            lifecycleOwner,
                                            selector,
                                            preview,
                                            imageAnalysis
                                        )
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                    previewView
                                }
                            )
                            //TODO: Refactor Composables to avoid Warnings
                            when (viewModel.qrCodeScannerState) {
                                QrCodeScannerState.Location -> if (code.isNotEmpty()) {
                                    locationFormatter(code = code)
                                } else {
                                    ScannerText(stringResource(R.string.qrs_scan_location_text))
                                }
                                QrCodeScannerState.Locker -> if (code.isNotEmpty()) {
                                    lockerFormatter(code = code)
                                } else {
                                    ScannerText(stringResource(R.string.qrs_scan_locker_text))
                                }
                                QrCodeScannerState.Gateway -> if (code.isNotEmpty()) {
                                    gatewayFormatter(code = code)
                                } else {
                                    ScannerText(stringResource(R.string.qrs_scan_gateway_text))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ScannerText(t: String) {
        Text(
            text = t,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        )
    }

    @Composable
    fun lockerFormatter(code: String) {
        if (viewModel.activeLocker == null) {
            findNavController(this).navigate(R.id.action_qr_code_fragment_to_landing_fragment)
            Toast.makeText(
                requireContext(),
                "No active Locker found",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            if (checkQrCodeForLocker(code)) {
                Log.d(TAG, viewModel.gatewaySerialnumber.toString())
                viewModel.setDataForLocker(
                    lifecycle,
                    viewModel.activeLocker?.lockerId!!,
                    viewModel.gatewaySerialnumber.first(),
                    code,
                    findNavController(this)
                )
            } else {
                Text(
                    text = "Locker QR-Code ungültig",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                )
            }
        }
    }

    @Composable
    private fun gatewayFormatter(code: String) {
        if (viewModel.activeLocker == null) {
            findNavController(this).navigate(R.id.action_qr_code_fragment_to_landing_fragment)
            Toast.makeText(
                requireContext(),
                "No active Locker found",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            // TODO: Validation for gateway Qr-Codes
            // It should not be possible to scan a locker qr code for a gateway like in dev test
            viewModel.setGatewayForLocker(
                lifecycle,
                viewModel.activeLocker?.lockerId!!,
                code,
                findNavController(this)
            )
        }
    }

    @Composable
    private fun locationFormatter(code: String) {
        val id = cutUrlForLocationId(code)
        if (id.isEmpty()) {
            Text(
                text = "Location QR-Code ungültig",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp)
            )
        } else {

            viewModel.setLocationQrCode(
                lifecycle, viewModel.task.value?.location?.locationId!!, code, this.findNavController()
            )
        }
    }

    private fun cutUrlForLocationId(code: String): String {
        //TODO: Errorhandling
        return try {
            val uri = Uri.parse(code)
            Log.v(TAG, "Queryparam: ${uri.getQueryParameter("qr")}")
            Log.v(TAG, "authority: ${uri.authority}")
            Log.v(TAG, "Path: ${uri.path}")
            if (uri.authority != Constants.LOCATION_SCANNER_FLAG) "" else uri.getQueryParameter("qr")
                ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    private fun checkQrCodeForLocker(code: String): Boolean {
        return try {
            code.toLong()
            code.length == 15
        } catch (e: Exception) {
            false
        }
    }
}