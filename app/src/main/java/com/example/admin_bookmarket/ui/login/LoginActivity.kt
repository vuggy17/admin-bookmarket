package com.example.admin_bookmarket.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.admin_bookmarket.EditProfileActivity
import com.example.admin_bookmarket.MainActivity
import com.example.admin_bookmarket.R
import com.example.admin_bookmarket.RegisterActivity
import com.example.admin_bookmarket.data.FullBookList
import com.example.admin_bookmarket.data.common.AppUtil
import com.example.admin_bookmarket.data.model.AppAccount
import com.example.admin_bookmarket.data.model.User
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
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
    private val dbSalerAccountsReference = db.collection("salerAccount")
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
        loginSignUp = findViewById(R.id.LoginSignUp)
        val resetPassword: TextView = findViewById(R.id.resetPass)
        resetPassword.setOnClickListener {
            startActivity(Intent(baseContext, ForgotPassword::class.java))
        }
        loginPassword.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                loginPasswordLayout.error = null
            }
        }
        loginButton.setOnClickListener {
            onButtonLoginClick()
        }
        loginSignUp.setOnClickListener {
            startActivity(Intent(baseContext, RegisterActivity::class.java))
            finish() //remove activity from backstack
        }
    }

    override fun onStart() {
        super.onStart()
        if (Firebase.auth.currentUser != null) {
            loginEmail.setText(Firebase.auth.currentUser!!.email.toString())
        }

    }

    private fun onButtonLoginClick() {

        if (isValidEmail() && isValidPassword()) {
            loadDialog = LoadDialog(this)
            loadDialog.startLoading()
            dbSalerAccountsReference.get().addOnSuccessListener { result ->
                var emailExist: Boolean = false
                for (document in result) {
                    if (document.id == loginEmail.text.toString()) {
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
                                AppUtil.currentAccount.email = email
                                FirebaseFirestore.getInstance().collection("salerAccount")
                                    .document(email).get().addOnSuccessListener {
                                    if (it.data?.get("isNew").toString() == "true") {
                                        loadDialog.dismissDialog()
                                        startActivity(
                                            Intent(
                                                baseContext,
                                                EditProfileActivity::class.java
                                            )
                                        )
                                        finish()
                                    } else {
                                        loadDialog.dismissDialog()
                                        startActivity(Intent(baseContext, MainActivity::class.java))
                                        val userMap = it.data?.get("user") as HashMap<*, *>
                                        val recentUser: User = User(
                                            fullName = userMap["fullName"].toString(),
                                            gender = userMap["gender"].toString(),
                                            birthDay = userMap["birthDay"].toString(),
                                            phoneNumber = userMap["phoneNumber"].toString(),
                                            addressLane = userMap["addressLane"].toString(),
                                            city = userMap["city"].toString(),
                                            district = userMap["district"].toString(),
                                        )
                                        AppUtil.currentUser = recentUser
                                        finish()
                                    }
                                }
                            } else {
                                loginPasswordLayout.error = task.exception!!.message.toString()
                                loginPassword.clearFocus()
                                loadDialog.dismissDialog()
                            }
                        }
                } else {
                    dbAccountsReference.get().addOnSuccessListener {
                        var isBuyer: Boolean = false
                        var name: String = ""
                        for (document in it) {
                            if (document.id == loginEmail.text.toString()) {
                                isBuyer = true
                                val mapOfUser: MutableMap<String, Any> =
                                    document.data["user"] as MutableMap<String, Any>
                                name = mapOfUser.get("fullName").toString()
                            }
                        }
                        if (isBuyer) {
                            val email = loginEmail.text.toString()
                            val password = loginPassword.text.toString()
                            val user = User(name)
                            val appAccount = AppAccount(email, "", user)
                            FirebaseFirestore.getInstance().collection("salerAccount")
                                .document(email).set(appAccount)
                            auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this) { task ->
                                    if (task.isSuccessful) {
                                        FullBookList.getInstance()
                                        AppUtil.currentAccount.email = email
                                        loadDialog.dismissDialog()
                                        startActivity(
                                            Intent(
                                                baseContext,
                                                EditProfileActivity::class.java
                                            )
                                        )
                                        finish()
                                    } else {
                                        loginPasswordLayout.error =
                                            task.exception!!.message.toString()
                                        loginPassword.clearFocus()
                                        loadDialog.dismissDialog()
                                    }
                                }
                        } else {
                            Toast.makeText(this, "Wrong email or password!", Toast.LENGTH_SHORT)
                                .show()
                            loadDialog.dismissDialog()
                        }
                    }
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