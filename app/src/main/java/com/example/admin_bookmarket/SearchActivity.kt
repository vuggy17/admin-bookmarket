package com.example.admin_bookmarket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.admin_bookmarket.ViewModel.SearchViewModel
import com.example.admin_bookmarket.data.FullBookList
import com.example.admin_bookmarket.data.common.Constants
import com.example.admin_bookmarket.data.model.Book
import com.example.admin_bookmarket.databinding.ActivitySearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var adapterSuggest: ArrayAdapter<String>
    private  var lstNameOfBook: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapterSuggest = ArrayAdapter(this, android.R.layout.simple_list_item_1, lstNameOfBook)

        binding.rcSuggestSearch.let {
            it.adapter = adapterSuggest
        }

        viewModel._lstBook.observe(this, changeObsever)

        binding.rcSuggestSearch.setOnItemClickListener(
            AdapterView.OnItemClickListener { parent, view, position, id ->
                for (book in FullBookList.getInstance().lstFullBook) {
                    if (book.Name == binding.rcSuggestSearch.getItemAtPosition(position)) {

                        putBookIntoIntent(book.id!!)
                    }
                }

            }
        )
        //searching
        binding.tbSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.tbSearchView.clearFocus()

                if (query == null)
                {
                    binding.rcSuggestSearch.visibility = View.INVISIBLE
                    return false
                }
                if(lstNameOfBook.contains(query))
                {
                    adapterSuggest.filter.filter(query)
                    binding.rcSuggestSearch.visibility = View.VISIBLE
                }
                else
                {
                    Toast.makeText(applicationContext,"Item not found !", Toast.LENGTH_LONG).show()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != "") {
                    adapterSuggest.filter.filter(newText)
                    binding.rcSuggestSearch.visibility = View.VISIBLE
                }
                else
                {
                    binding.rcSuggestSearch.visibility = View.INVISIBLE
                }
                return false
            }

        })
    }

    private val changeObsever = Observer<MutableList<Book>> { value -> value?.let {
        lstNameOfBook = mutableListOf()
        adapterSuggest.clear()
        for (i in 0 until  value.size)
        {
            lstNameOfBook.add(value[i].Name!!)
            adapterSuggest.add(value[i].Name!!)
        }
        var newString: String = binding.tbSearchView.query.toString()
        binding.tbSearchView.setQuery(binding.tbSearchView.query.toString() + "a", false)
        binding.tbSearchView.setQuery(newString, false)
    } }

    fun putBookIntoIntent(id:String)
    {
        val intent = Intent(binding.root.context, ItemDetailActivity::class.java)
        val bundle = Bundle()
        bundle.putString(Constants.ITEM, id)
        intent.putExtras(bundle)
        binding.root.context.startActivity(intent)
    }
}