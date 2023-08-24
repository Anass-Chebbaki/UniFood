package com.example.loginsignupauth

import com.example.unifood_definitivo.Model.User
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unifood_definitivo.Adapter.CategorieAdapter
import com.example.unifood_definitivo.Adapter.ProdottiAdapter
import com.example.unifood_definitivo.Model.Categorie
import com.example.unifood_definitivo.Model.Prodotti

import com.example.unifood_definitivo.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class MainActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView1: RecyclerView
    private lateinit var categorieAdapter: CategorieAdapter
    private lateinit var prodottiAdapter: ProdottiAdapter
    private lateinit var databaseReference: DatabaseReference
    private val fullProductList = ArrayList<Prodotti>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser == null) {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()
            return
        }

        val user = intent.getSerializableExtra("user") as? User
        if (user != null) {
            val userName = user.name
            val welcomeTextView = findViewById<TextView>(R.id.textView)
            welcomeTextView.text = "Ciao $userName!"
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        prodottiAdapter = ProdottiAdapter(ArrayList())
        recyclerView.adapter = prodottiAdapter

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val categories = listOf(
            Categorie("Pizza", R.drawable.cat_1, R.drawable.cat_background1),
            Categorie("Panini", R.drawable.cat_2, R.drawable.cat_background2),
            Categorie("Insalate", R.drawable.cat_3, R.drawable.cat_background3),
            Categorie("Bibite", R.drawable.cat_4, R.drawable.cat_background4),
            Categorie("Dolci", R.drawable.cat_5, R.drawable.cat_background5)
        )
        categorieAdapter = CategorieAdapter(categories)
        recyclerView1 = findViewById(R.id.recyclerView1)
        recyclerView1.layoutManager = LinearLayoutManager(this)
        recyclerView1.adapter = categorieAdapter

        recyclerView1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        databaseReference = FirebaseDatabase.getInstance().getReference("Prodotti")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val productList = ArrayList<Prodotti>()
                for (productSnapshot in snapshot.children) {
                    val id = productSnapshot.key
                    val nomeProdotto = productSnapshot.child("nome_prodotto").getValue(String::class.java)
                    val prezzo = productSnapshot.child("prezzo").getValue(Double::class.java)
                    val imgUri = productSnapshot.child("imgUri").getValue(String::class.java)
                    val ingredienti = productSnapshot.child("ingredienti").getValue(String::class.java)

                    val product = Prodotti(id, nomeProdotto, prezzo, imgUri, ingredienti)
                    product?.let {
                        productList.add(it)
                    }
                }
                fullProductList.clear()
                fullProductList.addAll(productList)
                prodottiAdapter.updateData(productList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if needed
            }
        })

        val searchEditText = findViewById<EditText>(R.id.editTextTextPersonName)
        val deleteBtn = findViewById<ImageView>(R.id.deleteBtn)

        deleteBtn.setOnClickListener {
            searchEditText.text.clear()
        }
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().toLowerCase()
                val filteredList = ArrayList<Prodotti>()

                for (product in fullProductList) {
                    if (product.nome_prodotto?.toLowerCase()?.contains(searchText) == true) {
                        filteredList.add(product)
                    }
                }

                prodottiAdapter.updateData(filteredList)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}