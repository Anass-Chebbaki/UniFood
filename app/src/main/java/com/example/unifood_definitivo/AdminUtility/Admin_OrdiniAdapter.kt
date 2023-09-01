    package com.example.unifood_definitivo.AdminUtility

    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Button
    import android.widget.TextView
    import androidx.recyclerview.widget.RecyclerView
    import com.example.unifood_definitivo.Model.Ordine
    import com.example.unifood_definitivo.Model.OrdineS
    import com.example.unifood_definitivo.R

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
            //holder.useridTextView.text="User Id: ${ordine.userId}"
            holder.prezzoTextView.text= ordine.prezzo.toString()
            holder.button.setOnClickListener {
                onDeleteClickListener.onDeleteClick(ordine)
            }
            // Aggiungi altre viste per gli altri campi dell'Ordine se necessario
        }

        inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val prezzoTextView:TextView=itemView.findViewById(R.id.total)
            //val useridTextView:TextView=itemView.findViewById(R.id.useridView)
            val numeroOrdineTextView: TextView = itemView.findViewById(R.id.ordineNumber)
            val orarioOrdineTextView: TextView = itemView.findViewById(R.id.Orario_ordine)
            val listaProdottiTextView: TextView = itemView.findViewById(R.id.listaprodotti)
            val button:TextView =itemView.findViewById(R.id.button)

            // Inizializza le altre viste del layout dell'elemento
        }
        override fun getItemCount(): Int {
            return orderList.size
        }
    }