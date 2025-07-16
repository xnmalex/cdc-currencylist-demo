package com.cdc.currencylistdemo.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.cdc.currencylistdemo.R
import com.cdc.currencylistdemo.domain.model.CurrencyInfo

class CustomSuggestionAdapter(
    context: Context,
) : ArrayAdapter<CurrencyInfo>(context, 0, mutableListOf()) {

    private val list = mutableListOf<CurrencyInfo>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val suggestion = getItem(position)

        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.item_search_suggestion, parent, false
        )

        val avatarText = view.findViewById<TextView>(R.id.tvAvatar)
        val nameText = view.findViewById<TextView>(R.id.tvName)
        val symbolText = view.findViewById<TextView>(R.id.tvSymbol)

        avatarText.text = suggestion?.name?.firstOrNull()?.uppercaseChar()?.toString() ?: "?"
        nameText.text = suggestion?.name ?: ""
        symbolText.text = suggestion?.symbol ?: ""

        symbolText.visibility = if(suggestion?.type == "crypto") View.VISIBLE else  View.GONE

        return view
    }

    override fun getItem(position: Int): CurrencyInfo? {
        return super.getItem(position)
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                val suggestions = mutableListOf<CurrencyInfo>()

                // Always return all items from the adapter
                suggestions.addAll(list)

                results.values = suggestions
                results.count = suggestions.size
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                clear()
                if (results != null && results.count > 0) {
                    @Suppress("UNCHECKED_CAST")
                    addAll(results.values as List<CurrencyInfo>)
                }
                notifyDataSetChanged()
            }
        }
    }

    fun updateItems(newItems: List<CurrencyInfo>) {
        clear()
        list.clear()
        list.addAll(newItems)
        addAll(newItems)
        notifyDataSetChanged()
    }
}