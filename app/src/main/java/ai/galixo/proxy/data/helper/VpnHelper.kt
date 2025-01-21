package ai.galixo.proxy.data.helper

import ai.galixo.proxy.domain.model.ConnectionStatus
import ai.galixo.proxy.domain.model.Stats
import ai.galixo.proxy.utils.extensions.hasNotificationPermission
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.RECEIVER_NOT_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.net.VpnService
import android.os.Build
import android.os.RemoteException
import com.github.shadowsocks.Core
import com.github.shadowsocks.aidl.IShadowsocksService
import com.github.shadowsocks.aidl.ShadowsocksConnection
import com.github.shadowsocks.aidl.TrafficStats
import com.github.shadowsocks.bg.BaseService
import com.github.shadowsocks.preference.PrefUtils
import com.github.shadowsocks.preference.ServerModel
import com.github.shadowsocks.utils.Key
import timber.log.Timber

open class VpnHelper(
    private val context: Context,
) : ShadowsocksConnection.Callback {
    private var state = BaseService.State.Idle

    private val connection = ShadowsocksConnection(true)
    private var selectedServer: ServerModel? = null

    open fun init() {
        val intentFilter = IntentFilter("statsState")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.registerReceiver(broadcastReceiver, intentFilter, RECEIVER_NOT_EXPORTED)
        } else {
            context.registerReceiver(broadcastReceiver, intentFilter)
        }
        bindToVPNService()
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (context == null || intent == null) return
            val time = intent.getStringExtra("duration") ?: ""
            val uploadSpeed = intent.getStringExtra("upload_speed") ?: ""
            val downloadSpeed = intent.getStringExtra("download_speed") ?: ""
            onStatesReceived(Stats(time, downloadSpeed, uploadSpeed))
        }
    }

    open fun dispose() {
        context.unregisterReceiver(broadcastReceiver)
    }

    fun connect() {
        if (hasConnectionStarted()) stopVpn()
        prepareVpn()
//        context.postAnalytics(CONNECT_PROXY + "_${selectedServer?.countryName}")
//        context.postAnalytics(CONNECT_PROXY)
    }

    private fun prepareVpn() {
        Timber.d("VpnHelper: prepareVpn----has connection started= ${hasConnectionStarted()}")
        if (!hasConnectionStarted()) {
            if (context.hasNotificationPermission()) {
                try {
                    val intent = VpnService.prepare(context)
                    if (intent != null) {
                        onRequestPermissionForVPN(intent)
                    } else {
                        startVpn()
                    }
                } catch (_: Exception) {
                }
            } else {
                onRequestPermissionForNotification()
            }
        }
    }

    private fun startVpn() {
        if (PrefUtils.selectedServiceMode != Key.modeVpn) {
            PrefUtils.selectedServiceMode = Key.modeVpn
        }
//        context.postAnalytics(CONNECTION_SUCCESS + "_${selectedServer?.countryName}")
        selectedServer?.let { Core.setServerModel(it) }
        Timber.d("VpnHelper: startVpn selected server= $selectedServer")
        Core.startService()
        /*CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            ConnectionInterstitial.loadIntersAd(
                context,
                admob_interstitial_id_connection
            )
        }
        context.postAnalytics(CONNECTION_SUCCESS)*/
    }

    fun stopVpn() {
        Core.stopService()
    }

    fun isConnected(): Boolean {
        return isVpnConnected()
    }

    open fun isVpnConnected(): Boolean {
        return state == BaseService.State.Connected
    }

    fun isConnecting(): Boolean {
        return hasConnectionStarted()
    }

    open fun onConnectivityStatusChanged(connectionStatus: ConnectionStatus) {}

    open fun onStatesReceived(stats: Stats){}

    fun mapToVpnConnectionStatus(state: String): ConnectionStatus {
        return when (state) {
            "DISCONNECTED" -> ConnectionStatus.DISCONNECTED
            "CONNECTED" -> ConnectionStatus.CONNECTED
            else -> {
                ConnectionStatus.UNKNOWN
            }
        }
    }

    fun bindToVPNService() {
        connection.connect(context, this)
        onConnectionStatusChanged()
    }

    fun hasConnectionStarted(): Boolean {
        return state == BaseService.State.Connecting
    }

    open fun onRequestPermissionForVPN(intent: Intent) {}

    open fun onRequestPermissionForNotification() {}

    open fun onConnectionStatusChanged() {}

    open fun onConnectionTimeout() {}

    override fun stateChanged(state: BaseService.State, profileName: String?, msg: String?) {
        changeState(state)
    }

    private fun changeState(
        state: BaseService.State
    ) {
        this.state = state
        when (state) {
            BaseService.State.Stopped -> {
                onConnectivityStatusChanged(ConnectionStatus.DISCONNECTED)
            }

            BaseService.State.Stopping -> {}

            BaseService.State.Connecting -> {
                onConnectivityStatusChanged(ConnectionStatus.CONNECTING)
            }

            BaseService.State.Connected -> {
                onConnectivityStatusChanged(ConnectionStatus.CONNECTED)
            }

            BaseService.State.Idle -> {}

            BaseService.State.Failed -> {
                onConnectivityStatusChanged(ConnectionStatus.ERROR)
//                context.postAnalytics(CONNECTION_FAILED + "_${selectedServer?.countryName}")
            }
        }
    }

    override fun onServiceConnected(service: IShadowsocksService) {
        changeState(
            try {
                BaseService.State.entries[service.state]
            } catch (_: RemoteException) {
                BaseService.State.Idle
            }
        )
    }

    fun setSelectedServer(selectedServer: ServerModel?) {
        this.selectedServer = selectedServer
    }

    override fun onBinderDied() {
        super.onBinderDied()
        connection.disconnect(context)
        connection.connect(context, this)
    }

    override fun trafficUpdated(profileId: Long, stats: TrafficStats) {
        super.trafficUpdated(profileId, stats)
    }
}