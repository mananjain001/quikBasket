package com.quik.baskt

import android.app.Dialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class checkBill : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_bill)

    }

    fun check(view: View) {
        val editText = findViewById<EditText>(R.id.checkbilledittext)
        val checkUser: Query =
            FirebaseDatabase.getInstance().getReference().child("Orders")
        checkUser.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                finish()
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child(editText.text.toString()).exists())
                {
                    alertbox(snapshot.child(editText.text.toString()))
                }
                else
                {
                    toast()
                }
            }
        })
    }

    private fun toast() {
        Toast.makeText(this,"No data found",Toast.LENGTH_SHORT).show()
    }

    fun alertbox(snapshot: DataSnapshot)
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Order exist in database")
        val str:String = "Order no: "+snapshot.key.toString() + "\n" + "Bill Amount: "+snapshot.child("Details").child("Total Amount: ").value.toString() + "\n" +"Total Items: "+ snapshot.child("Details").child("Total products: ").value.toString() +"\n"+"Payments: "+ snapshot.child("Details").child("Payment").value.toString()
        builder.setMessage(str)
        builder.setNegativeButton("Cancel"){
                dialog: DialogInterface?, which: Int -> finish()
        }
        builder.setPositiveButton("More Details"){
            dialog: DialogInterface?, which: Int -> Details(snapshot)
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun Details(snapshot: DataSnapshot) {
        var productList = mutableListOf<String>()
        var priceList = mutableListOf<String>()
        var quantityList = mutableListOf<Int>()
        for (i in snapshot.children)
        {
            if(i.key!="Details")
            {
                productList.add(i.child("Product name ").value.toString())
                priceList.add(i.child("Product price ").value.toString())
                quantityList.add(i.child("Product quantity ").value.toString().toInt())
            }
        }

        var recyclerView = findViewById<RecyclerView>(R.id.check_bill_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = Dialogue_recycler_adapter(productList,priceList,quantityList)
        recyclerView.visibility = View.VISIBLE
        var checkbill_constrainlayout = findViewById<ConstraintLayout>(R.id.checkbill_constrainlayout)
        checkbill_constrainlayout.visibility = View.INVISIBLE
        var ll = findViewById<LinearLayout>(R.id.ll)
        ll.visibility = View.VISIBLE
    }
}