package com.galixo.proxy.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.tencent.mmkv.MMKV
import com.galixo.proxy.R
import com.galixo.proxy.databinding.ActivitySubEditBinding
import com.galixo.proxy.dto.SubscriptionItem
import com.galixo.proxy.extension.toast
import com.galixo.proxy.util.MmkvManager
import com.galixo.proxy.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SubEditActivity : BaseActivity() {
    private val binding by lazy { ActivitySubEditBinding.inflate(layoutInflater) }

    var del_config: MenuItem? = null
    var save_config: MenuItem? = null

    private val subStorage by lazy { MMKV.mmkvWithID(MmkvManager.ID_SUB, MMKV.MULTI_PROCESS_MODE) }
    private val editSubId by lazy { intent.getStringExtra("subId").orEmpty() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = getString(R.string.title_sub_setting)

        val json = subStorage?.decodeString(editSubId)
        if (!json.isNullOrBlank()) {
            bindingServer(Gson().fromJson(json, SubscriptionItem::class.java))
        } else {
            clearServer()
        }
    }

    /**
     * bingding seleced server config
     */
    private fun bindingServer(subItem: SubscriptionItem): Boolean {
        binding.etRemarks.text = Utils.getEditable(subItem.remarks)
        binding.etUrl.text = Utils.getEditable(subItem.url)
        binding.chkEnable.isChecked = subItem.enabled
        binding.autoUpdateCheck.isChecked = subItem.autoUpdate
        return true
    }

    /**
     * clear or init server config
     */
    private fun clearServer(): Boolean {
        binding.etRemarks.text = null
        binding.etUrl.text = null
        binding.chkEnable.isChecked = true
        return true
    }

    /**
     * save server config
     */
    private fun saveServer(): Boolean {
        val subItem: SubscriptionItem
        val json = subStorage?.decodeString(editSubId)
        var subId = editSubId
        if (!json.isNullOrBlank()) {
            subItem = Gson().fromJson(json, SubscriptionItem::class.java)
        } else {
            subId = Utils.getUuid()
            subItem = SubscriptionItem()
        }

        subItem.remarks = binding.etRemarks.text.toString()
        subItem.url = binding.etUrl.text.toString()
        subItem.enabled = binding.chkEnable.isChecked
        subItem.autoUpdate = binding.autoUpdateCheck.isChecked

        if (TextUtils.isEmpty(subItem.remarks)) {
            toast(R.string.sub_setting_remarks)
            return false
        }
//        if (TextUtils.isEmpty(subItem.url)) {
//            toast(R.string.sub_setting_url)
//            return false
//        }

        subStorage?.encode(subId, Gson().toJson(subItem))
        toast(R.string.toast_success)
        finish()
        return true
    }

    /**
     * save server config
     */
    private fun deleteServer(): Boolean {
        if (editSubId.isNotEmpty()) {
            AlertDialog.Builder(this).setMessage(R.string.del_config_comfirm)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    lifecycleScope.launch(Dispatchers.IO) {
                        MmkvManager.removeSubscription(editSubId)
                        launch(Dispatchers.Main) {
                            finish()
                        }
                    }
                }
                .setNegativeButton(android.R.string.no) { _, _ ->
                    // do nothing
                }
                .show()
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.action_server, menu)
        del_config = menu.findItem(R.id.del_config)
        save_config = menu.findItem(R.id.save_config)

        if (editSubId.isEmpty()) {
            del_config?.isVisible = false
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.del_config -> {
            deleteServer()
            true
        }

        R.id.save_config -> {
            saveServer()
            true
        }

        else -> super.onOptionsItemSelected(item)
    }

}
