package com.quik.baskt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import kotlin.collections.ArrayList

class Bill_generation : AppCompatActivity() {
    var productList : ArrayList<String>? = null
    var priceList : ArrayList<String>? = null
    var quantityList : ArrayList<Int>? = null
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill_generation)
        productList = intent.getStringArrayListExtra("productList")
        priceList = intent.getStringArrayListExtra("priceList")
        quantityList = intent.getIntegerArrayListExtra("quantityList")


        recyclerView = findViewById(R.id.checkout_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecyclerAdapter(productList!!.toList(),priceList!!.toList(),quantityList!!.toList(),"Total amount")
        updateUI()
    }


    private fun updateUI() {

        val totalPrice= findViewById<TextView>(R.id.totalPrice)
        val totalItem= findViewById<TextView>(R.id.totalItems)
        val billSummaryText= findViewById<TextView>(R.id.billSummaryText)
        val summary_layout= findViewById<LinearLayout>(R.id.summary_layout)
        billSummaryText.text = "Bill Summary"
        summary_layout.visibility = View.VISIBLE
        var pricesum = 0
        var itemsum = 0
        for (i in 0..productList!!.size-1)
        {
            pricesum += this.priceList!![i].toInt()* this.quantityList!![i]
            itemsum += this.quantityList!![i]
        }
        totalPrice.text = "Total Amount :$pricesum"
        totalItem.text = "Total Items :$itemsum"

    }

    fun MakePayment(view: View) {
        intent = Intent(this,ModeofPayment::class.java)
        intent.putStringArrayListExtra("productList",productList)
        intent.putStringArrayListExtra("priceList",priceList)
        intent.putIntegerArrayListExtra("quantityList",quantityList)
        startActivity(intent)
    }

}