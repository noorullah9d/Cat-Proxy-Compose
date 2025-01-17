package com.github.shadowsocks.bg

interface TimerListener {
    fun startListening(
        time: (time: String) -> Unit,
        onTimeEnded: () -> Unit
    )

    fun stopListening()
}