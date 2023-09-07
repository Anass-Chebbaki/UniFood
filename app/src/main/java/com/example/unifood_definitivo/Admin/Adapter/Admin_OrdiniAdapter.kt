    package com.example.unifood_definitivo.Admin.Adapter

    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.TextView
    import androidx.recyclerview.widget.RecyclerView
    import com.example.unifood_definitivo.User.Model.OrdineS
    import com.example.unifood_definitivo.R
    /**
     * Adapter per la visualizzazione degli ordini nella schermata dell admin
     */
    class Admin_OrdiniAdapter(private val orderList: List<OrdineS>, private val onDeleteClickListener: OnDeleteClickListener): RecyclerView.Adapter<Admin_OrdiniAdapter.OrderViewHolder>() {
        interface OnDeleteClickListener {
            fun onDeleteClick(ordine: OrdineS)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.adminlistaordini_element, parent, false)
            return OrderViewHolder(view)
        }

        override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
            val ordine = orderList[position]

            // Popola le viste nel layout dell'elemento con i dati da ordine

            holder.numeroOrdineTextView.text = ordine.numero_ordine.toString()
            holder.orarioOrdineTextView.text = ordine.fascia_oraria
            holder.listaProdottiTextView.text = ordine.nomiProdotti.toString()
            holder.prezzoTextView.text= ordine.prezzo.toString()
            holder.button.setOnClickListener {
                onDeleteClickListener.onDeleteClick(ordine)
            }
        }

        inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val prezzoTextView:TextView=itemView.findViewById(R.id.total)
            val numeroOrdineTextView: TextView = itemView.findViewById(R.id.idUtente)
            val orarioOrdineTextView: TextView = itemView.findViewById(R.id.Orario_ordine)
            val listaProdottiTextView: TextView = itemView.findViewById(R.id.userName)
            val button:TextView =itemView.findViewById(R.id.cancellautente)

            // Possibilità di inizializzare le altre viste del layout dell'elemento
        }
        /**
         *  Questa funzione restituisce il numero di elementi nella lista degli ordini.
         */
        override fun getItemCount(): Int {
            return orderList.size
        }
    }