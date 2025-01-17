package com.github.shadowsocks.bg

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import com.github.shadowsocks.Core
import com.github.shadowsocks.extensions.formatTime
import com.github.shadowsocks.preference.PrefUtils
import timber.log.Timber

class TimerListenerImpl : TimerListener {
    private var handler: Handler? = null
    private val interval: Long = 1000L
    private var startTime: Long = 0L
    private var runnable: Runnable? = null

    override fun startListening(
        time: (time: String) -> Unit,
        onTimeEnded: () -> Unit
    ) {
        Timber.d("TimerListener: isUserPremium = ${PrefUtils.isAdsRemoved}")
        handler = Handler(Looper.getMainLooper())

        if (PrefUtils.isAdsRemoved) {
            // If the user is premium, run the timer infinitely
            startTime = SystemClock.elapsedRealtime()
            runnable = object : Runnable {
                override fun run() {
                    measureTime(time)
                    handler?.postDelayed(this, interval)
                }
            }
            handler?.post(runnable!!)
        } else {
            // Check and reset the free VPN time if it has been consumed
            if (PrefUtils.freeVpnTime <= 0L) {
                Timber.d("TimerListener: free time over")
//                PrefUtils.freeVpnTime = FREE_VPN_TIME_VALUE
            }

            // Set the start time based on elapsed real-time
            startTime = SystemClock.elapsedRealtime()

            // For non-premium users, start with the full time and count down
            runnable = object : Runnable {
                override fun run() {
                    val elapsedRealtime = SystemClock.elapsedRealtime() - startTime
                    val remainingTime = PrefUtils.freeVpnTime - elapsedRealtime

                    if (remainingTime <= 0L) {
                        onTimeEnded() // Stop measuring when time runs out
                        handler?.removeCallbacks(this)
//                        PrefUtils.freeVpnTime = FREE_VPN_TIME_VALUE
                        return
                    }
                    measureRemainingTime(time, remainingTime)
                    startTime = SystemClock.elapsedRealtime()
                    handler?.postDelayed(this, interval)
                }
            }
            handler?.post(runnable!!)
        }
    }

    private fun measureRemainingTime(timePassed: (time: String) -> Unit, remainingTime: Long) {
        BaseService.elapsedTime += interval
        timePassed(formatTime(remainingTime))
        // Persist the remaining time in shared preferences
        PrefUtils.freeVpnTime = remainingTime
    }

    private fun measureTime(timePassed: (time: String) -> Unit) {
        if (!PrefUtils.isAdsRemoved) {
            Core.stopService()
        }
        BaseService.elapsedTime += interval
        timePassed(formatTime(BaseService.elapsedTime))
    }

    override fun stopListening() {
        Timber.d("TimerListener: Stop listening")
        handler?.removeCallbacks(runnable!!)
        handler = null
        runnable = null
    }
}
