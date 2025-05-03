package com.example.trackit

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class BudgetGoalActivity : AppCompatActivity() {

    private lateinit var etMin: EditText
    private lateinit var etMax: EditText
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_goal)

        etMin = findViewById(R.id.etMinBudget)
        etMax = findViewById(R.id.etMaxBudget)
        prefs = getSharedPreferences("budget_goals", Context.MODE_PRIVATE)

        etMin.setText(prefs.getFloat("min", 0f).toString())
        etMax.setText(prefs.getFloat("max", 0f).toString())

        findViewById<Button>(R.id.btnSaveBudget).setOnClickListener {
            val min = etMin.text.toString().toFloatOrNull()
            val max = etMax.text.toString().toFloatOrNull()

            if (min == null || max == null) {
                Toast.makeText(this, "Enter valid amounts", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            prefs.edit().putFloat("min", min).putFloat("max", max).apply()
            Toast.makeText(this, "Budget goals saved!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
