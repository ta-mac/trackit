package com.example.trackit

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class ViewByDateActivity : AppCompatActivity() {

    private lateinit var etStartDate: EditText
    private lateinit var etEndDate: EditText
    private lateinit var btnFilter: Button
    private lateinit var container: LinearLayout
    private lateinit var dbHelper: ExpenseDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_by_date)

        dbHelper = ExpenseDatabaseHelper(this)

        etStartDate = findViewById(R.id.etStartDate)
        etEndDate = findViewById(R.id.etEndDate)
        btnFilter = findViewById(R.id.btnFilter)
        container = findViewById(R.id.filteredListContainer)

        etStartDate.setOnClickListener { showDatePicker(etStartDate) }
        etEndDate.setOnClickListener { showDatePicker(etEndDate) }

        btnFilter.setOnClickListener {
            val start = etStartDate.text.toString()
            val end = etEndDate.text.toString()

            if (start.isEmpty() || end.isEmpty()) {
                Toast.makeText(this, "Please select both dates", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val expenses = dbHelper.getExpensesByDateRange(start, end)
            container.removeAllViews()

            if (expenses.isEmpty()) {
                val tv = TextView(this)
                tv.text = "No expenses found in that period."
                container.addView(tv)
            } else {
                for (expense in expenses) {
                    val tv = TextView(this)
                    tv.text = """
                        Date: ${expense.date}
                        Amount: R${expense.amount}
                        Category: ${expense.category}
                        Description: ${expense.description}
                    """.trimIndent()
                    tv.setPadding(0, 0, 0, 24)
                    container.addView(tv)
                }
            }
        }
    }

    private fun showDatePicker(target: EditText) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(this, { _, year, month, day ->
            val date = String.format("%04d-%02d-%02d", year, month + 1, day)
            target.setText(date)
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }
}
