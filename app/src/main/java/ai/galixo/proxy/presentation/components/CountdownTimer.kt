package ai.galixo.proxy.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@Composable
fun CountdownTimer(
    waitingTime: Long = 3000,
    interval: Long = 1000,
    onTick: (timeUntilFinish: Long) -> Unit,
    onFinish: () -> Unit
) = LaunchedEffect(waitingTime, interval) {
    var remainingTime = waitingTime
    while (remainingTime > 0) {
        onTick(remainingTime)
        delay(interval)
        remainingTime -= interval
    }
    onFinish()
}