package uz.sher.currency.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.sher.currency.data.remote.models.CurrencyItem
import uz.sher.currency.databinding.RowItemRcListBinding
import uz.sher.currency.util.functions.Functions.getFlag


class MenuAdapter() : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {
    private var list: MutableList<CurrencyItem> = ArrayList()
    private var language: String = "en"
    private var listener: OnItemCurrencyClickListiner? = null

    inner class ViewHolder(private val binding: RowItemRcListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(currencyItem: CurrencyItem) {
            binding.rowCurrencyName.text = currencyItem.ccy
            if (language == "uz") {
                binding.rowCurrencyDesp.text = currencyItem.ccyNmUZ
            } else {
                binding.rowCurrencyDesp.text = currencyItem.ccyNmEN
            }
            binding.currencyIcon.setImageResource(getFlag(currencyItem.ccy))
            binding.root.setOnClickListener {
                listener?.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemRcListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnItemCurrencyClickListener(listener: OnItemCurrencyClickListiner) {
        this.listener = listener

    }

    interface OnItemCurrencyClickListiner {
        fun onItemClick(position: Int)

    }


    fun updateAdapterItems(list: List<CurrencyItem>, lang: String) {
        this.list.clear()
        this.list.addAll(list)
        this.language = lang
        notifyDataSetChanged()
    }
}