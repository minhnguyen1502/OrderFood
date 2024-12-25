package com.example.api.model

// food
data class FoodResponse(
    val code: Int,
    val msg: String,
    val data: FoodData
)

data class FoodData(
    val title: String,
    val list_food: List<FoodItem>
)

data class FoodItem(
    val id: Int,
    val name: String,
    val image: String,
    val price: Int
)
// drink
data class DrinkResponse(
    val code: Int,
    val msg: String,
    val data: DrinkData
)

data class DrinkData(
    val title: String,
    val list_drink: List<DrinkItem>
)

data class DrinkItem(
    val id: Int,
    val name: String,
    val image: String,
    val price: Int,
    val discount: Int,
    val percent_discount: Int
)
// location
data class LocationResponse(
    val code: Int,
    val msg: String,
    val data: LocationData
)

data class LocationData(
    val title: String,
    val list_location: List<LocationItem>
)

data class LocationItem(
    val id: Int,
    val name: String,
    val distance: String
)
// order
data class Order(
    val id: Int,
    val name: String,
    val price: Int
)

data class OrderResponse(
    val code: Int,
    val msg: String,
    val data: String
)
// cancel order
data class DeleteResponse(
    val code: Int,
    val msg: String,
    val data: String
)
// drink detail

data class DrinkItemResponse(
    val code: Int,
    val msg: String,
    val dat: DrinkDetail?
)

data class DrinkDetail(
    val id:Int,
    val name: String,
    val price: Int,
    val size: String,
    val discount: Int,
    val percent_discount: Int,
    val image: String,
    val ship_price: Int,
    val payment_channel: String
)
// food detail
data class FoodItemResponse(
    val code: Int,
    val msg: String,
    val data: FoodDetail?
)

data class FoodDetail(
    val id: Int,
    val name: String,
    val price: Int,
    val ship_price: Int,
    val image: String,
    val payment_channel: String,
    val percent_discount: Int,
    val discount: Int
)

// history order

