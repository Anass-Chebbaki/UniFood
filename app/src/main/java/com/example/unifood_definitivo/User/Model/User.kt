package com.example.unifood_definitivo.User.Model

import java.io.Serializable
/**
* Classe che rappredenta un utente con tutte le sue informazioni
*/
data class User(
    val id: String = "",
    val name: String = "",
    val surname: String = "",
    val email: String = "",
    val initialBalance: Double = 0.0,
    val password: String = ""
): Serializable