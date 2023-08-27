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

class CartAdapter(private val cartProducts: List<CartProduct>) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_cart_list, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val cartProduct = cartProducts[position]
        holder.bind(cartProduct)
    }

    override fun getItemCount(): Int {
        return cartProducts.size
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val picCardImageView: ImageView = itemView.findViewById(R.id.picCard)
        private val titleTextView: TextView = itemView.findViewById(R.id.title2Txt)
        private val numberItemTextView: TextView = itemView.findViewById(R.id.numberItemTxt)
        private val feeEachItemTextView: TextView = itemView.findViewById(R.id.feeEachItem)
        private val totalEachItemTextView: TextView = itemView.findViewById(R.id.totalEachItem)

        fun bind(cartProduct: CartProduct) {
            titleTextView.text = cartProduct.product.nome_prodotto
            numberItemTextView.text = cartProduct.quantity.toString()
            feeEachItemTextView.text = cartProduct.product.prezzo.toString()
            totalEachItemTextView.text = (cartProduct.quantity * cartProduct.product.prezzo!!).toString()

            // Carica l'immagine utilizzando Picasso
            Picasso.get().load(cartProduct.imgUri).into(picCardImageView)
        }
    }
}