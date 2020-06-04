package com.skaz.eliot.Controller

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.credentials.*
import com.google.android.gms.auth.api.credentials.CredentialRequest
import com.google.android.gms.common.api.ResolvableApiException
import com.skaz.eliot.Model.LoginRequest
import com.skaz.eliot.Model.UserInfoRequest
import com.skaz.eliot.R
import com.skaz.eliot.Services.DataService
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    val RC_SAVE = 11
    val RC_HINT = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableSpinner(false)

        if (App.prefs.session.isNotEmpty()) {
            trySingInSession()
            //https://developers.google.com/identity/smartlock-passwords/android/retrieve-credentials
            //https://developers.google.com/identity/smartlock-passwords/android/store-credentials
            //https://developers.google.com/identity/smartlock-passwords/android/retrieve-hints

        } else {
            requestLoginPasswordHint()
        }
    }

    private fun requestLoginPasswordHint() {
        val hintRequest = HintRequest.Builder()
            .setHintPickerConfig(
                CredentialPickerConfig.Builder()
                    .setShowCancelButton(true)
                    .build()
            )
            .setEmailAddressIdentifierSupported(true)
            .setAccountTypes(IdentityProviders.GOOGLE)
            .build()

        val client = Credentials.getClient(this)
        val intent: PendingIntent = client.getHintPickerIntent(hintRequest)
        try {
            startIntentSenderForResult(intent.intentSender, RC_HINT, null, 0, 0, 0)
        } catch (e: SendIntentException) {
            Log.e("ERROR", "Could not start hint picker Intent", e)
        }
    }

    fun spinnerStop() {
        Thread(Runnable {
            this@MainActivity.runOnUiThread {
                enableSpinner(false)
            }
        }).start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RC_SAVE -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.d("INFO", "Credential Save: OK")
                    val login = loginEmailTxt.text.toString()
                    val password = loginPasswordText.text.toString()
                    saveCredentials(login, password) { }
                } else {
                    Log.e("ERROR", "Credential Save: NOT OK")
                }
            }
            RC_HINT -> {
                if (resultCode == RESULT_OK) {
                    val client = Credentials.getClient(this)
                    val credectialRequest = CredentialRequest.Builder()
                        .setPasswordLoginSupported(true)
                        .build()
                    client.request(credectialRequest).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val credential = task.result?.credential
                            if (credential != null) {
                                loginEmailTxt.setText(credential.id)
                                loginPasswordText.setText(credential.password)
                            }
                        }
                    }
                    Log.e("INFO", "Credential hint: OK")
                } else {
                    Log.e("INFO", "Credential hint: NOT OK")
                }
            }
        }
    }

    private fun saveCredentials(login: String, password: String, onError: (rae: ResolvableApiException) -> Unit) {
        val credential = Credential.Builder(login)
            .setPassword(password)
            .build()
        val client = Credentials.getClient(this)
        client.save(credential).addOnCompleteListener {task ->
            if (task.isSuccessful) {
                Log.d("INFO", "Successfully saved login $login and password $password")
            } else {
                if (task.exception is ResolvableApiException) {
                    val rae = task.exception as ResolvableApiException
                    onError(rae)
                }
                Log.d("ERROR", "Error saving login $login and password $password. Error = ${task.exception?.message}")
            }
        }
    }

    private fun login() {
        val login = loginEmailTxt.text.toString()
        val password = loginPasswordText.text.toString()
        hideKeyboard()
        enableSpinner(true)
        Toast.makeText(this, "Выполняется вход в приложение", Toast.LENGTH_LONG).show()
        if (login.isNotEmpty() && password.isNotEmpty()) {

            DataService.loginRequest(LoginRequest(login, password)) { loginResponse ->
                if (loginResponse != null) {
                    App.prefs.session = loginResponse.session ?: ""

                    saveCredentials(login, password) { rae ->
                        rae.startResolutionForResult(this, RC_SAVE)
                    }

                    DataService.userInfoRequest(this, UserInfoRequest(App.prefs.session)) { response ->
                        if (response != null) {
                            spinnerStop()
                            nextActivity()
                        } else {
                            errorToast("Error: Wrong password or email")
                        }
                    }
                } else {
                    Toast.makeText(this, "Error: Wrong password or email", Toast.LENGTH_LONG).show()
                    errorToast("Something went wrong, please try again")
                }
            }
        } else {
            enableSpinner(false)
            Toast.makeText(this, "Please fill in both email and password", Toast.LENGTH_LONG).show()
        }
    }

    private fun trySingInSession() {
        enableSpinner(true)
        DataService.userInfoRequest(this, UserInfoRequest(App.prefs.session)) { response ->
            spinnerStop()
            if (response != null && response.access != false) {
                nextActivity()
            } else {
                requestLoginPasswordHint()
            }
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

    fun errorToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }
}