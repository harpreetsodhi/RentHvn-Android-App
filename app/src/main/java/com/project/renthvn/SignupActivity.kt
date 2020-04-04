package com.project.renthvn

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {
    // Initializing variables
    private lateinit var auth: FirebaseAuth
    private val TAG = "SignupActivity"
    private lateinit var emailEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var firstNameEt: EditText
    private lateinit var lastNameEt: EditText
    private lateinit var helpBtn: ImageButton
    private lateinit var mobileEt: EditText
    private lateinit var signUpBtn: Button
    private lateinit var loginBtn: ImageButton
    private var mDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var counIt: Int = 0
    val passwordPattern:Regex = """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!\-_?&])(?=\S+$).{8,}""".toRegex()
    val mobPattern:Regex = """^([+]\d{2})?\d{10}$""".toRegex()

    //overriding onCreate method to add custom functionality
    override fun onCreate(savedInstanceState: Bundle?) {
        setTitle("Create Account");
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        emailEt = findViewById(R.id.signup_email)
        passwordEt = findViewById(R.id.signup_pass)
        loginBtn = findViewById(R.id.signup_backBtn)
        signUpBtn = findViewById(R.id.signup_signupBtn)
        helpBtn = findViewById(R.id.signup_passHelp)
        firstNameEt = findViewById(R.id.signup_fn)
        lastNameEt = findViewById(R.id.signup_ln)
        mobileEt = findViewById(R.id.signup_mob)

        // action to perform when user presses sign up button button is pressed
        signUpBtn.setOnClickListener{
            var email: String = emailEt.text.toString()
            var password: String = passwordEt.text.toString()
            var firstName: String = firstNameEt.text.toString()
            var lastName: String = lastNameEt.text.toString()
            var mobile: String = mobileEt.text.toString()
            // check for empty fields and inform user about it
            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)|| TextUtils.isEmpty(mobile)|| TextUtils.isEmpty(firstName)|| TextUtils.isEmpty(lastName)) {
                Toast.makeText(this, "Please fill all the fields!", Toast.LENGTH_LONG).show()
            } else{
                // Input field validations applied
                if(mobPattern.matches(mobile)){
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        if (passwordPattern.matches(password)) {
                            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener{ task ->
                                if(task.isSuccessful){
                                    val userId = auth!!.currentUser!!.uid
                                    //send verification email to user
                                    verifyEmail();
                                    //check if email exists?
                                    auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(this, OnCompleteListener { task ->
                                        if(task.result?.signInMethods?.isEmpty()!!){
                                            Toast.makeText(this, "Email already in use!!", Toast.LENGTH_LONG).show()
                                        }
                                    })
                                    Toast.makeText(this, "Successfully Registered!", Toast.LENGTH_SHORT).show()
                                    // Insert values to realtime DB
                                    val currentUserDb = mDatabaseReference!!.child(userId)
                                    currentUserDb.child("firstName").setValue(firstName)
                                    currentUserDb.child("lastName").setValue(lastName)
                                    currentUserDb.child("mobile").setValue(mobile)
                                    currentUserDb.child("email").setValue(email)
                                }else {
                                    checkEmailExits(email);
                                    Toast.makeText(this, "Registration Error!", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }else{
                            Toast.makeText(this,"Invalid Password!", Toast.LENGTH_SHORT).show();
                        }
                    } else{
                        Toast.makeText(this,"Invalid Email Address!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this,"Invalid Mobile Number. Please refer requirements", Toast.LENGTH_SHORT).show();
                }
            }
        }
        // action to perform when user presses login up button button is pressed
        loginBtn.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        // action to perform when user presses help up button button is pressed
        helpBtn.setOnClickListener{

            val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            // Inflate a custom view using layout inflater
            val view = inflater.inflate(R.layout.password_help,null)

            // Initialize a new instance of popup window
            val popupWindow = PopupWindow(
                view, // Custom view to show in popup window
                LinearLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
                LinearLayout.LayoutParams.WRAP_CONTENT // Window height
            )

            // Set an elevation for the popup window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popupWindow.elevation = 5.0F
            }


            // If API level 23 or higher then execute the code
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                // Create a new slide animation for popup window enter transition
                val slideIn = Slide()
                slideIn.slideEdge = Gravity.LEFT
                popupWindow.enterTransition = slideIn

                // Slide animation for popup window exit transition
                val slideOut = Slide()
                slideOut.slideEdge = Gravity.LEFT
                popupWindow.exitTransition = slideOut

            }

            // Get the widgets reference from custom view
            val tv = view.findViewById<TextView>(R.id.help_Rules)
            val buttonPopup = view.findViewById<ImageButton>(R.id.help_closeBtn)

            // Set click listener for popup window's text view
            tv.setOnClickListener{
                // Change the text color of popup window's text view
                tv.setTextColor(Color.RED)
            }

            // Set a click listener for popup's button widget
            buttonPopup.setOnClickListener{
                // Dismiss the popup window
                popupWindow.dismiss()
            }

            // Set a dismiss listener for popup window
            popupWindow.setOnDismissListener {
                Toast.makeText(applicationContext,"Thank you!", Toast.LENGTH_SHORT).show()
            }


            // Finally, show the popup window on app
            TransitionManager.beginDelayedTransition(signup_layout)
            popupWindow.showAtLocation(
                signup_layout, // Location to display popup window
                Gravity.BOTTOM, // Exact position of layout to display popup
                0, // X offset
                0 // Y offset
            )
            counIt = 1
        }
    }

    private fun verifyEmail() {
        val mUser = auth!!.currentUser;
        mUser!!.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@SignupActivity,
                        "Verification email sent to " + mUser.getEmail(),
                        Toast.LENGTH_LONG).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(this@SignupActivity,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun checkEmailExits(email:String) {
        Log.d(TAG, email)
        auth.fetchSignInMethodsForEmail(email).addOnCompleteListener(this, OnCompleteListener { task ->
            if(!task.result?.signInMethods.isNullOrEmpty()){
                Toast.makeText(this, "Email already in use!", Toast.LENGTH_LONG).show()
            }
        })
    }

}
