package com.sliide.challenge.network.di

import com.sliide.challenge.network.services.AuthorizedUsersService
import com.sliide.challenge.network.services.UnauthorizedUsersService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

private const val CONNECT_TIMEOUT = 10L
private const val READ_TIMEOUT = 10L
private const val WRITE_TIMEOUT = 10L
private const val BASE_URL = "https://gorest.co.in/public/v1/"

@Module
class NetworkModule {

    @Named(AuthorizedUsersService.TAG)
    @Provides
    fun provideOkHttpClientAuthorizedUsersService(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
            .addInterceptor( Interceptor { chain ->
                val request = chain.request()
                val modifiedRequest = request.newBuilder()
                    .addHeader("Authorization", "Bearer 0439b99737639dac5d8c0353eb7674b33a91d649414da0fe5df957a43a3116c9")
                    .build()
                chain.proceed(modifiedRequest)
            })
            .build()
    }

    @Named(UnauthorizedUsersService.TAG)
    @Provides
    fun provideOkHttpClientUnauthorizedUsersService(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) })
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshiConverter(): MoshiConverterFactory {
        val moshi = Moshi.Builder().build()
        return MoshiConverterFactory.create(moshi)
    }

    @Singleton
    @Provides
    @Named(AuthorizedUsersService.TAG)
    fun provideRetrofitAuthorizedUsersService(
        @Named(AuthorizedUsersService.TAG) httpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(moshiConverterFactory)
            .baseUrl(BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    @Named(UnauthorizedUsersService.TAG)
    fun provideRetrofitUnauthorizedUsersService(
        @Named(UnauthorizedUsersService.TAG) httpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .client(httpClient)
            .addConverterFactory(moshiConverterFactory)
            .baseUrl(BASE_URL)
            .build()
    }

    @Singleton
    @Provides
    fun provideUnauthorizedUsersService(@Named(UnauthorizedUsersService.TAG) retrofit: Retrofit) : UnauthorizedUsersService {
        return retrofit.create(UnauthorizedUsersService::class.java)
    }

    @Singleton
    @Provides
    fun provideAuthorizedUsersService(@Named(AuthorizedUsersService.TAG) retrofit: Retrofit) : AuthorizedUsersService {
        return retrofit.create(AuthorizedUsersService::class.java)
    }
}