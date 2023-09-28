package jp.developer.bbee.englishmemory.data.repository

import jp.developer.bbee.englishmemory.data.source.remote.api.AwsApi
import jp.developer.bbee.englishmemory.data.source.remote.dto.TranslateDataDto
import jp.developer.bbee.englishmemory.domain.repository.TranslateRepository
import javax.inject.Inject

class TranslateRepositoryImpl @Inject constructor(
    private val awsApi: AwsApi
) : TranslateRepository {
    override suspend fun getTranslateData(token: String): TranslateDataDto {
        return awsApi.getTranslateData(token)
    }
}