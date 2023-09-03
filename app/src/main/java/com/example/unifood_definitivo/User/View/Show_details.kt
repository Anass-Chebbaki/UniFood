package com.example.unifood_definitivo.User.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.unifood_definitivo.User.Model.Prodotti
import com.example.unifood_definitivo.R
import com.squareup.picasso.Picasso
/**
 * Activity per visualizzare i dettagli di un prodotto selezionato e aggiungerlo al carrello.
 * Gli utenti possono vedere il nome, il prezzo e la descrizione del prodotto,
 * oltre a modificarne la quantità da aggiungere al carrello.
 */
class Show_details : AppCompatActivity() {
    private lateinit var quantityTextView: TextView
    private var quantity: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_details)

        val product = intent.getSerializableExtra("product") as? Prodotti
        val userId = intent.getStringExtra("userId")
        if (product != null) {
            // Popolare il layout con i dettagli del prodotto selezionato
            val productNameTextView = findViewById<TextView>(R.id.titleTxt)
            val productPriceTextView = findViewById<TextView>(R.id.priceTxt)
            val productDescriptionTextView = findViewById<TextView>(R.id.ingredientsTxt)
            productNameTextView.text = product.nome_prodotto
            productPriceTextView.text = "${product.prezzo.toString()}€"
            productDescriptionTextView.text = product.ingredienti.toString()
            val productImageView = findViewById<ImageView>(R.id.foodPic)
            product.imgUri2?.let {
                Picasso.get().load(it).into(productImageView)
            }
            val cardBtn=findViewById<TextView>(R.id.card_btn)
            val addToCartButton = findViewById<TextView>(R.id.addToCardBtn)
            addToCartButton.setOnClickListener {
                // Creazione dell'intent e passaggio dei dati
                val intent = Intent(this, Cart_List::class.java)
                intent.putExtra("product", product)
                intent.putExtra("quantity", quantity)
                intent.putExtra("imgUri",product.imgUri2)
                Toast.makeText(this, "Elemento aggiunto al carrello", Toast.LENGTH_SHORT).show()
                if (userId != null) {
                    Cart_List.CartManager.addToCart(userId, product, quantity, product.imgUri2)
                }
            }
            quantityTextView = findViewById(R.id.quantityTxt)
            quantityTextView.text = quantity.toString()

            val plusButton = findViewById<ImageView>(R.id.plusBtn)
            val minusButton = findViewById<ImageView>(R.id.minusBtn)

            plusButton.setOnClickListener {
                increaseQuantity()
            }

            minusButton.setOnClickListener {
                decreaseQuantity()
            }
        } else {
        }


    }
    /**
     * Incrementa la quantità selezionata.
     */
    private fun increaseQuantity() {
        quantity++
        updateQuantity()
    }
    /**
     * Decrementa la quantità selezionata, se possibile.
     */
    private fun decreaseQuantity() {
        if (quantity > 1) {
            quantity--
            updateQuantity()
        }
    }
    /**
     * Aggiorna la visualizzazione della quantità selezionata.
     */
    private fun updateQuantity() {
        quantityTextView.text = quantity.toString()
    }
}
