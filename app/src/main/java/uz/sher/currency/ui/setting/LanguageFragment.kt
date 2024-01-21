package uz.sher.currency.ui.setting


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.sher.currency.MainActivity
import uz.sher.currency.R
import uz.sher.currency.databinding.FragmentLanguageBinding
import uz.sher.currency.ui.vm.MainViewModel
import uz.sher.currency.util.Localization.changeLan

@AndroidEntryPoint
class LanguageFragment : Fragment(R.layout.fragment_language) {

    private var _binding: FragmentLanguageBinding? = null
    private val binding get() = _binding!!
    private var lang = ""

    private val mainViewModel: MainViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentLanguageBinding.bind(view)

        lang = mainViewModel.getLang()

        if (lang == "uz") {
            binding.btnRadioUzbek.isChecked = true
            binding.btnRadioEnglish.isChecked = false
        } else {
            binding.btnRadioUzbek.isChecked = false
            binding.btnRadioEnglish.isChecked = true
        }


        binding.englishLanguageContainer.setOnClickListener {
            binding.btnRadioEnglish.isChecked = true
            binding.btnRadioUzbek.isChecked = false
            lang = "en"
        }
        binding.uzbekLanguageContainer.setOnClickListener {
            binding.btnRadioUzbek.isChecked = true
            binding.btnRadioEnglish.isChecked = false
            lang = "uz"
        }
        binding.btnRadioEnglish.setOnClickListener {
            binding.btnRadioEnglish.isChecked = true
            binding.btnRadioUzbek.isChecked = false
            lang = "en"
        }
        binding.btnRadioUzbek.setOnClickListener {
            binding.btnRadioUzbek.isChecked = true
            binding.btnRadioEnglish.isChecked = false
            lang = "uz"
        }

        binding.btnStart.setOnClickListener {
            mainViewModel.saveLang(lang)

            changeLan(lang, requireActivity())
            val intent = Intent(requireActivity(), MainActivity::class.java)
            requireActivity().finishAffinity()
            startActivity(intent)
        }

        binding.backLanguageBtn.setOnClickListener {
            findNavController().popBackStack()
        }


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}