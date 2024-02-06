package uz.sher.currency.util.functions

import android.util.Log
import uz.sher.currency.R
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object Functions {


    fun getFlag(str: String): Int {
        return when (str) {
            "USD" -> R.drawable.us
            "EUR" -> R.drawable.eur
            "RUB" -> R.drawable.ru
            "GBP" -> R.drawable.gb
            "JPY" -> R.drawable.jp
            "AZN" -> R.drawable.az
            "BDT" -> R.drawable.bt
            "BGN" -> R.drawable.flag_bolgariya
            "BHD" -> R.drawable.flag_bahrain
            "BND" -> R.drawable.flag_bruney
            "BRL" -> R.drawable.brasil
            "BYN" -> R.drawable.belarus
            "CAD" -> R.drawable.flag_canada
            "CHF" -> R.drawable.shvitsariya
            "CNY" -> R.drawable.cn
            "CUP" -> R.drawable.flag_cuba
            "CZK" -> R.drawable.czech
            "DKK" -> R.drawable.dk
            "DZD" -> R.drawable.jazoir
            "EGP" -> R.drawable.misr
            "AFN" -> R.drawable.afgoniston
            "ARS" -> R.drawable.argentina
            "GEL" -> R.drawable.gruziya
            "HKD" -> R.drawable.honkon
            "HUF" -> R.drawable.vwngriay
            "IDR" -> R.drawable.indoniziya
            "ILS" -> R.drawable.isroil
            "INR" -> R.drawable.india
            "IQD" -> R.drawable.iroq
            "IRR" -> R.drawable.eron
            "ISK" -> R.drawable.islandiya
            "JOD" -> R.drawable.iordaniya
            "AUD" -> R.drawable.australiya
            "KGS" -> R.drawable.qirgisizton
            "KHR" -> R.drawable.komodo
            "KRW" -> R.drawable.koriya
            "KWD" -> R.drawable.quvayt
            "KZT" -> R.drawable.qozoq
            "LAK" -> R.drawable.laos
            "LBP" -> R.drawable.livan
            "LYD" -> R.drawable.liviya
            "MAD" -> R.drawable.marokash
            "MDL" -> R.drawable.moldava
            "MMK" -> R.drawable.miyamma
            "MNT" -> R.drawable.mongoliya
            "MXN" -> R.drawable.mexico
            "MYR" -> R.drawable.malaziya
            "NOK" -> R.drawable.norvigiya
            "NZD" -> R.drawable.yangi_zellandiya
            "OMR" -> R.drawable.ummon
            "PHP" -> R.drawable.flippin
            "PKR" -> R.drawable.pk
            "PLN" -> R.drawable.poland
            "QAR" -> R.drawable.qatar
            "RON" -> R.drawable.ruminiya
            "RSD" -> R.drawable.serbia
            "AMD" -> R.drawable.armaniston
            "SAR" -> R.drawable.saudi
            "SDG" -> R.drawable.sudan
            "SEK" -> R.drawable.shvitsiya
            "SGD" -> R.drawable.singapur
            "SYP" -> R.drawable.suriya
            "THB" -> R.drawable.tailand
            "TJS" -> R.drawable.tojik
            "TMT" -> R.drawable.turkman
            "TND" -> R.drawable.tunis
            "TRY" -> R.drawable.turkiya
            "UAH" -> R.drawable.ukraina
            "AED" -> R.drawable.saudi
            "UYU" -> R.drawable.urugvay
            "VES" -> R.drawable.vanisuiliya
            "VND" -> R.drawable.vitnam
            "XDR" -> R.drawable.sdr
            "YER" -> R.drawable.yaman
            "ZAR" -> R.drawable.janubiy_afrika
            else -> {
                R.drawable.uz
            }
        }
    }

    fun formatStringTop(value: String): String {
        val newText = StringBuilder()
        var reverseText = ""
        var newSecondPart = ""
        if (value.contains(".")) {
            val partArray = value.split('.')
            reverseText = reverseString(partArray[0])
            newSecondPart = partArray[1]

        } else {
            reverseText = reverseString(value)
        }
        for (i in reverseText.indices) {
            if (i % 3 == 0 && i != 0) newText.append(" ")
            newText.append(reverseText[i])
        }


        return if (value.contains("."))
            reverseString(newText.toString()) + "." + newSecondPart
        else reverseString(newText.toString()) + newSecondPart
    }


    fun formatNumber(number: Double): String {
        val decimalFormat = DecimalFormat("#,###.##")
        val symbols = DecimalFormatSymbols(Locale.getDefault())
        symbols.groupingSeparator = ' ' // bo'sh joy belgisini o'zgartiring
        decimalFormat.decimalFormatSymbols = symbols
        return decimalFormat.format(number)
    }

    private fun reverseString(input: String): String {
        var reversed = ""
        for (i in input.length - 1 downTo 0) {
            reversed += input[i]
        }
        return reversed
    }

    fun checkNumberLength(number: String): Boolean {
        val value = BigDecimal(number).toString()
        val partArray = value.split('.')
        val leftNumber = if (value.contains('.'))
            partArray[0]
        else value
        Log.e("valueNumber", number)

//        return  leftNumber.length>15 || (leftNumber.length==1 && leftNumber[0]!='0')
        return  leftNumber.length>15
    }


    fun checkPointRightCondition(str: String): String {
        if (!str.contains('.')) return ""
        val array = str.split('.')
        val rightString = array[1]

        return if (rightString.length < 10) "" else "_"
    }
}