package com.quik.baskt

import android.content.DialogInterface
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.FirebaseError
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class AddItem : AppCompatActivity() {
    var barcodenumber = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)
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

    override fun onActivityResult(requestCode:Int, resultCode: Int, data: Intent?)
    {
        val result: IntentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
        if (result != null)
        {
            if (result.contents != null)
            {
                UpdateUI(result.contents)
                barcodenumber = result.contents
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

    private fun UpdateUI(barcodeNumber: String?) {
        if(checkProduct()) {
            val scanLayout = findViewById<LinearLayout>(R.id.scanLayout)
            val scanformlayout = findViewById<LinearLayout>(R.id.scanformlayout)
            scanLayout.visibility = View.INVISIBLE
            scanformlayout.visibility = View.VISIBLE
            val barcode_number = findViewById<TextView>(R.id.barcode_number)
            barcode_number.text = barcodeNumber
        }
    }

    private fun checkProduct():Boolean {
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
                    dataExist(snapshot.child(barcodenumber))
                    exist = false
                }
            }
        })
        return exist
    }

    private fun dataExist(snapshot: DataSnapshot) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Product exist in database")
        val str:String = "Product Name: "+snapshot.child("name").value.toString() + "\n" + "Product Price: "+snapshot.child("price").value.toString() + "\n" +"Product Barcode: "+ snapshot.child("barcode").value.toString()
        builder.setMessage(str)
        builder.setPositiveButton("Scan Again"){
            dialog: DialogInterface?, which: Int -> scanCode()
        }
        builder.setNegativeButton("Cancel"){
            dialog: DialogInterface?, which: Int -> finish()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun sendData(view: View) {
        val productNameEditText = findViewById<EditText>(R.id.productNameEditText)
        val productPriceEditText = findViewById<EditText>(R.id.productPriceEditText)

        if (productNameEditText.text.isEmpty() ||productPriceEditText.text.isEmpty() || productPriceEditText.text.contains(" "))
        {
            Toast.makeText(this,"Fields can't be empty",Toast.LENGTH_SHORT).show()
        }
        else
        {
            val databaseReference = Firebase.database.reference
            val hashMap:HashMap<String,String> = HashMap<String,String>()
            hashMap.put("barcode",barcodenumber)
            hashMap.put("name",productNameEditText.text.toString())
            hashMap.put("price",productPriceEditText.text.toString())
            databaseReference.child("Products").child(barcodenumber).setValue(hashMap)
            finish()
        }

    }
}