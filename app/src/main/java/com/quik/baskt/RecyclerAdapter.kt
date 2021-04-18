package com.quik.baskt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(private var products: List<String>, private var prices:List<String>, private var quantities:List<Int>,private var str: String):
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productQuantity: TextView = itemView.findViewById(R.id.productQuantity)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v  = LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.productQuantity.text = "Quantity: "+quantities[position].toString()
        holder.productName.text = products[position]
        if (str == "Price per piece")
        {
            holder.productPrice.text = "$str: ₹"+prices[position]
        }
        else
        {
            holder.productPrice.text = "$str: ₹"+(prices[position].toInt()*quantities[position]).toString()
        }
    }
}