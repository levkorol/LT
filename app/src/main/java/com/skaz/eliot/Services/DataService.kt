package com.skaz.eliot.Services

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.skaz.eliot.Controller.App
import com.skaz.eliot.Utilities.*
import java.text.DecimalFormat
import java.util.*
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.skaz.eliot.Model.*
import java.lang.reflect.Type

object DataService {

    val dec = DecimalFormat("#.##")

    fun electricIndicationsRequest(request: ElectricIndicationsRequest, onResponse: (ElectricIndicationsResponse?) -> Unit) {
        makeJsonObjectRequest<ElectricIndicationsRequest, ElectricIndicationsResponse>(URL_ELECTRIC_GET_INDICATIONS, request,
            object : TypeToken<ElectricIndicationsResponse>() {}.type, onResponse);
    }

    fun waterIndicationsRequest(request: WaterIndicationsRequest, onResponse: (WaterIndicationsResponse?) -> Unit) {
        makeJsonObjectRequest<WaterIndicationsRequest, WaterIndicationsResponse>(URL_WATER_GET_INDICATIONS, request,
            object : TypeToken<WaterIndicationsResponse>() {}.type, onResponse)
    }

    fun deviceRequest(request: DevicesRequest, onResponse: (List<Device>?) -> Unit) {
        makeJsonArrayRequest(URL_DEVICES, request, object: TypeToken<List<Device>>() {}.type, onResponse)
    }

    fun loginRequest(request: LoginRequest, onResponse: (LoginResponse?) -> Unit) {
        makeJsonObjectRequest<LoginRequest, LoginResponse>(URL_LOGIN, request, object : TypeToken<LoginResponse>() {}.type, { response ->
            if (response != null && response.access && response.session != null) {
                UserDataService.authToken = response.session
                UserDataService.isLoggedIn = true
                UserDataService.isLoggedOut = false
            }
            onResponse(response)
        })
    }

    fun userInfoRequest(context: Context, request: UserInfoRequest, onResponse: (UserInfoResponse?) -> Unit) {
        makeJsonObjectRequest<UserInfoRequest, UserInfoResponse>(URL_USER_INFO, request, object : TypeToken<UserInfoResponse>() {}.type, { response ->
            if (response != null) {
                UserDataService.fio = response.fio
                UserDataService.schet = response.schet
                UserDataService.address = response.address

                val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                LocalBroadcastManager.getInstance(context).sendBroadcast(userDataChange)
            }
            onResponse(response)
        })
    }

    private fun <TRequest, TResponse> makeJsonObjectRequest(url: String, request: TRequest, responseType: Type, onResponse: (TResponse?) -> Unit) {
        val gson = GsonBuilder().create()
        val requestBody = gson.toJson(request)
        val req = object :
            JsonObjectRequest(Method.POST, url, null, Response.Listener { result ->
                try {
                    val response : TResponse = gson.fromJson(result.toString(), responseType)
                    onResponse(response)
                } catch (e: Exception) {
                    onResponse(null)
                }
            }, Response.ErrorListener { error ->
                Log.d("ERROR", "Could not obtain request $requestBody: $error")
                onResponse(null)
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
        App.prefs.requestQueue.add(req)
    }

    private fun <TRequest, TResponse> makeJsonArrayRequest(url: String, request: TRequest, responseType: Type, onResponse: (List<TResponse>?) -> Unit) {
        val gson = GsonBuilder().create()
        val requestBody = gson.toJson(request)
        val req = object :
            JsonArrayRequest(Method.POST, url, null, Response.Listener { result ->
                try {
                    val response : List<TResponse> = gson.fromJson(result.toString(), responseType)
                    onResponse(response)
                } catch (e: Exception) {
                    Log.d("ERROR", "Could not obtain request $requestBody: exception ${e.message}")
                    onResponse(null)
                }
            }, Response.ErrorListener { error ->
                Log.d("ERROR", "Could not obtain request $requestBody: error $error")
                onResponse(null)
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
        App.prefs.requestQueue.add(req)
    }
}
