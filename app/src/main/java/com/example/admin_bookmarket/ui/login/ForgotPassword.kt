package com.example.admin_bookmarket.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import com.example.admin_bookmarket.R
import com.example.admin_bookmarket.data.FullBookList
import com.example.admin_bookmarket.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {
    lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        FullBookList.getInstance()
        val imgBackArrow: AppCompatImageView = findViewById(R.id.imgBackArrow)
        imgBackArrow.setOnClickListener {
            onBackPressed()
        }
        submit()
    }
    override fun onBackPressed() {
        startActivity(Intent(baseContext, LoginActivity::class.java))
        finish()
    }

    private fun submit() {
        binding.submit.setOnClickListener {
            val email: String = binding.LoginEmail.text.toString().trim { it <= ' ' }
            if (email.isEmpty()) {
                binding.LoginEmailLayout.error = "Please enter email."
            } else {
                val loadDialog= LoadDialog(this)
                loadDialog.startLoading()
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener {
                    if (it.isSuccessful) {
                        loadDialog.dismissDialog()
                        Toast.makeText(
                            this,
                            "Request sent successfully, check your email to archive new password!",
                            Toast.LENGTH_LONG
                        ).show()
                        startActivity(Intent(baseContext, LoginActivity::class.java))
                        finish()
                    }else{
                        loadDialog.dismissDialog()
                        Toast.makeText(
                            this,
                            it.exception!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
}