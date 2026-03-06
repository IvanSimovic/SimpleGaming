package com.simovic.simplegaming.base

import android.app.Application
import com.simovic.simplegaming.BuildConfig
import com.simovic.simplegaming.feature.auth.featureAuthModules
import com.simovic.simplegaming.feature.games.featureGamesModules
import com.simovic.simplegaming.feature.reels.featureReelsModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import timber.log.Timber

class Application : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin()
        initTimber()
    }

    private fun initKoin() {
        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@Application)

            modules(appModule)
            modules(featureAuthModules)
            modules(featureGamesModules)
            modules(featureReelsModules)
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
