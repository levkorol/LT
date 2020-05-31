package com.skaz.eliot.Services

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
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

    var deviceId = ""
    var sessionForeReguest = ""
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

    fun deviceRequest(session: String, complete: (List<Device>?) -> Unit) {

        val jsonBody = JSONObject()
        jsonBody.put("session", session)
        val requestBody = jsonBody.toString()
        val builder = GsonBuilder()
        val gson = builder.create()

        val devicesRequest = object :
            JsonArrayRequest(Method.POST, URL_DEVICES, null, Response.Listener { response ->
                try {
                    val listType = object : TypeToken<List<Device>>() {}.type
                    val devices : List<Device>  = gson.fromJson(response.toString(), listType)
                    complete(devices)
                } catch (e: JSONException) {
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

        App.prefs.requestQueue.add(devicesRequest)
    }

    /*fun deviceInfoRequest(session: String, id: String, complete: (Boolean) -> Unit) {

        val jsonBody = JSONObject()

        jsonBody.put("session", session)
        jsonBody.put("id", id)
        val requestBody = jsonBody.toString()

        val deviceInfoRequest = object :
            JsonObjectRequest(Method.POST, URL_DEVICE_INFO, null, Response.Listener { response ->

                try {

                    val serial = response.getString("serial")
                    val last_act = response.getString("last_act")
                    val type = response.getString("type")

                    val newDeviceInfo = DeviceInfo(serial, last_act, type)

                    this.deviceInfoArray.add(newDeviceInfo)


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

        App.prefs.requestQueue.add(deviceInfoRequest)
    }*/

    fun deviceGetData(session: String, id: String, complete: (Boolean) -> Unit) {

        val jsonBody = JSONObject()


        jsonBody.put("session", session)
        jsonBody.put("id", id)
        val requestBody = jsonBody.toString()


        val deviceAllDataRequest = object :
            JsonObjectRequest(Method.POST, URL_EE_PW_PHASE, null, Response.Listener { response ->

                try {

                    var date1 = "o"
                    var pw1 = "o"
                    var pw2 = "o"
                    var pw3 = "b"
                    var amper1 = "-"
                    var amper2 = "-"
                    var amper3 = "-"
                    var volt1 = "-"
                    var volt2 = "-"
                    var volt3 = "-"
                    var cur = 0.1
                    var error = ""
                    var notice = ""

                    date1 = if (response.getString("date") != null) {
                        response.getString("date")
                    } else {
                        "2019-01-01"
                    }

                    pw1 = if (response.getString("pw_1") != null) {
                        response.getString("pw_1")
                    } else {
                        "-"
                    }

                    pw2 = if (response.getString("pw_2") != null) {
                        response.getString("pw_2")
                    } else {
                        "-"
                    }


                    pw3 = if (response.getString("pw_3") != null) {
                        response.getString("pw_3")
                    } else {
                        "-"
                    }

                    if (response.has("amper_1")) {
                        amper1 = if (!response.getString("amper_1").isNullOrBlank()) {
                            response.getString("amper_1")
                        } else {
                            "-"
                        }
                    }


                    if (response.has("amper_2")) {
                        if (response.getString("amper_2") != null) {
                            amper2 = response.getString("amper_2")
                        } else {
                            amper2 = "-"
                        }
                    }

                    if (response.has("amper_3")) {
                        if (response.getString("amper_3") != null) {
                            amper3 = response.getString("amper_3")
                        } else {
                            amper3 = "-"
                        }
                    }

                    if (response.has("volt_1")) {
                        volt1 = if (response.getString("volt_1") != null) {
                            response.getString("volt_1")
                        } else {
                            "-"
                        }
                    }

                    if (response.has("volt_2")) {
                        volt2 = if (response.getString("volt_2") != null) {
                            response.getString("volt_2")
                        } else {
                            "-"
                        }
                    }

                    if (response.has("volt_3")) {
                        volt3 = if (response.getString("volt_3") != null) {
                            response.getString("volt_3")
                        } else {
                            "-"
                        }
                    }

                    val newDeviceAllData = DeviceAllData(
                        date1,
                        pw1.toDouble(),
                        pw2.toDouble(),
                        pw3.toDouble(),
                        amper1.toDouble(),
                        amper2.toDouble(),
                        amper3.toDouble(),
                        volt1.toDouble(),
                        volt2.toDouble(),
                        volt3.toDouble(),
                        cur,
                        error,
                        notice
                    )

                    complete(true)
                } catch (e: JSONException) {

                    complete(false)
                }

            }, Response.ErrorListener { error ->

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

        App.prefs.requestQueue.add(deviceAllDataRequest)
    }

    /*fun deviceTariffRequest(session: String, id: String, complete: (DeviceTariff?) -> Unit) {

        val jsonBody = JSONObject()

        jsonBody.put("session", session)
        jsonBody.put("id", id)
        deviceId = id
        sessionForeReguest = session
        var t1: Double
        var t2: Double


        val requestBody = jsonBody.toString()

        val deviceTariffRequest = object :
            JsonObjectRequest(Method.POST, URL_EE_TARIF_ACC, null, Response.Listener { response ->

                try {

                    if (response.getDouble("t1") != null) {
                        val t1Double = response.getDouble("t1")
                        t1 = dec.format(t1Double).toDouble()

                    } else {
                        t1 = 0.0
                    }

                    if (response.getDouble("t2") != null) {
                        val t2Double = response.getDouble("t2")
                        t2 = dec.format(t2Double).toDouble()
                    } else {
                        t2 = 0.0
                    }
                    val t3 = response.getDouble("t1") + response.getDouble("t2")
                    val t3String = dec.format(t3)

                    val newDeviceTariffAcc = DeviceTariff(t1, t2, t3String, "", "", "", false)

                    complete(newDeviceTariffAcc)
                } catch (e: JSONException) {
                    complete(null)
                }

            }, Response.ErrorListener { error ->

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

        App.prefs.requestQueue.add(deviceTariffRequest)
    }*/

    fun deviceTariffSelectDateRequest(
        session: String,
        id: String,
        dateStart: String,
        dateEnd: String,
        complete: (DeviceTariff?) -> Unit
    ) {
        val jsonBody = JSONObject()

        jsonBody.put("session", session)
        jsonBody.put("id", id)
        jsonBody.put("date_start", dateStart)
        jsonBody.put("date_end", dateEnd)
        var t1: Double
        var t2: Double

        val requestBody = jsonBody.toString()

        val deviceTariffSelectDateRequest = object :
            JsonObjectRequest(Method.POST, URL_EE_TARIF_DATE, null, Response.Listener { response ->

                try {

                    t1 = if (response.getDouble("t1") != null) {
                        val t1Double = response.getDouble("t1")
                        dec.format(t1Double).toDouble()

                    } else {
                        0.0
                    }

                    t2 = if (response.getDouble("t2") != null) {
                        val t2Double = response.getDouble("t2")
                        dec.format(t2Double).toDouble()
                    } else {
                        0.0
                    }
                    val t3 = response.getDouble("t1") + response.getDouble("t2")
                    val t3String = dec.format(t3)

                    val newDeviceTariffAcc2 = DeviceTariff(t1, t2, t3String, "", "", "", false)

                    complete(newDeviceTariffAcc2)
                } catch (e: JSONException) {

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

        App.prefs.requestQueue.add(deviceTariffSelectDateRequest)
    }
}
