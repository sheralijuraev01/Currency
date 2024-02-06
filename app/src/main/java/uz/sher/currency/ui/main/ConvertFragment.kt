package uz.sher.currency.ui.main


import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import uz.sher.currency.R
import uz.sher.currency.adapters.MenuAdapter
import uz.sher.currency.data.remote.models.CurrencyItem
import uz.sher.currency.databinding.FragmentConvertBinding
import uz.sher.currency.util.CustomToast
import uz.sher.currency.util.constans.Constants.Companion.BOTTOM_CURRENCY
import uz.sher.currency.util.constans.Constants.Companion.CHOOSE_CURRENCY
import uz.sher.currency.util.constans.Constants.Companion.LIST_CURRENCY
import uz.sher.currency.util.constans.Constants.Companion.TOP_CURRENCY
import uz.sher.currency.util.functions.Functions.checkNumberLength
import uz.sher.currency.util.functions.Functions.checkPointRightCondition
import uz.sher.currency.util.functions.Functions.formatNumber
import uz.sher.currency.util.functions.Functions.formatStringTop
import uz.sher.currency.util.functions.Functions.getFlag
import java.util.Locale

class ConvertFragment : Fragment(R.layout.fragment_convert), View.OnClickListener {


    private var _binding: FragmentConvertBinding? = null
    private val binding get() = _binding!!
    private var list: MutableList<CurrencyItem> = ArrayList()
    private lateinit var adapter: MenuAdapter
    private var lang: String = "en"
    private lateinit var notFound: ImageView
    private var topCurrencyName = ""
    private var bottomCurrencyName = "UZS"
    private var topCurrencyValue: String = "1.0"
    private var bottomCurrencyValue = 1.0
    private var editTopValue = ""
    private lateinit var customToast: CustomToast

    private var numberStatus = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        topCurrencyName = arguments?.getString(CHOOSE_CURRENCY)!!
        list.clear()
        list.addAll(arguments?.getParcelableArrayList(LIST_CURRENCY)!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentConvertBinding.bind(view)

        adapter = MenuAdapter()





        customToast = CustomToast(binding.root.context)


        bottomCurrencyValue = list[determinePosition(topCurrencyName)].rate.toDouble()

        setCurrenciesValues()
//        setRateValues()

        binding.backConvert.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.selectCurrencyChange.setOnClickListener {
            dialogCurrency(TOP_CURRENCY)
        }

        binding.defaultCurrencyChange.setOnClickListener {
            dialogCurrency(BOTTOM_CURRENCY)
        }

        binding.buttonSwipe.setOnClickListener {
            swipeCurrency()
        }


        // klavatura tugmalari bosilishi
        binding.number0.setOnClickListener(this)
        binding.number1.setOnClickListener(this)
        binding.number2.setOnClickListener(this)
        binding.number3.setOnClickListener(this)
        binding.number4.setOnClickListener(this)
        binding.number5.setOnClickListener(this)
        binding.number6.setOnClickListener(this)
        binding.number7.setOnClickListener(this)
        binding.number8.setOnClickListener(this)
        binding.number9.setOnClickListener(this)
        binding.buttonPoint.setOnClickListener(this)


        binding.buttonClear.setOnClickListener {
            clearText()
        }

        binding.deleteLastValue.setOnClickListener {
            deleteLastValue()
        }


    }


    private fun setCurrenciesValues() {
        setTopCurrencyItem()
        setBottomCurrencyItem()

    }

    private fun setTopCurrencyItem() {
        binding.selectCurrencyName.text = topCurrencyName
        val value = formatStringTop(topCurrencyValue)
        binding.selectCurrencyValue.text = value
        adaptiveFontSize(value, binding.selectCurrencyValue)
        binding.selectFlag.setImageResource(getFlag(topCurrencyName))

    }

    private fun setBottomCurrencyItem() {
        binding.defaultCurrencyName.text = bottomCurrencyName

        val value =formatNumber(bottomCurrencyValue)
//        if (checkNumberLength(bottomCurrencyValue.toString())) {
//            bottomCurrencyValue.toString()
//        } else {
//            formatNumber(bottomCurrencyValue)
//        }

//        Toast.makeText(
//            binding.root.context,
//            checkNumberLength(bottomCurrencyValue.toString()).toString(),
//            Toast.LENGTH_SHORT
//        ).show()


        adaptiveFontSize(value, binding.defaultCurrencyValue)
//        binding.defaultCurrencyValue.text = bottomCurrencyValue.toString()
        binding.defaultCurrencyValue.text = value
        binding.defaultFlag.setImageResource(getFlag(bottomCurrencyName))
    }


    private fun swipeCurrency() {


        val topName = topCurrencyName
        val bottomName = bottomCurrencyName

        // top va bottom currency larni  o'zbek so'miga nisbatan qiymati
        var topValue = 1.0
        var bottomValue = 1.0

        topCurrencyName = bottomName
        bottomCurrencyName = topName


        topValue = if (topCurrencyName == "UZS") 1.0
        else list[determinePosition(topCurrencyName)].rate.toDouble()

        bottomValue = if (bottomCurrencyName == "UZS") 1.0
        else list[determinePosition(bottomCurrencyName)].rate.toDouble()


        // bottomCurrency ni yangi qiymati
        // faqat bottom currencini  qiymati o'zgaradi


        bottomCurrencyValue = (topValue / bottomValue) * topCurrencyValue.toDouble()
        setCurrenciesValues()


    }


    private fun dialogCurrency(currencyType: String) {
        val dialog = AlertDialog.Builder(binding.root.context).create()
        val view = layoutInflater.inflate(R.layout.currency_dialog, null)
        dialog.setView(view)
        dialog.setCancelable(false)
        dialog.show()
        val cancel = view.findViewById<TextView>(R.id.btn_cancel_dialog)
        val dialogRcView = view.findViewById<RecyclerView>(R.id.dialog_currency_rc_view)
        val dialogSearchView = view.findViewById<SearchView>(R.id.dialog_search_view)
        notFound = view.findViewById<ImageView>(R.id.not_found_dialog)

        cancel.setOnClickListener {
            dialog.dismiss()
        }


        adapter.updateAdapterItems(list, lang)
        dialogRcView.adapter = adapter
        adapter.setOnItemCurrencyClickListener(object : MenuAdapter.OnItemCurrencyClickListiner {
            override fun onItemClick(position: Int) {
                calculateCurrency(currencyType, list[position].ccy)
                dialog.dismiss()
            }

        })

        dialogSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val filterList = searchFilter(newText)
                adapter.setOnItemCurrencyClickListener(object :
                    MenuAdapter.OnItemCurrencyClickListiner {
                    override fun onItemClick(position: Int) {
                        calculateCurrency(currencyType, filterList[position].ccy)
                        dialog.dismiss()
                    }
                })
                return true
            }
        })


    }

    private fun calculateCurrency(currencyType: String, currencyName: String) {
        var topCurrencyRateUZS = 1.0
        var bottomCurrencyRateUZS = 1.0

        when (currencyType) {
            TOP_CURRENCY -> {
                // top currency nomini olish
                topCurrencyName = currencyName

                // top currency ni o'zbek so'mida qiymatini aniqlash
                topCurrencyRateUZS = if (topCurrencyName == "UZS") 1.0
                else
                    list[determinePosition(topCurrencyName)].rate.toDouble()

                // agar bottom currency UZS bo'lsa listda UZS ni UZS ga nisbatab qiymati yo'q shuning uchun
                // bottomCurrencyRateUZS UZS ni UZS ga nisbatan qiymati 1.0 deb belgilaymiz
                // agarda bottom currency  UZS ga teng bo'lmasa uni UZS ga nisbatan qiymatini aniqlaymiz
                bottomCurrencyRateUZS = if (bottomCurrencyName == "UZS") {
                    1.0
                } else {
                    list[determinePosition(bottomCurrencyName)].rate.toDouble()

                }
            }

            BOTTOM_CURRENCY -> {
                // bottom currency nomini olish
                bottomCurrencyName = currencyName

                // bottom currency ni o'zbek so'mida qiymatini aniqlash

                bottomCurrencyRateUZS =
                    list[determinePosition(bottomCurrencyName)].rate.toDouble()

                // agar top currency UZS bo'lsa listda UZS ni UZS ga nisbatab qiymati yo'q shuning uchun
                // topCurrencyRateUZS UZS ni UZS ga nisbatan qiymati 1.0 deb belgilaymiz
                // agarda top currency  UZS ga teng bo'lmasa uni UZS ga nisbatan qiymatini aniqlaymiz


                topCurrencyRateUZS = if (topCurrencyName == "UZS") {
                    1.0
                } else {
                    list[determinePosition(topCurrencyName)].rate.toDouble()
                }

            }
        }
        //   currency almashganda   faqat gina bottom currencyni qiymatini
        // o'zgaradi a top currencyni qiymati esa o'zgarmaydi
        // (chunki top currencyni qiymati qachonki user tomonidan edit qilinsa o'zgaradi,
        // bu yerda taqqososlash bottom currencyni  top currency ga nisbatan qiymati ko'ramiz)
        // bottom currencyni top currencyga nisbatan qiymatini top va bottom currencilarni
        // oldin UZS ga nisbatan qiymatlarini aniqlab ularni nisbatini (bo'linmasini)
        //top value ga ko'paytiramiz

        bottomCurrencyValue =
            ((topCurrencyRateUZS / bottomCurrencyRateUZS) * topCurrencyValue.toDouble())

        // ikkala holatni UI set qilish

        setCurrenciesValues()
//        setTopCurrencyItem()
//        setBottomCurrencyItem()
    }

    private fun searchFilter(text: String?): ArrayList<CurrencyItem> {
        val filteredlist: ArrayList<CurrencyItem> = ArrayList()
        for (item in list) {
            if (item.ccy.lowercase(Locale.ROOT).contains(text!!.lowercase(Locale.ROOT))) {
                notFound.visibility = View.GONE
                filteredlist.add(item)
            } else if (filteredlist.isEmpty()) {
                notFound.visibility = View.VISIBLE
                adapter.updateAdapterItems(filteredlist, lang)
            }
        }
        adapter.updateAdapterItems(filteredlist, lang)
        return filteredlist


    }


    private fun determinePosition(ccv: String): Int {
        for (i in list.indices)
            if (list[i].ccy == ccv)
                return i

        return -1
    }


    private fun clearText() {
        binding.selectCurrencyValue.text = "1.0"
        editTopValue = ""
        topCurrencyValue = "1.0"
        calculateCurrency(TOP_CURRENCY, topCurrencyName)

    }

    private fun deleteLastValue() {


        var value = editTopValue
        if (value.isNotEmpty()) {
            if (value.length == 1) {
                editTopValue = ""
            } else {
                value = value.substring(0, value.length - 1)

                if (value[value.length - 1] == '.') value = value.substring(0, value.length - 1)

                editTopValue = value
            }

            if (editTopValue.isEmpty()) {
                binding.selectCurrencyValue.text = "1.0"
                topCurrencyValue = "1.0"
            } else {
                binding.selectCurrencyValue.text = editTopValue
                topCurrencyValue = editTopValue
            }

            calculateCurrency(TOP_CURRENCY, topCurrencyName)
        }

    }


    override fun onClick(v: View?) {
        val message = determineNumberStatus()
        if (numberStatus) {
            val button = v as Button
            val tagInt: Int = Integer.parseInt(button.tag.toString())

            var newTopValue = ""

            if (tagInt == 0) {// nol raqami bosilishi
                if (editTopValue != "0") {
                    newTopValue = "0"
                }
            } else if (tagInt == 10) { // nuqta bosilishi
                if (editTopValue.isEmpty()) {
                    newTopValue = "0."
                } else if (!editTopValue.contains('.')) {// satrda nuqta bo'lmasa
                    newTopValue = "."
                }
            } else {
                if(editTopValue=="0") editTopValue=""
                 newTopValue = tagInt.toString()
            }

            editTopValue += newTopValue

//        binding.selectCurrencyValue.text = editTopValue


            topCurrencyValue = editTopValue



            calculateCurrency(TOP_CURRENCY, topCurrencyName)
        } else {
            customToast.showToast(message)
            numberStatus = true

        }

    }


    private fun adaptiveFontSize(value: String, view: TextView) {
        val textView = view as TextView

        if (value.length >= 18) {
            textView.textSize=resources.getDimension(R.dimen.d_currency_small_text)
        } else if (value.length in 13..17) {
            textView.textSize=resources.getDimension(R.dimen.d_currency_normal_text)
        } else {
            textView.textSize=resources.getDimension(R.dimen.d_currency_large_text)

        }
    }

    private fun determineNumberStatus(): String {
        val topValue = binding.selectCurrencyValue.text.toString().trim()
        val pointRightStatus = checkPointRightCondition(topValue)
        var result = ""
        val topResult = StringBuilder()
        topValue.forEach {
            if (it != ' ') topResult.append(it)
        }

        topResult.toString()


        val limitNumber = if (topResult.contains('.')) 16
        else 15


        if (pointRightStatus != "") {
            numberStatus = false
            result = binding.root.context.getString(R.string.point_right_message)
        } else if (topResult.length >= limitNumber) {
            numberStatus = false
            result = binding.root.context.getString(R.string.number_size_message)
        }

        return result
    }


    override fun onPause() {
        super.onPause()
        customToast.cancelTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        customToast.cancelTimer()
        _binding = null
    }
}