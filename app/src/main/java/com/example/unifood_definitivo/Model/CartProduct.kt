package com.example.unifood_definitivo.Model

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONArray
import java.io.Serializable
/*
* Classe che rappredenta un prodotto del carrello con tutte le sue informazioni
*/
data class CartProduct(
    val product: Prodotti,
    val quantity: Int,
    val imgUri: String?,
    var total: Double? // Aggiunto il campo per il totale
) : Serializable