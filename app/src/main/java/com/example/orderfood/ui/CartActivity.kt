package com.example.orderfood.ui

import android.app.Dialog
import android.content.Intent
import android.os.Handler
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.api.model.Order
import com.example.orderfood.R
import com.example.orderfood.adapter.OrderAdapter
import com.example.orderfood.base.BaseActivity
import com.example.orderfood.databinding.ActivityCartBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CartActivity : BaseActivity<ActivityCartBinding>(ActivityCartBinding::inflate) {
    private val orders: MutableList<Order> = mutableListOf()

    override fun initView() {
        loadOrdersFromFile()

        binding.recycleView.layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = OrderAdapter(orders) { order ->
            deleteOrder(order)
        }
    }

    override fun bindView() {
        binding.btnBuy.setOnClickListener { buyAll() }
        binding.back.setOnClickListener { finish() }
    }

    private fun deleteOrder(order: Order) {
        val index = orders.indexOf(order)
        if (index != -1) {
            orders.removeAt(index)
            binding.recycleView.adapter?.notifyItemRemoved(index)
            binding.recycleView.adapter?.notifyDataSetChanged()
            saveOrdersToFile()

        }
    }


    private fun buyAll() {
        for (order in orders) {
            if (order.status == 0) {
                order.status = 1
            }
        }

        binding.recycleView.adapter?.notifyDataSetChanged()

        saveOrdersToFile()
        showDialogSuccess()

    }

    private fun showDialogSuccess() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_success)
        dialog.setCancelable(false)
        Handler().postDelayed({
            dialog.dismiss()
            startActivity(Intent(this, HistoryActivity::class.java))
            finish()
        },3000)
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