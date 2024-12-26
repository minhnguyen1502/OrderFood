package com.example.orderfood.ui

import android.content.res.AssetManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.api.model.DrinkItem
import com.example.api.model.DrinkResponse
import com.example.api.model.FoodItem
import com.example.api.model.FoodResponse
import com.example.api.model.Order
import com.example.api.model.OrderResponse
import com.example.orderfood.R
import com.example.orderfood.base.BaseActivity
import com.example.orderfood.databinding.ActivityDetailBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader

class DetailActivity : BaseActivity<ActivityDetailBinding>(ActivityDetailBinding::inflate) {
    private lateinit var drinkList: List<DrinkItem>
    private lateinit var foodList: List<FoodItem>
    private var count: Int = 1

    override fun initView() {
        val gson = Gson()

        val drinks = loadJsonData<DrinkResponse>("drink_list.json", gson)
        drinkList = drinks?.data?.list_drink ?: emptyList()

        val foods = loadJsonData<FoodResponse>("food_list.json", gson)
        foodList = foods?.data?.list_food ?: emptyList()

        val drinkId = intent.getIntExtra("DRINK_DETAIL", -1)
        val foodId = intent.getIntExtra("FOOD_DETAIL", -1)

        if (drinkId == -1) {
            binding.food.visibility = View.VISIBLE
            binding.drink.visibility = View.GONE
        }

        if (foodId == -1) {
            binding.food.visibility = View.GONE
            binding.drink.visibility = View.VISIBLE
        }

        if (drinkId != -1) {
            val drink = findDrinkById(drinkId)

            if (drink != null) {
                binding.drinkName.text = "name: " + drink.name
                binding.drinkSize.text = "Size: " + drink.size
                binding.drinkPrice.text = "Price: " + drink.price.toString()
                binding.drinkShippingPrice.text = "Ship price: " + drink.ship_price.toString()
                binding.drinkPaymentChannel.text = "Payment: " + drink.payment_channel
                binding.drinkDiscount.text = "Discount: " + (drink.discount * 10).toString() + "%"
                updateTotalPrice()
            } else {
                Toast.makeText(this, "Drink not found", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Invalid drink ID", Toast.LENGTH_SHORT).show()
        }


    }

    private fun updateTotalPrice() {
        val drinkId = intent.getIntExtra("DRINK_DETAIL", -1)
        val drink = findDrinkById(drinkId)

        if (drink != null) {
            val drinkTotalPrice =
                (drink.price * count) * (1 - drink.discount * 0.1) + drink.ship_price
            binding.drinkTotalPrice.text = "Total: $drinkTotalPrice"
        }
    }


    private fun findDrinkById(id: Int): DrinkItem? {
        return drinkList.find { it.id == id }
    }

    private fun findFoodById(id: Int): FoodItem? {
        return foodList.find { it.id == id }
    }

    override fun bindView() {
        binding.up.setOnClickListener {
            count++
            binding.tvNumber.text = count.toString()
            updateTotalPrice()
        }
        binding.down.setOnClickListener {
            if (count > 1) {
                count--
                binding.tvNumber.text = count.toString()
                updateTotalPrice()
            }
        }
        binding.back.setOnClickListener { finish() }
        binding.btnAdd.setOnClickListener {
            saveInvoiceToJsonFile()
            finish()
        }

    }

    private fun saveInvoiceToJsonFile() {
        val drinkId = intent.getIntExtra("DRINK_DETAIL", -1)
        val drink = findDrinkById(drinkId)

        if (drink != null) {
            val existingOrders = readOrdersFromFile()
            val nextOrderId = getNextOrderId(existingOrders)

            val order = Order(
                id = nextOrderId,
                name = drink.name,
                price = ((drink.price * count) * (1 - drink.discount * 0.1) + drink.ship_price).toInt(),
                count = count,
                status = 0
            )

            existingOrders.add(order)

            val gson = Gson()
            val jsonOrderList = gson.toJson(existingOrders)

            saveInvoiceToInternalStorage(jsonOrderList)
        } else {
            Toast.makeText(this, "Drink not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readOrdersFromFile(): MutableList<Order> {
        val orders = mutableListOf<Order>()
        try {
            val fileName = "order_food.json"
            val fileInputStream = openFileInput(fileName)

            if (fileInputStream.available() > 0) {
                val fileContent = fileInputStream.bufferedReader().use { it.readText() }
                val gson = Gson()
                val orderListType = object : TypeToken<MutableList<Order>>() {}.type
                val existingOrders: List<Order> = gson.fromJson(fileContent, orderListType)
                orders.addAll(existingOrders)
            }
            fileInputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return orders
    }

    private fun getNextOrderId(existingOrders: List<Order>): Int {
        if (existingOrders.isEmpty()) {
            return 1
        }
        val maxId = existingOrders.maxOfOrNull { it.id } ?: 0
        return maxId + 1
    }

    private fun saveInvoiceToInternalStorage(jsonOrderList: String) {
        try {
            val fileName = "order_food.json"

            val fileOutputStream = openFileOutput(fileName, MODE_PRIVATE)
            fileOutputStream.write(jsonOrderList.toByteArray())
            fileOutputStream.close()

            Toast.makeText(this, "Order saved successfully!", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()

            Toast.makeText(this, "Failed to save order: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


    private inline fun <reified T> loadJsonData(fileName: String, gson: Gson): T? {
    try {
        val assetManager: AssetManager = assets
        val reader = InputStreamReader(assetManager.open(fileName))
        val bufferedReader = BufferedReader(reader)
        val stringBuilder = StringBuilder()
        var line: String?

        while (bufferedReader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }

        return gson.fromJson(stringBuilder.toString(), T::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(this, "Error reading the file: ${e.message}", Toast.LENGTH_SHORT).show()
        return null
    }
}

}