package com.example.unifood_definitivo.Model

import java.io.Serializable

data class User(
    val id: String = "",
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val initialBalance: Double = 0.0,
    val password: String = ""
): Serializable