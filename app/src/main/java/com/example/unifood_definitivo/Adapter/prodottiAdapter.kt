package com.example.unifood_definitivo.Adapter

import com.example.unifood_definitivo.Model.Prodotti


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.unifood_definitivo.databinding.ListaProdottiBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso


class prodottiAdapter(private  val prodottiList : java.util.ArrayList<Prodotti>) : RecyclerView.Adapter<prodottiAdapter.ViewHolder>() {

    class ViewHolder(val binding : ListaProdottiBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return  ViewHolder(ListaProdottiBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return prodottiList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = prodottiList[position]
        holder.apply {
            binding.apply {
                tvNameItem.text = currentItem.nome_prodotto
                tvPriceItem.text = currentItem.prezzo
                tvIdItem.text = currentItem.id
                Picasso.get().load(currentItem.imgUri).into(imgItem)


                /*rvContainer.setOnClickListener {

                    val action = HomeFragmentDirections.actionHomeFragmentToUpdateFragment(
                        currentItem.id.toString(),
                        currentItem.name.toString(),
                        currentItem.phoneNumber.toString(),
                        currentItem.imgUri.toString()
                    )
                    findNavController(holder.itemView).navigate(action)*/
                }

                }
            }
        }

