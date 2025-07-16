package com.cdc.currencylistdemo.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.cdc.currencylistdemo.R
import com.cdc.currencylistdemo.domain.model.CurrencyInfo

class CustomSuggestionAdapter(
    context: Context,
) : ArrayAdapter<CurrencyInfo>(context, 0, mutableListOf()) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        Log.d("cdc", "getView called for position: $position, item: ${getItem(position)}")
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

        return view
    }

    fun updateItems(newItems: List<CurrencyInfo>) {
        clear()
        addAll(newItems)
        notifyDataSetChanged()
    }
}