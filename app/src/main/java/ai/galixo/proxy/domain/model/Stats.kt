package ai.galixo.proxy.domain.model

import androidx.annotation.Keep

@Keep
data class Stats(
    val connectionTime: String = "",
    val uploadSpeed: String = "",
    val downloadSpeed: String = ""
)