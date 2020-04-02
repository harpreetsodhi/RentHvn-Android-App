package com.project.renthvn

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.single_order_item.view.*

class OrderAdapter (private val context: Context, private val items: List<OrderClass>)
    : RecyclerView.Adapter<OrderAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_order_item, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item: OrderClass) {
            Glide.with(itemView)
                .load(item.Oimage)
                .into(itemView.itemImage)
            itemView.itemName.text = item.Oname
            itemView.itemPrice.text = "${item.Oprice} CAD"
            itemView.delivery_date_view.text = "Delivery: ${item.OdeliveryDate}"
            itemView.period_view.text = "${item.Operiod} days"
            itemView.size_view.text = item.Osize
            itemView.pickup_date_view.text = "Pickup: ${item.OpickupDate}"
            itemView.delivery_date_for_note.text = item.OdeliveryDate
        }
    }
}