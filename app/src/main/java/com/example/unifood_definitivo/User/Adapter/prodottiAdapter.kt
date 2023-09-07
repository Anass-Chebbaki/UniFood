package com.example.unifood_definitivo.User.Adapter

import com.example.unifood_definitivo.User.Model.Prodotti
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.unifood_definitivo.databinding.ListaProdottiBinding
import com.squareup.picasso.Picasso

/**
 * Adapter per la visualizzazione dei prodotti nella mainActivity.
 */

class ProdottiAdapter(private var prodottiList: ArrayList<Prodotti>) :
    RecyclerView.Adapter<ProdottiAdapter.ViewHolder>() {
    var onItemClick: ((Prodotti) -> Unit)? = null
    class ViewHolder(val binding: ListaProdottiBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListaProdottiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    /**
     * Questa funzione restituisce il numero di elementi nella lista dei prodotti.
     */
    override fun getItemCount(): Int {
        return prodottiList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = prodottiList[position]

        holder.binding.apply {
            tvNameItem.text = currentItem.nome_prodotto
            tvPriceItem.text = currentItem.prezzo.toString()
            tvIngredientsView.text=currentItem.ingredienti.toString()

            currentItem.imgUri?.let {
                Picasso.get().load(it).into(imgItem)
            }

            root.setOnClickListener {
                onItemClick?.invoke(currentItem)
            }
        }
    }

    /**
     * Questa funzione aggiorna la lista dei prodotti con una nuova lista fornita come argomento e notifica l'adapter dei cambiamenti.
     */
    fun updateData(newData: List<Prodotti>) {
        prodottiList = ArrayList(newData)
        notifyDataSetChanged()
    }
}

