package com.example.admin_bookmarket

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.viewModels
import com.example.admin_bookmarket.ViewModel.UserViewModel
import com.example.admin_bookmarket.data.common.AppUtil
import com.example.admin_bookmarket.data.model.User
import com.example.admin_bookmarket.databinding.ActivityEditProfileBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import java.util.regex.Pattern

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {
    val viewModel: UserViewModel by viewModels()
    lateinit var binding: ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        genderSetUp()
        setData()
        setBirthDateSelector()
        setSaveButtonCommand()
//        setBackButton()
    }

//    private fun setBackButton(){
//        binding.imgBack.setOnClickListener {
//            onBackPressed()
//        }
//    }
    private fun setBirthDateSelector(){
        binding.edtBirthday.setOnClickListener {
            pickDateSetting()
        }
    }

    private fun setSaveButtonCommand(){
        binding.btnSaveProfile.setOnClickListener {
            if(isValidName() && isValidPhoneNumber()){
                var user: User = User(
                    fullName = binding.edtName.text.toString(),
                    gender = binding.gender.text.toString(),
                    birthDay = binding.edtBirthday.text.toString(),
                    phoneNumber = binding.edtPhoneNumber.text.toString(),
                    addressLane = binding.edtAddressLane.text.toString(),
                    city = binding.edtCity.text.toString(),
                    district = binding.edtDistrict.text.toString()
                )
                viewModel.updateUserInfo(user)
                val updates = mutableMapOf<String, Any>(
                    "isNew" to FieldValue.delete()
                )
                FirebaseFirestore.getInstance().collection("salerAccount").document(AppUtil.currentAccount.email).update(updates)
                Toast.makeText(this,"Saved", Toast.LENGTH_SHORT).show()
                startActivity(Intent(baseContext, ProfileActivity::class.java))
                finish()
            }
        }
    }

    private fun isValidName(): Boolean {
        return if (binding.edtName.text.isEmpty()) {
            binding.edtName.error = "Name can not empty"
            false
        } else {
            return if (isNameContainNumberOrSpecialCharacter(binding.edtName.text.toString())) {
                binding.edtName.error = "Name can not contain number of special character"
                false
            } else {
                binding.edtName.error = null
                true
            }
        }
    }
    private fun isNameContainNumberOrSpecialCharacter(name: String): Boolean {
        var hasNumber: Boolean = Pattern.compile(
            "[0-9]"
        ).matcher(name).find()
        var hasSpecialCharacter: Boolean = Pattern.compile(
            "[!@#$%&.,\"':;?*()_+=|<>?{}\\[\\]~-]"
        ).matcher(name).find()
        return hasNumber || hasSpecialCharacter
    }
    private fun isValidPhoneNumber():Boolean{
        var result = false
        if(binding.edtPhoneNumber.text.isNotEmpty()){
            result = Pattern.compile(
                "^[+]?[0-9]{10,13}\$"
            ).matcher(binding.edtPhoneNumber.text).find()
            if(!result){
                binding.edtPhoneNumber.error ="Please enter right phone number!"
            }
        }

        return result
    }
//    override fun onBackPressed() {
//        super.onBackPressed()
//        finish()
//    }
    private fun genderSetUp(){
        val adapter = ArrayAdapter(
            this,
            R.layout.gender_item,
            resources.getStringArray(R.array.gender)
        )
        binding.gender.setAdapter(adapter)
    }
    private fun setData(){
        binding.edtName.setText( viewModel.getUserInfo().fullName)
        binding.edtBirthday.setText(viewModel.getUserInfo().birthDay)
        binding.edtAddressLane.setText(viewModel.getUserInfo().addressLane)
        binding.edtDistrict.setText(viewModel.getUserInfo().district)
        binding.edtCity.setText(viewModel.getUserInfo().city)
        binding.edtPhoneNumber.setText(viewModel.getUserInfo().phoneNumber)
        binding.gender.setText(viewModel.getUserInfo().gender, false)

    }
    private fun pickDateSetting(){
        val c: Calendar = Calendar.getInstance()
        val day: Int = c.get(Calendar.DAY_OF_MONTH)
        val month: Int = c.get(Calendar.MONTH)
        val year: Int = c.get(Calendar.YEAR)

        val dpd: DatePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _: DatePicker, mYear: Int, mMonth: Int, mDay: Int ->

                binding.edtBirthday.setText("$mDay/${mMonth + 1}/$mYear")
            },
            year,
            month,
            day
        )
        dpd.show()
    }
}