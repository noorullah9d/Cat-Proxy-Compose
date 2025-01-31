package ai.galixo.proxy.utils

import com.github.shadowsocks.preference.PrefUtils
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object DateManager {
    fun getDate(): String {
        // Get Date
        val currentDate: Date = Calendar.getInstance().time
        // Convert Format
        val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return df.format(currentDate)
    }

    fun getDayOffset(): Int {
        val firstLaunchDate = PrefUtils.firstLaunchDate
        if (firstLaunchDate == 0L) return 1 // Default to Day 1 if no first launch date is set

        val currentTime = System.currentTimeMillis()
        val daysDifference = TimeUnit.MILLISECONDS.toDays(currentTime - firstLaunchDate).toInt()
        return daysDifference + 1 // Day 1 starts at the first launch date
    }
}
