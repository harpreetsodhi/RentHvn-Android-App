package com.project.renthvn

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.single_item.view.*


class ItemAdaptor (private val context: Context, private val items: List<Items>)
    : RecyclerView.Adapter<ItemAdaptor.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_item, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item: Items) {
            itemView.itemName.text = item.itemName
            itemView.itemPrice.text = "${item.itemPrice} CAD"
            itemView.itemQuantity.text = "${item.itemQuantity}"
            itemView.delivery_date_view.text = "Delivery: ${item.deliveryDate}"
            itemView.period_view.text = "${item.period} days"
            itemView.size_view.text = item.size
            itemView.pickup_date_view.text = "Pickup: ${item.pickupDate}"
        }
    }
}