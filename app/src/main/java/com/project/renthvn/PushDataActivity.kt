package com.project.renthvn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PushDataActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_push_data)


//        val ref = FirebaseDatabase.getInstance().getReference("Products")
//        val pid = ref.push().key

//        val data = ProductsClass("Indian Wedding Dress", 29, "Made of silk material", "Pink", R.drawable.image_wedding_gown)

//        val data = OrderClass(oid!!, pid, LocalDateTime.now(), imageURL, productName,
//            sizeSpinner.selectedItem.toString(), daysSpinner.selectedItem.toString().toInt(),
//            date_picker_label.text.toString(), delivery_date_value.text.toString(),
//            pickup_date_label.text.toString(), productColor, productDesc,
//            10.00, productPrice
//        )


//        val userid = FirebaseAuth.getInstance().currentUser?.uid

//            ref.child("Women").child(pid!!).setValue(data).addOnCompleteListener{
//                Toast.makeText(this, "product inserted", Toast.LENGTH_SHORT).show()
            }

    }

