package com.skaz.eliot.Controller

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.skaz.eliot.R
import com.skaz.eliot.Services.AuthService
import com.skaz.eliot.Services.DataService
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableSpinner(false)

        loginEmailTxt.setText(App.prefs.userEmail)
        loginPasswordText.setText(App.prefs.password)

        if (!AuthService.isLoggedIn && !AuthService.isLoggedOut && loginEmailTxt.text.isNotEmpty() && loginPasswordText.text.isNotEmpty()) {
            login()
        }
    }

    fun deviceInfo() {
        DataService.deviceRequest(AuthService.authToken) { response ->
            if (response) {

                Timer("SettingUp", false).schedule(2500) {
                   spinnerStop()
                    nextActivity()
                }

            } else {
                enableSpinner(false)
                Toast.makeText(this, "Не удалось войти, введите логин и пароль", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun spinnerStop() {
        Thread(Runnable {
            this@MainActivity.runOnUiThread {
                enableSpinner(false)
            }
        }).start()
    }

    fun login() {
        enableSpinner(true)
        val login = loginEmailTxt.text.toString()
        val password = loginPasswordText.text.toString()
        hideKeyboard()
        Toast.makeText(this, "Выполняется вход в приложение", Toast.LENGTH_LONG).show()
        if (login.isNotEmpty() && password.isNotEmpty()) {

            AuthService.loginRequest(login, password) { loginSuccess ->
                if (loginSuccess) {
                    App.prefs.userEmail = login
                    App.prefs.password = password
                    AuthService.userInfoRequest(AuthService.authToken, this) { getSession ->
                        if (getSession) {
                            AuthService.isLoggedIn = true
                            AuthService.isLoggedOut = false
                            DataService.deviceRequest(AuthService.authToken){ complete ->
                            }
                        }
                    }

                    Timer("SettingUp", false).schedule(2500) {
                        spinnerStop()
                        nextActivity()
                    }
                } else {
                    Toast.makeText(this, "Error: Wrong password or email", Toast.LENGTH_LONG).show()
                    errorToast()
                }
            }
        } else {
            enableSpinner(false)
            Toast.makeText(this, "Please fill in both email and password", Toast.LENGTH_LONG).show()
        }
    }

    fun loginBtnClicked(view: View) {
        login()
    }

    fun nextActivity() {
        val deviceIntent = Intent(this, DeviceActivity::class.java)
        startActivity(deviceIntent)
        finish()
    }

    fun enableSpinner(enable: Boolean) {
        if (enable) {
            loginSpinner.visibility = View.VISIBLE
        } else {
            loginSpinner.visibility = View.INVISIBLE
        }
    }

    fun hideKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputManager.isAcceptingText) {
            if (inputManager.isAcceptingText) {
                inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
            }
        }
    }

    fun errorToast() {
        Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }
}