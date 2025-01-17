package com.github.shadowsocks.plugin

import com.github.shadowsocks.Core.app
import com.github.shadowsocks.R

object NoPlugin : Plugin() {
    override val id: String get() = ""
    override val label: CharSequence get() = app.getText(R.string.plugin_disabled)
}
