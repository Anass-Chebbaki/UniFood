 package com.example.unifood_definitivo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.loginsignupauth.MainActivity
import com.example.unifood_definitivo.Adapter.CartAdapter
import com.example.unifood_definitivo.Model.CartProduct


import com.example.unifood_definitivo.Model.Prodotti
import com.google.android.material.floatingactionbutton.FloatingActionButton

 class Cart_List : AppCompatActivity() {


  private lateinit var recyclerView: RecyclerView
  private lateinit var cartAdapter: CartAdapter
  private val cartProducts: MutableList<CartProduct> = mutableListOf()

  override fun onCreate(savedInstanceState: Bundle?) {
   super.onCreate(savedInstanceState)
   setContentView(R.layout.activity_cart_list)

   recyclerView = findViewById(R.id.recyclerview)

   // Verifica se l'Intent contiene i dati necessari
   if (intent.hasExtra("product") && intent.hasExtra("quantity") && intent.hasExtra("imgUri")) {
    // Ottieni i dati dall'Intent
    val product = intent.getSerializableExtra("product") as? Prodotti
    val quantity = intent.getIntExtra("quantity", 0)
    val imgUri = intent.getStringExtra("imgUri")

    // Verifica se i dati non sono nulli
    if (product != null && imgUri != null) {
     // Dati ricevuti correttamente
     println("Product: $product")
     println("Quantity: $quantity")
     println("ImgUri: $imgUri")

     // Crea un oggetto CartProduct con le informazioni ricevute
     val cartProduct = CartProduct(product, quantity, imgUri)
     cartProducts.add(cartProduct)

     // Inizializza e imposta l'adapter
     cartAdapter = CartAdapter(cartProducts)
     recyclerView.layoutManager = LinearLayoutManager(this)
     recyclerView.adapter = cartAdapter
    } else {
     // Alcuni dati sono nulli, mostra un messaggio di errore
     Toast.makeText(this, "Dati ricevuti dall'Intent non validi", Toast.LENGTH_SHORT).show()
    }
   } else {
    // Messaggio di avviso nel caso in cui manchino i dati nell'Intent
    Toast.makeText(this, "Dati mancanti nell'Intent", Toast.LENGTH_SHORT).show()
    // Puoi anche chiudere questa activity se i dati sono necessari
    finish()
   }
  }
 }