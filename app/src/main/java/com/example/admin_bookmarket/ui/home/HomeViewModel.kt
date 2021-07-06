package com.example.admin_bookmarket.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.admin_bookmarket.data.FullBookList
import com.example.admin_bookmarket.data.common.AppUtil
import com.example.admin_bookmarket.data.common.Constants
import com.example.admin_bookmarket.data.model.Book
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.ArrayList
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    private var _lstCurrentBook = MutableLiveData<MutableList<Book>>()
    val lstCurrentBook get() = _lstCurrentBook

    init {
        loadBook()
    }

    private fun loadBook()
    {
        FirebaseFirestore.getInstance().collection("books").addSnapshotListener(
            object: EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.w(Constants.VMTAG, "Listen failed.", error)
                        return
                    }
                    val bookList: MutableList<Book> = ArrayList()
                    for (doc in value!!) {
                        if (doc.data["Saler"].toString() == AppUtil.currentAccount.email ) {
                            var bookItem = Book(
                                doc.id,
                                doc.data["Image"].toString(),
                                doc.data["Name"].toString(),
                                doc.data["Author"].toString(),
                                doc.data["Price"].toString().toDouble().roundToInt(),
                                doc.data["rate"].toString().toDouble().roundToInt(),
                                doc.data["Kind"].toString(),
                                doc.data["Counts"].toString().toDouble().roundToInt(),
                                doc.data["ImageID"].toString(),
                                doc.data["Description"].toString(),
                                doc.data["Saler"].toString(),
                                doc.data["SalerName"].toString()
                            )
                            bookList.add(bookItem)
                        }
                    }
                    lstCurrentBook.value = bookList
                }

            }
        )
    }
}