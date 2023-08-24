package com.example.unifood_definitivo.Adapter

import com.example.unifood_definitivo.Model.Prodotti


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.unifood_definitivo.R
import com.example.unifood_definitivo.databinding.ListaProdottiBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso



class ProdottiAdapter(private var prodottiList: ArrayList<Prodotti>) :
    RecyclerView.Adapter<ProdottiAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListaProdottiBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListaProdottiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return prodottiList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = prodottiList[position]

        holder.binding.apply {
            tvNameItem.text = currentItem.nome_prodotto
            tvPriceItem.text = currentItem.prezzo.toString()

            // Directly access the ingredientsView using findViewById
            //holder.itemView.findViewById<TextView>(R.id.ingredientsView).text = currentItem.ingredienti ?: "Ingredienti non disponibili"



            currentItem.imgUri?.let {
                Picasso.get().load(it).into(imgItem)
            }

            root.setOnClickListener {
                // Handle click event here
            }
        }
    }

    fun updateData(newData: List<Prodotti>) {
        prodottiList = ArrayList(newData)
        notifyDataSetChanged()
    }
}

