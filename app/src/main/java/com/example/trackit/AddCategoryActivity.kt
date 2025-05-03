package com.example.trackit

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddCategoryActivity : AppCompatActivity() {

    private lateinit var etCategoryName: EditText
    private lateinit var btnAddCategory: Button
    private lateinit var listView: ListView
    private lateinit var dbHelper: CategoryDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        etCategoryName = findViewById(R.id.etCategoryName)
        btnAddCategory = findViewById(R.id.btnAddCategory)
        listView = findViewById(R.id.categoryListView)
        dbHelper = CategoryDatabaseHelper(this)

        btnAddCategory.setOnClickListener {
            val category = etCategoryName.text.toString()
            if (category.isNotEmpty()) {
                val success = dbHelper.insertCategory(category)
                if (success) {
                    Toast.makeText(this, "Category added", Toast.LENGTH_SHORT).show()
                    etCategoryName.text.clear()
                    loadCategories()
                } else {
                    Toast.makeText(this, "Category already exists", Toast.LENGTH_SHORT).show()
                }
            }
        }

        loadCategories()
    }

    private fun loadCategories() {
        val categories = dbHelper.getAllCategories()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categories)
        listView.adapter = adapter
    }
}
