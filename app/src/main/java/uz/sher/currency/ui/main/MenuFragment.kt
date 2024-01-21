package uz.sher.currency.ui.main

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.sher.currency.R
import uz.sher.currency.adapters.MenuAdapter
import uz.sher.currency.data.remote.models.CurrencyItem
import uz.sher.currency.databinding.FragmentMenuBinding
import uz.sher.currency.ui.dialog.AttentionDialog
import uz.sher.currency.ui.vm.MainViewModel
import uz.sher.currency.util.constans.Constants.Companion.CHOOSE_CURRENCY
import uz.sher.currency.util.constans.Constants.Companion.LIST_CURRENCY
import uz.sher.currency.util.resource.Resource
import java.util.Locale


@AndroidEntryPoint
class MenuFragment : Fragment(R.layout.fragment_menu)  {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private var list: MutableList<CurrencyItem> = ArrayList()
    private lateinit var menuAdapter: MenuAdapter
    private var lang: String = "en"

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMenuBinding.bind(view)



        currencyObserve()

        menuAdapter = MenuAdapter()



        lang = mainViewModel.getLang()

        val animation = AnimationUtils.loadAnimation(binding.root.context, R.anim.rotate_icon)

        binding.settingIcon.setOnClickListener {
            binding.settingIcon.startAnimation(animation)
            findNavController().navigate(R.id.action_menuFragment_to_languageFragment)
        }


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchFilter(newText)
                return true
            }
        })

        binding.updateContainer.setOnClickListener {
            currencyObserve()
            val anim = AnimationUtils.loadAnimation(binding.root.context, R.anim.rotate_icon_360)
            anim.interpolator = LinearInterpolator()
            binding.refreshIcon.startAnimation(anim)

            binding.updateContainer.isEnabled = false
        }

    }


    private fun currencyObserve() {

        mainViewModel.getCurrencies().observe(requireActivity()) {
            when (it) {
                is Resource.Failure -> menuFailureObserve(it.t)
                is Resource.Loading -> menuLoadingObserve()
                is Resource.Network -> menuNetworkObserve(it.t)
                is Resource.Success -> menuSuccessObserve(it.data)
            }
        }


    }

    private fun menuSuccessObserve(dataList: List<CurrencyItem>) {
        unProgress()
        this.list.clear()
        this.list.addAll(dataList)
        menuAdapter.updateAdapterItems(list, lang)
        binding.menuCurrencyRcView.adapter = menuAdapter
        val text=binding.root.context.getString(R.string.last_update)
        binding.updateDate.text = "$text ${list[0].date}"
        clickListItem(list)
    }

    private fun menuNetworkObserve(message: String) {
        unProgress()
        errorDialog(message, R.raw.no_internet_connection)
    }


    private fun menuLoadingObserve() {
        binding.menuProgressContainer.visibility = View.VISIBLE

    }

    private fun menuFailureObserve(message: String) {
        unProgress()
        errorDialog(message, R.raw.server_error)

    }

    private fun errorDialog(message: String, lottieStatus: Int) {


        val dialog = AttentionDialog(
            binding.root.context, message = message, lottie = lottieStatus
        ) { buttonStatus ->
            if (buttonStatus) currencyObserve()
        }
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog.show()
    }


    private fun searchFilter(text: String?) {
        val filteredList: ArrayList<CurrencyItem> = ArrayList()
        for (item in list) {
            if (item.ccy.lowercase(Locale.ROOT).contains(text!!.lowercase(Locale.ROOT))) {
                binding.notFound.visibility = View.GONE
                filteredList.add(item)
            } else if (filteredList.isEmpty()) {
                menuAdapter.updateAdapterItems(filteredList, lang)
                binding.notFound.visibility = View.VISIBLE
            }
        }
        menuAdapter.updateAdapterItems(filteredList, lang)
        clickListItem(filteredList)


    }

    private fun unProgress() {
        binding.updateContainer.isEnabled = true
        binding.refreshIcon.clearAnimation()
        binding.menuProgressContainer.visibility = View.GONE

    }


    private fun clickListItem(itemList: List<CurrencyItem>) {
        menuAdapter.setOnItemCurrencyClickListener(object :
            MenuAdapter.OnItemCurrencyClickListiner {
            override fun onItemClick(position: Int) {
                binding.searchView.setQuery("", false)
                binding.searchView.clearFocus()
                navigateConvertFragment(itemList[position].ccy)
            }
        })
    }



    private fun navigateConvertFragment(currencyName: String) {
        val bundle = Bundle()
        bundle.putParcelableArrayList(
            LIST_CURRENCY,
            list as java.util.ArrayList<out Parcelable>
        )
        bundle.putString(CHOOSE_CURRENCY, currencyName)

        findNavController().navigate(R.id.action_menuFragment_to_convertFragment, bundle)
        binding.refreshIcon.clearAnimation()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
