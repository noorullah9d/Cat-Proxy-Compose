package com.galixo.proxy.service

import android.app.Service

interface ServiceControl {
    fun getService(): Service

    fun startService()

    fun stopService()

    fun vpnProtect(socket: Int): Boolean
}
