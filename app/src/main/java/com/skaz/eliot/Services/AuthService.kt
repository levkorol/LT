package com.skaz.eliot.Services


import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.skaz.eliot.Controller.App
import com.skaz.eliot.Utilities.BROADCAST_USER_DATA_CHANGE
import com.skaz.eliot.Utilities.URL_LOGIN
import com.skaz.eliot.Utilities.URL_USER_INFO
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Method


object AuthService {



    fun loginRequest(login: String, password: String, complete: (Boolean) -> Unit) {

        val jsonBody = JSONObject()
        jsonBody.put("login", login)
        jsonBody.put("password", password)
        val requestBody = jsonBody.toString()

        val loginRequest = object: JsonObjectRequest(Method.POST, URL_LOGIN, null, Response.Listener { response ->

            try {

                App.prefs.authToken = response.getString("session")
                App.prefs.isLoggedIn = true

                complete(true)
            } catch (e: JSONException) {

                complete(false)
            }

        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not login user: $error")
            complete(false)
        }) {

            override fun getBodyContentType(): String {
                return "application/json"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Accept", "application/json; version=1")
                headers.put("X-Platform", "Android")
                return headers
            }


        }

        App.prefs.requestQueue.add(loginRequest)
    }

    fun userInfoRequest(session: String, context: Context, complete: (Boolean) -> Unit) {

        val jsonBody = JSONObject()
        jsonBody.put("session", session)

        val requestBody = jsonBody.toString()

        val userInfoRequest = object: JsonObjectRequest(Method.POST, URL_USER_INFO, null, Response.Listener { response ->

            try {

                UserDataService.fio = response.getString("fio")
                UserDataService.schet = response.getString("schet")
                UserDataService.address = response.getString("address")

                                   val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                LocalBroadcastManager.getInstance(context).sendBroadcast(userDataChange)
                complete(true)
            } catch (e: JSONException) {
                Log.d("JSON", "EXC:" + e.localizedMessage)
                Log.d("JSON", "EXC" + response)
                complete(false)
            }

        }, Response.ErrorListener { error ->
            Log.d("ERROR", "Could not login user: $error")
            complete(false)
        }) {

            override fun getBodyContentType(): String {
                return "application/json"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Accept", "application/json; version=1")
                headers.put("X-Platform", "Android")
                return headers
            }


        }

        App.prefs.requestQueue.add(userInfoRequest)
    }
}