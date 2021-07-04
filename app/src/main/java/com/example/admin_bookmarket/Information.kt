package com.example.admin_bookmarket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.admin_bookmarket.ViewModel.OrderViewModel
import com.example.admin_bookmarket.ViewModel.UserViewModel
import com.example.admin_bookmarket.data.common.AppUtil
import com.example.admin_bookmarket.data.common.AppUtils
import com.example.admin_bookmarket.data.model.AppAccount
import com.example.admin_bookmarket.data.model.User
import com.example.admin_bookmarket.databinding.FragmentInformationBinding
import com.example.admin_bookmarket.databinding.FragmentOrdersBinding
import com.example.admin_bookmarket.ui.login.LoginActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Information : Fragment() {
    private lateinit var binding:FragmentInformationBinding
    val viewModel: UserViewModel by activityViewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInformationBinding.inflate(inflater, container, false)
        setInfoView()
        binding.btnLogout.setOnClickListener {
            Firebase.auth.signOut()
            AppUtil.currentUser = User()
            AppUtil.currentAccount = AppAccount()
            startActivity(Intent(binding.root.context, LoginActivity::class.java))
            requireActivity().finish()
        }
        binding.btnEdit.setOnClickListener {
            startActivity(Intent(binding.root.context, EditProfileActivity::class.java))

        }
        binding.imgBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setInfoView(){
        binding.email.text = viewModel.getAccountInfo().email
        binding.userName.text = viewModel.getUserInfo().fullName
        binding.birthday.text = viewModel.getUserInfo().birthDay
        binding.addressLane.text = viewModel.getUserInfo().addressLane
        binding.gender.text = viewModel.getUserInfo().gender
        binding.phone.text = viewModel.getUserInfo().phoneNumber
        binding.city.text = viewModel.getUserInfo().district +", "+ viewModel.getUserInfo().city
        if(viewModel.getUserInfo().gender =="Male"){
            binding.avatar.setImageResource(R.drawable.ic_male)
        }else{
            binding.avatar.setImageResource(R.drawable.ic_female)
        }
    }
}