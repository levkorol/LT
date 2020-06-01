package com.skaz.eliot.Model

data class LoginResponse(
    val access: Boolean,
    val session: String?)