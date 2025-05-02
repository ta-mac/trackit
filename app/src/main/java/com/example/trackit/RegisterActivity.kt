package com.example.trackit

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etNewUsername: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var btnCreateAccount: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dbHelper = DatabaseHelper(this)
        etNewUsername = findViewById(R.id.etNewUsername)
        etNewPassword = findViewById(R.id.etNewPassword)
        btnCreateAccount = findViewById(R.id.btnCreateAccount)

        btnCreateAccount.setOnClickListener {
            val username = etNewUsername.text.toString()
            val password = etNewPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                val success = dbHelper.registerUser(username, password)
                if (success) {
                    Toast.makeText(this, "Account created!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
