package com.example.trackit

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {

    private lateinit var btnToCategories: Button
    private lateinit var btnLogout: Button
    private lateinit var btnToExpenses: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        btnToCategories = findViewById(R.id.btnToCategories)

        btnLogout = findViewById(R.id.btnLogout)

        btnToCategories.setOnClickListener {
            startActivity(Intent(this, AddCategoryActivity::class.java))
        }

        btnToExpenses = findViewById(R.id.btnToExpenses)
        btnToExpenses.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

        btnLogout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
