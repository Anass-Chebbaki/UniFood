package com.example.unifood_definitivo.User.View

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unifood_definitivo.User.Adapter.ListaOrdiniAdapter
import com.example.unifood_definitivo.User.Model.OrdineS
import com.example.unifood_definitivo.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
/**
 * Questa classe gestisce l'attività che visualizza la lista degli ordini di un utente.
 * Mostra gli ordini dell'utente corrente recuperati dal database Firebase.
 */
class ListaOrdini : AppCompatActivity() {

    private lateinit var listaOrdiniAdapter: ListaOrdiniAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_ordini)

        // Ottieni l'ID dell'utente dall'Intent
        val userId = intent.getStringExtra("userId")

        // Inizializza la RecyclerView e l'adapter per visualizzare gli ordini
        recyclerView = findViewById(R.id.recyclerviewordini)
        recyclerView.layoutManager = LinearLayoutManager(this)
        listaOrdiniAdapter = ListaOrdiniAdapter(ArrayList())
        recyclerView.adapter = listaOrdiniAdapter

        val ordiniRef = FirebaseDatabase.getInstance().getReference("OrdiniSemplificati")

        // Recupera gli ordini dell'utente corrente dal database e aggiorna la RecyclerView
        ordiniRef.orderByChild("userId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val ordiniList = ArrayList<OrdineS>()

                    for (ordineSnapshot in snapshot.children) {
                        val ordine = ordineSnapshot.getValue(OrdineS::class.java)
                        ordine?.let {
                            ordiniList.add(it)
                            Log.d("Database", "Ordine recuperato: $it")
                        }
                    }
                    listaOrdiniAdapter.updateData(ordiniList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Database", "Errore nel recupero dei dati: ${error.message}")
                }
            })
    }
}