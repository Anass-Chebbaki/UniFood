package com.example.unifood_definitivo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unifood_definitivo.Adapter.CartAdapter
import com.example.unifood_definitivo.Adapter.OrariAdapter
import com.example.unifood_definitivo.Model.*
import com.google.firebase.database.*
/**
 * Questa classe gestisce l'attività del carrello degli acquisti dell'utente. Permette agli utenti
 * di visualizzare i prodotti nel carrello, modificarli e completare l'ordine.
 */
class Cart_List : AppCompatActivity() {
 private val userCarts = HashMap<String, ArrayList<CartProduct>>()
 private val savedCartItems = ArrayList<CartProduct>()
 private lateinit var cartRecyclerView: RecyclerView
 private lateinit var cartListAdapter: CartAdapter
 private val cartItems = ArrayList<CartProduct>()
 private lateinit var database: DatabaseReference
 private val orariList = ArrayList<Orari>()
 private lateinit var orariAdapter: OrariAdapter

 override fun onCreate(savedInstanceState: Bundle?) {
  val saldo = intent.getDoubleExtra("saldo", 0.0)
  super.onCreate(savedInstanceState)
  setContentView(R.layout.activity_cart_list)
  database = FirebaseDatabase.getInstance().reference.child("Orari")
  val orariRecyclerView = findViewById<RecyclerView>(R.id.recyclerview2)
  orariAdapter = OrariAdapter(emptyList())
  orariRecyclerView.adapter = orariAdapter
  orariRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
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
   //val userCart = userCarts.getOrPut(userId) { ArrayList() }
   if (product != null) {
    val cartItem = CartProduct(product, quantity, imgUri, product.prezzo?.times(quantity))
    cartItem.total = product.prezzo?.times(quantity)
    cartItems.add(cartItem)
    cartListAdapter.notifyDataSetChanged() // Aggiorna la RecyclerView
   }
  }
  val fasciaOrariaView = findViewById<TextView>(R.id.fasciaorariaView)
  val inviaOrdineButton = findViewById<TextView>(R.id.checkoutBtn)
  inviaOrdineButton.setOnClickListener {
   val user= intent.getStringExtra("userid")
   val fasciaOraria = orariAdapter.getSelectedFasciaOraria()?:"Fascia oraria non selezionata" // Ottieni la fascia oraria selezionata
   fasciaOrariaView.text = "Orario selezionato: $fasciaOraria"
   // Recupera l'ID dell'utente dall'Intent
   val total = calculateSubtotal(cartItems) + 2.0 // Calcola il totale del carrello
   // Chiamata alla funzione per creare e inviare l'ordine al database
   if (user != null) {
    creaEInviaOrdine(fasciaOraria, user, total,saldo)
    val ordiniSemplificato = OrdiniSemplificato()
    ordiniSemplificato.semplificaOrdini()
   }
  }

  calculateAndDisplayTotal()
  fetchOrariData()

  val cestinoButton = findViewById<ImageView>(R.id.cestinoBtn)
  cestinoButton.setOnClickListener {
   val user = intent.getStringExtra("userid")
   if (user != null) {
    CartManager.clearCart(user)
    cartListAdapter.updateCartItems(emptyList())
    calculateAndDisplayTotal()
    // Eventuali altre operazioni o messaggi che vuoi mostrare
   }
  }

 }
 /**
  * Calcola e visualizza il totale dell'ordine, includendo il totale dei prodotti e la commissione.
  */
 private fun calculateAndDisplayTotal() {
  val subtotal = calculateSubtotal(cartItems)
  val commission = 2.0
  val total = subtotal + commission

  //aaaa

  val subtotalView = findViewById<TextView>(R.id.totalFeeTxt)
  val commissionView = findViewById<TextView>(R.id.taxTxt)
  val totalView = findViewById<TextView>(R.id.totalTxt)

  subtotalView.text = "€$subtotal"
  commissionView.text = " €$commission"
  totalView.text = "$total"
 }
 /**
  * Calcola il totale dei prodotti nel carrello.
  * @param userCart Lista dei prodotti nel carrello dell'utente
  * @return Il totale dei prodotti nel carrello
  */
 private fun calculateSubtotal(userCart: ArrayList<CartProduct>): Double {
  var subtotal = 0.0
  for (cartItem in userCart) {
   subtotal += cartItem.total ?: 0.0
  }
  return subtotal
 }
 /**
  * Crea un nuovo ordine e lo invia al database Firebase. Aggiorna anche il saldo dell'utente e la disponibilità
  * della fascia oraria selezionata.
  * @param fasciaOraria Fascia oraria selezionata per l'ordine
  * @param userId ID dell'utente che effettua l'ordine
  * @param total Totale dell'ordine, comprensivo della commissione
  * @param balance Saldo dell'utente
  */
 private fun creaEInviaOrdine(fasciaOraria: String, userId: String, total: Double, balance: Double) {
  val numeroOrdine = (1..1000).random()
  val cartItems = cartItems
  if (cartItems.isEmpty()) {
   Toast.makeText(this, "Il carrello è vuoto. Aggiungi prodotti prima di effettuare l'ordine.", Toast.LENGTH_SHORT).show()
   return
  }
  if (balance < total) {
   Toast.makeText(this, "Saldo insufficiente", Toast.LENGTH_SHORT).show()
   return
  }

  val orariReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Orari")

  val orariQuery: Query = orariReference.orderByChild("fascia_oraria").equalTo(fasciaOraria)

  val orariListener = object : ValueEventListener {
   override fun onDataChange(orariSnapshot: DataSnapshot) {
    if (orariSnapshot.exists()) {
     for (orarioSnapshot in orariSnapshot.children) {
      val disponibilita = orarioSnapshot.child("disponibilita").getValue(Int::class.java) ?: 0

      if (disponibilita > 0) {
       val order = Ordine(
        fascia_oraria = fasciaOraria,
        lista_prodotti = cartItems,
        numero_ordine = numeroOrdine,
        prezzo = total,
        userId = userId
       )

       val ordersReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Ordini")
       val newOrderReference = ordersReference.push()
       newOrderReference.setValue(order)

       val updatedBalance = balance - total

       val usersReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("Utenti")
       val userReference = usersReference.child(userId)

       val childUpdates = HashMap<String, Any>()
       childUpdates["initialBalance"] = updatedBalance
       userReference.updateChildren(childUpdates)

       val updatedDisponibilita = disponibilita - 1
       orarioSnapshot.child("disponibilita").ref.setValue(updatedDisponibilita)

       Toast.makeText(this@Cart_List, "Ordine effettuato con successo!", Toast.LENGTH_SHORT).show()
       CartManager.clearCart(userId)
       cartListAdapter.updateCartItems(CartManager.getCartItems(userId))
       calculateSubtotal(cartItems)
       calculateAndDisplayTotal()
       return
      } else {
       Toast.makeText(this@Cart_List, "Fascia oraria non disponibile,scegli un'altra!!", Toast.LENGTH_SHORT).show()
       return
      }
     }
    } else {
     Toast.makeText(this@Cart_List, "Fascia oraria non valida", Toast.LENGTH_SHORT).show()
    }
   }

   override fun onCancelled(error: DatabaseError) {
    // Handle error if needed
   }
  }

  orariQuery.addListenerForSingleValueEvent(orariListener)
 }
 /**
  * Recupera i dati relativi agli orari disponibili dal database Firebase e li aggiorna nell'adapter.
  */
 private fun fetchOrariData() {
  val orariList = ArrayList<Orari>() // Create a list to store fetched data
  database.addValueEventListener(object : ValueEventListener {
   override fun onDataChange(snapshot: DataSnapshot) {
    orariList.clear()
    for (orariSnapshot in snapshot.children) {
     val disponibilita = orariSnapshot.child("disponibilita").getValue(Int::class.java)
     val fascia_oraria = orariSnapshot.child("fascia_oraria").getValue(String::class.java)
     if (disponibilita != null && fascia_oraria != null) {
      val orari = Orari(disponibilita, fascia_oraria)
      orariList.add(orari)
     }
    }
    orariAdapter.updateData(orariList) // Update the adapter with fetched data
   }

   override fun onCancelled(error: DatabaseError) {
    // Handle onCancelled if needed
   }
  })
 }


 /**
  * Gestisce il carrello degli acquisti degli utenti.
  */
 object CartManager {
  private val userCarts = HashMap<String, ArrayList<CartProduct>>()
  /**
   * Aggiunge un prodotto al carrello di un utente.
   * @param userId ID dell'utente a cui aggiungere il prodotto.
   * @param product Prodotto da aggiungere al carrello.
   * @param quantity Quantità del prodotto da aggiungere.
   * @param imgUri URL dell'immagine del prodotto.
   */
  fun addToCart(userId: String, product: Prodotti, quantity: Int, imgUri: String?) {
   val userCart = userCarts.getOrPut(userId) { ArrayList() }
   val cartItem = CartProduct(product, quantity, imgUri, product.prezzo?.times(quantity))
   cartItem.total = product.prezzo?.times(quantity)
   userCart.add(cartItem)
  }
  /**
   * Ottiene tutti gli elementi presenti nel carrello di un utente.
   * @param userId ID dell'utente di cui ottenere il carrello.
   * @return Lista di elementi nel carrello dell'utente o una lista vuota se il carrello è vuoto o non esiste.
   */
  fun getCartItems(userId: String): List<CartProduct> {
   return userCarts[userId] ?: emptyList()
  }
  /**
   * Svuota il carrello di un utente.
   * @param userId ID dell'utente di cui svuotare il carrello.
   */
  fun clearCart(userId: String) {
   userCarts.remove(userId)
  }
 }
}


