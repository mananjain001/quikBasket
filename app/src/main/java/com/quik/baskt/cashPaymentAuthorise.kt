package com.quik.baskt

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class cashPaymentAuthorise : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_payment_authorise)
    }

    fun authorise(view: View) {
        val editText = findViewById<EditText>(R.id.cashauthoriseedittext)
        val checkUser: Query =
                FirebaseDatabase.getInstance().getReference().child("Orders")
        checkUser.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                finish()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child(editText.text.toString()).exists()) {
                    alertbox(snapshot.child(editText.text.toString()), editText.text.toString())
                } else {
                    toast()
                }
            }
        })
    }

    private fun toast() {
        Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show()
    }

    fun alertbox(snapshot: DataSnapshot, token: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Order exist in database")
        val str: String = "Order no: " + snapshot.key.toString() + "\n" + "Bill Amount: " + snapshot.child("Details").child("Total Amount: ").value.toString() + "\n" + "Total Items: " + snapshot.child("Details").child("Total products: ").value.toString() + "\n" + "Payments: " + snapshot.child("Details").child("Payment").value.toString()
        builder.setMessage(str)
        if (snapshot.child("Details").child("Payment").value.toString() == "Unpaid") {
            builder.setNegativeButton("Mark Paid") { dialog: DialogInterface?, which: Int ->
                val databaseReference = Firebase.database.reference.child("Orders").child(token)
                databaseReference.child("Details").child("Payment").setValue("Paid")
                finish()

            }
        } else {
            builder.setNegativeButton("Bill Already Paid") { dialog: DialogInterface?, which: Int ->
                finish()
            }
            builder.setPositiveButton("Cancel") { dialog, which ->
                finish()
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
        }
    }
}