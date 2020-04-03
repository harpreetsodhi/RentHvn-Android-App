package com.project.renthvn

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_donation_details.*
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class DonationDetails : AppCompatActivity() {

    private val PERMISSION_CODE: Int = 1000
    private val IMAGE_CAPTURE_CODE = 1001
    var image_rui: Uri? = null

    var emailPattern = "(\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,6})".toRegex()
    var phonePattern = "^\\+[0-9]{10,13}\$".toRegex()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_details)

        title = "Donation"

        var flag:Boolean = false

        button1.setOnClickListener() {

            flag =  checkDataEntered()

            if (flag) {

                val firstName: String = nameOfDonor.text.toString()
                val email: String = emailOfDonor.text.toString()
                val description: String = descriptionOfDonation.text.toString()
                val phoneNumber: String = phoneNumberOfDonor.text.toString()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED || checkSelfPermission(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_DENIED
                    ) {

                        val permission = arrayOf(
                            Manifest.permission.CAMERA,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        requestPermissions(permission, PERMISSION_CODE)

                    }
                    else{
                        openCamera()
                    }
                } else {
                    openCamera()
                }

            }
            else{
                Toast.makeText(this,"Please enter details correctly",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun checkDataEntered(): Boolean{

        var flag:Boolean = true

        val firstName: String = nameOfDonor.text.toString()
        val email: String = emailOfDonor.text.toString()
        val description: String = descriptionOfDonation.text.toString()
        val phoneNumber: String = phoneNumberOfDonor.text.toString()

        if(TextUtils.isEmpty(firstName) && TextUtils.isEmpty(email) && TextUtils.isEmpty(description) && TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(this,"Please enter all the details ",Toast.LENGTH_SHORT).show()
            flag = false
        }


        if(!email.toString().trim().matches(emailPattern)){
            emailOfDonor.setError("Please enter a valid email")
            flag = false
        }
        if(!phoneNumber.toString().trim().matches(phonePattern)){
            phoneNumberOfDonor.setError("Please enter a valid phone number")
            flag = false
        }
        return flag
    }


    private fun openCamera(){
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE,"New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the Camera")
        image_rui = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_rui)
        startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openCamera()
                }
                else{
                    Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val name = findViewById<EditText>(R.id.nameOfDonor)
        val email = findViewById<EditText>(R.id.emailOfDonor)
        val phoneNumber = findViewById<EditText>(R.id.phoneNumberOfDonor)
        val description = findViewById<EditText>(R.id.descriptionOfDonation)

        if(resultCode == Activity.RESULT_OK){

            val intent = Intent(this,DonationActivity::class.java)
            val name = name.text.toString()
            val email = email.text.toString()
            val phoneNumber = phoneNumber.text.toString()
            val description = description.text.toString()
            intent.putExtra("donation picture",image_rui)
            intent.putExtra("nameOfDonor",name)
            intent.putExtra("emailOfDonor",email)
            intent.putExtra("phoneNumberOfDonor",phoneNumber)
            intent.putExtra("description",description)
            startActivity(intent)
        }
    }
}