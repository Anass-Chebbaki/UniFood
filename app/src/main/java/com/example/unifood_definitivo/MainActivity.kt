package com.example.loginsignupauth

import com.example.unifood_definitivo.Model.User
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unifood_definitivo.*
import com.example.unifood_definitivo.Adapter.CategorieAdapter
import com.example.unifood_definitivo.Adapter.ProdottiAdapter
import com.example.unifood_definitivo.Model.Categorie
import com.example.unifood_definitivo.Model.Prodotti
import com.example.unifood_definitivo.R

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.collections.ArrayList

/**
 * Activity principale dell'applicazione, che mostra una lista di prodotti suddivisi per categorie.
 * Gli utenti possono effettuare la ricerca di prodotti, visualizzare dettagli e aggiungere prodotti al carrello.
 */
class MainActivity : AppCompatActivity() {
    private var selectedCategory: String? = null
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerView1: RecyclerView
    private lateinit var categorieAdapter: CategorieAdapter
    private lateinit var prodottiAdapter: ProdottiAdapter
    private lateinit var databaseReference: DatabaseReference
    private lateinit var database:FirebaseDatabase
    private lateinit var textView:TextView
    private val fullProductList = ArrayList<Prodotti>()
    //aaaa

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.textView5)
        database = FirebaseDatabase.getInstance()
        // Riferimento al nodo "PrimoDelGiorno"
        val reference = database.reference.child("PrimoDelGiorno")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue(String::class.java)
                textView.text = value
            }

            override fun onCancelled(error: DatabaseError) {
                // Gestisci eventuali errori
            }
        })
        val user = intent.getSerializableExtra("user") as? User
        val userId= user?.id

        val imageStatoOrdini = findViewById<ImageView>(R.id.utentiView)
        imageStatoOrdini.setOnClickListener {
            val intent2 = Intent(this, ListaOrdini::class.java)
            intent2.putExtra("userId", userId)
            startActivity(intent2)
        }
        val profiloImg= findViewById<ImageView>(R.id.profiloImg)
        profiloImg.setOnClickListener{
            val intent= Intent(this,Profilo::class.java)
            if (user != null) {
                intent.putExtra("userName",user.name)
                intent.putExtra("userId", user.id)
            }
            startActivity(intent)
        }

        val balance = user?.initialBalance
        if (user != null) {
            val userName = user.name
            val userEmail=user.email
            val userId= user.id
            val welcomeTextView = findViewById<TextView>(R.id.textView)
            welcomeTextView.text = "Ciao $userName!"
        } else {
        }
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        prodottiAdapter = ProdottiAdapter(ArrayList())
        recyclerView.adapter = prodottiAdapter
        prodottiAdapter.onItemClick = { product ->
            val intent = Intent(this@MainActivity, Show_details::class.java)
            intent.putExtra("product", product)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }


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
        categorieAdapter.setOnItemClickListener { selectedCategory ->
            if (this.selectedCategory == selectedCategory) {
                // Deseleziona la categoria
                this.selectedCategory = null
                // Aggiorna la RecyclerView dei prodotti con tutti i prodotti
                prodottiAdapter.updateData(fullProductList)
            } else {
                // Seleziona la nuova categoria
                this.selectedCategory = selectedCategory
                // Filtra e aggiorna la RecyclerView dei prodotti
                filterProductsByCategory(selectedCategory)
            }
        }


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
                    val categoria_appartenenza=productSnapshot.child("categoria_appartenenza").getValue(String::class.java)
                    val imgUri2 = productSnapshot.child("imgUri2").getValue(String::class.java)
                    val product = Prodotti(id, nomeProdotto, prezzo, imgUri, ingredienti, categoria_appartenenza,imgUri2)
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
        val cardBtn = findViewById<FloatingActionButton>(R.id.card_btn)
        cardBtn.setOnClickListener {
            val intent = Intent(this, Cart_List::class.java)
            // Pass the cart items to the Cart_List activity
            val userId=user?.id ?: ""
            val cartItems = Cart_List.CartManager.getCartItems(userId)
            intent.putExtra("cartItems", ArrayList(cartItems))
            intent.putExtra("saldo",balance)
            intent.putExtra("userid",userId)

            startActivity(intent)
        }
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
    /**
     * Filtra i prodotti in base alla categoria selezionata o mostra tutti i prodotti se nessuna categoria è selezionata.
     */
    private fun filterProductsByCategory(category: String?) {
        val filteredProducts = if (category.isNullOrEmpty()) {
            fullProductList // Mostra tutti i prodotti se nessuna categoria è selezionata
        } else {
            fullProductList.filter { it.categoria_appartenenza == category }
        }
        prodottiAdapter.updateData(filteredProducts)
    }

}