package com.example.admin_bookmarket.ui.AddNewItem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.admin_bookmarket.R
import com.example.admin_bookmarket.SharedViewModel
import com.example.admin_bookmarket.databinding.FragmentDetailBinding
import com.example.admin_bookmarket.ui.detail.DetailFragmentArgs

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AddNewItem : Fragment() {
    // TODO: Rename and change types of parameters
    private var _binding: FragmentDetailBinding? = null // fix this


    private val model: SharedViewModel by activityViewModels()
    private val args: DetailFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_new_item, container, false)
    }

}