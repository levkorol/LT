package com.skaz.eliot.Model

import java.util.*


data class Device (
    val id: Int,
    val type_id: Int,
    val type: String,
    val category: String,
    val deviceInfo: List<DeviceInfo>
//    val last_data: DeviceAllData?,
//    val deviceTariff: DeviceTariff?,

//    val interval: Boolean,
//    val startDateToShow: String,
//    val endDateToShow: String
    ): Base()