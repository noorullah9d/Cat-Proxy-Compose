package com.galixo.proxy.compose.domain.model

import android.graphics.drawable.Drawable
import androidx.annotation.Keep

@Keep
data class AppInfo(
    var packageName: String = "",
    var name: String = "",
    var appIcon: Drawable? = null,
    var isAppHidden: Boolean = false
)
