package ai.galixo.proxy.presentation.splash

import ai.galixo.proxy.domain.repository.ServersRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ai.galixo.proxy.R
import com.github.shadowsocks.preference.LanguageModel
import com.github.shadowsocks.preference.PrefUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val serversRepository: ServersRepository,
//    private val remoteConfigRepository: RemoteConfigRepository
) : ViewModel() {

    init {
        getServers()
    }

    private fun getServers() = viewModelScope.launch(Dispatchers.IO) {
        serversRepository.getServers(hardRefresh = true)
    }

    fun isOnboardingShown(): Boolean {
        return PrefUtils.isOnboardingShown
    }

    fun isLanguageShown(): Boolean {
        return PrefUtils.isLanguagesShown
    }

    fun getDefaultLanguage(): LanguageModel {
        return LanguageModel("English", "English", "en", R.drawable.en)
    }

    fun getRemoteConfig() = viewModelScope.launch(Dispatchers.IO) {
//        remoteConfigRepository.getRemoteResponse()
    }
}