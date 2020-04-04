package com.project.renthvn

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class UpdatePassword : AppCompatActivity() {
    // initialize required variables
    private lateinit var auth: FirebaseAuth
    private lateinit var passwordEt: EditText
    private lateinit var changePasswordBtn: Button
    private lateinit var back: Button
    // override onCreate method to add custom functionality
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)

        auth = FirebaseAuth.getInstance()

        passwordEt = findViewById(R.id.up_pass)

        changePasswordBtn = findViewById(R.id.up_resetPassBtn)

        // action to perform if change password button is pressed
        changePasswordBtn.setOnClickListener{
            var password: String = passwordEt.text.toString()
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show()
            } else {
                auth.currentUser?.updatePassword(password)
                    ?.addOnCompleteListener(this, OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Password changed successfully", Toast.LENGTH_LONG)
                                .show()
                            finish()
                        } else {
                            Toast.makeText(this, "password not changed", Toast.LENGTH_LONG)
                                .show()
                        }
                    })
            }
        }
    }
}