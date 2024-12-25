package com.example.orderfood

import android.content.res.AssetManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.api.model.DrinkData
import com.example.api.model.DrinkResponse
import com.example.api.model.FoodData
import com.example.api.model.FoodResponse
import com.example.api.model.LocationData
import com.example.api.model.LocationResponse
import com.example.orderfood.adapter.MainAdapter
import com.example.orderfood.base.BaseActivity
import com.example.orderfood.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private lateinit var adapter: MainAdapter
//    private var drinkList: List<DrinkItem> = emptyList()

    override fun initView() {
        binding.recycleview.layoutManager = LinearLayoutManager(this)
        loadDataFromAssets()

//        if (drinkList.isNotEmpty()) {
//            adapter = DrinkAdapter(drinkList) // Chắc chắn drinkList không phải null
//            binding.recycleview.adapter = adapter
//        } else {
//            Toast.makeText(this, "No data available", Toast.LENGTH_SHORT).show()
//        }
    }

    override fun bindView() {


    }

    private fun loadDataFromAssets() {
        try {
            val gson = Gson()

            // Read data from JSON files
            val drinks = loadJsonData<DrinkResponse>("drink_list.json", gson)
            val foods = loadJsonData<FoodResponse>("food_list.json", gson)
            val locations = loadJsonData<LocationResponse>("location_receive.json", gson)

            // Check if the data is loaded correctly
            if (drinks != null && foods != null && locations != null) {
                // Set data to the adapter
                adapter = MainAdapter(drinks.data?.list_drink, foods.data?.list_food, locations.data?.list_location)
                binding.recycleview.adapter = adapter
            } else {
                Toast.makeText(this, "Error loading data: Some data is null", Toast.LENGTH_SHORT).show()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error loading data: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }


    // Phương thức loadJsonData để đọc dữ liệu từ file JSON
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