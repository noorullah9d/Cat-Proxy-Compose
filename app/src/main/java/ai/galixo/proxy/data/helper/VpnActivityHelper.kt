package ai.galixo.proxy.data.helper

import ai.galixo.proxy.domain.model.ConnectionStatus
import ai.galixo.proxy.presentation.dialogs.showNotificationPermissionDialog
import ai.galixo.proxy.presentation.home.HomeViewModel
import ai.galixo.proxy.utils.extensions.hasNotificationPermission
import ai.galixo.proxy.utils.isAnySystemDialogShown
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class VpnActivityHelper(private val activity: ComponentActivity) : VpnHelper(activity) {

    private val connectViewModel by activity.viewModels<HomeViewModel>()

    private val lifeCycleObserver = object : DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) {
            super.onCreate(owner)
            /* connectViewModel.getPreConnectionStatus {
                 connectViewModel.getServers(!(it == ConnectUiState.Connected || it == ConnectUiState.Connected))
             }*/

            /*  connectViewModel.getSelectedServer()*/
        }

        override fun onStart(owner: LifecycleOwner) {
            super.onStart(owner)
            bindToVPNService()
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            dispose()
        }
    }

    override fun dispose() {
        super.dispose()
        connectViewModel.cancelJobs()
    }

    init {
        activity.lifecycle.addObserver(lifeCycleObserver)
    }

    fun initializeAndObserve() = with(activity) {
        init()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                connectViewModel.getStatus()
                connectViewModel.getStats()
                connectViewModel.connectionStatus.collect { state ->
                    Timber.d("HomeScreen: connection status helper= $state")

                    when (state) {
                        ConnectionStatus.NEW_CONNECTION -> {
                            setConnection()
                        }

                        ConnectionStatus.STOP_CONNECTION -> {
                            stopConnection()
                        }

                        ConnectionStatus.RECONNECT -> {
                            stopConnection()
                            delay(200)
                            setConnection()
                        }

                        ConnectionStatus.ERROR -> {
                            stopConnection()
                        }

                        ConnectionStatus.CONNECTED -> {}

                        else -> {}
                    }
                }
            }
        }
    }

    private fun stopConnection() {
        if (hasConnectionStarted() || isVpnConnected()) {
            stopVpn()
        } else {
//            activity.postAnalytics(DISCONNECT_PROXY)
            connectViewModel.setPreConnectionStatus(ConnectionStatus.DISCONNECTED)
        }
    }

    private fun setConnection() {
        setSelectedServer(connectViewModel.getCurrentSelectedServer())
        connectViewModel.getCurrentSelectedServer()?.let {
            connectViewModel.setLastConnectedServer(it)
        }
        connect()
    }

    private val vpnResultContract = activity.registerForActivityResult(VPNServiceContract()) { ok ->
        isAnySystemDialogShown = true
        if (ok) {
            connect()
        } else {
            connectViewModel.setPreConnectionStatus(ConnectionStatus.DISCONNECTED)
        }
    }


    private val notificationPermission =
        activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { ok ->
            isAnySystemDialogShown = true
            if (ok) {
                connect()
            } else {
                notificationPermissionDialogSettings()
            }
        }

    private val settingsLauncher =
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
            if (activity.hasNotificationPermission()) {

                connect()
            } else {
                notificationPermissionDialogSettings()
            }
        }

    private class VPNServiceContract : ActivityResultContract<Intent, Boolean>() {
        override fun createIntent(context: Context, input: Intent): Intent {
            return input
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            return resultCode == Activity.RESULT_OK
        }
    }

    override fun onRequestPermissionForVPN(intent: Intent) {
        super.onRequestPermissionForVPN(intent)
        vpnResultContract.launch(intent)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onRequestPermissionForNotification() {
        super.onRequestPermissionForNotification()
        notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    override fun onConnectivityStatusChanged(connectionStatus: ConnectionStatus) {
        super.onConnectivityStatusChanged(connectionStatus)
        connectViewModel.updateState(connectionStatus)
    }

    /*override fun onStatsReceived(stats: Stats) {
        super.onStatsReceived(stats)

    }*/

    override fun onTimeReceived(time: ConnectionTime) {
        super.onTimeReceived(time)
        connectViewModel.updateTime(time)
    }

    override fun onConnectionStatusChanged() {
        super.onConnectionStatusChanged()
        when {
            isConnected() -> {
                connectViewModel.setPreConnectionStatus(ConnectionStatus.CONNECTED)
            }

            isConnecting() -> {
                connectViewModel.setPreConnectionStatus(ConnectionStatus.CONNECTING)
            }

            !isConnected() -> {
                connectViewModel.setPreConnectionStatus(ConnectionStatus.DISCONNECTED)
            }
        }
    }

    override fun onConnectionTimeout() {
        super.onConnectionTimeout()
        connectViewModel.setPreConnectionStatus(ConnectionStatus.ERROR)
    }

    private fun notificationPermissionDialogSettings() {
        connectViewModel.setPreConnectionStatus(ConnectionStatus.DISCONNECTED)
        activity.showNotificationPermissionDialog {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", activity.packageName, null)
            intent.data = uri
            settingsLauncher.launch(intent)
        }
    }
}