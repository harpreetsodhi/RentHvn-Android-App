package com.example.renthvn

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_donation2.*

class DonationActivity : AppCompatActivity() {
    var image_rui: Uri? = null

    private var mDatabase: FirebaseDatabase? = null

    private var mDatabaseReference: DatabaseReference? = null


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


        Toast.makeText(this,"Thanks for the donation",Toast.LENGTH_SHORT).show()

        val myDatabase = FirebaseDatabase.getInstance().getReference("users")
        val userId = myDatabase.push().key
        val user = DonationUser(userId,name,email,phoneNumber,description)
        myDatabase.child(userId.toString()).setValue(user).addOnCompleteListener(){
            Toast.makeText(this,"Thanks for the donation",Toast.LENGTH_SHORT).show()
        }



       // val fireDatabase = FirebaseDatabase.getInstance()

        //val databaseReference = fireDatabase.getReference("renthvn-995ea")

        //val user = DonationUser(name,email,phoneNumber,description)
        //val key = databaseReference.child("users").push().key
        //databaseReference.child("users").child(key.toString()).setValue(user)



        //val userid: String?

        //userid = ref.push().key


        //ref.child(userid.toString()).setValue("tejasvi")



        //val ref = FirebaseDatabase.getInstance().getReference("")
        //val userId = ref.push().key
        // currentDonationUser = ref!!.child(userId.toString())

      //  val user = DonationUser(name,email,phoneNumber,description)

      //  currentDonationUser.setValue(user).addOnCompleteListener(){
     //       Toast.makeText(this,"Value has been entered in the database",Toast.LENGTH_SHORT).show()
    //    }

      //ref.child(userId.toString()).setValue(user).addOnCompleteListener{
       //Toast.makeText(this,"Value has been entered in the database",Toast.LENGTH_SHORT).show()
      //}


    }
}
