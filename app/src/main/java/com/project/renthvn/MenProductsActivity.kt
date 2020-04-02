package com.project.renthvn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_men_products.*
import kotlinx.android.synthetic.main.single_product_item.*
import kotlin.math.log

class MenProductsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_men_products)

        val gender = getIntent().getStringExtra("gender")

        val items = createItems(gender)

        val gridLayoutManager = GridLayoutManager(this,2)
        rvMenProducts.layoutManager = gridLayoutManager
        rvMenProducts.adapter = ProductsAdapter(this, items)
    }

    private fun createItems(gender: String): List<ProductsClass> {

        val items = mutableListOf<ProductsClass>()
        val ref = FirebaseDatabase.getInstance().getReference("Products")

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (x in dataSnapshot.children) {
                    items.add(
                        ProductsClass(
                            x.key.toString(),
                            gender,
                            x.child("pname").value.toString(),
                            x.child("pprice").value.toString().toDouble(),
                            x.child("pdesc").value.toString(),
                            x.child("pcolor").value.toString(),
                            x.child("pimage").value.toString()

                        )
                    )
                }
                rvMenProducts.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }


        }
        ref.child(gender).addListenerForSingleValueEvent(postListener)
//        ref.child("Men").addValueEventListener(postListener)
        return items
    }

}
