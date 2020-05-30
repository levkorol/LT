package com.skaz.eliot.Model

import java.util.*

data class MyDate (
    val year: Int,
    val month: Int,
    val day: Int
) {
    companion object {
        fun now() : MyDate {
            val cal = GregorianCalendar.getInstance()
            val res = MyDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
            return res
        }
    }
}