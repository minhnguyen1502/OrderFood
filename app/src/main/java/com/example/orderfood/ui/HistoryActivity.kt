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
    private lateinit var adapter: HistoryAdapter

    override fun initView() {
        loadOrdersFromFile()
        adapter = HistoryAdapter(orders,
            onCancelClick = { order ->
                cancelOrder(order)
            },
            onReceivedClick = { order ->
                receivedOrder(order)
            }
        )
        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = adapter

    }

    override fun bindView() {
        binding.back.setOnClickListener { finish() }

    }

    private fun cancelOrder(order: Order) {
        order.status = 2
        binding.recycleView.adapter?.notifyDataSetChanged()
        saveOrdersToFile()
    }

    private fun receivedOrder(order: Order) {
        order.status = 3
        binding.recycleView.adapter?.notifyDataSetChanged()
        saveOrdersToFile()
    }

    private fun loadOrdersFromFile() {
        try {
            val fileName = "order_food.json"
            val fileInputStream = openFileInput(fileName)
            val jsonContent = fileInputStream.bufferedReader().use { it.readText() }
            fileInputStream.close()

            val gson = Gson()
            val type = object : TypeToken<List<Order>>() {}.type
            val loadedOrders: List<Order> = gson.fromJson(jsonContent, type)
            orders.clear()
            orders.addAll(loadedOrders)

        } catch (e: Exception) {
            Log.e("CartActivity", "Error reading orders: ${e.message}")
        }
    }
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