package com.example.orderfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.api.model.DrinkItem
import com.example.api.model.FoodItem
import com.example.api.model.LocationItem
import com.example.orderfood.databinding.ItemDrinkBinding
import com.example.orderfood.databinding.ItemHeaderBinding
import com.example.orderfood.databinding.ItemLocationBinding

class MainAdapter(
    private val drinkList: List<DrinkItem>?,
    private val foodList: List<FoodItem>?,
    private val locationList: List<LocationItem>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_TITLE = 0
        private const val TYPE_DRINK = 1
        private const val TYPE_FOOD = 2
        private const val TYPE_LOCATION = 3
    }

    private val expandedItems = mutableSetOf<Int>() // Lưu trữ các vị trí đã mở rộng

    override fun getItemCount(): Int {
        return 3 + (drinkList?.size ?: 0) + (foodList?.size ?: 0) + (locationList?.size ?: 0)
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 -> TYPE_TITLE // Title Drink
            position == (drinkList?.size ?: 0) + 1 -> TYPE_TITLE // Title Food
            position == (drinkList?.size ?: 0) + (foodList?.size ?: 0) + 2 -> TYPE_TITLE // Title Location
            position < (drinkList?.size ?: 0) + 1 -> TYPE_DRINK
            position < (drinkList?.size ?: 0) + (foodList?.size ?: 0) + 2 -> TYPE_FOOD
            else -> TYPE_LOCATION
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_TITLE -> TitleViewHolder(ItemHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            TYPE_DRINK -> DrinkViewHolder(ItemDrinkBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            TYPE_FOOD -> FoodViewHolder(ItemDrinkBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            TYPE_LOCATION -> LocationViewHolder(ItemLocationBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_TITLE -> bindTitleViewHolder(holder as TitleViewHolder, position)
            TYPE_DRINK -> bindDrinkViewHolder(holder as DrinkViewHolder, position)
            TYPE_FOOD -> bindFoodViewHolder(holder as FoodViewHolder, position)
            TYPE_LOCATION -> bindLocationViewHolder(holder as LocationViewHolder, position)
        }
    }

    private fun bindTitleViewHolder(holder: TitleViewHolder, position: Int) {
        when (position) {
            0 -> holder.bind("Drinks", TYPE_DRINK)
            (drinkList?.size ?: 0) + 1 -> holder.bind("Food", TYPE_FOOD)
            (drinkList?.size ?: 0) + (foodList?.size ?: 0) + 2 -> holder.bind("Location", TYPE_LOCATION)
        }
    }

    private fun bindDrinkViewHolder(holder: DrinkViewHolder, position: Int) {
        drinkList?.get(position - 1)?.let { holder.bind(it) }
    }

    private fun bindFoodViewHolder(holder: FoodViewHolder, position: Int) {
        foodList?.get(position - (drinkList?.size ?: 0) - 2)?.let { holder.bind(it) }
    }

    private fun bindLocationViewHolder(holder: LocationViewHolder, position: Int) {
        locationList?.get(position - (drinkList?.size ?: 0) - (foodList?.size ?: 0) - 3)?.let { holder.bind(it) }
    }

    inner class TitleViewHolder(private val binding: ItemHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String, type: Int) {
            binding.titleTextView.text = title
            binding.expandButton.setOnClickListener {
                if (expandedItems.contains(type)) {
                    expandedItems.remove(type)
                } else {
                    expandedItems.add(type)
                }
                notifyDataSetChanged() // Update the view
            }
            binding.root.setOnClickListener {
                if (expandedItems.contains(type)) {
                    expandedItems.remove(type)
                } else {
                    expandedItems.add(type)
                }
                notifyDataSetChanged()
            }
        }
    }

    inner class DrinkViewHolder(private val binding: ItemDrinkBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(drink: DrinkItem) {
            if (expandedItems.contains(TYPE_DRINK)) {
                binding.root.visibility = RecyclerView.VISIBLE
            } else {
                binding.root.visibility = RecyclerView.GONE
            }
            binding.nameTextView.text = drink.name
            binding.priceTextView.text = drink.price.toString()
        }
    }

    inner class FoodViewHolder(private val binding: ItemDrinkBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(food: FoodItem) {
            if (expandedItems.contains(TYPE_FOOD)) {
                binding.root.visibility = RecyclerView.VISIBLE
            } else {
                binding.root.visibility = RecyclerView.GONE
            }
            binding.nameTextView.text = food.name
            binding.priceTextView.text = food.price.toString()
        }
    }

    inner class LocationViewHolder(private val binding: ItemLocationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(location: LocationItem) {
            if (expandedItems.contains(TYPE_LOCATION)) {
                binding.root.visibility = RecyclerView.VISIBLE
            } else {
                binding.root.visibility = RecyclerView.GONE
            }
            binding.nameTextView.text = location.name
            binding.distance.text = location.distance
        }
    }
}
