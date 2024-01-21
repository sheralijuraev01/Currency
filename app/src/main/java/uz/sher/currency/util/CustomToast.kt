package uz.sher.currency.util

import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.widget.Toast
import uz.sher.currency.databinding.CustomToastItemBinding


class CustomToast(context: Context) {

    private var isToastVisible = false
    private lateinit var countDownTimer: CountDownTimer


    private val binding = CustomToastItemBinding.inflate(LayoutInflater.from(context))

    private val toast = Toast(context)

    init {
        toast.duration = Toast.LENGTH_SHORT
        toast.view = binding.root
    }


    fun showToast(message: String) {
        if (!isToastVisible) {
            // Show the toast
            binding.root.text = message
//                binding.root.context.getDrawable(R.drawable.danger_button)
            toast.show()
//            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

            isToastVisible = true

            // Set a countdown timer to reset isToastVisible after a certain duration (e.g., 3 seconds)
            countDownTimer = object : CountDownTimer(2000, 1000) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    // Reset isToastVisible when the countdown timer finishes
                    isToastVisible = false
                }
            }.start()
        }
    }

    fun cancelTimer() {
        // Cancel the countdown timer when needed
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }
}