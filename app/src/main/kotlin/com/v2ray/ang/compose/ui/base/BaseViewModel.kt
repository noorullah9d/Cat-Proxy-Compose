package com.v2ray.ang.compose.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import com.v2ray.ang.compose.utils.Result

abstract class BaseViewModel<U> : ViewModel() {

    private val _uiState: MutableSharedFlow<UiState<U>> =
        MutableSharedFlow(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
        )
    val uiState get() = _uiState.asSharedFlow()

    open fun getUiState() = viewModelScope.launch {}

    protected suspend fun updateUi(uiState: U) =
        _uiState.emit(UiState(data = uiState))

    protected suspend fun <D> collectFlow(
        flow: Flow<Result<D>>,
        onLoading: (suspend (() -> Unit))? = null,
        onError: (suspend ((error: Throwable) -> Unit))? = null,
        onSuccess: suspend ((data: D) -> Unit),
    ) =
        flow
            .onStart {
                when (onLoading) {
                    null -> _uiState.emit(UiState(loading = true))
                    else -> onLoading.invoke()
                }
            }
            .catch { e ->
                when (onError) {
                    null -> _uiState.emit(UiState(error = e))
                    else -> onError.invoke(e)
                }
            }
            .collect { output ->
                when (output) {
                    is Result.Error -> {
                        when (onError) {
                            null -> _uiState.emit(UiState(error = Throwable(output.message)))
                            else -> onError.invoke(Throwable(output.message))
                        }
                    }

                    is Result.Success -> {
                        onSuccess.invoke(output.data)
                    }

                    is Result.Loading -> {
                        _uiState.emit(UiState(loading = true))
                    }
                }
            }
}