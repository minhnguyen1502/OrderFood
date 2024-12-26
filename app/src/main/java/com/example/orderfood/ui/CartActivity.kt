package com.example.orderfood.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.api.model.Order
import com.example.orderfood.R
import com.example.orderfood.adapter.OrderAdapter
import com.example.orderfood.base.BaseActivity
import com.example.orderfood.databinding.ActivityCartBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CartActivity : BaseActivity<ActivityCartBinding>(ActivityCartBinding::inflate){
    private val orders: MutableList<Order> = mutableListOf()

    override fun initView() {
        loadOrdersFromFile()

        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = OrderAdapter(orders)
    }

    override fun bindView() {
        binding.btnBuy.setOnClickListener { buyAll() }
    }

    private fun buyAll() {
        for (order in orders) {
            if (order.status == 0) {
                order.status = 1
            }
        }

        binding.recycleView.adapter?.notifyDataSetChanged()

        saveOrdersToFile()
        showPurchaseSuccessDialog()

    }
    private fun showPurchaseSuccessDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Success")
        builder.setMessage("Your purchase was successful!")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
            startActivity(Intent(this, HistoryActivity::class.java))
            finish()
        }
        builder.setCancelable(false)

        val dialog = builder.create()
        dialog.show()
    }
    private fun saveOrdersToFile() {
        try {
            val fileName = "order_food.json"
            val gson = Gson()
            val jsonContent = gson.toJson(orders)

            openFileOutput(fileName, MODE_PRIVATE).use {
                it.write(jsonContent.toByteArray())
            }
        } catch (e: Exception) {
            Log.e("CartActivity", "Error saving orders: ${e.message}")
        }
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
}