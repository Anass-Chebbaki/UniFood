package com.example.unifood_definitivo.User.Model
/**
* Classe che rappresenta un ordine di tipo semplificato  con tutte le sue informazioni
*/
data class OrdineS(
    val numero_ordine: Int=0,
    val userId: String= "",
    val prezzo: Double=0.0,
    val fascia_oraria: String= "" ,
    val imgUri : String="",
    val nomiProdotti: List<String> = listOf()
)


