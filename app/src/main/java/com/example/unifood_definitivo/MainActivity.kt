package com.example.loginsignupauth

import com.example.unifood_definitivo.Model.User
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unifood_definitivo.Adapter.ProdottiAdapter
import com.example.unifood_definitivo.Model.Prodotti

import com.example.unifood_definitivo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var prodottiAdapter: ProdottiAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Inizializza l'istanza di FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()

        // Controlla se l'utente è già autenticato
        if (firebaseAuth.currentUser == null) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
            return
        }

        // Ottieni l'utente dalla intent (se presente)
        val user = intent.getSerializableExtra("user") as? User
        if(user!=null){
            val userName = user.name
            val welcomeTextView = findViewById<TextView>(R.id.textView)
            welcomeTextView.text = "Ciao $userName!"
        }

        // Inizializza la RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        prodottiAdapter = ProdottiAdapter(ArrayList())
        recyclerView.adapter = prodottiAdapter

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)



        // Inizializza il riferimento al database
        databaseReference = FirebaseDatabase.getInstance().getReference("Prodotti")


        // Recupera i dati dei prodotti dal database
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = ArrayList<Prodotti>()
                for (productSnapshot in snapshot.children) {
                    val id = productSnapshot.key // Recupera l'id
                    val nomeProdotto = productSnapshot.child("nome_prodotto").getValue(String::class.java)
                    val prezzo = productSnapshot.child("prezzo").getValue(Double::class.java)
                    val imgUri = productSnapshot.child("imgUri").getValue(String::class.java)
                    val ingredienti=productSnapshot.child("ingredienti").getValue(String::class.java)

                    val product = Prodotti(id, nomeProdotto, prezzo, imgUri,ingredienti)
                    product?.let {
                        productList.add(it)
                        Log.d("ProductData", "Id: $id, Nome: $nomeProdotto, Prezzo: $prezzo, Uri: $imgUri")
                    }
                }
                prodottiAdapter.updateData(productList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Gestisci l'errore se necessario
            }
        })
    }
}