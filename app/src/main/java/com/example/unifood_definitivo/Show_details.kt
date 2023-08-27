package com.example.unifood_definitivo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.unifood_definitivo.Model.Prodotti
import com.squareup.picasso.Picasso

class Show_details : AppCompatActivity() {
    private lateinit var quantityTextView: TextView
    private var quantity: Int = 1 // Initial quantity value

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_details)

        val product = intent.getSerializableExtra("product") as? Prodotti
        if (product != null) {
            // Populate your layout with the details of the selected product
            val productNameTextView = findViewById<TextView>(R.id.titleTxt)
            val productPriceTextView = findViewById<TextView>(R.id.priceTxt)
            val productDescriptionTextView = findViewById<TextView>(R.id.ingredientsTxt)
            // ... (other views you want to populate)

            productNameTextView.text = product.nome_prodotto
            productPriceTextView.text = product.prezzo.toString()
            productDescriptionTextView.text = product.ingredienti.toString()


            // Load and display the image using Picasso or any other image loading library
            val productImageView = findViewById<ImageView>(R.id.foodPic)
            product.imgUri2?.let {
                Picasso.get().load(it).into(productImageView)
            }
            val addToCartButton = findViewById<TextView>(R.id.addToCardBtn)
            addToCartButton.setOnClickListener {
                // Creazione dell'intent e passaggio dei dati
                val intent = Intent(this, Cart_List::class.java)
                intent.putExtra("product", product)
                intent.putExtra("quantity", quantity)
                intent.putExtra("imgUri",product.imgUri2)
                Toast.makeText(this, "Elemento aggiunto al carrello", Toast.LENGTH_SHORT).show()
                println("Dati inviati nell'Intent:")
                println("Product: $product")
                println("Quantity: $quantity")
                println("ImgUri: ${product.imgUri2}")


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
            // Handle the case where the product data is missing or invalid
            // You might want to show an error message or return to the previous screen
        }


    }
    private fun increaseQuantity() {
        quantity++
        updateQuantity()
    }

    private fun decreaseQuantity() {
        if (quantity > 1) {
            quantity--
            updateQuantity()
        }
    }

    private fun updateQuantity() {
        quantityTextView.text = quantity.toString()
    }
}
