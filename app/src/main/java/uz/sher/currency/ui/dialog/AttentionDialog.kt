package uz.sher.currency.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import uz.sher.currency.databinding.AttentionDialogBinding

class AttentionDialog(
    context: Context,
    private val message: String,
    private val lottie: Int,
    var buttonStatus: (Boolean) -> Unit
) : BaseCustomDialog(context) {
    private var clickGlobalTime = 0L
    private val binding = AttentionDialogBinding.inflate(LayoutInflater.from(context))

    init {

        binding.apply {
            dialogMessage.text = message
            dialogAnim.setAnimation(lottie)

            val clickTime = System.currentTimeMillis()
            if (clickTime - clickGlobalTime > 500)
                btnRetry.setOnClickListener {
                    buttonStatus(true)
                    dismiss()
                }

            clickGlobalTime = clickTime

        }


        setCancelable(false)
        setContentView(binding.root)

    }

}