package com.example.projekt1.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt1.R
import com.example.projekt1.databinding.ItemFoodBinding
import com.example.projekt1.model.Food
import java.time.LocalDate

class FoodItem(private val itemViewBinding: ItemFoodBinding) : RecyclerView.ViewHolder(itemViewBinding.root){
    fun onBind(foodItem: Food, onItemClick: () -> Unit) = with(itemViewBinding){
        name.text = foodItem.name
        bbd.text = foodItem.bbd.toString()
        category.text = foodItem.category.toString()
        if(quantity!=null){
        quantity.text = foodItem.amount}
        image.setImageResource(foodItem.icon)
        root.setOnClickListener {
            onItemClick()
        }
    }
}

class FoodListAdapter(private val onItemClick: (Int) -> Unit,   private val onItemLongClick: (Int) -> Unit):RecyclerView.Adapter<FoodItem>() {
    var foodList: List<Food> = emptyList()
        set(value) {
            field = value.sortedBy { it.bbd } // sort with bbd
            notifyDataSetChanged()
        }
    /*set(value){
            field=value
            notifyDataSetChanged()
        }*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItem {
        val  layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFoodBinding.inflate(layoutInflater,parent, false)
        return FoodItem(binding)
    }

    override fun getItemCount(): Int = foodList.size

    override fun onBindViewHolder(holder: FoodItem, position: Int) {
        holder.onBind(foodList[position]){
            val foodItem = foodList[position]
            if (isFoodExpired(foodItem)) {
                showNotification(holder)
            } else {
                onItemClick(position)
            }
        }

        // long hold

        holder.itemView.setOnLongClickListener {
            val foodItem = foodList[position]
            if (isFoodExpired(foodItem)) {
                holder.itemView.setOnLongClickListener {
                    onItemLongClick(position)
                    true
                }
            } else {
                showDeleteConfirmation(position, holder)
            }
            true
        }
        // end of long hold
    }
    private fun isFoodExpired(foodItem: Food): Boolean {
        val currentDate = LocalDate.now()
        return foodItem.bbd.isBefore(currentDate)
    }
    private fun showDeleteConfirmation(position: Int, holder: FoodItem ) {
        val context = holder.itemView.context
        AlertDialog.Builder(context)
            .setTitle("Usun produkt")
            .setMessage("Czy jestes pewien?")
            .setPositiveButton("Usun") { dialog, _ ->
                dialog.dismiss()
                onItemLongClick(position)
            }
            .setNegativeButton("Anuluj") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    private fun showNotification(holder: FoodItem) {
        val context = holder.itemView.context
        AlertDialog.Builder(context)
            .setTitle("Uwaga")
            .setMessage("Produkt przekroczyl date przydatnosci, nie mozna go juz edytowac!")
            .setNeutralButton("Rozumiem") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}