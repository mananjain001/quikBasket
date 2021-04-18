package com.quik.baskt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class OnlinePaymentUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_payment_user)
        val token = intent.getStringExtra("token")
        val token_textview = findViewById<TextView>(R.id.paymentid)
        token_textview.text = token
    }

    override fun onBackPressed() {
        finishAffinity()
        startActivity(Intent(this@OnlinePaymentUser,MainScreen::class.java))
        super.onBackPressed()
    }
}