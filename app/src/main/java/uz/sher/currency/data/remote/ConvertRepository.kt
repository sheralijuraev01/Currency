package uz.sher.currency.data.remote


import uz.sher.currency.data.local.shared.SharedInterface
import javax.inject.Inject

class ConvertRepository @Inject constructor(
    private val sharedInterface: SharedInterface,
    private val apiService: ApiService
) {
    suspend fun getAllCurrencies() = apiService.getAllCurrencies()

    fun saveLang(lang: String) = sharedInterface.saveLang(lang)

    fun getLang(): String = sharedInterface.getLang()
}