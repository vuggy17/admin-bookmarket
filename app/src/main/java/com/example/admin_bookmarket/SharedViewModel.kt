package com.example.admin_bookmarket

import androidx.lifecycle.ViewModel
import com.example.admin_bookmarket.model.BookDetailItem
import java.util.*

class SharedViewModel : ViewModel() {

    private val COUNT = 25
    val ITEMS: MutableList<BookDetailItem> = ArrayList()

    public fun getDataList(): MutableList<BookDetailItem> {
        return ITEMS
    }

    public fun deleteItem(item: BookDetailItem) {
        with(ITEMS) {
            removeAt(this.indexOf(item))
        }
    }

    public fun addItem(item: BookDetailItem): Boolean {

        // TODO: 05/06/2021 nếu tồn tại thì báo lỗi, chưa tồn tại thì thêm mới và trả vê success
        if (item.name == "") return false
        val filteredItem = ITEMS.find { it.name == item.name }
        return if (filteredItem == null) {
            item.id = ITEMS.size + 1
            ITEMS.add(item)
            true
        } else false
    }

    public fun updateItem(item: BookDetailItem) {
        val filteredItem = ITEMS.find { it.id == item.id }
        with(ITEMS) {
            this[indexOf(filteredItem)] = item
        }

    }


    //create temp data
    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            ITEMS.add(createBookItem(i))
        }
    }

    private fun createBookItem(id: Int): BookDetailItem {
        return BookDetailItem(
            id,
            "Item $id",
            (id * 12),
            "science, romance",
            "science",
            "4",
            "12",
            "More details information here"
        )
    }
    /// end==============================
}