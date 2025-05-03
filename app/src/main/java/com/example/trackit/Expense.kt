package com.example.trackit

data class Expense(
    val amount: Double,
    val date: String,
    val description: String,
    val category: String,
    val imageUri: String?
)
