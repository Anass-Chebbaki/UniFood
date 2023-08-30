package com.example.unifood_definitivo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unifood_definitivo.Adapter.ListaOrdiniAdapter
import com.example.unifood_definitivo.Model.Ordine
import com.example.unifood_definitivo.Model.OrdineS
import com.example.unifood_definitivo.Model.OrdiniSemplificato
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ListaOrdini : AppCompatActivity() {

    private lateinit var listaOrdiniAdapter: ListaOrdiniAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_ordini)

        val userId = intent.getStringExtra("userId")

        recyclerView = findViewById(R.id.recyclerviewordini)
        recyclerView.layoutManager = LinearLayoutManager(this)
        listaOrdiniAdapter = ListaOrdiniAdapter(ArrayList())
        recyclerView.adapter = listaOrdiniAdapter

        val ordiniRef = FirebaseDatabase.getInstance().getReference("OrdiniSemplificati")

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
