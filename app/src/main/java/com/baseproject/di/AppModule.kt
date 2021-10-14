package com.baseproject.di

import android.content.Context
import android.content.res.Resources
import com.baseproject.BuildConfig
import com.baseproject.data.room.BaseRoomDatabase
import com.baseproject.utils.TIME_OUT
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context): BaseRoomDatabase {
        return BaseRoomDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {

        val client = OkHttpClient.Builder()
            .apply {
                connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                readTimeout(TIME_OUT, TimeUnit.SECONDS)
                writeTimeout(TIME_OUT, TimeUnit.SECONDS)
                if (BuildConfig.DEBUG) addInterceptor(
                    HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                )
            }
            .build()

        return Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }

    @Provides
    @Singleton
    fun provideResources(@ApplicationContext context: Context): Resources {
        return context.resources
    }

}