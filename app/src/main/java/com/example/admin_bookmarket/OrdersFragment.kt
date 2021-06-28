package com.example.admin_bookmarket

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin_bookmarket.ViewModel.OrderViewModel
import com.example.admin_bookmarket.data.adapter.OrderAdapter
import com.example.admin_bookmarket.data.common.AppUtils
import com.example.admin_bookmarket.data.model.*
import com.example.admin_bookmarket.databinding.FragmentOrdersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : Fragment(), RecyclerViewClickListener {
    private lateinit var binding: FragmentOrdersBinding
    private val viewModel: OrderViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        val orderListAdapter: OrderAdapter = OrderAdapter(mutableListOf(), this.requireContext(), this)
        getOrders(orderListAdapter)
        binding.ordersList.apply {
            adapter = orderListAdapter
            layoutManager = LinearLayoutManager(binding.root.context)

        }
        return binding.root
    }
    private fun getOrders(orderAdapter: OrderAdapter) {
        viewModel.getAllOrder().observe(this.viewLifecycleOwner, { changes ->
            orderAdapter.addOrder(changes)
        })
    }

    override fun recyclerViewListClicked(v: View?, id: String) {
        Toast.makeText(this.requireContext(), AppUtils.currentOrder.id.toString(),Toast.LENGTH_LONG).show()
        startActivity(Intent(binding.root.context, OrderDetail::class.java))
    }


}