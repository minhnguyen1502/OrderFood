package com.example.orderfood.ui

import android.content.Intent
import android.content.res.AssetManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.api.model.DrinkItem
import com.example.api.model.DrinkResponse
import com.example.api.model.FoodItem
import com.example.api.model.FoodResponse
import com.example.api.model.LocationResponse
import com.example.orderfood.OnItemClickListener
import com.example.orderfood.adapter.MainAdapter
import com.example.orderfood.base.BaseActivity
import com.example.orderfood.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate),
    OnItemClickListener {
    private lateinit var adapter: MainAdapter

    override fun initView() {
        binding.recycleview.layoutManager = LinearLayoutManager(this)
        loadDataFromAssets()
    }

    override fun bindView() {
        binding.cart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        binding.bill.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }


    }

    private fun loadDataFromAssets() {
        try {
            val gson = Gson()

            val drinks = loadJsonData<DrinkResponse>("drink_list.json", gson)
            val foods = loadJsonData<FoodResponse>("food_list.json", gson)
            val locations = loadJsonData<LocationResponse>("location_receive.json", gson)

            if (drinks != null && foods != null && locations != null) {
                adapter = MainAdapter(drinks.data?.list_drink, foods.data?.list_food, locations.data?.list_location, this@MainActivity)
                binding.recycleview.adapter = adapter
            } else {
                Toast.makeText(this, "Error loading data: Some data is null", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error loading data: ${e.message}", Toast.LENGTH_SHORT).show()
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

    override fun onDrinkItemClick(drink: DrinkItem) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("DRINK_DETAIL", drink.id)
        startActivity(intent)
    }

    override fun onFoodItemClick(food: FoodItem) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("FOOD_DETAIL", food.id)
        startActivity(intent)
    }


}