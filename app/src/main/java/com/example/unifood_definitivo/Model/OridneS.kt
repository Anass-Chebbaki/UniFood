package com.example.unifood_definitivo.Model

data class OrdineS(
    val numero_ordine: Int=0,
    val userId: String= "",
    val prezzo: Double=0.0,
    val fascia_oraria: String= "" ,
    val nomiProdotti: List<String> = listOf()
)


