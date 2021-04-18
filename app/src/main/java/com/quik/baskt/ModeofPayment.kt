package com.quik.baskt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ModeofPayment : AppCompatActivity() {

    var productList : ArrayList<String>? = null
    var priceList : ArrayList<String>? = null
    var quantityList : ArrayList<Int>? = null
    val databaseReference = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modeof_payment)
        productList = intent.getStringArrayListExtra("productList")
        priceList = intent.getStringArrayListExtra("priceList")
        quantityList = intent.getIntegerArrayListExtra("quantityList")
    }
    fun getmap(str:String):HashMap<String,HashMap<String,String>>
    {
        val hashMap:HashMap<String,HashMap<String,String>> = HashMap<String,HashMap<String,String>>()
        var pricesum = 0
        var itemsum = 0

        for (i in 0..productList!!.size-1)
        {
            val map:HashMap<String,String> = HashMap<String,String>()
            map.put("Product name ",productList!![i])
            map.put("Product price ",(priceList!![i].toInt()*quantityList!![i]).toString())
            map.put("Product quantity ",quantityList!![i].toString())
            pricesum += this.priceList!![i].toInt()* this.quantityList!![i]
            itemsum += this.quantityList!![i]
            hashMap.put("Item ${i+1}",map)
        }
        val map:HashMap<String,String> = HashMap<String,String>()
        map.put("Total products: ",itemsum.toString())
        map.put("Total Amount: ",pricesum.toString())
        map.put("Payment",str)
        hashMap.put("Details",map)
        return hashMap
    }
    fun getRandomString(length: Int) : String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

    fun payThroughCash(view: View) {
        val hashmap = getmap("Unpaid")
        val token = getRandomString(10)
        for(i in hashmap)
            databaseReference.child("Orders").child(token).child(i.key).setValue(i.value)
        intent = Intent(this,CashPaymentUser::class.java)
        intent.putExtra("token",token)
        finishAffinity()
        startActivity(intent)

    }
    fun payThroughOnline(view: View) {
        val hashmap = getmap("Paid")
        val token = getRandomString(10)
        for(i in hashmap)
            databaseReference.child("Orders").child(token).child(i.key).setValue(i.value)
        intent = Intent(this,OnlinePaymentUser::class.java)
        intent.putExtra("token",token)
        finishAffinity()
        startActivity(intent)
    }
}