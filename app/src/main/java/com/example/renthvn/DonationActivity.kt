package com.example.renthvn

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_donation2.*

class DonationActivity : AppCompatActivity() {
    var image_rui: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation2)

        image_rui = intent.getParcelableExtra("donation picture")
        imageViewDonation.setImageURI(image_rui)

        btn.setOnClickListener(){
            saveData()
        }

    }


    private fun saveData(){





        val name = intent.getStringExtra("nameOfDonor")
        val email = intent.getStringExtra("emailOfDonor")
        val phoneNumber = intent.getStringExtra("phoneNumberOfDonor")
        val description = intent.getStringExtra("description")

       // val db = FirebaseDatabase.getInstance()

        val ref = FirebaseDatabase.getInstance().getReference("users")
        val userId = ref.push().key

        val user = DonationUser(userId,name,email,phoneNumber,description)

        ref.child(userId.toString()).setValue(user).addOnCompleteListener{
            Toast.makeText(this,"Value has been entered in the database",Toast.LENGTH_SHORT).show()
        }

//        val user = DonationUser(name,email,phoneNumber,description)

  //      val userId = ref.child("users").push().key

    //    ref.child("users").setValue(user)

    }
}
