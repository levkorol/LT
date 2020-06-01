package com.skaz.eliot.Model

data class Device (
    val id: Int,
    val type_id: Int,
    val type: String,
    val category: String,
    val device_info: List<DeviceInfo>,
    val last_data: DeviceLastData?,
    val accumulated_en: DeviceAccumulatedEn?)