package com.quik.baskt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

open class UserHome : AppCompatActivity() {

    private var productList = mutableListOf<String>()
    private var priceList = mutableListOf<String>()
    private var quantityList = mutableListOf<Int>()
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_home)
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecyclerAdapter(productList,priceList,quantityList,"Price per piece")
    }

    fun Scan(view: View) {
        scanCode()
    }

    fun scanCode()
    {
        val intentIntegrator = IntentIntegrator(this)
        intentIntegrator.setCaptureActivity(CaptureClass::class.java)
        intentIntegrator.setOrientationLocked(false)
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        intentIntegrator.setPrompt("Scanning code")
        intentIntegrator.initiateScan()
    }

    override fun onActivityResult(requestCode:Int, resultCode: Int, data:Intent?)
    {
        val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
        if (result != null)
        {
            if (result.contents != null)
            {
                checkProduct(result.contents)
            }
            else{
                Toast.makeText(applicationContext,"No result", Toast.LENGTH_LONG).show()
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    private fun checkProduct(barcodenumber:String){
        var exist = true
        val checkUser: Query =
            FirebaseDatabase.getInstance().getReference().child("Products")
        checkUser.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                finish()
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child(barcodenumber).exists())
                {
                    quantityInput(snapshot.child(barcodenumber))
                }
                else
                {
                    Toast.makeText(this@UserHome,"Product not found ",Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun quantityInput(snapshot: DataSnapshot) {
        val builder = AlertDialog.Builder(this)
        val inflater= layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_text_layout,null)
        val edit_text = dialogLayout.findViewById<EditText>(R.id.edit_text)
        var quantity: Int

        with(builder)
        {
            setTitle("Enter quantity")
            setPositiveButton("Ok"){dialog, which ->
                quantity = edit_text.text.toString().toInt()
                addTolist(snapshot.child("name").value.toString(),snapshot.child("price").value.toString(),quantity)
            }
            setNegativeButton("Cancel"){dialog, which ->
                finish()
            }
            setView(dialogLayout)
            show()
        }
    }

    private fun addTolist(name:String , price:String, quantity:Int)
    {
        productList.add(name)
        priceList.add(price)
        quantityList.add(quantity)
        updateUI()
    }

    fun checkout(view: View) {
        val intent = Intent(this, Bill_generation::class.java)
        intent.putStringArrayListExtra("productList",ArrayList(productList))
        intent.putStringArrayListExtra("priceList",ArrayList(priceList))
        intent.putIntegerArrayListExtra("quantityList",ArrayList(quantityList))
        startActivity(intent)
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
        totalPrice.text = "Price :$pricesum"
        totalItem.text = "Items :$itemsum"

    }


}

