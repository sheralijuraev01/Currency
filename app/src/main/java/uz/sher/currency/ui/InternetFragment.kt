package uz.sher.currency.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import uz.sher.currency.databinding.FragmentInternetBinding


private const val fragment_name = "frag_name"


class InternetFragment : Fragment() {
    private var _binding: FragmentInternetBinding? = null
    private val binding get() = _binding!!
    private lateinit var fragmentName: String
    private var currencyName: String = "USD"
    private var defaultName: String = "UZS"
    private var nominal: String = "1"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentInternetBinding.inflate(inflater, container, false)
        return _binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            fragmentName = it.getString(fragment_name)!!
        }

//        binding.internetSwipeRefreshLayout.setOnRefreshListener {
//            if (CheckInternet.checkInternet(binding.root.context)) {
//
//                when (fragmentName) {
//                    Constants.MENU_FRAGMENT -> {
//                        parentFragmentManager.beginTransaction()
//                            .replace(R.id.convert_fragment_container, MenuFragment()).commit()
//                    }
//                    Constants.CONVERT_FRAGMENT -> {
//                        getSharedPreferenceItems()
//                        parentFragmentManager.beginTransaction()
//                            .replace(
//                                R.id.convert_fragment_container,
//                                ConvertFragment.putPosition(currencyName, defaultName, nominal)
//                            )
//                            .commit()
//                    }
//                    Constants.EDIT_FRAGMENT -> {
//                        getSharedPreferenceItems()
//                        parentFragmentManager.beginTransaction()
//                            .replace(
//                                R.id.convert_fragment_container,
//                                EditFragment.putPositionEdit(currencyName, defaultName, nominal)
//                            )
//                            .commit()
//                    }
//
//                }
//
//            }
//            binding.internetSwipeRefreshLayout.isRefreshing = false
//        }
    }


    private fun getSharedPreferenceItems() {
        val getSharedPreferences =
            binding.root.context.getSharedPreferences("currentValues", Context.MODE_PRIVATE)

        currencyName = getSharedPreferences.getString("convertCurrency", "USD").toString()
        currencyName = getSharedPreferences.getString("defaultCurrency", "UZS").toString()
        currencyName = getSharedPreferences.getString("nominal", "1").toString()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun putFragmentName(fragmentName: String) = InternetFragment().apply {
            arguments = Bundle().apply {
                putString(fragment_name, fragmentName)
            }


        }
    }

}