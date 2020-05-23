package com.skaz.eliot.Controller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.skaz.eliot.R
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.skaz.eliot.Model.Device
import com.skaz.eliot.Model.DeviceInfo
import com.skaz.eliot.Services.AuthService
import com.skaz.eliot.Services.DataService
import com.skaz.eliot.Services.UserDataService
import com.skaz.eliot.Utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import java.util.*
import kotlin.concurrent.schedule



class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loginSpinner.visibility = View.INVISIBLE

        if (App.prefs.isLoggedIn) {

            enableSpinner(true)
            Toast.makeText(this, "Выполняется вход в приложение", Toast.LENGTH_LONG).show()

                    AuthService.userInfoRequest(App.prefs.authToken, this) { getSession ->
                        if (getSession) {
                            deviceInfo()
                        } else {
                            Toast.makeText(this, "Не удалось войти, введите логин и пароль", Toast.LENGTH_LONG).show()
                            enableSpinner(false)
                        }
                    }
                } else {
            enableSpinner(false)
          //  Toast.makeText(this, "Не удалось войти, введите логин и пароль", Toast.LENGTH_LONG).show()
                }

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        when (month) {
            0 -> UserDataService.ruStartMonth = "янв."
            1 -> UserDataService.ruStartMonth = "фев."
            2 -> UserDataService.ruStartMonth = "мар."
            3 -> UserDataService.ruStartMonth = "апр."
            4 -> UserDataService.ruStartMonth = "май."
            5 -> UserDataService.ruStartMonth = "июн."
            6 -> UserDataService.ruStartMonth = "июл."
            7 -> UserDataService.ruStartMonth = "авг."
            8 -> UserDataService.ruStartMonth = "сен."
            9 -> UserDataService.ruStartMonth = "окт."
            10 -> UserDataService.ruStartMonth = "ноя."
            11 -> UserDataService.ruStartMonth = "дек."
            else -> UserDataService.ruStartMonth = "янв."
        }

      UserDataService.defaultEndDate = ("" + day + " " + (UserDataService.ruStartMonth) + " " + year)
        UserDataService.defaultEndDateSend = ("" + year + "-" + (month + 1) + "-" + day)
    }



    fun deviceInfo() {
        DataService.deviceRequest(App.prefs.authToken) { response ->
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
            // performing some dummy time taking operation
            // try to touch View of UI thread
            this@MainActivity.runOnUiThread {
                enableSpinner(false)
            }
        }).start()
    }

    fun loginBtnClicked(view: View) {
        enableSpinner(true)
        val login = loginEmailTxt.text.toString()
        val password = loginPasswordText.text.toString()
        hideKeyboard()
        if (login.isNotEmpty() && password.isNotEmpty()) {

            AuthService.loginRequest(login, password) { loginSuccess ->
                    if (loginSuccess) {

                        AuthService.userInfoRequest(App.prefs.authToken, this) { getSession ->
                            if (getSession) {
                                App.prefs.isLoggedIn = true
                                DataService.deviceRequest(App.prefs.authToken){ complete ->
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