package com.example.admin_bookmarket.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.admin_bookmarket.R
import com.example.admin_bookmarket.SharedViewModel
import com.example.admin_bookmarket.databinding.FragmentDetailBinding
import com.example.admin_bookmarket.model.BookDetailItem

class DetailFragment : Fragment() {

    private lateinit var detailViewModel: DetailViewModel
    private var _binding: FragmentDetailBinding? = null
    private val model: SharedViewModel by activityViewModels()
    private val args: DetailFragmentArgs by navArgs()
    private lateinit var itemDetail: BookDetailItem
    var itemId: Int = -1

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailViewModel =
            ViewModelProvider(this).get(DetailViewModel::class.java)

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = binding.dName
        val price = binding.dPrice
        val tag = binding.dTag
        val rate = binding.dRatePoint
        val category = binding.dCategory
        val quantity = binding.dQuantity
        val description = binding.dDescription


        if (args.shouldIRenderItemValue) {
            itemDetail = model.ITEMS.find { it.id == args.bookId }!!

            with(itemDetail) {
                name.setText(this.name)
                price.setText(this.price.toString())
                tag.setText(this.tag)
                rate.setText(this.ratePoint)
                category.setText(this.category)
                quantity.setText(this.quantity)
                description.setText(this.description)
            }

            // update item
            binding.dFab.setOnClickListener {
                //create item to save
                var newItem = getValueFromScren()
                newItem.id = itemDetail.id

                model.updateItem(newItem)
                Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show()

            }
        } else {
            binding.dFab.setOnClickListener {
                var newItem = getValueFromScren()

                val r = if (model.addItem(newItem)) {
                    val action = DetailFragmentDirections.actionNavDetailToNavHome()
                    view.findNavController().navigate(action)
                    "success"
                } else "false"

                Toast.makeText(context, "Add $r", Toast.LENGTH_SHORT)
                    .show()

            }
        }
    }


    private fun getValueFromScren(): BookDetailItem {
        val name = binding.dName
        val price = binding.dPrice
        val tag = binding.dTag
        val rate = binding.dRatePoint
        val category = binding.dCategory
        val quantity = binding.dQuantity
        val description = binding.dDescription
        var iPrice = price.text.toString().toIntOrNull()

        //input validation here
        // TODO: 05/06/2021 need input validation
        if (iPrice == null || iPrice < 1) {
            iPrice = 1
        }
        return BookDetailItem().apply {
            this.name = name.text.toString()
            this.price = iPrice
            this.tag = tag.text.toString()
            this.ratePoint = rate.text.toString()
            this.category = category.text.toString()
            this.quantity = quantity.text.toString()
            this.description = description.text.toString()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (args.shouldIRenderItemValue)
            inflater.inflate(R.menu.detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        return if (menuItem.itemId == R.id.d_delete) {
            model.deleteItem(itemDetail)
            Toast.makeText(context, "Delete success", Toast.LENGTH_SHORT).show()
            val action = DetailFragmentDirections.actionNavDetailToNavHome()
            this.view?.findNavController()?.navigate(action)
            true
        } else
            super.onOptionsItemSelected(menuItem)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}