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

  private val savedCartItems = ArrayList<CartProduct>()
  private lateinit var cartRecyclerView: RecyclerView
  private lateinit var cartListAdapter: CartAdapter
  private val cartItems = ArrayList<CartProduct>()

  override fun onCreate(savedInstanceState: Bundle?) {
   super.onCreate(savedInstanceState)
   setContentView(R.layout.activity_cart_list)

   cartRecyclerView = findViewById(R.id.recyclerview)
   cartListAdapter = CartAdapter(cartItems)
   cartRecyclerView.adapter = cartListAdapter
   cartRecyclerView.layoutManager = LinearLayoutManager(this)
   val intentCartItems = intent.getSerializableExtra("cartItems") as ArrayList<CartProduct>?
   if (intentCartItems != null) {
    cartItems.addAll(intentCartItems)
    cartListAdapter.notifyDataSetChanged()
   } else {
    // Verifica se ci sono dati nell'Intent e aggiungi il prodotto al carrello
    val product = intent.getSerializableExtra("product") as Prodotti?
    val quantity = intent.getIntExtra("quantity", 0)
    val imgUri = intent.getStringExtra("imgUri")
    println("Dati Ricevuti nell'Intent:")
    println("Product: $product")
    println("Quantity: $quantity")
    println("ImgUri: $imgUri")

    if (product != null) {
     val cartItem = CartProduct(product, quantity, imgUri, product.prezzo?.times(quantity))
     cartItem.total = product.prezzo?.times(quantity)
     cartItems.add(cartItem)
     cartListAdapter.notifyDataSetChanged() // Aggiorna la RecyclerView
    }
   }
   calculateAndDisplayTotal()
  }
  private fun calculateAndDisplayTotal() {
   val subtotal = calculateSubtotal()
   val commission = 2.0
   val total = subtotal + commission

   val subtotalView = findViewById<TextView>(R.id.totalFeeTxt)
   val commissionView = findViewById<TextView>(R.id.taxTxt)
   val totalView = findViewById<TextView>(R.id.totalTxt)

   subtotalView.text = "$$subtotal"
   commissionView.text = " $$commission"
   totalView.text = "$$total"
  }

  private fun calculateSubtotal(): Double {
   var subtotal = 0.0
   for (cartItem in cartItems) {
    subtotal += cartItem.total ?: 0.0
   }
   return subtotal
  }

  object CartManager {
   private val cartItems = ArrayList<CartProduct>()
   fun addToCart(product: Prodotti, quantity: Int, imgUri: String?) {
    val cartItem = CartProduct(product, quantity, imgUri, product.prezzo?.times(quantity))
    cartItem.total = product.prezzo?.times(quantity)
    cartItems.add(cartItem)
   }

   fun getCartItems(): List<CartProduct> {
    return cartItems
   }
  }
 }