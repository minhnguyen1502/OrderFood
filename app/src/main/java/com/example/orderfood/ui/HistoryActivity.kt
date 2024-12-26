package com.example.orderfood.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.api.model.Order
import com.example.orderfood.R
import com.example.orderfood.adapter.HistoryAdapter
import com.example.orderfood.base.BaseActivity
import com.example.orderfood.databinding.ActivityHistoryBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HistoryActivity : BaseActivity<ActivityHistoryBinding>(ActivityHistoryBinding::inflate){
    private val orders: MutableList<Order> = mutableListOf()

    override fun initView() {
        loadOrdersFromFile()
        val adapter = HistoryAdapter(orders) { order ->
            // Handle the "Hủy đơn" button click by changing the order's status
            cancelOrder(order)
        }
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = adapter

    }

    override fun bindView() {


    }

    private fun cancelOrder(order: Order) {
        order.status = 2  // Change the status to 2
        // Optionally, update the UI and notify the adapter
        binding.recycleView.adapter?.notifyDataSetChanged()

        // If you want to save the updated order back to the file, do so here
        saveOrdersToFile()
    }
    private fun loadOrdersFromFile() {
        try {
            val fileName = "order_food.json"
            val fileInputStream = openFileInput(fileName)
            val jsonContent = fileInputStream.bufferedReader().use { it.readText() }
            fileInputStream.close()

            // Parse JSON thành danh sách Order
            val gson = Gson()
            val type = object : TypeToken<List<Order>>() {}.type
            val loadedOrders: List<Order> = gson.fromJson(jsonContent, type)
            orders.clear()
            orders.addAll(loadedOrders)

        } catch (e: Exception) {
            Log.e("CartActivity", "Error reading orders: ${e.message}")
        }
    }
    // Method to save updated orders to file (optional)
    private fun saveOrdersToFile() {
        try {
            val gson = Gson()
            val jsonContent = gson.toJson(orders)
            openFileOutput("order_food.json", MODE_PRIVATE).use {
                it.write(jsonContent.toByteArray())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}