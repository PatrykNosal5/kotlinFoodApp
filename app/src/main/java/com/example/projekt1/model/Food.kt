package com.example.projekt1.model

import java.time.LocalDate

data class Food(
    val id: Int,
    val icon: Int,
    val name: String,
    val bbd: LocalDate,
    val category: Category,
    val amount: String? = null
)
