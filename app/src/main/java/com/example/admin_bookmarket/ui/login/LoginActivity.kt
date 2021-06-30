package com.example.admin_bookmarket.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.example.admin_bookmarket.MainActivity
import com.example.admin_bookmarket.databinding.ActivityLoginBinding

import com.example.admin_bookmarket.R
import com.example.admin_bookmarket.data.FullBookList
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.tasks.await
import java.util.regex.Pattern

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    //init view
    private lateinit var loginEmailLayout: TextInputLayout
    private lateinit var loginPasswordLayout: TextInputLayout
    private lateinit var loginEmail: EditText
    private lateinit var loginPassword: EditText
    private lateinit var loginSignUp: TextView
    private lateinit var loginButton: Button
    private val TAG = "LOGIN"


    //init database reference
    private val db = Firebase.firestore
    private val dbAccountsReference = db.collection("accounts")
    private lateinit var auth: FirebaseAuth
    private lateinit var loadDialog: LoadDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
        loginEmailLayout = findViewById(R.id.LoginEmailLayout)
        loginEmail = findViewById(R.id.LoginEmail)
        loginPasswordLayout = findViewById(R.id.LoginPasswordLayout)
        loginPassword = findViewById(R.id.LoginPassword)
        loginButton = findViewById(R.id.loginButton)
        val resetPassword: TextView = findViewById(R.id.resetPass)
        resetPassword.setOnClickListener {
            startActivity(Intent(baseContext, ForgotPassword::class.java))
        }
        loginPassword.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                loginPasswordLayout.error = null
            }
        }
        loginButton.setOnClickListener {
            onButtonLoginClick()
        }

    }

    override fun onStart() {
        super.onStart()
        if(Firebase.auth.currentUser != null){
            loginEmail.setText(Firebase.auth.currentUser!!.email.toString())
        }

    }

    private fun onButtonLoginClick() {

        if (isValidEmail() && isValidPassword()) {
            loadDialog= LoadDialog(this)
            loadDialog.startLoading()
            dbAccountsReference.get().addOnSuccessListener { result ->
                var emailExist: Boolean = false
                for (document in result) {
                    if (document.id == loginEmail.text.toString() && document.data["isAdmin"] != null) {
                        emailExist = true
                    }
                }
                if (emailExist) {
                    val email = loginEmail.text.toString()
                    val password = loginPassword.text.toString()
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                FullBookList.getInstance()
                                loadDialog.dismissDialog()
                                startActivity(Intent(baseContext, MainActivity::class.java))
                                finish()
                            } else {
                                loginPasswordLayout.error = task.exception!!.message.toString()
                                loginPassword.clearFocus()
                                loadDialog.dismissDialog()
                            }
                        }

                } else {
                    loginEmailLayout.error = "Email is not exist or been wrong!"
                    loadDialog.dismissDialog()
                }
            }
        }
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

    private fun isValidPassword(): Boolean {

        return if (loginPassword.text.isEmpty()) {
            loginPasswordLayout.error = "Password can not empty"
            loginPassword.clearFocus()
            false
        } else {
            if (loginPassword.text.count() < 8) {
                loginPasswordLayout.error = "Password must have more than 8 character"
                loginPassword.clearFocus()
                false
            } else {
                loginPasswordLayout.error = null
                true
            }
        }
    }

    private fun isValidEmail(): Boolean {
        return if (loginEmail.text.isEmpty()) {
            loginEmailLayout.error = "Email can not empty"
            false
        } else {
            if (isEmailRightFormat(loginEmail.text.toString().trim())) {
                loginEmailLayout.error = null
                true
            } else {
                loginEmailLayout.error = "Invalid email"
                false
            }
        }
    }
}