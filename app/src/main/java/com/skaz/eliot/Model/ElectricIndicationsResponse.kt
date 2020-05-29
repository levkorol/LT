package com.skaz.eliot.Model

data class ElectricIndicationsResponse(
    val t1: Double,
    val t2: Double,
    val t1_date: String?,
    val t2_date: String?,
    val notice: String?,
    val error: String?
) : Base()