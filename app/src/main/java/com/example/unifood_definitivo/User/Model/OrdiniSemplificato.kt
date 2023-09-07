package com.example.unifood_definitivo.User.Model

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
/**
* Classe che permette di creare una nuova sezione nel database che ci garantisce di avere delle
* informazioni sugli ordini più semplificate.
 */
class OrdiniSemplificato {
    private val database = FirebaseDatabase.getInstance()
    private val ordiniRef = database.getReference("Ordini")
    private val ordiniSemplificatiRef = database.getReference("OrdiniSemplificati")

    fun semplificaOrdini() {
        ordiniRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ordineSnapshot in snapshot.children) {
                    val numeroOrdine = ordineSnapshot.child("numero_ordine").getValue(Int::class.java) ?: 0
                    val userId = ordineSnapshot.child("userId").getValue(String::class.java) ?: ""
                    val fasciaOraria = ordineSnapshot.child("fascia_oraria").getValue(String::class.java) ?: ""
                    val prezzo = ordineSnapshot.child("prezzo").getValue(Double::class.java) ?: 0.0
                    val nomiProdotti = mutableListOf<String>()

                    val prodottiSnapshot = ordineSnapshot.child("lista_prodotti")
                    for (prodottoSnapshot in prodottiSnapshot.children) {
                        val nomeProdotto = prodottoSnapshot.child("product").child("nome_prodotto").getValue(String::class.java)
                        val quantity = prodottoSnapshot.child("quantity").getValue(Int::class.java) ?: 0
                        nomeProdotto?.let {
                            nomiProdotti.add("$it x$quantity")
                        }
                    }
                    val imgUri = ordineSnapshot.child("lista_prodotti").children.first().child("product").child("imgUri").getValue(String::class.java) ?: ""
                    val ordineSemplificato = OrdineS(numeroOrdine, userId, prezzo, fasciaOraria, imgUri,nomiProdotti)
                    ordiniSemplificatiRef.child(ordineSnapshot.key!!).setValue(ordineSemplificato)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Database error: ${error.message}")
            }
        })
    }
}
