package com.project.renthvn

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.single_product_item.view.*

class ProductsAdapter (private val context: Context, private val items: List<ProductsClass>)
    : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_product_item, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        holder.itemView.setOnClickListener{
            val intent = Intent(this.context, ProductDetailsActivity::class.java)
            intent.putExtra("pid", item.Pid)
            intent.putExtra("gender", item.Pgender)
            this.context.startActivity(intent)
        }
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bind(item: ProductsClass) {
            Glide.with(itemView)
                .load(item.Pimage)
                .into(itemView.product_image)
            itemView.product_name.text = item.Pname
            itemView.product_price.text = "${item.Pprice} CAD"
        }
    }
}