package ai.galixo.proxy.presentation.dialogs

import ai.galixo.proxy.databinding.DialogNotificationPermissionBinding
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup

fun Context.showNotificationPermissionDialog(retry: () -> Unit) {
    val dialog = Dialog(this)
    val bottomSheetBinding = DialogNotificationPermissionBinding.inflate(LayoutInflater.from(this))
    dialog.setContentView(bottomSheetBinding.root)
    dialog.setCancelable(false)
    dialog.show()
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
    val height = ViewGroup.LayoutParams.WRAP_CONTENT
    dialog.window?.setLayout(width, height)

    bottomSheetBinding.apply {
        btnAllow.setOnClickListener {
            retry()
            dialog.dismiss()
        }
        btnDeny.setOnClickListener {
            dialog.dismiss()
        }
    }
}