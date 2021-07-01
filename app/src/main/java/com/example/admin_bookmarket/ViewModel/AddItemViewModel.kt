package com.example.admin_bookmarket.ViewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context
): ViewModel() {

    fun addtoDb(newBook: MutableMap<String, Any>)
    {
        FirebaseFirestore.getInstance().collection("books").add(newBook).addOnFailureListener {
            Toast.makeText(appContext,"Upload book false $it", Toast.LENGTH_SHORT).show()
        }
    }
}