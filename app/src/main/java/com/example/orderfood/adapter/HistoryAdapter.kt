package com.example.orderfood.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.api.model.Order
import com.example.orderfood.databinding.ItemOrderShipBinding

class HistoryAdapter(
    private val orders: MutableList<Order>,
    private val onCancelClick: (Order) -> Unit,
    private val onReceivedClick: (Order) -> Unit
) :
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

    inner class OrderViewHolder(private val binding: ItemOrderShipBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.apply {
                tvName.text = order.name
                tvPrice.text = order.price.toString()

                if (order.status == 1) {
                    tvCancel.visibility = View.VISIBLE
                    tvReceive.visibility = View.VISIBLE
                    tvStatus.text = "shipping"
                } else if (order.status == 2) {
                    tvCancel.visibility = View.GONE
                    tvReceive.visibility = View.GONE
                    tvStatus.text = "canceled"
                } else if (order.status == 3) {
                    tvCancel.visibility = View.GONE
                    tvReceive.visibility = View.GONE
                    tvStatus.text = "received"
                }

                tvCancel.setOnClickListener {
                    onCancelClick(order)
                }

                tvReceive.setOnClickListener {
                    onReceivedClick(order)
                }
            }
        }
    }
}