package com.example.unifood_definitivo

import android.content.Intent
import com.example.unifood_definitivo.Model.User
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginsignupauth.MainActivity

import com.example.unifood_definitivo.Model.Ordine
import com.example.unifood_definitivo.Model.OrdineS
import com.google.firebase.database.*
import com.example.unifood_definitivo.AdminUtility.Admin_OrdiniAdapter
import java.util.*


class AdminActivity :  AppCompatActivity(), Admin_OrdiniAdapter.OnDeleteClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: Admin_OrdiniAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var database: FirebaseDatabase
    private lateinit var sendButton: TextView
    private lateinit var editText: EditText
    private var orderList: MutableList<OrdineS> = mutableListOf()
    private val updateInterval = 24*60*60 * 1000 // 24 ore in millisecondi


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // aggiornamento periodico della disponibilità delle fasce oraria all'apertura dell'attività
        val now = Calendar.getInstance()
        val tomorrow7AM = Calendar.getInstance()
        tomorrow7AM.set(Calendar.HOUR_OF_DAY, 7)
        tomorrow7AM.set(Calendar.MINUTE, 0)
        tomorrow7AM.set(Calendar.SECOND, 0)
        if (now.after(tomorrow7AM)) {
            tomorrow7AM.add(Calendar.DATE, 1)
        }
        val timeUntilNextUpdate = tomorrow7AM.timeInMillis - now.timeInMillis

        // Avvia l'aggiornamento periodico ogni giorno alle 8:00
        startPeriodicUpdate(timeUntilNextUpdate)
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

    private fun startPeriodicUpdate(delayMillis: Long) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                // Esegui l'aggiornamento della quantità a 10 per tutte le fasce orarie
                updateQuantitiesTo10()

                // Programma il prossimo aggiornamento per il giorno successivo alle 8:00
                val now = Calendar.getInstance()
                val tomorrow7AM = Calendar.getInstance()
                tomorrow7AM.set(Calendar.HOUR_OF_DAY, 7)
                tomorrow7AM.set(Calendar.MINUTE, 0)
                tomorrow7AM.set(Calendar.SECOND, 0)
                if (now.after(tomorrow7AM)) {
                    tomorrow7AM.add(Calendar.DATE, 1)
                }
                val timeUntilNextUpdate = tomorrow7AM.timeInMillis - now.timeInMillis
                handler.postDelayed(this, timeUntilNextUpdate)
            }
        }, delayMillis)
    }

    private fun updateQuantitiesTo10() {
        val reference = database.reference.child("Orari")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    // Aggiorna la quantità (disponibilita) a 10 per ogni fascia oraria
                    dataSnapshot.child("disponibilita").ref.setValue(10)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Gestisci eventuali errori
            }
        })
    }
}


