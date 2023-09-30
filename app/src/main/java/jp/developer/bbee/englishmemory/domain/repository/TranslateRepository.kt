package jp.developer.bbee.englishmemory.domain.repository

import jp.developer.bbee.englishmemory.data.source.remote.dto.TranslateDataDto
import jp.developer.bbee.englishmemory.domain.model.TranslateData

interface TranslateRepository {
    suspend fun getTranslateData(token: String): List<TranslateData>
    suspend fun getTranslateData(): List<TranslateData>
    suspend fun saveTranslateData(translateDataList: List<TranslateData>)
}