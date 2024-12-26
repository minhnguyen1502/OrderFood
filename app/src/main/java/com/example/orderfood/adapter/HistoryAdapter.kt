package com.example.orderfood.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.api.model.Order
import com.example.orderfood.databinding.ItemOrderShipBinding

class HistoryAdapter(private val orders: MutableList<Order>, private val onCancelClick: (Order) -> Unit) :
    RecyclerView.Adapter<HistoryAdapter.OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderShipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.bind(order)
    }

    override fun getItemCount(): Int = orders.size

    inner class OrderViewHolder(private val binding: ItemOrderShipBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.apply {
                // Bind other views (e.g., name, price, etc.)
                tvName.text = order.name
                tvPrice.text = order.price.toString()
                tvStatus.text = order.status.toString()

                // Show "Hủy đơn" button if status is 1
                if (order.status == 1) {
                    tvCancel.visibility = View.VISIBLE
                } else {
                    tvCancel.visibility = View.GONE
                }

                // Set the OnClickListener for the "Hủy đơn" button
                tvCancel.setOnClickListener {
                    onCancelClick(order)
                }
            }
        }
    }
}