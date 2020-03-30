package com.example.renthvn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.braintreepayments.api.dropin.DropInActivity
import com.braintreepayments.api.dropin.DropInRequest
import com.braintreepayments.api.dropin.DropInResult
import kotlinx.android.synthetic.main.activity_cart.*

class Cart : AppCompatActivity() {

    var clientToken: String = "sandbox_24nry2m5_gmtpk7pqf995crhx"
    var REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)
        getSupportActionBar()?.setLogo(R.drawable.ic_arrow_back_black_24dp)
        getSupportActionBar()?.setDisplayUseLogoEnabled(true)
        val items = createItems()

        var DEFAULT_DELIVERY: Double = 10.00
        var TOTAL_VALUE: Double = items.sumByDouble { it.itemPrice }

        rvItems.adapter = ItemAdaptor(this, items)
        rvItems.layoutManager = LinearLayoutManager(this)
        priceValue.text = "${TOTAL_VALUE} CAD"
        deliveryValue.text = "${DEFAULT_DELIVERY} CAD"
        totalValue.text = "${DEFAULT_DELIVERY + TOTAL_VALUE} CAD"

        checkoutButton.setOnClickListener {
            val dropInRequest = DropInRequest()
                .clientToken(clientToken)
            startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE)
        }

    }

    private fun createItems(): List<Items> {
        val items = mutableListOf<Items>()
        for (i in 1..10) {
            items.add(
                Items(
                    "Cream Sherwani",
                    42.00 * 0.5 * i,
                    i,
                    "10 MAR 2020",
                    3,
                    "XL",
                    "14 MAR 2020"
                )
            )
        }
        return items
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val result =
                    data!!.getParcelableExtra<DropInResult>(DropInResult.EXTRA_DROP_IN_RESULT)
                Toast.makeText(this, "transaction successful", Toast.LENGTH_SHORT).show()
                // use the result to update your UI and send the payment method nonce to your server
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "transaction cancelled", Toast.LENGTH_SHORT).show()
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
                val error = data!!.getSerializableExtra(DropInActivity.EXTRA_ERROR) as Exception
                Toast.makeText(this, "exception occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
