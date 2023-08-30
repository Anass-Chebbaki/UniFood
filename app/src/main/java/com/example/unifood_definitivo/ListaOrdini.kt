package com.example.unifood_definitivo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unifood_definitivo.Adapter.ListaOrdiniAdapter
import com.example.unifood_definitivo.Model.Ordine
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ListaOrdini : AppCompatActivity() {

    // Qui assumo che tu abbia un adapter chiamato ListaOrdiniAdapter.
    // Sostituisci con il nome corretto se è diverso.
    private lateinit var listaOrdiniAdapter: ListaOrdiniAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_ordini)

        val userId = intent.getStringExtra("userId")


        // Inizializzazione della RecyclerView e dell'Adapter
        recyclerView = findViewById(R.id.recyclerviewordini) // Assicurati di sostituire con l'ID corretto dal tuo layout
        recyclerView.layoutManager = LinearLayoutManager(this)
        listaOrdiniAdapter = ListaOrdiniAdapter(ArrayList())
        recyclerView.adapter = listaOrdiniAdapter

        // Assumendo che tu stia salvando gli ordini in una sottocartella specifica per ogni utente
        val ordiniRef = FirebaseDatabase.getInstance().getReference("Ordini").child(userId!!)

        ordiniRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ordiniList = ArrayList<Ordine>() // Sostituisci 'Order' con il nome della tua classe ordine
                for (ordineSnapshot in snapshot.children) {
                    val ordine = ordineSnapshot.getValue(Ordine::class.java)
                    if (ordine != null) {
                        ordiniList.add(ordine)
                    }
                }
                listaOrdiniAdapter.updateData(ordiniList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Gestisci errori, se necessario. Ad esempio, puoi mostrare un messaggio all'utente.
            }
        })
    }
}
