package jp.developer.bbee.englishmemory.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.developer.bbee.englishmemory.BuildConfig.BASE_URL
import jp.developer.bbee.englishmemory.data.repository.TranslateRepositoryImpl
import jp.developer.bbee.englishmemory.data.source.remote.api.AwsApi
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideAwsApi(): AwsApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                )
            ).build().create(AwsApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTranslateRepository(awsApi: AwsApi): TranslateRepository {
        return TranslateRepositoryImpl(awsApi)
    }
}