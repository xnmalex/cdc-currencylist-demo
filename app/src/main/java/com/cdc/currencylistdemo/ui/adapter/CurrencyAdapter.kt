package com.cdc.currencylistdemo.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cdc.currencylistdemo.databinding.ItemCurrencyBinding
import com.cdc.currencylistdemo.domain.model.CurrencyInfo

class CurrencyAdapter(
    private var items: List<CurrencyInfo> = listOf(),
    private val onItemClick: ((CurrencyInfo) -> Unit)? = null
) : RecyclerView.Adapter<CurrencyAdapter.CurrencyViewHolder>() {

    inner class CurrencyViewHolder(private val binding: ItemCurrencyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CurrencyInfo) {
            binding.tvAvatar.text = item.name.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
            binding.tvName.text = item.name
            binding.tvSymbol.text = item.symbol

            binding.tvSymbol.visibility = if(item.type == "crypto") View.VISIBLE else View.GONE

            binding.root.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder  {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCurrencyBinding.inflate(inflater, parent, false)
        return CurrencyViewHolder(binding)

    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun submitList(data: List<CurrencyInfo>) {
        items = data
        notifyDataSetChanged()
    }

}