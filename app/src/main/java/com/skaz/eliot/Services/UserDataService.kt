package com.skaz.eliot.Services

import com.skaz.eliot.Controller.App

object UserDataService {

    var fio = ""
    var schet = ""
    var address = ""

    fun logout() {
        fio = ""
        App.prefs.authToken = ""
        App.prefs.isLoggedIn = false
        DataService.devices.clear()
        DataService.deviceAccTariff.clear()
        DataService.deviceAllData.clear()
        DataService.deviceInfoArray.clear()

    }

    var startDate = ""
    var endDate = ""
    var defaultBeginDate = "01 янв. 2019"
    var defaultEndDate = ""

    var ruStartMonth = "" 
    var ruEndMonth = ""

    var defaultBeginDateSend = "2019-01-01"
    var defaultEndDateSend = "2019-01-22"

}