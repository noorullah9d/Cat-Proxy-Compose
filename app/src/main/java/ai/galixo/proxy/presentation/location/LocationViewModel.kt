package ai.galixo.proxy.presentation.location

import ai.galixo.proxy.domain.model.LocationData
import ai.galixo.proxy.domain.repository.LocationRepository
import ai.galixo.proxy.utils.Result
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _locationData = MutableStateFlow<Result<LocationData>>(Result.Loading("Loading..."))
    val locationData: StateFlow<Result<LocationData>> get() = _locationData

    fun fetchLocationData() {
        viewModelScope.launch {
            val result = locationRepository.fetchLocationInfo()
            Timber.d("LocationData: viewmodel result= $result")
            when (result) {
                is Result.Loading -> {
                    _locationData.value = Result.Loading("Loading...")
                }
                is Result.Success -> {
                    _locationData.value = result
                }
                is Result.Error -> {
                    _locationData.value = result
                }
            }
        }
    }
}
