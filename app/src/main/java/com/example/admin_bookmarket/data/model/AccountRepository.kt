package com.example.admin_bookmarket.data.model

import com.example.admin_bookmarket.data.common.AppUtil
import com.example.admin_bookmarket.data.common.Constants
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject
import javax.inject.Named

class AccountRepository@Inject constructor(
    @Named(Constants.USERS_REF) private val accountColRef: CollectionReference
) {
    var account = AppAccount()

    fun loadData() {
        account = AppUtil.currentAccount
    }

}