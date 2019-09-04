package com.skaz.eliot.Services

import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.skaz.eliot.Controller.App
import com.skaz.eliot.Model.Device
import com.skaz.eliot.Model.DeviceAllData
import com.skaz.eliot.Model.DeviceInfo
import com.skaz.eliot.Model.DeviceTariff
import com.skaz.eliot.Utilities.*
import org.json.JSONException
import org.json.JSONObject
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

object DataService {

        val devices =  ArrayList<Device>()
        val deviceInfoArray = ArrayList<DeviceInfo>()
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

            val devicesRequest = object: JsonArrayRequest(Method.POST, URL_DEVICES, null, Response.Listener { response ->

                try {
                    for (x in 0 until response.length()) {
                        val deviceNum = response.getJSONObject(x)
                        val id = deviceNum.getString("id")
                        val type_id = deviceNum.getString("type_id")
                        val type = deviceNum.getString("type")
                        val newDevice = Device(id, type_id, type)

                        this.devices.clear()
                        this.devices.add(newDevice)

                        DataService.deviceInfoRequest(session, id) { completed ->


                       }

                        DataService.deviceTariffRequest(session, id) { completed2 ->


                        }

                        DataService.deviceGetData(session, id) { completed ->


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
                    return headers
                }


            }

            App.prefs.requestQueue.add(devicesRequest)
        }

        fun deviceInfoRequest(session: String, id: String, complete: (Boolean) -> Unit) {

            val jsonBody = JSONObject()

            jsonBody.put("session", session)
            jsonBody.put("id", id )
            val requestBody = jsonBody.toString()

            val deviceInfoRequest = object: JsonObjectRequest(Method.POST, URL_DEVICE_INFO, null, Response.Listener { response ->

                try {

                    val serial = response.getString("serial")
                   val last_act = response.getString("last_act")




                    val newDeviceInfo = DeviceInfo(serial, last_act)

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
                    return headers
                }


            }

            App.prefs.requestQueue.add(deviceInfoRequest)
        }

    fun deviceGetData(session: String, id: String, complete: (Boolean) -> Unit) {

        val jsonBody = JSONObject()


        jsonBody.put("session", session)
        jsonBody.put("id", id )
        val requestBody = jsonBody.toString()


        val deviceAllDataRequest = object: JsonObjectRequest(Method.POST, URL_EE_PW_PHASE, null, Response.Listener { response ->



            try {

                var date1 = "o"
                var pw1 = "o"
                var pw2 = "o"
                var pw3 = "b"
                var amper1 = "-"
                var amper2= "-"
                var amper3 = "-"
                var volt1 = "-"
                var volt2 = "-"
                var volt3 = "-"

                if (response.getString("date") != null) {
                    date1 = response.getString("date")
                } else {
                    date1 = "2019-01-01"
                }

                if (response.getString("pw_1") != null) {
                    pw1 = response.getString("pw_1")
                } else {
                    pw1 = "-"
                }

                if (response.getString("pw_2") != null) {
                    pw2 = response.getString("pw_2")
                } else {
                    pw2 = "-"
                }



                if (response.getString("pw_3") != null) {
                    pw3 = response.getString("pw_3")
                } else {
                    pw3 = "-"
                }

                if (response.has("amper_1")) {
                    if (!response.getString("amper_1").isNullOrBlank()) {
                        amper1 = response.getString("amper_1")
                    } else {
                        amper1 = "-"
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

                 val newDeviceAllData = DeviceAllData(date1, pw1, pw2, pw3, amper1, amper2, amper3, volt1, volt2, volt3)



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
        jsonBody.put("id", id )
        deviceId = id
        sessionForeReguest = session
        var t1: String
        var t2: String


        val requestBody = jsonBody.toString()

        val deviceTariffRequest = object: JsonObjectRequest(Method.POST, URL_EE_TARIF_ACC, null, Response.Listener { response ->

            try {

                if (response.getDouble("t1") != null) {
                    val t1Double = response.getDouble("t1")
                    t1 = dec.format(t1Double)

                } else {
                    t1 = "0.0"
                }

                if (response.getDouble("t2") != null) {
                    val t2Double = response.getDouble("t2")
                    t2 = dec.format(t2Double)
                } else {
                    t2 = "0.0"
                }
                val t3 = response.getDouble("t1") + response.getDouble("t2")
                val t3String = dec.format(t3)

                val newDeviceTariffAcc = DeviceTariff("$t1", "$t2", "$t3String")

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
                return headers
            }


        }

        App.prefs.requestQueue.add(deviceTariffRequest)
    }


    fun deviceTariffSelectDateRequest(session: String, id: String, dateStart: String, dateEnd: String, complete: (Boolean) -> Unit) {
        deviceSelectData.clear()

        val jsonBody = JSONObject()

        jsonBody.put("session", session)
        jsonBody.put("id", id )
        jsonBody.put("date_start", dateStart)
        jsonBody.put("date_end", dateEnd)
        var t1: String
        var t2: String

        val requestBody = jsonBody.toString()

        val deviceTariffSelectDateRequest = object: JsonObjectRequest(Method.POST, URL_EE_TARIF_DATE, null, Response.Listener { response ->

            try {

                if (response.getDouble("t1") != null) {
                    val t1Double = response.getDouble("t1")
                    t1 = dec.format(t1Double)

                } else {
                    t1 = "0.0"
                }

                if (response.getDouble("t2") != null) {
                    val t2Double = response.getDouble("t2")
                    t2 = dec.format(t2Double)
                } else {
                    t2 = "0.0"
                }
                val t3 = response.getDouble("t1") + response.getDouble("t2")
                val t3String = dec.format(t3)

                val newDeviceTariffAcc2 = DeviceTariff("$t1", "$t2", "$t3String")

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
                return headers
            }


        }

        App.prefs.requestQueue.add(deviceTariffSelectDateRequest)
    }



}