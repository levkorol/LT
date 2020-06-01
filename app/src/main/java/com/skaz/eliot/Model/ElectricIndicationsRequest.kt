package com.skaz.eliot.Model

data class ElectricIndicationsRequest(
    val session: String,
    val id: String,
    val date_start: String?,
    val date_end: String?)