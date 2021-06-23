package com.example.admin_bookmarket.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.admin_bookmarket.data.FullBookList
import com.example.admin_bookmarket.data.common.Constants
import com.example.admin_bookmarket.data.common.Constants.ITEM
import com.example.admin_bookmarket.data.model.Book
import com.google.firebase.firestore.*
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.ArrayList
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class ItemDetailViewModel @Inject constructor( private val savedStateHandle: SavedStateHandle): ViewModel() {

    private var _book = MutableLiveData<Book>()
    val book get() = _book

    init {
        loadBook()
    }

    private fun loadBook()
    {
        val id = savedStateHandle.getLiveData<String>(ITEM)
        FirebaseFirestore.getInstance().collection("books").document(id.value!!).addSnapshotListener(object : EventListener<DocumentSnapshot> {
            override fun onEvent(value: DocumentSnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.w(Constants.VMTAG, "Listen failed.", error)
                    return
                }

                var bookItem: Book = Book(id.value,
                    value?.data?.get("Image").toString(),
                    value?.data?.get("Name").toString(),
                    value?.data?.get("Author").toString(),
                    value?.data?.get("Price").toString().toDouble().roundToInt(),
                    value?.data?.get("rate").toString().toDouble().roundToInt(),
                    value?.data?.get("Kind").toString(),
                    value?.data?.get("Counts").toString().toDouble().roundToInt(),
                    value?.data?.get("Description").toString())
                book.value = bookItem
            }
        })
    }
    fun updateToDb(id : String, newBook: Book)
    {
        FirebaseFirestore.getInstance().collection("books").document(id).update("Image", newBook.Image)
        FirebaseFirestore.getInstance().collection("books").document(id).update("Name", newBook.Name)
        FirebaseFirestore.getInstance().collection("books").document(id).update("Author", newBook.Author)
        FirebaseFirestore.getInstance().collection("books").document(id).update("Price", newBook.Price)
        FirebaseFirestore.getInstance().collection("books").document(id).update("rate", newBook.rate)
        FirebaseFirestore.getInstance().collection("books").document(id).update("Kind", newBook.Kind)
        FirebaseFirestore.getInstance().collection("books").document(id).update("Counts", newBook.Counts)
        FirebaseFirestore.getInstance().collection("books").document(id).update("Description", newBook.Description)

    }

}