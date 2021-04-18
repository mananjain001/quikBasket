package com.quik.baskt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class AdminPanel : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_panel)
    }

    fun addItemScreen(view: View) {
        val intent = Intent(this, AddItem::class.java)
        startActivity(intent)
    }
    fun updateItemScreen(view: View) {
        val intent = Intent(this, EditItem::class.java)
        startActivity(intent)
    }

    fun checkBill(view: View) {
        val intent = Intent(this, checkBill::class.java)
        startActivity(intent)
    }
    fun CashPayment(view: View) {
        val intent = Intent(this, cashPaymentAuthorise::class.java)
        startActivity(intent)
    }
}