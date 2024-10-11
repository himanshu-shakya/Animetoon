package com.animetoon.app

import android.app.Application
import com.animetoon.app.di.module
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class AnimetoonApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AnimetoonApp)
            modules(module)
        }
    }
}