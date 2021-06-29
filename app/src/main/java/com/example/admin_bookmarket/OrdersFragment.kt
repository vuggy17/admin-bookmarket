package com.example.admin_bookmarket

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin_bookmarket.ViewModel.OrderViewModel
import com.example.admin_bookmarket.data.adapter.OrderAdapter
import com.example.admin_bookmarket.data.common.AppUtils
import com.example.admin_bookmarket.data.model.Order
import com.example.admin_bookmarket.databinding.FragmentOrdersBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class OrdersFragment : Fragment(), RecyclerViewClickListener {
    private lateinit var binding: FragmentOrdersBinding
    private val viewModel: OrderViewModel by activityViewModels()
    private lateinit var orderListAdapter: OrderAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    private var showList: MutableList<Order> = ArrayList()
    private var listStatus: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        orderListAdapter =
            OrderAdapter(mutableListOf(), this.requireContext(), this, viewModel)
        getOrders(orderListAdapter)
        binding.ordersList.apply {
            adapter = orderListAdapter
            layoutManager = LinearLayoutManager(binding.root.context)
        }
        (activity as AppCompatActivity).setSupportActionBar(binding.toolBar)
        setHasOptionsMenu(true)

        return binding.root
    }

    private fun getOrders(orderAdapter: OrderAdapter) {
        viewModel.getAllOrder().observe(this.viewLifecycleOwner, { changes ->
            showList = changes
            if (listStatus != "") {
                orderAdapter.addOrder(filterList(showList, listStatus))
            } else {
                orderAdapter.addOrder(showList)
            }

        })
    }

    private fun filterList(orderList: MutableList<Order>, condition: String): MutableList<Order> {
        val filterOrderList: MutableList<Order> = ArrayList()
        for (order in orderList) {
            if (order.status == condition) {
                filterOrderList.add(order)
            }
        }
        return filterOrderList

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
        //super.onCreateOptionsMenu(menu, inflater)
    }

    override fun recyclerViewListClicked(v: View?, id: String) {
        Toast.makeText(
            this.requireContext(),
            AppUtils.currentOrder.id.toString(),
            Toast.LENGTH_LONG
        ).show()
        startActivity(Intent(binding.root.context, OrderDetail::class.java))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.waiting -> listStatus ="WAITING"
            R.id.confirmed ->listStatus="CONFIRMED"
            R.id.delivering -> listStatus ="DELIVERING"
            R.id.complete -> listStatus ="COMPLETE"
            R.id.cancel ->listStatus ="CANCEL"
        }
        if(listStatus!=""){
            orderListAdapter.addOrder(filterList(showList, listStatus))
        }
        return super.onOptionsItemSelected(item)
    }


}