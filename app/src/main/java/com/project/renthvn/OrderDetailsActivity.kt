package com.project.renthvn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_order_details.*
import java.time.LocalDateTime

class OrderDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        val items = createItems()

        rvOrderItems.adapter = OrderAdapter(this, items)
        rvOrderItems.layoutManager = LinearLayoutManager(this)
    }


    private fun createItems(): List<OrderClass> {

        val items = mutableListOf<OrderClass>()

        val ref = FirebaseDatabase.getInstance().getReference("Orders")
        val userId = FirebaseAuth.getInstance().currentUser?.uid!!

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                items.clear()
                for (x in dataSnapshot.children) {
                    items.add(
                        OrderClass(
                            x.child("oid").value.toString(),
                            x.child("pid").value.toString(),
                            LocalDateTime.now(),
                            x.child("oimage").value.toString(),
                            x.child("oname").value.toString(),
                            x.child("osize").value.toString(),
                            x.child("operiod").value.toString().toInt(),
                            x.child("oeventDate").value.toString(),
                            x.child("odeliveryDate").value.toString(),
                            x.child("opickupDate").value.toString(),
                            x.child("ocolor").value.toString(),
                            x.child("odesc").value.toString(),
                            x.child("oprice").value.toString().toDouble(),
                            x.child("ogender").value.toString()
                        )
                    )
                }

                if (items.isEmpty()) {
                    tv.visibility = View.VISIBLE
                }
                else {
                    tv.visibility = View.GONE
                }
                rvOrderItems.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        ref.child(userId).addValueEventListener(postListener)
        return items
    }


}
