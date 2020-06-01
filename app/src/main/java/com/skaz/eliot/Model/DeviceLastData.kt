package com.skaz.eliot.Model

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