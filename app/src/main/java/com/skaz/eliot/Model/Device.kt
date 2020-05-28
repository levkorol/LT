package com.skaz.eliot.Model


data class Device (
    val id: Int,
    val type_id: Int,
    val type: String,
    val category: String,
    val device_info: List<DeviceInfo>,
    val last_data: DeviceAllData?,
    val accumulated_en: DeviceTariff?

//    val interval: Boolean,
//    val date_start: String,
//    val date_end: String
    ): Base()