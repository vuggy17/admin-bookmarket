package com.example.admin_bookmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.example.admin_bookmarket.data.common.AppUtils
import com.example.admin_bookmarket.data.model.Order

import com.example.admin_bookmarket.databinding.ActivityStatisticBinding

class StatisticActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStatisticBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpPieChart()
        setUpShowData()
        binding.backClick.setOnClickListener {
            onBackPressed()
        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
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
    private fun setUpShowData(){
        val listOrder: MutableList<Order> = AppUtils.oderList
        binding.apply {
            allOrders.text = listOrder.size.toString()
            waitingOrder.text = filterList(AppUtils.oderList,"WAITING").size.toString()
            confirmedOrders.text = filterList(AppUtils.oderList,"CONFIRMED").size.toString()
            deliveringOrders.text = filterList(AppUtils.oderList,"DELIVERING").size.toString()
            completeOrders.text = filterList(AppUtils.oderList,"COMPLETE").size.toString()
            cancelOrders.text = filterList(AppUtils.oderList,"CANCEL").size.toString()
        }
    }
    private fun setUpPieChart() {
        var pie: Pie = AnyChart.pie()
        var dataEntries: ArrayList<DataEntry> = ArrayList()
        dataEntries.add(ValueDataEntry("Waiting", filterList(AppUtils.oderList,"WAITING").size))
        dataEntries.add(ValueDataEntry("Confirmed", filterList(AppUtils.oderList,"CONFIRMED").size))
        dataEntries.add(ValueDataEntry("Delivering", filterList(AppUtils.oderList,"DELIVERING").size))
        dataEntries.add(ValueDataEntry("Complete", filterList(AppUtils.oderList,"COMPLETE").size))
        dataEntries.add(ValueDataEntry("Cancel", filterList(AppUtils.oderList,"CANCEL").size))
        pie.data(dataEntries)
        binding.anyChart.setChart(pie)

    }
}