package com.example.admin_bookmarket.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.admin_bookmarket.data.model.AccountRepository
import com.example.admin_bookmarket.data.model.AppAccount
import com.example.admin_bookmarket.data.model.User
import com.example.admin_bookmarket.data.model.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.reflect.KProperty

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {

    fun getUserInfo(): User
    {
        userRepository.loadData()
        return userRepository.user
    }
    fun getAccountInfo(): AppAccount {
        accountRepository.loadData()
        return  accountRepository.account
    }
    fun updateUserInfo(user: User){
        userRepository.updateUserData(user)
    }


}