package com.example.admin_bookmarket.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.admin_bookmarket.AddItemActivity
import com.example.admin_bookmarket.ItemDetailActivity
import com.example.admin_bookmarket.RecyclerViewClickListener
import com.example.admin_bookmarket.SearchActivity
import com.example.admin_bookmarket.data.common.Constants.ITEM
import com.example.admin_bookmarket.data.common.MarginItemDecoration
import com.example.admin_bookmarket.data.model.Book
import com.example.admin_bookmarket.databinding.FragmentHomeBinding
import com.example.admin_bookmarket.ui.Adapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), RecyclerViewClickListener {

    private val homeViewModel: HomeViewModel by activityViewModels()
    private var _binding: FragmentHomeBinding? = null
    private var isBackPressed = false
    private var lstBook: MutableList<Book> = mutableListOf()
    private lateinit var bookAdapter : Adapter
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    //private val model: SharedViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val homeRc = binding.hList
        bookAdapter = Adapter(lstBook, this )
        homeRc.apply {
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            addItemDecoration(MarginItemDecoration(spaceSize = 15, spanCount = 2))
            adapter = bookAdapter
        }
        binding.option.setOnClickListener {
            startActivity(Intent(context, AddItemActivity::class.java))
        }
        homeViewModel.lstCurrentBook.observe(viewLifecycleOwner, changeObserver)

        binding.htbHomeToolBar.edtSearchText.setOnClickListener{
            startActivity(Intent(context, SearchActivity::class.java))
        }
        setUpMessage()
        binding.addNewBook.setOnClickListener {
            startActivity(Intent(context, AddItemActivity::class.java))
        }
        return binding.root
    }
    private fun setUpMessage(){
        if(lstBook.size == 0){
            binding.noItemToShow.visibility = View.VISIBLE
        }else{
            binding.noItemToShow.visibility = View.GONE
        }
    }

    private val changeObserver = Observer<MutableList<Book>> { value ->
        value?.let {
            lstBook = value
            bookAdapter.onChange(value)
            bookAdapter.notifyDataSetChanged()
        }
        setUpMessage()
    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//
////        HomeFragmentDirections.actionNavHomeToNavDetail()
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        isBackPressed = false
    }

    override fun recyclerViewListClicked(v: View?, id: String) {
//        val action = HomeFragmentDirections.actionNavHomeToNavDetail().apply {
//            bookId = position
//            shouldIRenderItemValue = true
//        }
//
//        v?.findNavController()?.navigate(action)
        val intent = Intent(binding.root.context, ItemDetailActivity::class.java)
        val bundle = Bundle()
        bundle.putString(ITEM, id)
        intent.putExtras(bundle)
        binding.root.context.startActivity(intent)
    }

    public fun onBackPressed() {
        isBackPressed = true
    }


}