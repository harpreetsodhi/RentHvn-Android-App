package com.project.renthvn

import android.content.Intent
import android.graphics.Color
import android.graphics.ColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.braintreepayments.api.dropin.DropInActivity
import com.braintreepayments.api.dropin.DropInRequest
import com.braintreepayments.api.dropin.DropInResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_men_products.*
import java.time.LocalDateTime

class Cart : AppCompatActivity() {

    var clientToken: String = "sandbox_24nry2m5_gmtpk7pqf995crhx"
    var REQUEST_CODE = 100
    var DEFAULT_DELIVERY: Double = 10.00

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val items = createItems()

        rvItems.adapter = ItemAdaptor(this, items)
        rvItems.layoutManager = LinearLayoutManager(this)

        checkoutButton.setOnClickListener {
            val dropInRequest = DropInRequest()
                .clientToken(clientToken)
            startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE)
        }

    }

    private fun createItems(): List<Items> {

        val items = mutableListOf<Items>()
        val ref = FirebaseDatabase.getInstance().getReference("Cart")
        val userId = FirebaseAuth.getInstance().currentUser?.uid!!

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                items.clear()
                for (x in dataSnapshot.children) {
                    items.add(
                        Items(
                            x.child("cid").value.toString(),
                            x.child("oimage").value.toString(),
                            x.child("oname").value.toString(),
                            x.child("oprice").value.toString().toDouble(),
                            x.child("odeliveryDate").value.toString(),
                            x.child("operiod").value.toString().toInt(),
                            x.child("osize").value.toString(),
                            x.child("opickupDate").value.toString(),
                            x.child("pid").value.toString(),
                            x.child("ogender").value.toString()
                            )
                    )
                }

                Log.d("cart - in Cart", items.count().toString())

                if (items.isEmpty()){
                    DEFAULT_DELIVERY = 0.00
                    checkoutButton.setEnabled(false)
                    checkoutButton.setBackgroundColor(Color.GRAY)

                }else{
                    DEFAULT_DELIVERY = 10.00
                    checkoutButton.setEnabled(true)
                    checkoutButton.setBackgroundColor(resources.getColor(R.color.colorThemeColor))
                }


                var TOTAL_VALUE: Double = items.sumByDouble { it.itemPrice }
                priceValue.text = "${TOTAL_VALUE} CAD"
                deliveryValue.text = "${DEFAULT_DELIVERY} CAD"
                totalValue.text = "${DEFAULT_DELIVERY + TOTAL_VALUE} CAD"

                rvItems.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
//        ref.child(userId).addListenerForSingleValueEvent(postListener)
        ref.child(userId).addValueEventListener(postListener)
        return items
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val result =
                    data!!.getParcelableExtra<DropInResult>(DropInResult.EXTRA_DROP_IN_RESULT)

                val refCart = FirebaseDatabase.getInstance().getReference("Cart")
                val refOrders = FirebaseDatabase.getInstance().getReference("Orders")

                val userId = FirebaseAuth.getInstance().currentUser?.uid!!

                val postListener = object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (x in dataSnapshot.children) {

                            val oid = refOrders.push().key
                            val data = OrderClass(
                                    oid!!,
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

                            refOrders.child(userId).child(oid!!).setValue(data)
                        }

                        refCart.child(userId).removeValue()

                        val intent = Intent(this@Cart, OrderSuccessfulActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    override fun onCancelled(dataSnapshot: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                }
                refCart.child(userId).addListenerForSingleValueEvent(postListener)

                // use the result to update your UI and send the payment method nonce to your server
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, " Payment failed \nPlease try again", Toast.LENGTH_SHORT).show()
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
                val error = data!!.getSerializableExtra(DropInActivity.EXTRA_ERROR) as Exception
                Toast.makeText(this, "exception occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
