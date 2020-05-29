package com.skaz.eliot.Model

import java.util.*

data class ElectricIndicationsRequest(
    val session: String,
    val id: String,
    val date_start: String?,
    val date_end: String?
) : Base()