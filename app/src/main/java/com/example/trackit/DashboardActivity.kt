package com.example.trackit

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class DashboardActivity : AppCompatActivity() {

    private lateinit var dbHelper: ExpenseDatabaseHelper
    private lateinit var tvMonthlyTotal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        dbHelper = ExpenseDatabaseHelper(this)

        val btnAddCategory = findViewById<Button>(R.id.btnAddCategory)
        val btnAddExpense = findViewById<Button>(R.id.btnAddExpense)
        val btnViewByDate = findViewById<Button>(R.id.btnViewByDate)
        val btnViewCategoryTotals = findViewById<Button>(R.id.btnViewCategoryTotals)
        val btnSetBudget = findViewById<Button>(R.id.btnSetBudget)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val switchTheme = findViewById<Switch>(R.id.switchTheme)
        tvMonthlyTotal = findViewById(R.id.tvMonthlyTotal)



        // Show monthly spending
        showMonthlyTotal()

        // Buttons
        btnAddCategory.setOnClickListener {
            startActivity(Intent(this, AddCategoryActivity::class.java))
        }

        btnAddExpense.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }

        btnViewByDate.setOnClickListener {
            startActivity(Intent(this, ViewByDateActivity::class.java))
        }

        btnViewCategoryTotals.setOnClickListener {
            startActivity(Intent(this, CategorySummaryActivity::class.java))
        }

        btnSetBudget.setOnClickListener {
            startActivity(Intent(this, BudgetGoalActivity::class.java))
        }

        // Logout with confirmation
        btnLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Log out")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes") { _, _ ->
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        // Theme Toggle
        val prefs = getSharedPreferences("themePrefs", Context.MODE_PRIVATE)
        val isDark = prefs.getBoolean("dark_mode", false)
        switchTheme.isChecked = isDark

        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("dark_mode", isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    private fun showMonthlyTotal() {
        val total = dbHelper.getTotalForCurrentMonth()
        tvMonthlyTotal.text = "Total this month: R%.2f".format(total)
    }

    override fun onResume() {
        super.onResume()
        showMonthlyTotal()
    }
}
