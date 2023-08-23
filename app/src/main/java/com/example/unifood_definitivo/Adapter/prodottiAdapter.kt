package com.example.unifood_definitivo.Adapter

import com.example.unifood_definitivo.Model.Prodotti


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.unifood_definitivo.databinding.ListaProdottiBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso


class ProdottiAdapter(private val prodottiList: ArrayList<Prodotti>) :
    RecyclerView.Adapter<ProdottiAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListaProdottiBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListaProdottiBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return prodottiList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = prodottiList[position]
        val imgUri = currentItem.imgUri

        holder.binding.apply {
            tvNameItem.text = currentItem.nome_prodotto
            tvPriceItem.text = currentItem.prezzo
            tvIdItem.text = currentItem.id

            // Load the image using Picasso if imgUri is not null
            imgUri?.let {
                Picasso.get().load(imgUri).into(imgItem)
            }

            root.setOnClickListener {
                // Handle the item click event here
            }
        }
    }
}

