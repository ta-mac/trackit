package com.example.trackit

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var etAmount: EditText
    private lateinit var etDescription: EditText
    private lateinit var etDate: EditText
    private lateinit var etStartTime: EditText
    private lateinit var etEndTime: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var btnPickImage: Button
    private lateinit var btnSaveExpense: Button
    private lateinit var imgPreview: ImageView

    private lateinit var dbHelper: ExpenseDatabaseHelper
    private lateinit var categoryHelper: ExpenseDatabaseHelper
    private var imageUri: Uri? = null

    companion object {
        const val IMAGE_PICK_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        // Bind views
        etAmount = findViewById(R.id.etAmount)
        etDescription = findViewById(R.id.etDescription)
        etDate = findViewById(R.id.etDate)
        etStartTime = findViewById(R.id.etStartTime)
        etEndTime = findViewById(R.id.etEndTime)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        btnPickImage = findViewById(R.id.btnPickImage)
        btnSaveExpense = findViewById(R.id.btnSaveExpense)
        imgPreview = findViewById(R.id.imgPreview)

        dbHelper = ExpenseDatabaseHelper(this)
        categoryHelper = ExpenseDatabaseHelper(this)

        setupCategorySpinner()

        // Date picker
        etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, day ->
                etDate.setText(String.format("%04d-%02d-%02d", year, month + 1, day))
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        // Time pickers
        etStartTime.setOnClickListener {
            showTimePicker(etStartTime)
        }

        etEndTime.setOnClickListener {
            showTimePicker(etEndTime)
        }

        btnPickImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        btnSaveExpense.setOnClickListener {
            val amountText = etAmount.text.toString()
            val description = etDescription.text.toString()
            val date = etDate.text.toString()
            val startTime = etStartTime.text.toString()
            val endTime = etEndTime.text.toString()
            val category = spinnerCategory.selectedItem?.toString() ?: ""

            if (amountText.isEmpty() || date.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || category.isEmpty()) {
                Toast.makeText(this, "Please complete all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amount = amountText.toDouble()
            val fullDescription = "$description\nFrom: $startTime\nTo: $endTime"

            val success = dbHelper.insertExpense(
                amount,
                date,
                fullDescription,
                category,
                imageUri?.toString()
            )

            if (success) {
                Toast.makeText(this, "Expense saved!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Failed to save expense", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun showTimePicker(targetField: EditText) {
        val calendar = Calendar.getInstance()
        TimePickerDialog(this, { _, hour, minute ->
            targetField.setText(String.format("%02d:%02d", hour, minute))
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
    }

    private fun setupCategorySpinner() {
        val categories = categoryHelper.getAllCategories()
        if (categories.isEmpty()) {
            Toast.makeText(this, "Please add categories first", Toast.LENGTH_LONG).show()
            finish()
            return
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        spinnerCategory.adapter = adapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            imgPreview.setImageURI(imageUri)
        }
    }
}
