package com.example.admin_bookmarket

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.admin_bookmarket.data.adapter.OrderAdapter
import com.example.admin_bookmarket.data.common.AppUtils
import com.example.admin_bookmarket.data.model.Cart
import com.example.admin_bookmarket.data.model.MyUser
import com.example.admin_bookmarket.data.model.Order
import com.example.admin_bookmarket.data.model.UserDeliverAddress
import com.example.admin_bookmarket.databinding.FragmentOrdersBinding


class OrdersFragment : Fragment(), RecyclerViewClickListener {
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
            numbers = 3,
            id = "0955dasd5564adawdas"
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
            id = "PMT5464646546PLKSN",
            dateTime = "20/12/2021",
            status = "WAITING",
            totalPrince = "5360000",
            listbooks = tempListCart,
            currentUser = MyUser(fullName = "Phạm Minh Tân", phoneNumber = "0343027600", email = "tan.lk16.cla@gmail.com")
        )
        val temp2: Order =Order(
            userDeliverAddress =UserDeliverAddress(
                fullName = "Phạm Minh Tân",
                phoneNumber = "0343027600",
                addressLane = "026/D",
                district = "Bến Lức",
                city = "Long An"
            ),
            id = "PMT5464646546PLKSN",
            dateTime = "20/12/2021",
            status = "WAITING",
            totalPrince = "5360000",
            listbooks = tempListCart,
            currentUser = MyUser(fullName = "Phạm Minh Tân", phoneNumber = "0343027600", email = "tan.lk16.cla@gmail.com")
        )
        tempList.add(temp)
        tempList.add(temp2)
        tempList.add(temp)
        val orderListAdapter: OrderAdapter = OrderAdapter(tempList, this.requireContext(), this )
        binding.ordersList.apply {
            adapter = orderListAdapter
            layoutManager = LinearLayoutManager(binding.root.context)
        }

        return binding.root
    }

    override fun recyclerViewListClicked(v: View?, id: String) {
        Toast.makeText(this.requireContext(), AppUtils.currentOrder.id.toString(),Toast.LENGTH_LONG).show()
        startActivity(Intent(binding.root.context, OrderDetail::class.java))
    }


}