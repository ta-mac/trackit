package com.example.trackit

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ExpenseDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "trackit.db", null, 2) {

    companion object {
        private const val TABLE_EXPENSES = "expenses"
        private const val COLUMN_ID = "id"
        private const val COLUMN_AMOUNT = "amount"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_CATEGORY = "category"
        private const val COLUMN_IMAGE_URI = "image_uri"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createExpensesTable = """
        CREATE TABLE IF NOT EXISTS expenses (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            amount REAL NOT NULL,
            date TEXT NOT NULL,
            description TEXT,
            category TEXT NOT NULL,
            image_uri TEXT
        )
    """.trimIndent()

        val createCategoriesTable = """
        CREATE TABLE IF NOT EXISTS categories (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL UNIQUE
        )
    """.trimIndent()

        db.execSQL(createExpensesTable)
        db.execSQL(createCategoriesTable)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS expenses")
        db.execSQL("DROP TABLE IF EXISTS categories")
        onCreate(db)
    }

    fun insertExpense(amount: Double, date: String, description: String, category: String, imageUri: String?): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_AMOUNT, amount)
            put(COLUMN_DATE, date)
            put(COLUMN_DESCRIPTION, description)
            put(COLUMN_CATEGORY, category)
            put(COLUMN_IMAGE_URI, imageUri)
        }

        val result = db.insert(TABLE_EXPENSES, null, values)
        db.close()
        return result != -1L
    }

    fun insertCategory(name: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", name)
        }
        val result = db.insert("categories", null, values)
        db.close()
        return result != -1L
    }


    fun getAllCategories(): List<String> {
        val categories = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT name FROM categories", null)
        while (cursor.moveToNext()) {
            categories.add(cursor.getString(0))
        }
        cursor.close()
        db.close()
        return categories
    }

    fun getExpensesByDateRange(start: String, end: String): List<Expense> {
        val expenses = mutableListOf<Expense>()
        val db = readableDatabase
        val query = "SELECT amount, date, description, category, image_uri FROM expenses WHERE date BETWEEN ? AND ?"
        val cursor = db.rawQuery(query, arrayOf(start, end))

        while (cursor.moveToNext()) {
            expenses.add(
                Expense(
                    amount = cursor.getDouble(0),
                    date = cursor.getString(1),
                    description = cursor.getString(2),
                    category = cursor.getString(3),
                    imageUri = cursor.getString(4)
                )
            )
        }

        cursor.close()
        db.close()
        return expenses
    }

}
