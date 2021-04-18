package com.quik.baskt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
    }

    fun startUser(view: View) {
        val intent = Intent(this, UserHome::class.java)
        startActivity(intent)
    }
    fun startAdmin(view: View) {
        val intent = Intent(this, AdminPanel::class.java)
        startActivity(intent)
    }
}