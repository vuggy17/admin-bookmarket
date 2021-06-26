package com.example.admin_bookmarket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin_bookmarket.data.adapter.OrderAdapter
import com.example.admin_bookmarket.data.model.Cart
import com.example.admin_bookmarket.data.model.Order
import com.example.admin_bookmarket.data.model.UserDeliverAddress
import com.example.admin_bookmarket.databinding.FragmentOrdersBinding


class OrdersFragment : Fragment() {
    private lateinit var binding: FragmentOrdersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        val tempCart: Cart = Cart(
            name = "Sự im lặng của đàn bò",
            price = 20000,
            numbers = 3
        )
        val tempListCart: ArrayList<Cart> = ArrayList()
        tempListCart.add(tempCart)
        tempListCart.add(tempCart)
        tempListCart.add(tempCart)
        val tempList: MutableList<Order> = ArrayList()
        val temp: Order =Order(
            userDeliverAddress =UserDeliverAddress(
                fullName = "Phạm Minh Tân",
                phoneNumber = "0343027600",
                addressLane = "026/D",
                district = "Bến Lức",
                city = "Long An"
            ),
            dateTime = "20/12/2021",
            status = "CONFIRMED",
            totalPrince = "5360000",
            listbooks = tempListCart
        )
        tempList.add(temp)
        tempList.add(temp)
        tempList.add(temp)
        val orderListAdapter: OrderAdapter = OrderAdapter(tempList, this.requireContext())
        binding.ordersList.apply {
            adapter = orderListAdapter
            layoutManager = LinearLayoutManager(binding.root.context)
        }


        return binding.root
    }


}