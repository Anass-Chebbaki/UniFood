package com.example.unifood_definitivo.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unifood_definitivo.Model.CartProduct
import com.example.unifood_definitivo.R
import com.squareup.picasso.Picasso

class CartAdapter(private val cartProducts: List<CartProduct>) : RecyclerView.Adapter<CartAdapter.CartItemViewHolder>() {



    class CartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title2Txt)
        val quantityTextView: TextView = itemView.findViewById(R.id.numberItemTxt)
        val priceTextView: TextView = itemView.findViewById(R.id.feeEachItem)
        val totalTextView: TextView = itemView.findViewById(R.id.totalEachItem)
        val picCardTextView: ImageView=itemView.findViewById(R.id.picCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cart_element, parent, false)
        return CartItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val currentItem = cartProducts[position]

        holder.titleTextView.text = currentItem.product.nome_prodotto
        holder.quantityTextView.text = currentItem.quantity.toString()
        holder.priceTextView.text = currentItem.product.prezzo.toString()
        holder.totalTextView.text = currentItem.total.toString()
        Picasso.get()
            .load(currentItem.imgUri) // Assumi che imgUri sia l'URL dell'immagine
            .into(holder.picCardTextView)

        // Aggiungi eventuali logiche per i pulsanti o altre interazioni
    }


    override fun getItemCount(): Int {
        return cartProducts.size
    }

}