package com.example.admin_bookmarket.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.admin_bookmarket.data.common.Constants
import com.example.admin_bookmarket.data.model.Book
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class SearchViewModel @Inject constructor(): ViewModel() {

    private var _lstBook = MutableLiveData<MutableList<Book>>()
    val lstBook get() = _lstBook

    init {
        loadData()
    }

    private fun loadData()
    {
        FirebaseFirestore.getInstance().collection("books").addSnapshotListener(object: EventListener<QuerySnapshot>
        {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.w(Constants.VMTAG, "Listen failed.", error)
                    return
                }

                if (value != null) {
                    var lstAllBook: MutableList<Book> = mutableListOf()
                    for(doc in value) {
                        var bookItem: Book = Book(doc.id,
                            doc.data["Image"].toString(),
                            doc.data["Name"].toString(),
                            doc.data["Author"].toString(),
                            doc.data["Price"].toString().toDouble().roundToInt(),
                            doc.data["rate"].toString().toDouble().roundToInt(),
                            doc.data["Kind"].toString(),
                            doc.data["Counts"].toString().toDouble().roundToInt(),
                            doc.data["Description"].toString())
                        lstAllBook.add(bookItem)
                    }
                    lstBook.value = lstAllBook
                }
            }

        })
    }
}