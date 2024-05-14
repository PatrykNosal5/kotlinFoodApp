package com.example.projekt1.data

import com.example.projekt1.model.Food

interface FoodRepository {
    fun getFoodList(): List<Food>
    fun add(dish: Food)
    fun getFoodById(id: Int): Food
    fun set(id: Int, dish: Food)
    fun removeFood(deletedFood: Food)
}