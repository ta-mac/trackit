package com.example.trackit

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class DashboardActivity : AppCompatActivity() {

    private lateinit var tvMonthlyTotal: TextView
    private lateinit var tvWelcome: TextView
    private lateinit var switchTheme: Switch
    private lateinit var dbHelper: ExpenseDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        // Get passed username from intent
        val username = intent.getStringExtra("username") ?: "User"

        // Initialize views
        tvWelcome = findViewById(R.id.tvWelcome)
        tvMonthlyTotal = findViewById(R.id.tvMonthlyTotal)
        switchTheme = findViewById(R.id.switchTheme)

        // Show welcome message
        tvWelcome.text = "Welcome, $username!"

        dbHelper = ExpenseDatabaseHelper(this)
        val totalThisMonth = dbHelper.getTotalExpensesThisMonth()
        tvMonthlyTotal.text = "Total this month: R%.2f".format(totalThisMonth)

        findViewById<Button>(R.id.btnAddCategory).setOnClickListener {
            startActivity(Intent(this, AddCategoryActivity::class.java))
        }

        findViewById<Button>(R.id.btnAddExpense).setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

        findViewById<Button>(R.id.btnViewByDate).setOnClickListener {
            startActivity(Intent(this, ViewByDateActivity::class.java))
        }

        findViewById<Button>(R.id.btnViewCategoryTotals).setOnClickListener {
            startActivity(Intent(this, CategorySummaryActivity::class.java))
        }

        findViewById<Button>(R.id.btnSetBudget).setOnClickListener {
            startActivity(Intent(this, BudgetGoalActivity::class.java))
        }

        // Dark mode toggle
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        // Logout confirmation
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes") { _, _ ->
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }
}
