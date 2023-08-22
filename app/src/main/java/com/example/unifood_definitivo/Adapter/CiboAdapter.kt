package com.example.unifood_definitivo.Adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.unifood_definitivo.Model.CiboData
import com.example.unifood_definitivo.databinding.CibiLayoutBinding

class CiboAdapter(
    var c:Context, var cibiList: ArrayList<CiboData>
) :RecyclerView.Adapter<CiboAdapter.CibiViewHolder>()
{
    inner class CibiViewHolder(v:CibiLayoutBinding):RecyclerView.ViewHolder(v.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CibiViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: CibiViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}