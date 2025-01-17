package com.github.shadowsocks.extensions

import java.util.Locale

private val kb = 1024
private val mb = kb * 1024
private val gb = mb * 1024

fun formatSpeed(value: Long, inBits: Boolean = false): String {
    val speed =
        if (inBits) value.toDouble() * 8 else value.toDouble() // Convert to bits if necessary
    return when {
        speed < kb -> String.format(
            Locale.ENGLISH,
            "%.0f " + (if (inBits) "b" else "B"),
            speed
        )

        speed < mb -> String.format(
            Locale.ENGLISH,
            "%.0f K" + (if (inBits) "b" else "B"),
            speed / kb
        )

        speed < gb -> String.format(
            Locale.ENGLISH,
            "%.0f M" + (if (inBits) "b" else "B"),
            speed / mb
        )

        else -> String.format(
            Locale.ENGLISH,
            "%.0f G" + (if (inBits) "b" else "B"),
            speed / gb
        )
    }
}


fun formatTime(elapsedTime: Long): String {
    val hours = elapsedTime / 3600000
    val minutes = (elapsedTime % 3600000) / 60000
    val seconds = ((elapsedTime % 3600000) % 60000) / 1000
    return String.format(Locale.ENGLISH, "%02d : %02d : %02d", hours, minutes, seconds)
}

fun Long.formatToTime(): String {
    val seconds = this / 1000
    val minutes = seconds / 60
    val hours = minutes / 60

    return when {
        hours > 0 -> "$hours hr ${minutes % 60} min"
        minutes > 0 -> "$minutes min ${seconds % 60} sec"
        else -> "$seconds sec"
    }
}
