package com.example.trackit

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class CategorySummaryActivity : AppCompatActivity() {

    private lateinit var etStart: EditText
    private lateinit var etEnd: EditText
    private lateinit var container: LinearLayout
    private lateinit var dbHelper: ExpenseDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_summary)

        etStart = findViewById(R.id.etStartDate)
        etEnd = findViewById(R.id.etEndDate)
        container = findViewById(R.id.categorySummaryContainer)
        dbHelper = ExpenseDatabaseHelper(this)

        etStart.setOnClickListener { showDatePicker(etStart) }
        etEnd.setOnClickListener { showDatePicker(etEnd) }

        findViewById<Button>(R.id.btnViewSummary).setOnClickListener {
            val start = etStart.text.toString()
            val end = etEnd.text.toString()
            if (start.isEmpty() || end.isEmpty()) {
                Toast.makeText(this, "Select both dates", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val totals = dbHelper.getTotalSpentPerCategory(start, end)
            container.removeAllViews()

            if (totals.isEmpty()) {
                val tv = TextView(this)
                tv.text = "No expenses found."
                container.addView(tv)
            } else {
                totals.forEach { (category, total) ->
                    val tv = TextView(this)
                    tv.text = "$category: R$total"
                    container.addView(tv)
                }
            }
        }
    }

    private fun showDatePicker(target: EditText) {
        val c = Calendar.getInstance()
        DatePickerDialog(this, { _, y, m, d ->
            target.setText(String.format("%04d-%02d-%02d", y, m + 1, d))
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
    }
}
