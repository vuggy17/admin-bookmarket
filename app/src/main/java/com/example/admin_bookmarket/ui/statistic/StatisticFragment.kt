package com.example.admin_bookmarket.ui.statistic

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.admin_bookmarket.SharedViewModel
import com.example.admin_bookmarket.databinding.FragmentStatisticBinding
import kotlin.properties.Delegates

class StatisticFragment : Fragment() {

    private var _binding:FragmentStatisticBinding? = null
    private val model: SharedViewModel by activityViewModels()
    private val binding get() = _binding!!

    private var totalBook:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       _binding = FragmentStatisticBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        totalBook = model.ITEMS.size
    binding.sTotalBook.text = "You have $totalBook books \n Total user is 100 user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


}