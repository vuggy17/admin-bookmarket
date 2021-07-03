package com.example.admin_bookmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.example.admin_bookmarket.data.model.AppAccount
import com.example.admin_bookmarket.data.model.User
import com.example.admin_bookmarket.data.model.UserDeliverAddress
import com.example.admin_bookmarket.ui.login.LoginActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {
    //Init view
    private lateinit var edtPassword: EditText
    private lateinit var edtConfirmPasswordLayout: TextInputLayout
    private lateinit var edtPasswordConfirm: EditText
    private lateinit var edtPasswordLayout: TextInputLayout
    private lateinit var edtName: EditText
    private lateinit var edtNameLayout: TextInputLayout
    private lateinit var edtEmail: EditText
    private lateinit var edtEmailLayout: TextInputLayout
    private lateinit var tvSignIn: TextView
    private val TAG = "REGISTER";

    //Init Data
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var user: User
    private lateinit var appAccount: AppAccount
    private lateinit var auth: FirebaseAuth
// ...
// Initialize Firebase Auth

    //Init FireBase
    private val db = Firebase.firestore
    private val dbReference = db.collection("accounts")
    private val dbSalerReference = db.collection("salerAccount")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        edtPassword = findViewById(R.id.edtUserPassword)
        edtConfirmPasswordLayout = findViewById(R.id.inputTextPasswordConfirmLayout)
        edtPasswordConfirm = findViewById(R.id.edtUserPasswordConfirm)
        edtPasswordLayout = findViewById(R.id.inputTextPasswordLayout)
        edtName = findViewById(R.id.edtUserName)
        edtNameLayout = findViewById(R.id.inputTextUserNameLayout)
        edtEmail = findViewById(R.id.edtUserEmail)
        edtEmailLayout = findViewById(R.id.inputTextEmailLayout)
        tvSignIn = findViewById(R.id.tvSignIn)
        auth = Firebase.auth

        tvSignIn.setOnClickListener{
            startActivity(Intent(baseContext, LoginActivity::class.java))
        }

        val imgBackArrow: AppCompatImageView = findViewById(R.id.imgBackArrow)
        imgBackArrow.setOnClickListener {
            onBackPressed()
        }
        val btnSignUp: Button = findViewById(R.id.btn_Register)
        btnSignUp.setOnClickListener {
            signUpClick()
        }

    }

    override fun onBackPressed() {
        startActivity(Intent(baseContext,LoginActivity::class.java))
    }

    private fun signUpClick() {
        if (isValidName() && isValidEmail() && isValidPassword() && isValidConfirmPassword()) {
            dbReference.get().addOnSuccessListener() { result ->
                var emailExist: Boolean=false
                for (document in result) {
                    if (document.id == edtEmail.text.toString()) {
                        emailExist = true
                    }
                }
                if(emailExist){
                    edtEmailLayout.error = "Email is exists"
                }else {
                    email = edtEmail.text.toString()
                    password = edtPassword.text.toString()
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success")
                                val user = auth.currentUser
                                updateData()
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        }
    }
    private fun updateData(){
        user = User(fullName = edtName.text.toString())
        appAccount = AppAccount(email, "", user)
        dbSalerReference.document(appAccount.email).set(appAccount)
        val isNewMap: MutableMap<String, Any> = mutableMapOf("isNew" to "true")
        dbSalerReference.document(appAccount.email).set(isNewMap)
        dbReference.document(appAccount.email).set(appAccount).addOnSuccessListener {
            Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Register Fail$e", Toast.LENGTH_SHORT).show()
        }
        val dbReferenceRecentAccountUserAddressAccount = dbReference.document(appAccount.email).collection("userDeliverAddresses")
//        dbReferenceRecentAccountUserAddressAccount.document().set(UserDeliverAddress("",user.fullName, user.phoneNumber, user.addressLane, user.district,user.city, true))
        finish()
        startActivity(Intent(baseContext,LoginActivity::class.java))
    }




    private fun isValidConfirmPassword(): Boolean {

        return if (edtPasswordConfirm.text.isEmpty()) {
            edtConfirmPasswordLayout.error = "Confirm password can not empty"
            false
        } else {
            return if (edtPassword.text.toString() != edtPasswordConfirm.text.toString()) {
                edtConfirmPasswordLayout.error = "Password and Confirm Password must be one"
                false
            } else {
                edtConfirmPasswordLayout.error = null
                true
            }
        }
    }

    private fun isValidPassword(): Boolean {

        return if (edtPassword.text.isEmpty()) {
            edtPasswordLayout.error = "Password can not empty"
            false
        } else {
            if (edtPassword.text.count() < 8) {
                edtPasswordLayout.error = "Password must have more than 8 character"
                false
            } else {
                edtPasswordLayout.error = null
                true
            }
        }
    }

    private fun isValidName(): Boolean {
        return if (edtName.text.isEmpty()) {
            edtNameLayout.error = "Name can not empty"
            false
        } else {
            return if (isNameContainNumberOrSpecialCharacter(edtName.text.toString())) {
                edtNameLayout.error = "Name can not contain number of special character"
                false
            } else {
                edtNameLayout.error = null
                true
            }
        }
    }

    private fun isNameContainNumberOrSpecialCharacter(name: String): Boolean {
        var hasNumber: Boolean = Pattern.compile(
            "[0-9]"
        ).matcher(name).find()
        var hasSpecialCharacter: Boolean = Pattern.compile(
            "[!@#$%&.,\"':;?*()_+=|<>?{}\\[\\]~-]"
        ).matcher(name).find()
        return hasNumber || hasSpecialCharacter
    }

    private fun isEmailRightFormat(email: String): Boolean {
        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9]))|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }


    private fun isValidEmail(): Boolean {
        return if (edtEmail.text.isEmpty()) {
            edtEmailLayout.error = "Email can not empty"
            false
        } else {
            if (isEmailRightFormat(edtEmail.text.toString().trim())) {
                edtEmailLayout.error = null
                true
            } else {
                edtEmailLayout.error = "Invalid email"
                false
            }
        }
    }
}