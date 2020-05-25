package com.skaz.eliot.Model

data class DeviceWater (
    val value: Double,
    val notice: String,
    val date: String ,

    val error: String,
    val access: Boolean
) : Base()