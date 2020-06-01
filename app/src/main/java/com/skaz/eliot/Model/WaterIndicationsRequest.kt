package com.skaz.eliot.Model

data class WaterIndicationsRequest (
    val session: String,
    val id: String,
    val date_start: String?,
    val date_end: String?)