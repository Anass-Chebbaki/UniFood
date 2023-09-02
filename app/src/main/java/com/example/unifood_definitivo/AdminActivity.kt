package com.example.unifood_definitivo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unifood_definitivo.Model.OrdineS
import com.google.firebase.database.*
import com.example.unifood_definitivo.AdminUtility.Admin_OrdiniAdapter
import com.example.unifood_definitivo.AdminUtility.ListaOrdiniAdmin

class AdminActivity :  AppCompatActivity(), Admin_OrdiniAdapter.OnDeleteClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Admin_OrdiniAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var database: FirebaseDatabase
    private lateinit var sendButton: TextView
    private lateinit var editText: EditText
    private lateinit var utentiView: ImageView
    private var orderList: MutableList<OrdineS> = mutableListOf()
    private val updateInterval = 24 * 60 * 60 * 1000 // 24 ore in millisecondi


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)
        editText = findViewById(R.id.txtprimo)
        sendButton = findViewById(R.id.sendButton)
        recyclerView = findViewById(R.id.recyclerView1)
        utentiView=findViewById<ImageView>(R.id.utentiView)
        layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val orderList: MutableList<OrdineS> = mutableListOf()
        adapter = Admin_OrdiniAdapter(orderList, this@AdminActivity)
        recyclerView.adapter = adapter
        database = FirebaseDatabase.getInstance()
        val reference = database.reference.child("OrdiniSemplificati")
        val reference2 = database.reference.child("PrimoDelGiorno")
        reference2.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(String::class.java)
                editText.setText(value) // Imposta il testo dell'EditText con il valore dal database
            }

            override fun onCancelled(error: DatabaseError) {
                // Gestisci eventuali errori
            }
        })
        utentiView.setOnClickListener {
            val intent=Intent(this,ListaOrdiniAdmin::class.java)
            adapter.notifyDataSetChanged()
            startActivity(intent)
        }
        sendButton.setOnClickListener {
            val userInput = editText.text.toString()
            reference2.setValue(userInput) // Aggiorna il valore nel database
        }
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                orderList.clear()
                for (dataSnapshot in snapshot.children) {
                    val ordine = dataSnapshot.getValue(OrdineS::class.java)
                    // Verifica che l'ordine non sia nullo
                    if (ordine != null) {
                        orderList.add(ordine)
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Gestisci eventuali errori
            }
        })
    }

    override fun onDeleteClick(ordine: OrdineS) {
        val orderNumber = ordine.numero_ordine
        // Remove the item from the RecyclerView
        val position = orderList.indexOf(ordine)
        if (position != -1) {
            orderList.removeAt(position)
            adapter.notifyItemRemoved(position)
        }
        // Remove all order data for the specific order number from the "OrdiniSemplificati" section
        val database = FirebaseDatabase.getInstance()
        val semplificatiReference = database.reference.child("OrdiniSemplificati")
        semplificatiReference.orderByChild("numero_ordine").equalTo(orderNumber.toDouble())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val orderKey = snapshot.key
                        if (orderKey != null) {
                            semplificatiReference.child(orderKey).removeValue()
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle any errors
                }
            })
        // Remove all order data for the specific order number from the "Ordini" section
        val ordiniReference = database.reference.child("Ordini")
        ordiniReference.orderByChild("numero_ordine").equalTo(orderNumber.toDouble())
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val orderKey = snapshot.key
                        if (orderKey != null) {
                            ordiniReference.child(orderKey).removeValue()
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle any errors
                }
            })
    }
}
