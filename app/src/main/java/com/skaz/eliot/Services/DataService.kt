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
    //val deviceInfoArray = ArrayList<DeviceInfo>()
    val deviceAccTariff = ArrayList<DeviceTariff>()
    val deviceAllData = ArrayList<DeviceAllData>()
    val deviceSelectData = ArrayList<DeviceTariff>()
    var deviceId = ""
    var sessionForeReguest = ""
    val dec = DecimalFormat("#.##")


    fun deviceRequest(session: String, complete: (Boolean) -> Unit) {

        val jsonBody = JSONObject()
        jsonBody.put("session", session)
        val requestBody = jsonBody.toString()

        val devicesRequest = object :
            JsonArrayRequest(Method.POST, URL_DEVICES, null, Response.Listener { response ->
                try {
                    this.devices.clear()
                    for (x in 0 until response.length()) {
                        val deviceNum = response.getJSONObject(x)
                        val id = deviceNum.getInt("id")
                        val type_id = deviceNum.getInt("type_id")
                        val type = deviceNum.getString("type")
                        val category = deviceNum.getString("category")
                        
                        //info
                        val device_info_json_array = deviceNum.getJSONArray("device_info")
                        val device_info_list = mutableListOf<DeviceInfo>()
                        for (device_info_num in 0 until device_info_json_array.length()) {
                            val device_info_json =
                                device_info_json_array.getJSONObject(device_info_num)
                            val device_info_serial = device_info_json.getString("serial")
                            val device_info_last_act = device_info_json.getString("last_act")
                            var device_info_type: String? = null
                            if (device_info_json.has("type")) {
                                device_info_type = device_info_json.getString("type")
                            }
                            val device_info = DeviceInfo(
                                device_info_serial,
                                device_info_last_act,
                                device_info_type
                            )
                            device_info_list.add(device_info)
                        }

                        //tariff
                        val device_tariff_json_array = deviceNum.getJSONArray("accumulated_en")
                        val device_tariff_list = mutableListOf<DeviceTariff>()
                        for (device_tariff_num in 0 until device_tariff_json_array.length()) {
                            val device_tariff_json =
                                device_tariff_json_array.getJSONObject(device_tariff_num)
                            val t1 = device_tariff_json.getDouble("t1")
                            val t2 = device_tariff_json.getDouble("t2")
                            val t1_date = device_tariff_json.getString("t1_date")
                            val t2_date = device_tariff_json.getString("t2_date")
                            val notice = device_tariff_json.getString("notice")
                            val error = device_tariff_json.getString("error")
                            val access = device_tariff_json.getBoolean("access")
                        }

                        //data
                        val device_date_json_array = deviceNum.getJSONArray("last_data")
                        val device_date_list = mutableListOf<DeviceAllData>()
                        for (device_data_num in 0 until device_date_json_array.length()) {
                            val device_data_json =
                                device_date_json_array.getJSONObject(device_data_num)
                            val deviceDate = device_data_json.getString("deviceData")
                            val pw_1 = device_data_json.getDouble("pw_1")
                            val pw_2 = device_data_json.getDouble("pw_2")
                            val pw_3 = device_data_json.getDouble("pw_3")
                            val amper_1 = device_data_json.getDouble("amper_1")
                            val amper_2 = device_data_json.getDouble("amper_2")
                            val amper_3 = device_data_json.getDouble("amper_2")
                            val volt_1 = device_data_json.getDouble("volt_1")
                            val volt_2 = device_data_json.getDouble("volt_2")
                            val volt_3 = device_data_json.getDouble("volt_3")
                            val cur = device_data_json.getDouble("cur")
                            val notice = device_data_json.getString("notice")
                            val error = device_data_json.getString("error")
                        }


                        val newDevice = Device(
                            id,
                            type_id,
                            type,
                            category,
                            device_info_list
//                            deviceData,
//                            deviceTariff,
//                            false,
//                            "",
//                            ""
                        )

                        this.devices.add(newDevice)
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
                headers.put("X-Platform", "Android")
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
                headers.put("X-Platform", "Android")
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
                headers.put("X-Platform", "Android")
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