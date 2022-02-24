package at.tamburi.tamburimontageservice.modules

import at.tamburi.tamburimontageservice.repositories.network.IAuthenticationRepository
import at.tamburi.tamburimontageservice.repositories.network.implementation.AuthenticationRepositoryImpl
import at.tamburi.tamburimontageservice.services.network.services.AuthenticationService
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
    fun authService(): AuthenticationService =
        retrofitBuilder.build().create(AuthenticationService::class.java)

    @Provides
    @Singleton
    fun provideAuthRepository(service: AuthenticationService): IAuthenticationRepository =
        AuthenticationRepositoryImpl(service)
}