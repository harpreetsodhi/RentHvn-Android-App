package com.project.renthvn

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.drawer_nav_header.view.*
import kotlinx.android.synthetic.main.single_item.view.*
import android.content.DialogInterface
import android.R.drawable
import android.R.string
import android.app.AlertDialog
import android.util.Log


class ItemAdaptor (private val context: Context, private val items: List<Items>)
    : RecyclerView.Adapter<ItemAdaptor.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_item, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        holder.itemView.delete_icon.
            setOnClickListener{
                openDialog(item.cartitemID)
            }

        holder.itemView.remove_view.
            setOnClickListener{
                  openDialog(item.cartitemID)
            }

        holder.itemView.itemImage.
            setOnClickListener{
            val intent = Intent(this.context, ProductDetailsActivity::class.java)
            intent.putExtra("pid", item.productID)
            intent.putExtra("gender", item.gender)
            this.context.startActivity(intent)
        }

        holder.itemView.itemName.
            setOnClickListener{
                val intent = Intent(this.context, ProductDetailsActivity::class.java)
                intent.putExtra("pid", item.productID)
                intent.putExtra("gender", item.gender)
                this.context.startActivity(intent)
            }
    }

    private fun openDialog(cartID: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Remove")
        builder.setMessage("Do you want to remove the item from the cart?")
        builder.setIcon(R.drawable.app_src_main_res_drawable_logo_burned)
        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
            dialog.dismiss()
            val ref = FirebaseDatabase.getInstance().getReference("Cart")
            val userId = FirebaseAuth.getInstance().currentUser?.uid!!
            ref.child(userId).child(cartID).removeValue()
        })
        builder.setNegativeButton("No",
            DialogInterface.OnClickListener { dialog, id -> dialog.dismiss() })
        val alert = builder.create()
        alert.show()
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(item: Items) {
            Glide.with(itemView)
                .load(item.itemImage)
                .into(itemView.itemImage)
            itemView.itemName.text = item.itemName
            itemView.itemPrice.text = "${item.itemPrice} CAD"
            itemView.delivery_date_view.text = "Delivery: ${item.deliveryDate}"
            itemView.period_view.text = "${item.period} days"
            itemView.size_view.text = item.size
            itemView.pickup_date_view.text = "Pickup: ${item.pickupDate}"
        }
    }
}