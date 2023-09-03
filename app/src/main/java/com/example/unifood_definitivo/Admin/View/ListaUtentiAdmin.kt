package com.example.unifood_definitivo.Admin.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unifood_definitivo.Admin.Adapter.LIstaUtentiAdapter
import com.example.unifood_definitivo.User.Model.User
import com.example.unifood_definitivo.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
/**
 * Activity per la visualizzazione e la gestione della lista degli utenti nel pannello
 * amministratore dell'applicazione.
 */
class ListaUtentiAdmin : AppCompatActivity() {

    private lateinit var ordiniAdapter: LIstaUtentiAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestione_utenti)
        //recyclerviewordini = findViewById(R.id.recyclerviewordini)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerviewlistautenti)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val databaseReference = FirebaseDatabase.getInstance().getReference("Utenti")
        val userList = mutableListOf<User>()
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                userList.clear()
                for (userSnapshot in dataSnapshot.children) {
                    val id = userSnapshot.child("id").getValue(String::class.java) ?: ""
                    // controllo per far si che l'account dell'admin non compaia nella lista
                    if(id!="80XlMK") {
                        val email = userSnapshot.child("email").getValue(String::class.java) ?: ""
                        val name = userSnapshot.child("name").getValue(String::class.java) ?: ""
                        val surname =
                            userSnapshot.child("surname").getValue(String::class.java) ?: ""

                        val user = User(id, name, surname, email)
                        userList.add(user)
                    }
                }
                // Crea e imposta l'adapter della RecyclerView
                ordiniAdapter = LIstaUtentiAdapter(userList)
                recyclerView.adapter = ordiniAdapter
                //ordiniAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Gestisci l'errore qui se necessario
            }
        })

    }
}