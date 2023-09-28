package jp.developer.bbee.englishmemory.data.repository

import jp.developer.bbee.englishmemory.data.source.remote.api.AwsApi
import jp.developer.bbee.englishmemory.data.source.remote.dto.TranslateDataDto
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository

class TranslateRepositoryImpl(
    private val awsApi: AwsApi
) : TranslateRepository {
    override suspend fun getTranslateData(): TranslateDataDto {
        return awsApi.getTranslateData()
    }
}