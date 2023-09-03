package com.example.unifood_definitivo.User.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.unifood_definitivo.User.Model.OrdineS
import com.example.unifood_definitivo.R

/**
 * Adapter utilizzato per la visualizzazione delle informazioni degli ordini effettuati dall'utente
 */
class ListaOrdiniAdapter(private var ordiniList: List<OrdineS>) : RecyclerView.Adapter<ListaOrdiniAdapter.OrderViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listaordini_element, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val ordine = ordiniList[position]

        holder.ordineNumber.text = "Numero Ordine: ${ordine.numero_ordine}"
        holder.orario.text = "Orario: ${ordine.fascia_oraria}"
        holder.totale.text = ordine.prezzo.toString()
        holder.listaprodotti.text = ordine.nomiProdotti.joinToString(", ")
    }

    /**
     *  Questa funzione restituisce il numero di elementi nella lista degli ordini.
     */
    override fun getItemCount() = ordiniList.size

    inner class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ordineNumber: TextView = view.findViewById(R.id.idUtente)
        val orario: TextView = view.findViewById(R.id.Orario_ordine)
        val totale: TextView = view.findViewById(R.id.total)
        val listaprodotti: TextView = view.findViewById(R.id.userName)
    }

    /**
     * Questa funzione aggiorna i dati dell'adapter con una nuova lista fornita come argomento e notifica l'adapter dei cambiamenti.
     */
    fun updateData(newOrdiniList: List<OrdineS>) {
        this.ordiniList = newOrdiniList
        notifyDataSetChanged()
    }
}