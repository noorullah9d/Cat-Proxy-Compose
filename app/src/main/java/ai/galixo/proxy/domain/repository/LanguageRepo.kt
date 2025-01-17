package ai.galixo.proxy.domain.repository

import com.github.shadowsocks.preference.LanguageModel
import kotlinx.coroutines.flow.Flow
import ai.galixo.proxy.utils.Result

interface LanguageRepo {
    suspend fun getLanguageList(): Flow<Result<List<LanguageModel>>>
    fun getDefaultLanguage(): LanguageModel
}