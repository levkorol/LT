package com.skaz.eliot.Model

data class Device (
    val id: Int,
    val type_id: Int,
    val type: String,
    val category: String,
    val device_info: List<DeviceInfo>,
    val last_data: DeviceLastData?,
    val accumulated_en: DeviceAccumulatedEn?) {

    data class DeviceInfo (
        var serial: String,
        var last_act: String,
        val type: String?)

    data class DeviceAccumulatedEn(
        var t1: Double,
        var t2: Double,
        var t1_date: String,
        var t2_date:String,
        var notice: String?,
        var error: String?,
        var access: Boolean?)

    data class DeviceLastData(
        var date: String?,
        var pw_1: Double?,
        var pw_2: Double?,
        var pw_3: Double?,
        var amper_1: Double?,
        var amper_2: Double?,
        var amper_3: Double?,
        var volt_1: Double?,
        var volt_2: Double?,
        var volt_3: Double?,
        var cur: Double?,
        var notice: String?,
        var error: String?)
}