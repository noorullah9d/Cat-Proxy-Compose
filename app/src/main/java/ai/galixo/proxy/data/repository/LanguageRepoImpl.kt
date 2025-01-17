package ai.galixo.proxy.data.repository

import ai.galixo.proxy.R
import ai.galixo.proxy.domain.repository.LanguageRepo
import ai.galixo.proxy.utils.languagesList
import com.github.shadowsocks.preference.LanguageModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import ai.galixo.proxy.utils.Result

class LanguageRepoImpl @Inject constructor() : LanguageRepo {
    override suspend fun getLanguageList() = callbackFlow {
        trySend(Result.Loading(""))
        trySend(Result.success(languagesList()))
        awaitClose()
    }

    override fun getDefaultLanguage(): LanguageModel {
        return LanguageModel("English", "English", "en", R.drawable.en)
    }
}