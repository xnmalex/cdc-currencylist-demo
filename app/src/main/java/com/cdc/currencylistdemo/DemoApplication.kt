package com.cdc.currencylistdemo

import android.app.Application
import com.cdc.currencylistdemo.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class DemoApplication : Application(){
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@DemoApplication)
            modules(appModule) // <-- your Koin module
        }
    }
}