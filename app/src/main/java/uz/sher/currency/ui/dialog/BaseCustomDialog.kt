package uz.sher.currency.ui.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager

open class BaseCustomDialog(context: Context) : Dialog(context){
    init {
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window!!.attributes.gravity = Gravity.CENTER

    }
}