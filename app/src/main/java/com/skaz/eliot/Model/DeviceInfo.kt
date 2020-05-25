package com.skaz.eliot.Model

data class DeviceInfo (
    var serial: String,
    var last_act: String,
    val type: String) : Base()