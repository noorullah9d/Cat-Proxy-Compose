package com.galixo.proxy.ui

import android.content.Intent
import android.graphics.Color
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.galixo.proxy.AngApplication.Companion.application
import com.galixo.proxy.AppConfig
import com.galixo.proxy.R
import com.galixo.proxy.databinding.ItemQrcodeBinding
import com.galixo.proxy.databinding.ItemRecyclerFooterBinding
import com.galixo.proxy.databinding.ItemRecyclerMainBinding
import com.galixo.proxy.dto.EConfigType
import com.galixo.proxy.dto.SubscriptionItem
import com.galixo.proxy.extension.toast
import com.galixo.proxy.helper.ItemTouchHelperAdapter
import com.galixo.proxy.helper.ItemTouchHelperViewHolder
import com.galixo.proxy.service.V2RayServiceManager
import com.galixo.proxy.util.AngConfigManager
import com.galixo.proxy.util.MmkvManager
import com.galixo.proxy.util.Utils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

class MainRecyclerAdapter(val activity: MainActivity) : RecyclerView.Adapter<MainRecyclerAdapter.BaseViewHolder>(), ItemTouchHelperAdapter {
    companion object {
        private const val VIEW_TYPE_ITEM = 1
        private const val VIEW_TYPE_FOOTER = 2
    }

    private var mActivity: MainActivity = activity
    private val shareMethod: Array<out String> by lazy {
        mActivity.resources.getStringArray(R.array.share_method)
    }
    var isRunning = false

    override fun getItemCount() = mActivity.mainViewModel.serversCache.size + 1

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (holder is MainViewHolder) {
            val guid = mActivity.mainViewModel.serversCache[position].guid
            val profile = mActivity.mainViewModel.serversCache[position].profile
//            //filter
//            if (mActivity.mainViewModel.subscriptionId.isNotEmpty()
//                && mActivity.mainViewModel.subscriptionId != config.subscriptionId
//            ) {
//                holder.itemMainBinding.cardView.visibility = View.GONE
//            } else {
//                holder.itemMainBinding.cardView.visibility = View.VISIBLE
//            }

            val server = mActivity.mainViewModel.serversCache[position]
            Log.d("MainRecyclerAdapter:", "onBindViewHolder: server= $server")

            val aff = MmkvManager.decodeServerAffiliationInfo(guid)

            holder.itemMainBinding.tvName.text = profile.remarks
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
            holder.itemMainBinding.tvTestResult.text = aff?.getTestDelayString().orEmpty()
            if ((aff?.testDelayMillis ?: 0L) < 0L) {
                holder.itemMainBinding.tvTestResult.setTextColor(ContextCompat.getColor(mActivity, R.color.colorPingRed))
            } else {
                holder.itemMainBinding.tvTestResult.setTextColor(ContextCompat.getColor(mActivity, R.color.colorPing))
            }
            if (guid == MmkvManager.mainStorage?.decodeString(MmkvManager.KEY_SELECTED_SERVER)) {
                holder.itemMainBinding.layoutIndicator.setBackgroundResource(R.color.colorAccent)
            } else {
                holder.itemMainBinding.layoutIndicator.setBackgroundResource(0)
            }
            holder.itemMainBinding.tvSubscription.text = ""
            val json = MmkvManager.subStorage?.decodeString(profile.subscriptionId)
            if (!json.isNullOrBlank()) {
                val sub = Gson().fromJson(json, SubscriptionItem::class.java)
                holder.itemMainBinding.tvSubscription.text = sub.remarks
            }

            var shareOptions = shareMethod.asList()
            when (profile.configType) {
                EConfigType.CUSTOM -> {
                    holder.itemMainBinding.tvType.text = mActivity.getString(R.string.server_customize_config)
                    shareOptions = shareOptions.takeLast(1)
                }

                EConfigType.VLESS -> {
                    holder.itemMainBinding.tvType.text = profile.configType.name
                }

                else -> {
                    holder.itemMainBinding.tvType.text = profile.configType.name.lowercase()
                }
            }

            val strState = "${profile.server?.dropLast(3)}*** : ${profile.serverPort}"

            holder.itemMainBinding.tvStatistics.text = strState

            holder.itemMainBinding.layoutShare.setOnClickListener {
                AlertDialog.Builder(mActivity).setItems(shareOptions.toTypedArray()) { _, i ->
                    try {
                        when (i) {
                            0 -> {
                                if (profile.configType == EConfigType.CUSTOM) {
                                    shareFullContent(guid)
                                } else {
                                    val ivBinding = ItemQrcodeBinding.inflate(LayoutInflater.from(mActivity))
                                    ivBinding.ivQcode.setImageBitmap(AngConfigManager.share2QRCode(guid))
                                    AlertDialog.Builder(mActivity).setView(ivBinding.root).show()
                                }
                            }

                            1 -> {
                                if (AngConfigManager.share2Clipboard(mActivity, guid) == 0) {
                                    mActivity.toast(R.string.toast_success)
                                } else {
                                    mActivity.toast(R.string.toast_failure)
                                }
                            }

                            2 -> shareFullContent(guid)
                            else -> mActivity.toast("else")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }.show()
            }

            holder.itemMainBinding.layoutEdit.setOnClickListener {
                val intent = Intent().putExtra("guid", guid)
                    .putExtra("isRunning", isRunning)
                if (profile.configType == EConfigType.CUSTOM) {
                    mActivity.startActivity(intent.setClass(mActivity, ServerCustomConfigActivity::class.java))
                } else {
                    mActivity.startActivity(intent.setClass(mActivity, ServerActivity::class.java))
                }
            }

            holder.itemMainBinding.layoutRemove.setOnClickListener {
                if (guid != MmkvManager.mainStorage?.decodeString(MmkvManager.KEY_SELECTED_SERVER)) {
                    if (MmkvManager.settingsStorage?.decodeBool(AppConfig.PREF_CONFIRM_REMOVE) == true) {
                        AlertDialog.Builder(mActivity).setMessage(R.string.del_config_comfirm)
                            .setPositiveButton(android.R.string.ok) { _, _ ->
                                removeServer(guid, position)
                            }
                            .setNegativeButton(android.R.string.cancel) { _, _ ->
                                //do noting
                            }
                            .show()
                    } else {
                        removeServer(guid, position)
                    }
                } else {
                    application.toast(R.string.toast_action_not_allowed)
                }
            }

            holder.itemMainBinding.infoContainer.setOnClickListener {
                val selected = MmkvManager.mainStorage?.decodeString(MmkvManager.KEY_SELECTED_SERVER)
                if (guid != selected) {
                    MmkvManager.mainStorage?.encode(MmkvManager.KEY_SELECTED_SERVER, guid)
                    if (!TextUtils.isEmpty(selected)) {
                        notifyItemChanged(mActivity.mainViewModel.getPosition(selected.orEmpty()))
                    }
                    notifyItemChanged(mActivity.mainViewModel.getPosition(guid))
                    if (isRunning) {
                        Utils.stopVService(mActivity)
                        val result = Observable.timer(500, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                V2RayServiceManager.startV2Ray(mActivity)
                            }
                        Log.d("MainRecyclerAdapter", "onBindViewHolder: result= $result")
                    }
                }
            }
        }
        if (holder is FooterViewHolder) {
            //if (activity?.defaultDPreference?.getPrefBoolean(AppConfig.PREF_IN_APP_BUY_IS_PREMIUM, false)) {
            if (true) {
                holder.itemFooterBinding.layoutEdit.visibility = View.INVISIBLE
            } else {
                holder.itemFooterBinding.layoutEdit.setOnClickListener {
                    Utils.openUri(mActivity, "${Utils.decode(AppConfig.PromotionUrl)}?t=${System.currentTimeMillis()}")
                }
            }
        }
    }

    private fun shareFullContent(guid: String) {
        if (AngConfigManager.shareFullContent2Clipboard(mActivity, guid) == 0) {
            mActivity.toast(R.string.toast_success)
        } else {
            mActivity.toast(R.string.toast_failure)
        }
    }

    private fun removeServer(guid: String, position: Int) {
        mActivity.mainViewModel.removeServer(guid)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mActivity.mainViewModel.serversCache.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM ->
                MainViewHolder(ItemRecyclerMainBinding.inflate(LayoutInflater.from(parent.context), parent, false))

            else ->
                FooterViewHolder(ItemRecyclerFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mActivity.mainViewModel.serversCache.size) {
            VIEW_TYPE_FOOTER
        } else {
            VIEW_TYPE_ITEM
        }
    }

    open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        fun onItemClear() {
            itemView.setBackgroundColor(0)
        }
    }

    class MainViewHolder(val itemMainBinding: ItemRecyclerMainBinding) :
        BaseViewHolder(itemMainBinding.root), ItemTouchHelperViewHolder

    class FooterViewHolder(val itemFooterBinding: ItemRecyclerFooterBinding) :
        BaseViewHolder(itemFooterBinding.root)

    override fun onItemDismiss(position: Int) {
        val guid = mActivity.mainViewModel.serversCache.getOrNull(position)?.guid ?: return
        if (guid != MmkvManager.mainStorage?.decodeString(MmkvManager.KEY_SELECTED_SERVER)) {
//            mActivity.alert(R.string.del_config_confirm) {
//                positiveButton(android.R.string.ok) {
            mActivity.mainViewModel.removeServer(guid)
            notifyItemRemoved(position)
//                }
//                show()
//            }
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        mActivity.mainViewModel.swapServer(fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        // position is changed, since position is used by click callbacks, need to update range
        if (toPosition > fromPosition)
            notifyItemRangeChanged(fromPosition, toPosition - fromPosition + 1)
        else
            notifyItemRangeChanged(toPosition, fromPosition - toPosition + 1)
        return true
    }

    override fun onItemMoveCompleted() {
        // do nothing
    }
}
