package com.quik.baskt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class CashPaymentUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_payment_user)
        val token = intent.getStringExtra("token")
        val token_textview = findViewById<TextView>(R.id.token)
        token_textview.text = token
    }

    override fun onBackPressed() {
        finishAffinity()
        startActivity(Intent(this@CashPaymentUser,MainScreen::class.java))
        super.onBackPressed()
    }
}