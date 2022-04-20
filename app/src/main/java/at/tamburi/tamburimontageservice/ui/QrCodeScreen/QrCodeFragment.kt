package at.tamburi.tamburimontageservice.ui.QrCodeScreen

import android.os.Build
import android.os.Bundle
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
                        if (viewModel.activeLocker == null) {
                            Toast.makeText(
                                requireContext(),
                                "No active Locker found",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(R.id.action_qr_code_fragment_to_landing_fragment)
                        }
                        val locker = viewModel.activeLocker!!
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
//                                    locationFormatter(code = code)
                                    if (viewModel.checkLocationQrCode(code)) {
                                        viewModel.submitLocationQrCode(
                                            lifecycle,
                                            context,
                                            findNavController(),
                                            code,
                                            locker.locationId
                                        )
                                    } else {
                                        ScannerText(t = stringResource(id = R.string.qrs_scan_error))
                                    }
                                } else {
                                    ScannerText(stringResource(R.string.qrs_scan_location_text))
                                }
                                QrCodeScannerState.Locker -> if (code.isNotEmpty()) {
                                    if (viewModel.checkQrCodeForLocker(code)) {
                                        try {
                                            val safeTask = viewModel.task.value
                                                ?: throw Exception("setQrCode - Error getting Task")
                                            val gatewaySerial = safeTask.lockerList
                                                .map { it.gatewaySerialnumber }
                                                .find { it.isNotEmpty() }
                                                ?: throw Exception("Enter gateway Serial first")
                                            viewModel.setQrCodeForLocker(
                                                lifecycle,
                                                locker.lockerId,
                                                code,
                                                null
                                            )
                                            viewModel.setGatewayForLocker(
                                                lifecycle,
                                                locker.lockerId,
                                                gatewaySerial,
                                                findNavController()
                                            )
                                        } catch (e: Exception) {
                                            findNavController().navigate(R.id.action_qr_code_fragment_to_landing_fragment)
                                            Toast.makeText(
                                                context,
                                                e.message,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } else {
                                        ScannerText(stringResource(id = R.string.qrs_scan_error))
                                    }
                                } else {
                                    ScannerText(stringResource(R.string.qrs_scan_locker_text))
                                }
                                QrCodeScannerState.Gateway -> if (code.isNotEmpty()) {
                                    //TODO Gateway Scanner validation
                                    if (viewModel.checkGatewaySerial(code)) {
                                        viewModel.setGatewayForLocker(
                                            lifecycle,
                                            locker.lockerId,
                                            code,
                                            findNavController()
                                        )
                                    } else {
                                        ScannerText(stringResource(id = R.string.qrs_scan_error))
                                    }
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
    private fun gatewayFormatter(code: String) {
        if (viewModel.activeLocker == null) {
            findNavController(this).navigate(R.id.action_qr_code_fragment_to_landing_fragment)
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
        val id = viewModel.cutUrlForLocationId(code)
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
                lifecycle,
                viewModel.task.value?.location?.locationId!!,
                code,
                this.findNavController()
            )
        }
    }
}