package com.example.unifood_definitivo.Model

import android.net.Uri
import java.io.Serializable

data class Prodotti(

    val id: String? = null,
    val nome_prodotto: String? = null,
    val prezzo: Double? = null,
    val imgUri: String? = null,
    val ingredienti: String?=null,
    val categoria_appartenenza: String?=null,
    val imgUri2: String?=null //seconda immagine piu grande 900x900
): Serializable
