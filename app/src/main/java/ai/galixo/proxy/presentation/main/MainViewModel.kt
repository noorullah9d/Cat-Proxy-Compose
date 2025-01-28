package ai.galixo.proxy.presentation.main

import ai.galixo.proxy.domain.repository.RemoteConfigRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val remoteConfigRepository: RemoteConfigRepository
) : ViewModel() {

    fun getRemoteConfig() = viewModelScope.launch(Dispatchers.IO) {
        remoteConfigRepository.getRemoteResponse()
    }
}