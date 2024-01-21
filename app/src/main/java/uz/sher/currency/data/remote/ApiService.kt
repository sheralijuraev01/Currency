package uz.sher.currency.data.remote

import retrofit2.Response
import retrofit2.http.GET
import uz.sher.currency.data.remote.models.CurrencyItem


interface ApiService {
    @GET("uz/arkhiv-kursov-valyut/json/")
    suspend fun getAllCurrencies(): Response<List<CurrencyItem>>
}