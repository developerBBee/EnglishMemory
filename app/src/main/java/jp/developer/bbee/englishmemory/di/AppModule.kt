package jp.developer.bbee.englishmemory.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jp.developer.bbee.englishmemory.BuildConfig.BASE_URL
import jp.developer.bbee.englishmemory.data.repository.TranslateRepositoryImpl
import jp.developer.bbee.englishmemory.data.source.local.EnglishMemoryDao
import jp.developer.bbee.englishmemory.data.source.local.EnglishMemoryDatabase
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
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context, EnglishMemoryDatabase::class.java, "english_memory_database"
    ).build()

    @Provides
    @Singleton
    fun provideDao(db: EnglishMemoryDatabase) = db.getEnglishMemoryDao()

    @Provides
    @Singleton
    fun provideTranslateRepository(awsApi: AwsApi, dao: EnglishMemoryDao): TranslateRepository {
        return TranslateRepositoryImpl(awsApi, dao)
    }
}