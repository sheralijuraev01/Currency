package uz.sher.currency.ui.vm

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.sher.currency.R
import uz.sher.currency.data.remote.ConvertRepository
import uz.sher.currency.data.remote.models.CurrencyItem
import uz.sher.currency.data.remote.models.ResponseError
import uz.sher.currency.util.network.NetworkHelper
import uz.sher.currency.util.resource.Resource
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val context: Context,
    private val repository: ConvertRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {


    fun getCurrencies(): MutableLiveData<Resource<List<CurrencyItem>>> {
        val currencies: MutableLiveData<Resource<List<CurrencyItem>>> = MutableLiveData()
        currencies.postValue(Resource.Loading())
        if (networkHelper.isNetworkConnected()) {
            // internet bor
            viewModelScope.launch {
                try {
                    repository.getAllCurrencies().let {
                        if (it.isSuccessful) {

                            it.body()?.let { list ->
                                currencies.postValue(Resource.Success(list))
                            }

                        } else {
                            val gson = Gson()
                            val type = object : TypeToken<ResponseError>() {}.type
                            val errorResponse: ResponseError? =
                                gson.fromJson(it.errorBody()!!.charStream(), type)

                            errorResponse?.let { error ->
                                currencies.postValue(Resource.Failure(error.message))
                            }

                        }

                    }

                } catch (e: Exception) {
                    currencies.postValue(Resource.Failure(e.message!!))

                }
            }

        } else {
            // internet yo'q
            val networkText = context.resources.getText(R.string.internet_message)
            currencies.postValue(Resource.Network(networkText.toString()))
        }
        return currencies
    }


    fun getLang() = repository.getLang()

    fun saveLang(lang: String) = repository.saveLang(lang)

}