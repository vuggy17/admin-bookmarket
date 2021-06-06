package com.example.admin_bookmarket.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin_bookmarket.RecyclerViewClickListener
import com.example.admin_bookmarket.SharedViewModel
import com.example.admin_bookmarket.databinding.FragmentHomeBinding
import com.example.admin_bookmarket.ui.Adapter

class HomeFragment : Fragment(), RecyclerViewClickListener {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private var isBackPressed = false

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val model: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val homeRc = binding.hList
        homeRc.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = Adapter(model.ITEMS, this@HomeFragment)

        }
        return binding.root
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

    override fun recyclerViewListClicked(v: View?, position: Int) {
        val action = HomeFragmentDirections.actionNavHomeToNavDetail().apply {
            bookId = position
            shouldIRenderItemValue = true
        }

        v?.findNavController()?.navigate(action)
    }

    public fun onBackPressed() {
        isBackPressed = true
    }


}