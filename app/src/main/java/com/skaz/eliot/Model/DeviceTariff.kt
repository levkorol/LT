package com.skaz.eliot.Model

data class DeviceTariff(
    var t1: Double?,
    var t2: Double?,
    var t1_date: String?,
    var t2_date:String?,

    var notice: String?,
    var error: String?,
    var access: Boolean?

) : Base()