package jp.developer.bbee.englishmemory.domain.repository

import jp.developer.bbee.englishmemory.data.source.remote.dto.TranslateDataDto

interface TranslateRepository {
    suspend fun getTranslateData(): TranslateDataDto
}