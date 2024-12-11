package com.galixo.proxy.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.galixo.proxy.AppConfig.ANG_PACKAGE
import com.galixo.proxy.R
import com.galixo.proxy.databinding.ActivityLogcatBinding
import com.galixo.proxy.extension.toast
import com.galixo.proxy.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class LogcatActivity : BaseActivity() {
    private val binding by lazy {
        ActivityLogcatBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = getString(R.string.title_logcat)

        logcat(false)
    }

    private fun logcat(shouldFlushLog: Boolean) {

        try {
            binding.pbWaiting.visibility = View.VISIBLE

            lifecycleScope.launch(Dispatchers.Default) {
                if (shouldFlushLog) {
                    val lst = LinkedHashSet<String>()
                    lst.add("logcat")
                    lst.add("-c")
                    withContext(Dispatchers.IO) {
                        val process = Runtime.getRuntime().exec(lst.toTypedArray())
                        process.waitFor()
                    }
                }
                val lst = LinkedHashSet<String>()
                lst.add("logcat")
                lst.add("-d")
                lst.add("-v")
                lst.add("time")
                lst.add("-s")
                lst.add("GoLog,tun2socks,${ANG_PACKAGE},AndroidRuntime,System.err")
                val process = withContext(Dispatchers.IO) {
                    Runtime.getRuntime().exec(lst.toTypedArray())
                }
//                val bufferedReader = BufferedReader(
//                        InputStreamReader(process.inputStream))
//                val allText = bufferedReader.use(BufferedReader::readText)
                val allText = process.inputStream.bufferedReader().use { it.readText() }
                launch(Dispatchers.Main) {
                    binding.tvLogcat.text = allText
                    binding.tvLogcat.movementMethod = ScrollingMovementMethod()
                    binding.pbWaiting.visibility = View.GONE
                    Handler(Looper.getMainLooper()).post { binding.svLogcat.fullScroll(View.FOCUS_DOWN) }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_logcat, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.copy_all -> {
            Utils.setClipboard(this, binding.tvLogcat.text.toString())
            toast(R.string.toast_success)
            true
        }

        R.id.clear_all -> {
            logcat(true)
            true
        }

        else -> super.onOptionsItemSelected(item)
    }
}
