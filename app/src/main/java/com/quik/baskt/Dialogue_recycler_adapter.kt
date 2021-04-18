package com.quik.baskt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Dialogue_recycler_adapter(private var products: List<String>, private var prices:List<String>, private var quantities:List<Int>):
RecyclerView.Adapter<Dialogue_recycler_adapter.ViewHolder>()
{
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val detail_name: TextView = itemView.findViewById(R.id.detail_name)
        val detail_quantity: TextView = itemView.findViewById(R.id.detail_quantity)
        val detail_price: TextView = itemView.findViewById(R.id.detail_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Dialogue_recycler_adapter.ViewHolder {
        val v  = LayoutInflater.from(parent.context).inflate(R.layout.item_detail_layout,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.detail_quantity.text = quantities[position].toString()
        holder.detail_name.text = products[position]
        holder.detail_price.text = prices[position]
    }

}