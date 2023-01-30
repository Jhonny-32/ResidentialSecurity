package com.edifice.residentialsecurity.ui.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.edifice.residentialsecurity.R
import com.edifice.residentialsecurity.data.model.Order


class OrdersClientAdapter(val context: Activity, private val orders: ArrayList<Order>) :
    RecyclerView.Adapter<OrdersClientAdapter.OrdersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_orders, parent, false)
        return OrdersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val order = orders[position]

        holder.textViewOrderId.text = "Orden #${order.id}"
        holder.textViewDate.text = "${order.timestamp}"
        holder.textViewNameResident.text ="${order.client?.getFullName()}"
        holder.textViewPhone.text = "${order.client?.phone}"
        holder.textViewtower.text = "${order.sets?.tower}"
        holder.textViewapartament.text = "${order.sets?.apartament}"


    }

    class OrdersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewOrderId: TextView
        val textViewDate: TextView
        val textViewNameResident: TextView
        val textViewPhone: TextView
        val textViewtower: TextView
        val textViewapartament: TextView

        init {
            textViewOrderId = view.findViewById(R.id.textview_order_id)
            textViewDate = view.findViewById(R.id.textview_data_order)
            textViewNameResident = view.findViewById(R.id.textview_name)
            textViewPhone = view.findViewById(R.id.textview_phone)
            textViewtower = view.findViewById(R.id.textview_tower)
            textViewapartament = view.findViewById(R.id.textview_apartament)
        }
    }

}