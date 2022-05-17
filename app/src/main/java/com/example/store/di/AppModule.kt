package com.example.store.di

import android.content.Context
import com.example.store.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    private val baseUrl: String = BuildConfig.apiUrl

    @Provides
    @Singleton
    fun providesRetrofit(
        @ApplicationContext context: Context
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(createClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .validateEagerly(BuildConfig.DEBUG)
            .build()
    }

    private fun createClient(context: Context): OkHttpClient {
        val cacheSize = 20 * 1024 * 1024L // 20 MiB
        val cache = Cache(
            context.getDir(
                "network_cache",
                Context.MODE_PRIVATE
            ),
            cacheSize
        )
        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }

        return OkHttpClient.Builder()
            .cache(cache)
            .connectTimeout(
                20,
                TimeUnit.SECONDS
            )
            .readTimeout(
                20,
                TimeUnit.SECONDS
            )
            .writeTimeout(
                20,
                TimeUnit.SECONDS
            )
            .addInterceptor(logging)
            .build()
    }
}
