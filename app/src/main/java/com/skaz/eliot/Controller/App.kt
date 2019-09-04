package com.skaz.eliot.Controller

import android.app.Application
import com.skaz.eliot.Utilities.SharedPrefs

class App : Application()  {
    companion object {
        lateinit var prefs: SharedPrefs
    }

    override fun onCreate() {
        prefs = SharedPrefs(applicationContext)
        super.onCreate()
    }
}