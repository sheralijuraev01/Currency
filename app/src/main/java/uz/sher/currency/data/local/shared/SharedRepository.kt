package uz.sher.currency.data.local.shared

import android.content.Context
import uz.sher.currency.util.constans.Constants.Companion.LANGUAGE_KEY
import uz.sher.currency.util.constans.Constants.Companion.LANGUAGE_SHARED
import javax.inject.Inject

class SharedRepository  (context: Context) : SharedInterface {
    private val sharedPreferences =
        context.getSharedPreferences(LANGUAGE_SHARED, Context.MODE_PRIVATE)

    override fun saveLang(lang: String) {
        sharedPreferences.edit().putString(LANGUAGE_KEY, lang).apply()
    }

    override fun getLang(): String {
        return sharedPreferences.getString(LANGUAGE_KEY, "en") ?: "en"
    }
}