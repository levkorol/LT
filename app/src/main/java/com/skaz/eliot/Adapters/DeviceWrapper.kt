package com.skaz.eliot.Adapters

import com.skaz.eliot.Model.Device
import com.skaz.eliot.Model.MyDate
import com.skaz.eliot.Services.UserDataService

data class DeviceWrapper (
    val device: Device
) {
    var startDate: MyDate? = UserDataService.getDefStartDate()
    var finishDate: MyDate? = UserDataService.defFinishDate
}