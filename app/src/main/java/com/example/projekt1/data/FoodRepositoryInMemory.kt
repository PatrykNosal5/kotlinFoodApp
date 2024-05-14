package com.example.projekt1.data

import com.example.projekt1.R
import com.example.projekt1.model.Category
import com.example.projekt1.model.Food
import java.time.LocalDate

object FoodRepositoryInMemory: FoodRepository {
    private var idCounter = 3
    private val foodList = mutableListOf<Food>(
        Food(0,R.drawable.images,"Pizza", LocalDate.of(2024,4,30), Category.Produkty_Spozywcze,"2 szt." ),
        Food(1,R.drawable.pierogi,"Pierogi", LocalDate.of(2024,5,12), Category.Produkty_Spozywcze,"20 szt." ),
        Food(2,R.drawable.aspiryna,"Aspiryna", LocalDate.of(2025,5,30), Category.Leki)
    )
    override fun getFoodList(): List<Food> {
        return foodList
    }

    override fun add(dish: Food) {
        val foodWithId = dish.copy(id = idCounter++)
        foodList.add(foodWithId)
    }

    //override fun getFoodById(id: Int): Food = foodList[id]
    // tutaj jest problem, najlepiej sobie zamienic na sztuczne id niz bazowac na kolejnosci, i niech kazdy ma swoje id i git
    // bo jesli w recycler veiw posortujemy to sie zmienia id, a tutaj nie...
    // wiec lepiej szukac po id polu

    override fun getFoodById(id: Int): Food {
        return foodList.firstOrNull { it.id == id } ?: throw IllegalArgumentException("Food with id $id not found")
    }

    //override fun set(id: Int, dish: Food) {
    //    foodList[id] = dish
    //}
    override fun set(id: Int, dish: Food) {
        val index = foodList.indexOfFirst { it.id == id }
        if (index != -1) {
            foodList[index] = dish.copy(id = id)
        } else {
            throw IllegalArgumentException("Food with id $id not found")
        }
    }

    override fun removeFood(deletedFood: Food) {
        foodList.remove(deletedFood)
    }

}