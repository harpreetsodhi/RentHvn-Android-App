package com.project.renthvn

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    // Initializing required variables
    private lateinit var auth: FirebaseAuth
    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var signupBtn: Button
    private lateinit var loginBtn: Button
    private val TAG = "LoginActivity"
    private lateinit var resetPasswordTv: TextView

    //overriding onCreate method to add custom functionality
    override fun onCreate(savedInstanceState: Bundle?) {
        setTitle("Login");
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var context: Context? = this.applicationContext

        // if user is already logged in
        if (FirebaseAuth.getInstance().currentUser != null) {
//            Toast.makeText(this, "User logged in!", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        emailEt = findViewById(R.id.login_email)
        passwordEt = findViewById(R.id.login_pass)
        signupBtn = findViewById(R.id.login_signupButton)
        loginBtn = findViewById(R.id.login_loginBtn)
        resetPasswordTv = findViewById(R.id.login_resetPassTV)
        auth = FirebaseAuth.getInstance()

        // action to perform when login button is pressed
        loginBtn.setOnClickListener {
            var email: String = emailEt.text.toString()
            var password: String = passwordEt.text.toString()
            //if input fields are empty, give appropriate message
            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this@LoginActivity, "Please fill all the fields!", Toast.LENGTH_LONG).show()
            } else{
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener { task ->
                    if(task.isSuccessful) {
                        auth.currentUser?.reload() // reloads user fields, like emailVerified:
//                        if (!auth.currentUser?.isEmailVerified!!) {
//                            val toast = Toast.makeText(this, "Email not verified!!", Toast.LENGTH_LONG)
//                            toast.setGravity(Gravity.BOTTOM, 0,0)
//                            toast.show()
//                        }
//                        if(true){// auth.currentUser?.isEmailVerified!!){
                            //login
//                            Toast.makeText(this, "Successfully Logged In!", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
//                        }else{
//                            Toast.makeText(this, "Please verify email!", Toast.LENGTH_LONG).show()
//
//                        }
                    }else {
                        Toast.makeText(this, "Login Failed!", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
        // action to perform when sign up button is pressed
        signupBtn.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }
        //action to perform when reset password is pressed
        resetPasswordTv.setOnClickListener{
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }
}