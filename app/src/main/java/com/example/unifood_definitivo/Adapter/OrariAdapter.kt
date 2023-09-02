package com.example.unifood_definitivo.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unifood_definitivo.Model.Orari
import com.example.unifood_definitivo.R
/**
 * Adapter per la visualizzazione delle fasce orarie.
 */
class OrariAdapter(private var orariList: List<Orari>) : RecyclerView.Adapter<OrariAdapter.ViewHolder>() {
    private var selectedFasciaOraria: String? = null


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fascia_orariaTextView: TextView = itemView.findViewById(R.id.orariName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_orari, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orariData = orariList[position]
        holder.fascia_orariaTextView.text=orariData.fascia_oraria
        holder.itemView.setOnClickListener {
            selectedFasciaOraria = orariData.fascia_oraria
            notifyDataSetChanged()
        }

    }

    /**
     * Questa funzione restituisce il numero di elementi nella lista degli orari.
     */
    override fun getItemCount(): Int {
        return orariList.size
    }

    /**
     * Questa funzione aggiorna i dati dell'adapter con una nuova lista fornita come argomento e notifica l'adapter dei cambiamenti.
     */
    fun updateData(newOrariList: List<Orari>) {
        orariList = newOrariList
        notifyDataSetChanged()
    }

    /**
     * Questa funzione restituisce la fascia oraria selezionata.
     */
    fun getSelectedFasciaOraria(): String? {
        return selectedFasciaOraria
    }

}