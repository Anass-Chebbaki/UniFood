package com.example.unifood_definitivo.Model

import org.json.JSONArray

data class CartProduct(
    val product: Prodotti, // Le informazioni del prodotto
    val quantity: Int,     // La quantità selezionata
    val imgUri: String?    // L'URI dell'immagine
)