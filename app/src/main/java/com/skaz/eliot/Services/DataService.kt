package com.skaz.eliot.Services

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.skaz.eliot.Controller.App
import com.skaz.eliot.Utilities.*
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.skaz.eliot.Model.*

object DataService {

    val devices = ArrayList<Device>()
    val dec = DecimalFormat("#.##")

    fun electricIndicationsRequest(request: ElectricIndicationsRequest, complete: (ElectricIndicationsResponse?) -> Unit) {

        val gson = GsonBuilder().create()
        val requestBody = gson.toJson(request)

        val req = object :
            JsonObjectRequest(Method.POST, URL_ELECTRIC_GET_INDICATIONS, null, Response.Listener { response ->
                try {
                    val responseType = object : TypeToken<ElectricIndicationsResponse>() {}.type
                    val response : ElectricIndicationsResponse = gson.fromJson(response.toString(), responseType)
                    complete(response)
                } catch (e: Exception) {
                    complete(null)
                }
            }, Response.ErrorListener { error ->
                Log.d("ERROR", "Could not login user: $error")
                complete(null)
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

    fun waterIndicationsRequest(request: WaterIndicationsRequest, complete: (WaterIndicationsResponse?) -> Unit) {

        val gson = GsonBuilder().create()
        val requestBody = gson.toJson(request)

        val req = object :
            JsonObjectRequest(Method.POST, URL_WATER_GET_INDICATIONS, null, Response.Listener { response ->
                try {
                    val responseType = object : TypeToken<WaterIndicationsResponse>() {}.type
                    val response : WaterIndicationsResponse = gson.fromJson(response.toString(), responseType)
                    complete(response)
                } catch (e: Exception) {
                    complete(null)
                }
            }, Response.ErrorListener { error ->
                Log.d("ERROR", "Could not login user: $error")
                complete(null)
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

    fun deviceRequest(session: String, complete: (Boolean) -> Unit) {

        val jsonBody = JSONObject()
        jsonBody.put("session", session)
        val requestBody = jsonBody.toString()
        val builder = GsonBuilder()
        val gson = builder.create()

        val devicesRequest = object :
            JsonArrayRequest(Method.POST, URL_DEVICES, null, Response.Listener { response ->
                try {
                    this.devices.clear()
                    val listType = object : TypeToken<List<Device>>() {}.type
                    val devs : List<Device>  = gson.fromJson(response.toString(), listType)
                    this.devices.addAll(devs)

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

        App.prefs.requestQueue.add(devicesRequest)
    }
}
