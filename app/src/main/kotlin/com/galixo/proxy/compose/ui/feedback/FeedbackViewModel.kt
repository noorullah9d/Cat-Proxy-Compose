package com.galixo.proxy.compose.ui.feedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galixo.proxy.compose.domain.repository.FeedbackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val feedbackRepository: FeedbackRepository
) : ViewModel() {

    fun sendFeedback(message: String, callback: (Boolean) -> Unit) = viewModelScope.launch {
        feedbackRepository.sendFeedBack(message) {
            callback.invoke(it)
        }
    }
}