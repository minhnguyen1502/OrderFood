package com.example.orderfood.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.api.model.Order
import com.example.orderfood.databinding.ItemCartBinding

class OrderAdapter(private val orders: List<Order>) :
    RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    private val filteredOrders: List<Order> = orders.filter { it.status == 0 }

    class OrderViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.tvName.text = "Name: ${order.name}"
            binding.tvPrice.text = "Price: ${order.price}"
            binding.tvCount.text = "Count: ${order.count}"
            binding.ivDelete.setOnClickListener { }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(filteredOrders[position])
    }

    override fun getItemCount() = filteredOrders.size
}
