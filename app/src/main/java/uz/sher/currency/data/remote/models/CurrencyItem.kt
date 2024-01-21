package uz.sher.currency.data.remote.models



import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class CurrencyItem(
    @SerializedName("Ccy")
    val ccy: String,
    @SerializedName("CcyNm_EN")
    val ccyNmEN: String,
    @SerializedName("CcyNm_RU")
    val ccyNmRU: String,
    @SerializedName("CcyNm_UZ")
    val ccyNmUZ: String,
    @SerializedName("CcyNm_UZC")
    val ccyNmUZC: String,
    @SerializedName("Code")
    val code: String,
    @SerializedName("Date")
    val date: String,
    @SerializedName("Diff")
    val diff: String,
    val id: Int,
    @SerializedName("Nominal")
    val nominal: String,
    @SerializedName("Rate")
    val rate: String
):Parcelable