package com.cdc.currencylistdemo.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cdc.currencylistdemo.R
import com.cdc.currencylistdemo.domain.model.CurrencyInfo
import com.cdc.currencylistdemo.ui.adapter.CurrencyAdapter
import com.cdc.currencylistdemo.ui.adapter.CustomSuggestionAdapter
import com.cdc.currencylistdemo.ui.viewmodel.CurrencyViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrencyListFragment : Fragment() {

    private val viewModel : CurrencyViewModel by viewModel()

    companion object {
        private const val ARG_CURRENCY_LIST = "arg_currency_list"
        private const val ARG_CURRENCY_TYPE = "arg_currency_type"

        fun newInstance(currencyList: ArrayList<CurrencyInfo>, currencyType: String): CurrencyListFragment {
            return CurrencyListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_CURRENCY_LIST, currencyList)
                    putString(ARG_CURRENCY_TYPE, currencyType)
                }
            }
        }
    }

    private lateinit var currencyList: List<CurrencyInfo>
    private lateinit var currencyType: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: View
    private lateinit var searchEmptyView: View
    private lateinit var adapter: CurrencyAdapter
    private lateinit var suggestionAdapter: CustomSuggestionAdapter
    private var isSearchActive = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currencyList = arguments?.getParcelableArrayList<CurrencyInfo>(ARG_CURRENCY_LIST) ?: emptyList()
        currencyType = arguments?.getString(ARG_CURRENCY_TYPE) ?: "crypto"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_currency_list, container, false)

        recyclerView = view.findViewById(R.id.currencyRecyclerView)
        emptyView = view.findViewById(R.id.emptyView)
        searchEmptyView = view.findViewById(R.id.searchEmptyView)

        adapter = CurrencyAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        observeData()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        // Setup suggestion adapter for SearchView dropdown
        suggestionAdapter = CustomSuggestionAdapter(requireContext())

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_currency_list, menu)

                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem.actionView as SearchView

                searchView.maxWidth = Integer.MAX_VALUE

                val autoCompleteTextView = searchView.findViewById<AutoCompleteTextView>(androidx.appcompat.R.id.search_src_text)
                autoCompleteTextView.setTextColor(Color.BLACK)
                autoCompleteTextView.setHintTextColor(Color.GRAY)
                autoCompleteTextView.setAdapter(suggestionAdapter)
                autoCompleteTextView.threshold = 1
                autoCompleteTextView.dropDownWidth = ViewGroup.LayoutParams.MATCH_PARENT

                searchView.queryHint = "Search currencies..."
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        query?.let {
                            viewModel.searchCurrencies(it, currencyType)
                        }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        viewModel.searchCurrencies(newText ?: "", currencyType)
                        return true
                    }
                })

                searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
                    isSearchActive = hasFocus
                    updateUiState()
                }

                searchView.setOnCloseListener {
                    isSearchActive = false
                    updateUiState()
                    false
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_search -> {
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.suggestionFlow.collect { suggestions ->
                    suggestionAdapter.updateItems(suggestions)
                    updateUiState(suggestions)
                }
            }
        }
    }

    private fun observeData() {
        adapter.submitList(currencyList)
    }

    private fun updateEmptyViewVisibility(isEmpty: Boolean) {
        recyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
        emptyView.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun updateUiState(suggestions: List<CurrencyInfo>? = null){
        if (isSearchActive) {
            recyclerView.visibility = View.GONE
            emptyView.visibility = View.GONE
            searchEmptyView.visibility = if (suggestions.isNullOrEmpty()) View.VISIBLE else View.GONE
        } else {
            updateEmptyViewVisibility(currencyList.isEmpty())
            searchEmptyView.visibility = View.GONE
        }
    }
}