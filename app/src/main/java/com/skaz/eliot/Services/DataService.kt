package com.skaz.eliot.Services

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.google.gson.Gson
import com.skaz.eliot.Controller.App
import com.skaz.eliot.Model.Device
import com.skaz.eliot.Model.DeviceAllData
import com.skaz.eliot.Model.DeviceInfo
import com.skaz.eliot.Model.DeviceTariff
import com.skaz.eliot.Utilities.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

object DataService {

    val devices = ArrayList<Device>()
    val deviceInfoArray = ArrayList<DeviceInfo>()
    val deviceAccTariff = ArrayList<DeviceTariff>()
    val deviceAllData = ArrayList<DeviceAllData>()
    val deviceSelectData = ArrayList<DeviceTariff>()
    var deviceId = ""
    var sessionForeReguest = ""
    val dec = DecimalFormat("#.##")


    fun deviceRequest(session: String, complete: (Boolean) -> Unit) {

//        val gson = Gson()
//
//        var deviceInfo = gson.toJson(deviceInfoArray)
//
//        var js = Gson().fromJson(deviceInfo, Device::class.java)
//
        val deviceInf = DeviceInfo(last_act = "", serial = "", type = "")

        val deviceData = DeviceAllData(
            deviceDate = "",
            pw_1 = 0.0,
            pw_2 = 0.0,
            pw_3 = 0.0,
            amper_1 = 0.0,
            amper_2 = 0.0,
            amper_3 = 0.0,
            volt_1 = 0.0,
            volt_2 = 0.0,
            volt_3 = 0.0,
            cur = 0.0,
            notice = "",
            error = ""
        )

        val deviceTariff = DeviceTariff(
            t1 = 0.0,
            t2 = 0.0,
            t1_date = "",
            t2_date = "",
            notice = "",
            error = "",
            access = false
        )

        val jsonBody = JSONObject()
        jsonBody.put("session", session)
        val requestBody = jsonBody.toString()

        val devicesRequest = object :
            JsonArrayRequest(Method.POST, URL_DEVICES, null, Response.Listener { response ->

                try {
                    for (x in 0 until response.length()) {
                        val deviceNum = response.getJSONObject(x)
                        val id = deviceNum.getInt("id")
                        val type_id = deviceNum.getInt("type_id")
                        var type = deviceNum.getString("type")
                        val category = deviceNum.getString("category")
                        with(deviceInf) {
                            val deviceInfo = deviceNum.getJSONObject("device_info")
                            last_act = deviceInfo.getString("last_act")
                            serial = deviceInfo.getString("serial")
                            type = deviceInfo.getString("type")
                        }
                        with(deviceData) {
                            val data = deviceNum.getJSONObject("last_data")
                            deviceDate = data.getString("deviceData")
                            pw_1 = data.getDouble("pw_1")
                            pw_2 = data.getDouble("pw_2")
                            pw_3 = data.getDouble("pw_3")
                            amper_1 = data.getDouble("amper_1")
                            amper_2 = data.getDouble("amper_2")
                            amper_3 = data.getDouble("amper_2")
                            volt_1 = data.getDouble("volt_1")
                            volt_2 = data.getDouble("volt_2")
                            volt_3 = data.getDouble("volt_3")
                            cur = data.getDouble("cur")
                            notice = data.getString("notice")
                            error = data.getString("error")
                        }

                        with(deviceTariff) {
                            val devTar = deviceNum.getJSONObject("accumulated_en")
                            t1 = devTar.getDouble("t1")
                            t2 = devTar.getDouble("t2")
                            t1_date = devTar.getString("t1_date")
                            t2_date = devTar.getString("t2_date")
                            notice = devTar.getString("notice")
                            error = devTar.getString("error")
                            access = devTar.getBoolean("access")
                        }
                        //  val deviceTariff = deviceNum.getJSONArray("accumulated_en")

                        val newDevice = Device(
                            id,
                            type_id,
                            type,
                            category,
                            deviceInf,
                            deviceData,
                            deviceTariff,
                            false,
                            "",
                            ""
                        )

                        this.devices.clear()
                        this.devices.add(newDevice)

                        deviceInfoRequest(session, id.toString()) { completed ->

                        }

                        deviceTariffRequest(session, id.toString()) { completed2 ->


                        }

                        deviceGetData(session, id.toString()) { completed ->

                        }
                    }

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
                headers.put("X-Platform", "ANDROID")
                return headers
            }


        }

        App.prefs.requestQueue.add(devicesRequest)
    }

    fun deviceInfoRequest(session: String, id: String, complete: (Boolean) -> Unit) {

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
                headers.put("X-Platform", "ANDROID")
                return headers
            }


        }

        App.prefs.requestQueue.add(deviceInfoRequest)
    }

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
                        if (response.getString("volt_1") != null) {
                            volt1 = response.getString("volt_1")
                        } else {
                            volt1 = "-"
                        }
                    }

                    if (response.has("volt_2")) {
                        if (response.getString("volt_2") != null) {
                            volt2 = response.getString("volt_2")
                        } else {
                            volt2 = "-"
                        }
                    }

                    if (response.has("volt_3")) {
                        if (response.getString("volt_3") != null) {
                            volt3 = response.getString("volt_3")
                        } else {
                            volt3 = "-"
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



                    this.deviceAllData.clear()
                    this.deviceAllData.add(newDeviceAllData)

                    complete(true)
                } catch (e: JSONException) {
                    Log.d("JSON_DEVICEALL", "EXC:" + e.localizedMessage)
                    Log.d("JSON_DEVICEALL", "EXC" + response)
                    complete(false)
                }

            }, Response.ErrorListener { error ->
                Log.d("ERROR_DEVICE", "Could not login user: $error")
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
                return headers
            }


        }

        App.prefs.requestQueue.add(deviceAllDataRequest)
    }

    fun deviceTariffRequest(session: String, id: String, complete: (Boolean) -> Unit) {

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

                    this.deviceAccTariff.add(newDeviceTariffAcc)

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
                headers.put("X-Platform", "ANDROID")
                return headers
            }


        }

        App.prefs.requestQueue.add(deviceTariffRequest)
    }

    fun deviceTariffSelectDateRequest(
        session: String,
        id: String,
        dateStart: String,
        dateEnd: String,
        complete: (Boolean) -> Unit
    ) {
        deviceSelectData.clear()

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

                    val newDeviceTariffAcc2 = DeviceTariff(t1, t2, t3String, "", "", "", false)

                    this.deviceSelectData.add(newDeviceTariffAcc2)

                    Log.d("DEVICE_ID", DataService.deviceId)
                    Log.d("ARRAy", "${deviceSelectData.count()}")
                    Log.d("T1", "${deviceSelectData[0].t1}")

                    complete(true)
                } catch (e: JSONException) {
                    Log.d("JSON", "EXC:" + e.localizedMessage)
                    Log.d("JSON", "EXC" + response)
                    Log.d("Trouble", "trouble")
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
                headers.put("X-Platform", "ANDROID")
                return headers
            }
        }

        App.prefs.requestQueue.add(deviceTariffSelectDateRequest)
    }
}

//class Response(json: String) : JSONObject(json) {
//    val type: String? = this.optString("type")
//    val data = this.optJSONArray("data")
//        ?.let { 0.until(it.length()).map { i -> it.optJSONObject(i) } } // returns an array of JSONObject
//        ?.map { Device(id = ) } // transforms each JSONObject of the array into Foo
//}