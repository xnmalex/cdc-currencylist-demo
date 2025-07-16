package com.cdc.currencylistdemo.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cdc.currencylistdemo.R
import com.cdc.currencylistdemo.domain.model.CurrencyInfo
import com.cdc.currencylistdemo.ui.viewmodel.CurrencyViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DemoActivity : AppCompatActivity() {
    private val viewModel : CurrencyViewModel by viewModel()
    private var currencyType: String = "crypto"
    private var userRequestedCurrency = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        setUpButtons()
        observeCurrencyState()

        supportFragmentManager.addOnBackStackChangedListener{
            val isCurrencyListVisible = supportFragmentManager.findFragmentById(R.id.fragmentContainer) is CurrencyListFragment

            if (!isCurrencyListVisible) {
                toggleButtonAndFragmentView(showFragment = false)
            }
        }
    }

    private fun setUpButtons(){
        findViewById<Button>(R.id.clear_db_btn).setOnClickListener {
            viewModel.clearCurrencies()
        }

        findViewById<Button>(R.id.insert_db_btn).setOnClickListener {
            viewModel.loadCurrenciesFromAsset()
        }

        findViewById<Button>(R.id.crypto_list_btn).setOnClickListener {
            currencyType = "crypto"
            userRequestedCurrency = true
            viewModel.loadCurrencyByType(currencyType)
        }

        findViewById<Button>(R.id.fiat_list_btn).setOnClickListener {
            currencyType = "fiat"
            userRequestedCurrency = true
            viewModel.loadCurrencyByType(currencyType)
        }

        findViewById<Button>(R.id.purchase_list_btn).setOnClickListener {
            userRequestedCurrency = true
            viewModel.getAllPurchasableCurrencies()
        }
    }

    private fun openCurrencyListFragment(currencyList: List<CurrencyInfo>) {
        val fragment = CurrencyListFragment.newInstance(ArrayList(currencyList), currencyType)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commitAllowingStateLoss()

        toggleButtonAndFragmentView(showFragment = true)
    }

    private fun observeCurrencyState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.currencyListFlow.collectLatest  { list ->
                        if(userRequestedCurrency) {
                            openCurrencyListFragment(list)
                            userRequestedCurrency = false
                        }
                    }
                }

                launch{
                    viewModel.msgFlow.collect {
                        toastMessage(it)
                    }
                }
            }
        }
    }

    private fun toastMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun toggleButtonAndFragmentView(showFragment:Boolean){
        val buttonLayout = findViewById<LinearLayout>(R.id.button_layout)
        val fragmentLayout = findViewById<FrameLayout>(R.id.fragmentContainer)

        buttonLayout.visibility = if(showFragment) View.GONE else View.VISIBLE
        fragmentLayout.visibility =if(showFragment) View.VISIBLE else View.GONE
    }
}