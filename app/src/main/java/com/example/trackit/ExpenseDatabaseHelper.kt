package com.example.trackit

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*


class ExpenseDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "trackit.db", null, 2) {

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

    fun insertExpense(
        amount: Double,
        date: String,
        description: String,
        category: String,
        imageUri: String?
    ): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("amount", amount)
            put("date", date)
            put("description", description)
            put("category", category)
            put("image_uri", imageUri)
        }
        val result = db.insert("expenses", null, values)
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
        val list = mutableListOf<Expense>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT amount, date, description, category, image_uri FROM expenses WHERE date BETWEEN ? AND ?",
            arrayOf(start, end)
        )
        while (cursor.moveToNext()) {
            list.add(
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
        return list
    }

    fun getTotalSpentPerCategory(startDate: String, endDate: String): Map<String, Double> {
        val totals = mutableMapOf<String, Double>()
        val db = readableDatabase
        val query = """
            SELECT category, SUM(amount) 
            FROM expenses 
            WHERE date BETWEEN ? AND ? 
            GROUP BY category
        """
        val cursor = db.rawQuery(query, arrayOf(startDate, endDate))
        while (cursor.moveToNext()) {
            val category = cursor.getString(0)
            val total = cursor.getDouble(1)
            totals[category] = total
        }
        cursor.close()
        db.close()
        return totals
    }

    fun getTotalForCurrentMonth(): Double {
        val db = readableDatabase
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val startDate = String.format("%04d-%02d-01", year, month)
        val endDate = String.format("%04d-%02d-31", year, month)

        val cursor = db.rawQuery(
            "SELECT SUM(amount) FROM expenses WHERE date BETWEEN ? AND ?",
            arrayOf(startDate, endDate)
        )

        val total = if (cursor.moveToFirst()) cursor.getDouble(0) else 0.0
        cursor.close()
        db.close()
        return total
    }
}
