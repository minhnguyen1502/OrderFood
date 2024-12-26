package com.example.orderfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.api.model.DrinkItem
import com.example.api.model.FoodItem
import com.example.api.model.LocationItem
import com.example.orderfood.OnItemClickListener
import com.example.orderfood.databinding.ItemDrinkBinding
import com.example.orderfood.databinding.ItemHeaderBinding
import com.example.orderfood.databinding.ItemLocationBinding

class MainAdapter(
    private val drinkList: List<DrinkItem>?,
    private val foodList: List<FoodItem>?,
    private val locationList: List<LocationItem>?,
    private val itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_TITLE = 0
        private const val TYPE_DRINK = 1
        private const val TYPE_FOOD = 2
        private const val TYPE_LOCATION = 3
    }

    private val expandedItems = mutableSetOf(TYPE_DRINK, TYPE_FOOD, TYPE_LOCATION)
    private var displayList: MutableList<Any> = mutableListOf()

    init {
        updateDisplayList()
    }

    private fun updateDisplayList() {
        displayList.clear()
        displayList.add(TYPE_DRINK)
        if (expandedItems.contains(TYPE_DRINK)) {
            drinkList?.let { displayList.addAll(it) }
        }

        displayList.add(TYPE_FOOD)
        if (expandedItems.contains(TYPE_FOOD)) {
            foodList?.let { displayList.addAll(it) }
        }

        displayList.add(TYPE_LOCATION)
        if (expandedItems.contains(TYPE_LOCATION)) {
            locationList?.let { displayList.addAll(it) }
        }
    }

    override fun getItemCount(): Int = displayList.size

    override fun getItemViewType(position: Int): Int {
        return when (val item = displayList[position]) {
            TYPE_DRINK -> TYPE_TITLE
            TYPE_FOOD -> TYPE_TITLE
            TYPE_LOCATION -> TYPE_TITLE
            is DrinkItem -> TYPE_DRINK
            is FoodItem -> TYPE_FOOD
            is LocationItem -> TYPE_LOCATION
            else -> throw IllegalArgumentException("Unknown item type")
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
        when (val item = displayList[position]) {
            is Int -> bindTitleViewHolder(holder as TitleViewHolder, item)
            is DrinkItem -> bindDrinkViewHolder(holder as DrinkViewHolder, item)
            is FoodItem -> bindFoodViewHolder(holder as FoodViewHolder, item)
            is LocationItem -> bindLocationViewHolder(holder as LocationViewHolder, item)
        }
    }

    private fun bindTitleViewHolder(holder: TitleViewHolder, type: Int) {
        val title = when (type) {
            TYPE_DRINK -> "Drinks"
            TYPE_FOOD -> "Food"
            TYPE_LOCATION -> "Location"
            else -> ""
        }
        holder.bind(title, type)
    }

    private fun bindDrinkViewHolder(holder: DrinkViewHolder, drink: DrinkItem) {
        holder.bind(drink)
    }

    private fun bindFoodViewHolder(holder: FoodViewHolder, food: FoodItem) {
        holder.bind(food)
    }

    private fun bindLocationViewHolder(holder: LocationViewHolder, location: LocationItem) {
        holder.bind(location)
    }

    inner class TitleViewHolder(private val binding: ItemHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(title: String, type: Int) {
            binding.titleTextView.text = title
            binding.expandButton.setOnClickListener {
                toggleExpansion(type)
            }
            binding.root.setOnClickListener {
                toggleExpansion(type)
            }
        }

        private fun toggleExpansion(type: Int) {
            if (expandedItems.contains(type)) {
                expandedItems.remove(type)
            } else {
                expandedItems.add(type)
            }
            updateDisplayList()
            notifyDataSetChanged()
        }
    }

    inner class DrinkViewHolder(private val binding: ItemDrinkBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(drink: DrinkItem) {
            Glide.with(binding.imageView.context)
                .load(drink.image)
                .into(binding.imageView)
            binding.nameTextView.text = drink.name
            binding.priceTextView.text = drink.price.toString()
            binding.root.setOnClickListener {
                itemClickListener.onDrinkItemClick(drink)
            }
        }
    }

    inner class FoodViewHolder(private val binding: ItemDrinkBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(food: FoodItem) {
            Glide.with(binding.imageView.context)
                .load(food.image)
                .into(binding.imageView)
            binding.nameTextView.text = food.name
            binding.priceTextView.text = food.price.toString()
            binding.root.setOnClickListener {
                itemClickListener.onFoodItemClick(food)
            }
        }
    }

    inner class LocationViewHolder(private val binding: ItemLocationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(location: LocationItem) {
            binding.nameTextView.text = location.name
            binding.distance.text = location.distance
        }
    }
}
