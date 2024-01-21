package uz.sher.currency.data.local.shared

interface SharedInterface {
    fun saveLang(lang: String)
    fun getLang(): String
}