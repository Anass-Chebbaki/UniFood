package com.example.unifood_definitivo.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unifood_definitivo.Model.Orari
import com.example.unifood_definitivo.R

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

    override fun getItemCount(): Int {
        return orariList.size
    }

    // Update the adapter's data with a new list
    fun updateData(newOrariList: List<Orari>) {
        orariList = newOrariList
        notifyDataSetChanged()
    }
    fun setSelectedFasciaOraria(fasciaOraria: String) {
        selectedFasciaOraria = fasciaOraria
        notifyDataSetChanged()
    }

    fun getSelectedFasciaOraria(): String? {
        return selectedFasciaOraria
    }

}
