package com.quik.baskt

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class EditItem : AppCompatActivity() {
    var barcodenumber = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)
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
                barcodenumber = result.contents
                val data = checkProduct()
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

    private fun UpdateUI(data:DataSnapshot) {
            val scanLayout = findViewById<LinearLayout>(R.id.scaneditLayout)
            val scanformlayout = findViewById<LinearLayout>(R.id.scaneditformlayout)
            scanLayout.visibility = View.INVISIBLE
            scanformlayout.visibility = View.VISIBLE
            val barcode_number = findViewById<TextView>(R.id.barcode_number)
            val productNameEditText = findViewById<EditText>(R.id.productNameEditText)
            val productPriceEditText = findViewById<EditText>(R.id.productPriceEditText)
            barcode_number.text = data.child("barcode").value.toString()
            productNameEditText.setText(data.child("name").value.toString())
            productPriceEditText.setText(data.child("price").value.toString())

        }
    private fun checkProduct():DataSnapshot? {
        var exist : DataSnapshot? = null
        val checkUser: Query =
                FirebaseDatabase.getInstance().getReference().child("Products")
        checkUser.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.child(barcodenumber).exists())
                {
                    UpdateUI(snapshot.child(barcodenumber))
                }
                else
                {
                    productNotFound()
                }
            }
        })
        return exist
    }

    private fun productNotFound() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Product don't exist in database")
        builder.setMessage("Product you scanned is not available in database")
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

    fun Update(view: View) {
        val productNameEditText = findViewById<EditText>(R.id.productNameEditText)
        val productPriceEditText = findViewById<EditText>(R.id.productPriceEditText)

        if (productNameEditText.text.isEmpty() || productNameEditText.text.contains(" ")||productPriceEditText.text.isEmpty() || productPriceEditText.text.contains(" "))
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
    fun Delete(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure?")
        builder.setMessage("Are you sure you want to delete the product from database")
        builder.setPositiveButton("Yes"){
            dialog: DialogInterface?, which: Int -> deleteData()
        }
        builder.setNegativeButton("Cancel"){
            dialog: DialogInterface?, which: Int -> finish()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()


    }

    private fun deleteData() {
        val databaseReference = Firebase.database.reference
        databaseReference.child("Products").child(barcodenumber).setValue(null)
        finish()
    }

}
