package uz.sher.currency.di

import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.sher.currency.data.local.shared.SharedInterface
import uz.sher.currency.data.local.shared.SharedRepository
import uz.sher.currency.data.remote.ApiService
import uz.sher.currency.data.remote.ConvertRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideBaseUrl(): String = "https://cbu.uz"

    @Provides
    @Singleton
    fun provideAppUpdateManager(@ApplicationContext context: Context): AppUpdateManager =
        AppUpdateManagerFactory.create(context)


    @Provides
    @Singleton
    fun provideSharedInterface(@ApplicationContext context: Context): SharedInterface =
        SharedRepository(context)


    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient().newBuilder().addInterceptor(loggingInterceptor).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, client: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(
                GsonConverterFactory
                    .create()
            ).client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)


    @Provides
    @Singleton
    fun provideRepository(
        sharedInterface: SharedInterface,
        apiService: ApiService
    ): ConvertRepository {
        return ConvertRepository(sharedInterface = sharedInterface, apiService = apiService)
    }


}