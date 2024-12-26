package com.example.orderfood

import com.example.api.model.DrinkItem
import com.example.api.model.FoodItem

interface OnItemClickListener {
    fun onDrinkItemClick(drink: DrinkItem)
    fun onFoodItemClick(food: FoodItem)
}
