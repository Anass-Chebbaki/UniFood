package com.example.unifood_definitivo.Model

data class Ordine(
    val fascia_oraria: String? = null,
    val lista_prodotti: List<CartProduct>,
    val numero_ordine: Int?,
    val prezzo: Double?,
    val userId:String?= null
)