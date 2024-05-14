package com.example.projekt1.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.projekt1.R
import com.example.projekt1.data.FoodRepository
import com.example.projekt1.data.FoodRepositoryInMemory
import com.example.projekt1.data.RepositoryLocator
import com.example.projekt1.databinding.FragmentFormBinding
import com.example.projekt1.model.Category
import com.example.projekt1.model.Food
import com.example.projekt1.model.FormType
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Calendar

private const val TYPE_KEY = "type"
class FormFragment : Fragment() {
    private lateinit var type: FormType
    private lateinit var binding: FragmentFormBinding
    private lateinit var repository:FoodRepository
    private var edited: Food? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        repository=RepositoryLocator.foodRepository
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                 it.getSerializable(TYPE_KEY,FormType::class.java)
            } else{
                it.getSerializable(TYPE_KEY) as? FormType
            } ?: FormType.New
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentFormBinding.inflate(layoutInflater, container, false).also{
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.button.text = when (type) {
            is FormType.Edit -> "Zapisz"
            FormType.New -> "Dodaj"
        }
        binding.button.setOnClickListener {
            if (isValidForm()) {
                saveDish((type as? FormType.Edit)?.id)
                findNavController().popBackStack()
            }
        }
        (type as? FormType.Edit)?.let {
            edited = repository.getFoodById(it.id).also{
            with(binding.fieldName){
                setText(it.name)
            }
            with(binding.fieldDate){
                setText(it.bbd.toString())
            }
            with(binding.fieldQuantity){
                setText(it.amount)
            }
            when (it.category) {
                Category.Produkty_Spozywcze -> binding.radioButtonProd.isChecked = true
                Category.Leki -> binding.radioButtonLeki.isChecked = true
                Category.Kosmetyki -> binding.radioButtonKosm.isChecked = true
            }
            }
        }

    }


    private fun saveDish(id: Int?){

        val selectedCategory: Category = when {
            binding.radioButtonProd.isChecked -> Category.Produkty_Spozywcze
            binding.radioButtonLeki.isChecked -> Category.Leki
            binding.radioButtonKosm.isChecked -> Category.Kosmetyki
            else -> Category.Produkty_Spozywcze
        }
        val dateText = binding.fieldDate.text.toString()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date: LocalDate = LocalDate.parse(dateText, dateFormatter)
        val name =  binding.fieldName.text.toString()
        val image = R.drawable.ic_image
        val quantity = binding.fieldQuantity.text.toString()

        val dish = edited?.copy(
            name = name,
            bbd=date,
            category = selectedCategory,
            amount = quantity
        )?: Food(
            id = id ?: 0,
            icon = image,
            name = name,
            bbd = date,
            category = selectedCategory,
            amount = quantity
        )

        if(id==null){
            repository.add(dish)
        } else{
            repository.set(id, dish)
        }

    }
    private fun isValidForm(): Boolean {
        if (binding.fieldName.text.isBlank()) {
            Toast.makeText(requireContext(), "Nazwa nie moze byc pusta!", Toast.LENGTH_SHORT).show()
            return false
        }

        try {
            val selectedDate = LocalDate.parse(binding.fieldDate.text.toString())
            if (selectedDate.isBefore(LocalDate.now())) {
                Toast.makeText(requireContext(), "Data nie moze byc wczesniejsza niz dzisiaj!", Toast.LENGTH_SHORT).show()
                return false
            }
        } catch (e: DateTimeParseException) {
            Toast.makeText(requireContext(), "Nieprawidłowy format daty, wymagany: yyyy-mm-dd", Toast.LENGTH_SHORT).show()
            return false
        }

        if (binding.fieldQuantity.text.isNotBlank()) {
            val quantity = binding.fieldQuantity.text.toString()
            val regex = Regex("^\\d+\\s.+")
            if (!regex.matches(quantity)) {
                Toast.makeText(requireContext(), "Ilosc musi byc liczba calkowita z jednostka (np.'5 szt.','10 g')", Toast.LENGTH_SHORT).show()
                return false
            }
        }

        return true
    }
}