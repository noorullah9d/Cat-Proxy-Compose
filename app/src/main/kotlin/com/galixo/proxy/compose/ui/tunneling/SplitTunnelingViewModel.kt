package com.galixo.proxy.compose.ui.tunneling

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galixo.proxy.compose.domain.model.AppInfo
import com.galixo.proxy.compose.domain.repository.AppsRepository
import com.galixo.proxy.compose.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplitTunnelingViewModel @Inject constructor(
    private val appsRepository: AppsRepository
) : ViewModel() {
    private val _appsFlow = MutableStateFlow<Result<ArrayList<AppInfo>>?>(null)
    val appsFlow: StateFlow<Result<ArrayList<AppInfo>>?> = _appsFlow

    private var allApps: ArrayList<AppInfo> = arrayListOf()

    init {
        getApps()
    }

    private fun getApps() = viewModelScope.launch {
        appsRepository.getAppsList().collect { result ->
            if (result is Result.Success) {
                allApps = result.data
            }
            _appsFlow.value = result
        }
    }

    fun filterApps(query: String) {
        val filteredApps = if (query.isEmpty()) {
            allApps
        } else {
            allApps.filter {
                it.name.contains(query, ignoreCase = true) == true
            } as ArrayList<AppInfo>
        }
        _appsFlow.value = Result.Success(filteredApps)
    }

    fun resetApps() {
        _appsFlow.value = Result.Success(allApps)
    }

    fun updateAppState(app: AppInfo) {
        val currentApps = (_appsFlow.value as? Result.Success)?.data
        currentApps?.let { apps ->
            val updatedApps = apps.map {
                if (it.packageName == app.packageName) app else it
            }
            _appsFlow.value = Result.Success(ArrayList(updatedApps))
        }
    }
}