package com.example.admin_bookmarket.ViewModel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AddItemViewModel @Inject constructor(): ViewModel() {

    fun addtoDb(newBook: MutableMap<String, String>)
    {
        FirebaseFirestore.getInstance().collection("books").add(newBook)
    }
}