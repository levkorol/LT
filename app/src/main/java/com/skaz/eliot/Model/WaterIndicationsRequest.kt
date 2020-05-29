package com.skaz.eliot.Model

import java.util.*

data class WaterIndicationsRequest (
    val session: String,
    val id: String,
    val date_start: Date?,
    val date_end: Date?
) : Base()