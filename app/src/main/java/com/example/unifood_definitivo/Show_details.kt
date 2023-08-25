package com.example.unifood_definitivo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.example.unifood_definitivo.Model.Prodotti
import com.squareup.picasso.Picasso

class Show_details : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_details)

        val product = intent.getSerializableExtra("product") as? Prodotti
        if (product != null) {
            // Populate your layout with the details of the selected product
            val productNameTextView = findViewById<TextView>(R.id.titleTxt)
            val productPriceTextView = findViewById<TextView>(R.id.priceTxt)
            val productDescriptionTextView=findViewById<TextView>(R.id.ingredientsTxt)
            // ... (other views you want to populate)

            productNameTextView.text = product.nome_prodotto
            productPriceTextView.text = product.prezzo.toString()
            productDescriptionTextView.text=product.ingredienti


            // Load and display the image using Picasso or any other image loading library
            val productImageView = findViewById<ImageView>(R.id.foodPic)
            product.imgUri?.let {
                Picasso.get().load(it).into(productImageView)
            }
        } else {
            // Handle the case where the product data is missing or invalid
            // You might want to show an error message or return to the previous screen
        }
    }
}