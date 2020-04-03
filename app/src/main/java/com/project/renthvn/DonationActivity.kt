package com.project.renthvn

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_donation2.*

class DonationActivity : AppCompatActivity() {
    var image_rui: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation2)
        title = "Donation"

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


        Toast.makeText(this,"Thanks for the donation",Toast.LENGTH_SHORT).show()

        ////////////////////////////////////////////

        val ref = FirebaseDatabase.getInstance().getReference("DonationUsers")
        val did = ref.push().key

        val userId = FirebaseAuth.getInstance().currentUser?.uid!!

        val data = DonationUser(did!!, userId,name,email,phoneNumber,description)

        ref.child(userId).child(did.toString()).setValue(data).addOnCompleteListener{
            val intent = Intent(this, Home_Screen::class.java)
            startActivity(intent)
            finish()
        }
    }

}