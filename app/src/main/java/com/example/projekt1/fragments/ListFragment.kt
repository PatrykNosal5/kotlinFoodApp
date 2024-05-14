package com.example.projekt1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projekt1.R
import com.example.projekt1.adapters.FoodListAdapter
import com.example.projekt1.data.FoodRepository
import com.example.projekt1.data.FoodRepositoryInMemory
import com.example.projekt1.data.RepositoryLocator
import com.example.projekt1.databinding.ActivityMainBinding
import com.example.projekt1.databinding.FragmentListBinding
import com.example.projekt1.model.FormType

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    lateinit var foodRepository: FoodRepository
    lateinit var foodListAdapter: FoodListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentListBinding.inflate(layoutInflater, container, false).also {
            binding = it
        }.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ////////////////////binding.count.text = foodListAdapter.foodList.size.toString()
        foodRepository = RepositoryLocator.foodRepository

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        // code for product amount
        /*foodListAdapter = FoodListAdapter {
            findNavController()
                .navigate(
                    R.id.action_listFragment_to_formFragment,
                    bundleOf("type" to FormType.Edit(it))
                )
        }*/
        foodListAdapter = FoodListAdapter(
            onItemClick = { position ->
                // Handle item click
                findNavController().navigate(
                    R.id.action_listFragment_to_formFragment,
                    bundleOf("type" to FormType.Edit(position))
                )
            },
            onItemLongClick = { position ->
                // Handle item long click
                val deletedFood = foodListAdapter.foodList[position]
                foodRepository.removeFood(deletedFood) // Implement removeFood in your repository
                foodListAdapter.foodList = foodRepository.getFoodList() // Update adapter data after deletion
            }
        )
        foodListAdapter?.foodList = foodRepository.getFoodList()
        binding.count.text = "${foodListAdapter?.itemCount ?: 0} produktów"

        foodListAdapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                binding.count.text = "${foodListAdapter.itemCount} produktów"
            }
        })

        binding.add.setOnClickListener {
            findNavController()
                .navigate(
                    R.id.action_listFragment_to_formFragment
                )
        }
        // end of code for product amount


        /*foodListAdapter = FoodListAdapter {
            findNavController()
                .navigate(
                    R.id.action_listFragment_to_formFragment,
                    bundleOf("type" to FormType.Edit(it))
                )
        }*/////////////////////////////////////////////////////////////////////////


        binding.foodList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = foodListAdapter
        }

        foodListAdapter.foodList = foodRepository.getFoodList()



        binding.add.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_formFragment)
            /*with(binding.count){
            text = foodListAdapter.foodList.size.toString()
            }*/
        }
    }

    fun onDstChanged( controller: NavController, destination: NavDestination, arguments: Bundle?){
        if (destination.id == R.id.listFragment) {
            foodListAdapter.foodList = foodRepository.getFoodList()
        }
    }

    override fun onStart() {
        super.onStart()
        findNavController().addOnDestinationChangedListener(::onDstChanged)

    }

    override fun onStop() {
        findNavController().removeOnDestinationChangedListener(::onDstChanged)
        super.onStop()
    }
}
