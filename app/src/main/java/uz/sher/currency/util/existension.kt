package uz.sher.currency.util

import android.app.Activity
import android.content.ContentValues
import android.content.res.Configuration
import android.util.Log
import java.util.*

object Localization {
    @JvmStatic
    fun changeLan(changeLang: String, context: Activity) {
        Log.d(ContentValues.TAG, "languageUpdate123: $changeLang")
        val locale = getLocale(changeLang)
        Locale.setDefault(locale)
        val res = context.resources
        val config = Configuration(res.configuration)
        config.setLocale(locale)
        context.createConfigurationContext(config)
        res.updateConfiguration(config, res.displayMetrics)

    }

    private fun getLocale(languageCode: String): Locale {
        return Locale(languageCode)
    }
}