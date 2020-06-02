package com.skaz.eliot.Utilities

import android.content.Context
import android.content.SharedPreferences
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley


class SharedPrefs(context: Context) {
    val PREFS_FILENAME = "prefs"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)
    val LOGIN = "login"
    val PASSWORD = "password"
    val SESSION = "session"

    var userEmail: String
        get() = prefs.getString(LOGIN, "") ?: ""
        set(value) = prefs.edit().putString(LOGIN, value).apply()

    var password: String
        get() = prefs.getString(PASSWORD, "") ?: ""
        set(value) = prefs.edit().putString(PASSWORD, value).apply()

    var session: String
        get() = prefs.getString(SESSION, "") ?: ""
        set(value) = prefs.edit().putString(SESSION, value).apply()

    val requestQueue: RequestQueue = Volley.newRequestQueue(context)
}

