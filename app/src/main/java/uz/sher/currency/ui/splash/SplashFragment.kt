package uz.sher.currency.ui.splash

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import uz.sher.currency.R
import uz.sher.currency.databinding.FragmentSplashBinding


class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val count: CountDownTimer = object : CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                val editor =
                    binding.root.context.getSharedPreferences("isloggin", Context.MODE_PRIVATE)
                val isLoggin = editor.getBoolean("islog", false)
//                if(isLoggin){
                findNavController().navigate(R.id.action_splashFragment_to_menuFragment)
//                }else{
//                    parentFragmentManager.beginTransaction().replace(R.id.fragment_container,
//                        LanguageFragment()
//                    ).addToBackStack(null).commit()
//                }
            }

        }.start()


    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}