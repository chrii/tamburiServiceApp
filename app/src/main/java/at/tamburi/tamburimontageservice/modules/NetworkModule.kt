package at.tamburi.tamburimontageservice.modules

import at.tamburi.tamburimontageservice.repositories.network.IAuthenticationRepository
import at.tamburi.tamburimontageservice.repositories.network.INetworkMontageTaskRepository
import at.tamburi.tamburimontageservice.repositories.network.implementation.AuthenticationRepositoryImpl
import at.tamburi.tamburimontageservice.repositories.network.implementation.NetworkMontageTaskRepositoryImpl
import at.tamburi.tamburimontageservice.services.network.services.IAuthenticationService
import at.tamburi.tamburimontageservice.services.network.services.INetworkMontageTaskService
import at.tamburi.tamburimontageservice.utils.Constants
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    private val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.API_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
    }

    @Provides
    @Singleton
    fun authService(): IAuthenticationService =
        retrofitBuilder.build().create(IAuthenticationService::class.java)

    @Provides
    @Singleton
    fun provideAuthRepository(service: IAuthenticationService): IAuthenticationRepository =
        AuthenticationRepositoryImpl(service)

    @Provides
    @Singleton
    fun montageTaskService() =
        retrofitBuilder.build().create(INetworkMontageTaskService::class.java)

    @Provides
    @Singleton
    fun provideMontageTaskRepository(serviceNetwork: INetworkMontageTaskService): INetworkMontageTaskRepository =
        NetworkMontageTaskRepositoryImpl(serviceNetwork)
}