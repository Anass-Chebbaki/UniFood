package com.example.unifood_definitivo.AdminUtility

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unifood_definitivo.Model.User
import com.example.unifood_definitivo.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ListaOrdiniAdmin : AppCompatActivity() {
   // private lateinit var recyclerviewordini: RecyclerView
    private lateinit var ordiniAdapter: LIstaOrdiniAdapter // Assicurati di aver creato l'adapter come spiegato in precedenza

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_ordini_admin)
        //recyclerviewordini = findViewById(R.id.recyclerviewordini)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerviewlistautenti)// Sostituisci R.id.recyclerView con l'ID corretto della tua RecyclerView nel layout XML
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Recupera i dati dal database Firebase come mostrato nel mio messaggio precedente
        val databaseReference = FirebaseDatabase.getInstance().getReference("Utenti")

        val userList = mutableListOf<User>()

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
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
                ordiniAdapter = LIstaOrdiniAdapter(userList)
                recyclerView.adapter = ordiniAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Gestisci l'errore qui se necessario
            }
        })
    }
}