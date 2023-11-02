package com.example.tawsila

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_login)

        val goButton = findViewById<Button>(R.id.goButton)
        goButton.setOnClickListener {
            // Start the target activity when the "Go" button is clicked
            val intent = Intent(this, Interface::class.java)
            startActivity(intent)
        }

        val goSignupButton = findViewById<Button>(R.id.goSignup)
        goSignupButton.setOnClickListener {
            // Start the SignUp activity when the "goSignup" button is clicked
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
    }
}