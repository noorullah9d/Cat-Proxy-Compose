package ai.galixo.proxy.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import timber.log.Timber

@Composable
fun CountdownTimer(
    waitingTime: Long,
    interval: Long = 1000L,
    isCancelled: Boolean,
    onTick: (Long) -> Unit,
    onFinish: () -> Unit
) {
    var remainingTime by remember { mutableLongStateOf(waitingTime) }
    val isCancelledState = rememberUpdatedState(isCancelled) // Track cancellation changes

    LaunchedEffect(isCancelledState.value) {
        if (isCancelledState.value) {
            Timber.d("Timer Cancelled: Stopping Immediately")
            return@LaunchedEffect
        }

        while (remainingTime > 0) {
            if (isCancelledState.value) {
                Timber.d("Timer Cancelled: Stopping at $remainingTime ms")
                return@LaunchedEffect // Exit immediately
            }

            delay(interval)
            remainingTime -= interval

            if (!isCancelledState.value) {
                onTick(remainingTime)
            } else {
                Timber.d("Timer Cancelled: Exiting on Tick at $remainingTime ms")
                return@LaunchedEffect
            }
        }

        if (!isCancelledState.value) {
            Timber.d("Timer Finished!")
            onFinish()
        }
    }
}