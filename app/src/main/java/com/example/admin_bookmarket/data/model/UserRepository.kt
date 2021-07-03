package com.example.admin_bookmarket.data.model

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.admin_bookmarket.data.common.AppUtil
import com.example.admin_bookmarket.data.common.Constants
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.lang.Thread.sleep
import javax.inject.Inject
import javax.inject.Named

class UserRepository @Inject constructor(
    @Named(Constants.USERS_REF) private val userCollRef: CollectionReference
) {
    var user: User = User()

    fun loadData() {
       user = AppUtil.currentUser
    }
    fun updateUserData(user: User){
       AppUtil.currentAccount.user = user
        AppUtil.currentUser = user
        FirebaseFirestore.getInstance().collection("accounts").document(AppUtil.currentAccount.email).set(AppUtil.currentAccount)
        FirebaseFirestore.getInstance().collection("salerAccount").document(AppUtil.currentAccount.email).set(AppUtil.currentAccount)
    }
}

