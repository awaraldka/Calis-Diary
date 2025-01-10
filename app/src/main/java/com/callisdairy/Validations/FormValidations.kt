package com.callisdairy.Validations

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.isVisible
import com.callisdairy.R
import com.callisdairy.UI.Activities.AddPetProfileActivity
import de.hdodenhof.circleimageview.CircleImageView

object FormValidations : Activity() {

    val emailPattern =
        "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

    var MobilePattern = "[0-9]{10}"
    val Password = " "
    val Name = "^[A-Za-z]+$"
    val UserName = "^(?=[a-zA-Z0-9._]{8,20}\$)(?!.*[_.]{2})[^_.].*[^_.]\$"
    var any_Number = "[0-9]"
    var link =
        "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})"
    var otp_regex = "^[0-9]{6}\$"


    fun loginByPhone(
        mobileNumber: EditText,
        mobileTV: TextView,
        mobileLL: LinearLayout,
        password: EditText,
        passwordTV: TextView,
        passwordLL: LinearLayout,
        context: Context
    ) {
        val Mobile = mobileNumber.text.toString()
        val Pass = password.text.toString()

        mobileTV.visibility = View.GONE
        passwordTV.visibility = View.GONE
        mobileTV.text = ""
        passwordTV.text = ""
        mobileLL.setBackgroundResource(R.drawable.white_border_background)
        passwordLL.setBackgroundResource(R.drawable.white_border_background)

        if (Mobile.isEmpty()) {
            mobileTV.visibility = View.VISIBLE
            mobileTV.text = context.getString(R.string.enter_phone)
            mobileTV.setTextColor(Color.parseColor("#C63636"))
            mobileLL.setBackgroundResource(R.drawable.errordrawable)

        } else if (Mobile.length < 10) {
            mobileTV.visibility = View.VISIBLE
            mobileTV.text = context.getString(R.string.enter_valid_phone)
            mobileTV.setTextColor(Color.parseColor("#C63636"))
            mobileLL.setBackgroundResource(R.drawable.errordrawable)

        } else if (Pass.isEmpty()) {
            passwordTV.visibility = View.VISIBLE
            passwordTV.text = context.getString(R.string.enter_password)
            passwordLL.setBackgroundResource(R.drawable.errordrawable)

        } else if (Pass.length < 6) {
            passwordTV.visibility = View.VISIBLE
            passwordTV.text = context.getString(R.string.validPassword)
            passwordLL.setBackgroundResource(R.drawable.errordrawable)

        } else {
            mobileTV.visibility = View.GONE
            passwordTV.visibility = View.GONE
            mobileTV.text = ""
            passwordTV.text = ""
            mobileLL.setBackgroundResource(R.drawable.white_border_background)
            passwordLL.setBackgroundResource(R.drawable.white_border_background)
        }
    }


    fun loginByMail(
        email: EditText,
        emailTv: TextView,
        emailLL: LinearLayout,
        password: EditText,
        passwordTV: TextView,
        passwordLL: LinearLayout,
        context: Context
    ) {

        val email = email.text.toString()
        val Pass = password.text.toString()

        emailTv.visibility = View.GONE
        passwordTV.visibility = View.GONE
        emailTv.text = ""
        passwordTV.text = ""
        emailLL.setBackgroundResource(R.drawable.white_border_background)
        passwordLL.setBackgroundResource(R.drawable.white_border_background)

        if (email.isEmpty()) {
            emailTv.visibility = View.VISIBLE
            emailTv.text = context.getString(R.string.email_enter)
            emailTv.setTextColor(Color.parseColor("#C63636"))
            emailLL.setBackgroundResource(R.drawable.errordrawable)

        } else if (!email.matches(Regex(emailPattern))) {
            emailTv.visibility = View.VISIBLE
            emailTv.text = context.getString(R.string.email_valid)
            emailTv.setTextColor(Color.parseColor("#C63636"))
            emailLL.setBackgroundResource(R.drawable.errordrawable)

        } else if (Pass.isEmpty()) {
            passwordTV.visibility = View.VISIBLE
            passwordTV.text = context.getString(R.string.enter_password)
            passwordLL.setBackgroundResource(R.drawable.errordrawable)

        } else if (Pass.length < 6) {
            passwordTV.visibility = View.VISIBLE
            passwordTV.text = context.getString(R.string.validPassword)
            passwordLL.setBackgroundResource(R.drawable.errordrawable)

        } else {
            emailTv.visibility = View.GONE
            passwordTV.visibility = View.GONE
            emailTv.text = ""
            passwordTV.text = ""
            emailLL.setBackgroundResource(R.drawable.white_border_background)
            passwordLL.setBackgroundResource(R.drawable.white_border_background)
        }
    }

    fun signUp(
        etFirstName: EditText,
        tvFirstName: TextView,
        firstNameLL: LinearLayout,
        etLastName: EditText,
        tvLastName: TextView,
        lastNameLL: LinearLayout,
        etMail: EditText,
        tvEmail: TextView,
        llEmail: LinearLayout,
        etMobileNumber: EditText,
        tvMobileNumber: TextView,
        llMobileNumber: LinearLayout,
        genderSpinner: Spinner,
        tvGender: TextView,
        llGender: LinearLayout,
        etAddress: EditText,
        tvAddress: TextView,
        llAddress: LinearLayout,
        etCity: TextView,
        tvCity: TextView,
        llCity: LinearLayout,
        etState: TextView,
        tvState: TextView,
        stateLL: LinearLayout,
        etZipCode: EditText,
        tvZip: TextView,
        llZipCode: LinearLayout,
        etCountry: TextView,
        tvCountry: TextView,
        llCountry: LinearLayout,
        etPassword: EditText,
        tvPassword: TextView,
        llPassword: LinearLayout,
        registerTandC: CheckBox,
        tvTerms: TextView,
        etUserName: EditText,
        tvUserName: TextView,
        llUserName: LinearLayout,
        petCategorySpinner: TextView,
        tvPetType: TextView,
        llPetType: LinearLayout,
        etPetType: TextView,
        llPetCategory: LinearLayout,
        tvPetCategory: TextView,
        context: Activity,
        petPic: String,
        imgPetProfile: CircleImageView,
        profilepic: String,
        userProfile: CircleImageView,
        llPetBreed: LinearLayout,
        etPetBreed: TextView,
        tvPetBreed: TextView,
        txtDateOfBirth: TextView,
        llDateOfBirth: LinearLayout,
        tvDateOfBirth: TextView
    ):Boolean {

        firstNameLL.setBackgroundResource(R.drawable.white_border_background)
        lastNameLL.setBackgroundResource(R.drawable.white_border_background)
        llEmail.setBackgroundResource(R.drawable.white_border_background)
        llMobileNumber.setBackgroundResource(R.drawable.white_border_background)
        llGender.setBackgroundResource(R.drawable.white_border_background)
        llAddress.setBackgroundResource(R.drawable.white_border_background)
        llCity.setBackgroundResource(R.drawable.white_border_background)
        stateLL.setBackgroundResource(R.drawable.white_border_background)
        llZipCode.setBackgroundResource(R.drawable.white_border_background)
        llCountry.setBackgroundResource(R.drawable.white_border_background)
        llPassword.setBackgroundResource(R.drawable.white_border_background)
        llUserName.setBackgroundResource(R.drawable.white_border_background)
        llPetType.setBackgroundResource(R.drawable.white_border_background)
        llPetCategory.setBackgroundResource(R.drawable.white_border_background)
        llPetBreed.setBackgroundResource(R.drawable.white_border_background)
        llDateOfBirth.setBackgroundResource(R.drawable.white_border_background)
        imgPetProfile.borderColor = Color.parseColor("#6FCFB9")
        userProfile.borderColor = Color.parseColor("#6FCFB9")

        tvFirstName.visibility = View.GONE
        tvLastName.visibility = View.GONE
        tvEmail.visibility = View.GONE
        tvMobileNumber.visibility = View.GONE
        tvGender.visibility = View.GONE
        tvAddress.visibility = View.GONE
        tvCity.visibility = View.GONE
        tvState.visibility = View.GONE
        tvZip.visibility = View.GONE
        tvCountry.visibility = View.GONE
        tvPassword.visibility = View.GONE
        tvTerms.visibility = View.GONE
        tvUserName.visibility = View.GONE
        tvPetType.visibility = View.GONE
        tvPetCategory.visibility = View.GONE
        tvDateOfBirth.visibility = View.GONE
        tvPetBreed.visibility = View.GONE

        tvFirstName.text = ""
        tvLastName.text = ""
        tvEmail.text = ""
        tvMobileNumber.text = ""
        tvGender.text = ""
        tvAddress.text = ""
        tvCity.text = ""
        tvState.text = ""
        tvZip.text = ""
        tvCountry.text = ""
        tvPassword.text = ""
        tvTerms.text = ""
        tvUserName.text = ""
        tvPetType.text = ""
        tvPetCategory.text = ""
        tvPetBreed.text = ""
        tvDateOfBirth.text = ""


        if (etFirstName.text.isEmpty()) {
            tvFirstName.visibility = View.VISIBLE
            tvFirstName.text = context.getString(R.string.first_name_enter)
            tvFirstName.setTextColor(Color.parseColor("#C63636"))
            firstNameLL.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (etFirstName.text.length < 3) {
            tvFirstName.visibility = View.VISIBLE
            tvFirstName.text = context.getString(R.string.valid_first_name_enter)
            tvFirstName.setTextColor(Color.parseColor("#C63636"))
            firstNameLL.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (etLastName.text.isEmpty()) {
            tvLastName.visibility = View.VISIBLE
            tvLastName.text = context.getString(R.string.last_name_enter)
            tvLastName.setTextColor(Color.parseColor("#C63636"))
            lastNameLL.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (etLastName.text.length < 2) {
            tvLastName.visibility = View.VISIBLE
            tvLastName.text = context.getString(R.string.valid_last_name_enter)
            tvLastName.setTextColor(Color.parseColor("#C63636"))
            lastNameLL.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (petPic.isEmpty()) {
            imgPetProfile.borderColor = Color.parseColor("#C63636")
            return false
        } else if (profilepic.isEmpty()) {
            userProfile.borderColor = Color.parseColor("#C63636")
            return false
        } else if (etUserName.text.isEmpty()) {
            tvUserName.visibility = View.VISIBLE
            tvUserName.text = context.getString(R.string.enter_user_name_valid)
            tvUserName.setTextColor(Color.parseColor("#C63636"))
            llUserName.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (etMail.text.isEmpty()) {
            tvEmail.visibility = View.VISIBLE
            tvEmail.text = context.getString(R.string.email_enter)
            tvEmail.setTextColor(Color.parseColor("#C63636"))
            llEmail.setBackgroundResource(R.drawable.errordrawable)
            return false

        } else if (!etMail.text.matches(Regex(emailPattern))) {
            tvEmail.visibility = View.VISIBLE
            tvEmail.text = context.getString(R.string.email_valid)
            tvEmail.setTextColor(Color.parseColor("#C63636"))
            llEmail.setBackgroundResource(R.drawable.errordrawable)
            return false

        } else if (etMobileNumber.text.isEmpty()) {
            tvMobileNumber.visibility = View.VISIBLE
            tvMobileNumber.text = context.getString(R.string.enter_phone)
            tvMobileNumber.setTextColor(Color.parseColor("#C63636"))
            llMobileNumber.setBackgroundResource(R.drawable.errordrawable)
            return false

        } else if (etMobileNumber.text.length < 10) {
            tvMobileNumber.visibility = View.VISIBLE
            tvMobileNumber.text = context.getString(R.string.enter_valid_phone)
            tvMobileNumber.setTextColor(Color.parseColor("#C63636"))
            llMobileNumber.setBackgroundResource(R.drawable.errordrawable)
            return false

        } else if (genderSpinner.selectedItem.equals("Select gender")) {

            tvGender.visibility = View.VISIBLE
            tvGender.text = context.getString(R.string.select_gender)
            tvGender.setTextColor(Color.parseColor("#C63636"))
            llGender.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (txtDateOfBirth.text.isEmpty()) {

            tvDateOfBirth.visibility = View.VISIBLE
            tvDateOfBirth.text = context.getString(R.string.date_of_birth_select)
            tvDateOfBirth.setTextColor(Color.parseColor("#C63636"))
            llDateOfBirth.setBackgroundResource(R.drawable.errordrawable)
            return false


        }  else if (etAddress.text.isEmpty()) {

            tvAddress.visibility = View.VISIBLE
            tvAddress.text = context.getString(R.string.enter_address)
            tvAddress.setTextColor(Color.parseColor("#C63636"))
            llAddress.setBackgroundResource(R.drawable.errordrawable)
            return false


        } else if (etAddress.text.length < 5) {

            tvAddress.visibility = View.VISIBLE
            tvAddress.text = context.getString(R.string.enter_address_valid)
            tvAddress.setTextColor(Color.parseColor("#C63636"))
            llAddress.setBackgroundResource(R.drawable.errordrawable)
            return false

        } else if (etCountry.text.isEmpty()) {
            tvCountry.visibility = View.VISIBLE
            tvCountry.text = context.getString(R.string.select_country)
            tvCountry.setTextColor(Color.parseColor("#C63636"))
            llCountry.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (etState.text.isEmpty()) {
            tvState.visibility = View.VISIBLE
            tvState.text = context.getString(R.string.state)
            tvState.setTextColor(Color.parseColor("#C63636"))
            stateLL.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (etCity.text.isEmpty()) {
            tvCity.visibility = View.VISIBLE
            tvCity.text = context.getString(R.string.select_city)
            tvCity.setTextColor(Color.parseColor("#C63636"))
            llCity.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (etPassword.text.isEmpty()) {
            tvPassword.visibility = View.VISIBLE
            tvPassword.text = context.getString(R.string.enter_password)
            tvPassword.setTextColor(Color.parseColor("#C63636"))
            llPassword.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (etPassword.text.length < 6) {
            tvPassword.visibility = View.VISIBLE
            tvPassword.text = context.getString(R.string.validPassword)
            tvPassword.setTextColor(Color.parseColor("#C63636"))
            llPassword.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (etPetType.text.isEmpty()) {
            tvPetType.visibility = View.VISIBLE
            tvPetType.text = context.getString(R.string.select_pet_tpe)
            tvPetType.setTextColor(Color.parseColor("#C63636"))
            llPetType.setBackgroundResource(R.drawable.errordrawable)
            return false
        }else if (petCategorySpinner.text.isEmpty()) {
            tvPetCategory.visibility = View.VISIBLE
            tvPetCategory.text = context.getString(R.string.select_pet_category)
            tvPetCategory.setTextColor(Color.parseColor("#C63636"))
            llPetCategory.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (!registerTandC.isChecked) {
            tvTerms.visibility = View.VISIBLE
            tvTerms.text = context.getString(R.string.accept_term_condition)
            return false
        } else {
            firstNameLL.setBackgroundResource(R.drawable.white_border_background)
            lastNameLL.setBackgroundResource(R.drawable.white_border_background)
            llEmail.setBackgroundResource(R.drawable.white_border_background)
            llMobileNumber.setBackgroundResource(R.drawable.white_border_background)
            llGender.setBackgroundResource(R.drawable.white_border_background)
            llAddress.setBackgroundResource(R.drawable.white_border_background)
            llCity.setBackgroundResource(R.drawable.white_border_background)
            stateLL.setBackgroundResource(R.drawable.white_border_background)
            llZipCode.setBackgroundResource(R.drawable.white_border_background)
            llCountry.setBackgroundResource(R.drawable.white_border_background)
            llPassword.setBackgroundResource(R.drawable.white_border_background)
            llUserName.setBackgroundResource(R.drawable.white_border_background)
            llPetType.setBackgroundResource(R.drawable.white_border_background)
            llPetCategory.setBackgroundResource(R.drawable.white_border_background)
            llPetBreed.setBackgroundResource(R.drawable.white_border_background)
            llDateOfBirth.setBackgroundResource(R.drawable.white_border_background)
            imgPetProfile.borderColor = Color.parseColor("#6FCFB9")
            userProfile.borderColor = Color.parseColor("#6FCFB9")

            tvFirstName.visibility = View.GONE
            tvLastName.visibility = View.GONE
            tvEmail.visibility = View.GONE
            tvMobileNumber.visibility = View.GONE
            tvGender.visibility = View.GONE
            tvAddress.visibility = View.GONE
            tvCity.visibility = View.GONE
            tvState.visibility = View.GONE
            tvZip.visibility = View.GONE
            tvCountry.visibility = View.GONE
            tvPassword.visibility = View.GONE
            tvTerms.visibility = View.GONE
            tvUserName.visibility = View.GONE
            tvPetType.visibility = View.GONE
            tvPetCategory.visibility = View.GONE
            tvPetBreed.visibility = View.GONE
            tvDateOfBirth.visibility = View.GONE

            tvDateOfBirth.text = ""
            tvFirstName.text = ""
            tvLastName.text = ""
            tvEmail.text = ""
            tvMobileNumber.text = ""
            tvGender.text = ""
            tvAddress.text = ""
            tvCity.text = ""
            tvState.text = ""
            tvZip.text = ""
            tvCountry.text = ""
            tvPassword.text = ""
            tvTerms.text = ""
            tvUserName.text = ""
            tvPetType.text = ""
            tvPetCategory.text = ""
            tvPetBreed.text = ""




            return true
        }

    }

    fun AddPetProfile(
        tvPetType: TextView,
        llPetType: LinearLayout,
        etPetType: TextView,
        llPetCategory: LinearLayout,
        tvPetCategory: TextView,
        petCategorySpinner: TextView,
        context: AddPetProfileActivity,
        imgPetProfile: CircleImageView,
        petPic: String
    ) {


        llPetType.setBackgroundResource(R.drawable.white_border_background)
        llPetCategory.setBackgroundResource(R.drawable.white_border_background)
        imgPetProfile.borderColor = Color.parseColor("#6FCFB9")

        tvPetType.visibility = View.GONE
        tvPetCategory.visibility = View.GONE


        tvPetType.text = ""
        tvPetCategory.text = ""


        if (etPetType.text.isEmpty()) {
            tvPetType.visibility = View.VISIBLE
            tvPetType.text = context.getString(R.string.select_pet_tpe)
            tvPetType.setTextColor(Color.parseColor("#C63636"))
            llPetType.setBackgroundResource(R.drawable.errordrawable)
        } else if (petPic.isEmpty()) {
            imgPetProfile.borderColor = Color.parseColor("#C63636")
        } else if (petCategorySpinner.text.isEmpty()) {
            tvPetCategory.visibility = View.VISIBLE
            tvPetCategory.text = context.getString(R.string.select_pet_category)
            tvPetCategory.setTextColor(Color.parseColor("#C63636"))
            llPetCategory.setBackgroundResource(R.drawable.errordrawable)
        } else {

        }


    }

    fun editProfile(
        etFirstName: EditText,
        tvFirstName: TextView,
        firstNameLL: LinearLayout,
        etLastName: EditText,
        tvLastName: TextView,
        lastNameLL: LinearLayout,
        etMail: EditText,
        tvEmail: TextView,
        llEmail: LinearLayout,
        etMobileNumber: EditText,
        tvMobileNumber: TextView,
        llMobileNumber: LinearLayout,
        genderSpinner: Spinner,
        TvGender: TextView,
        llGender: LinearLayout,
        etAddress: EditText,
        tvAddress: TextView,
        llAddress: LinearLayout,
        etCity: TextView,
        tvCity: TextView,
        llCity: LinearLayout,
        etState: TextView,
        tvState: TextView,
        StateLL: LinearLayout,
        etZipCode: EditText,
        tvZip: TextView,
        llZipCode: LinearLayout,
        etCountry: TextView,
        tvCountry: TextView,
        llCountry: LinearLayout,
        etUserName: EditText,
        tvUserName: TextView,
        llUserName: LinearLayout,
        context: Context
    ) {

        firstNameLL.setBackgroundResource(R.drawable.white_border_background)
        lastNameLL.setBackgroundResource(R.drawable.white_border_background)
        llEmail.setBackgroundResource(R.drawable.white_border_background)
        llMobileNumber.setBackgroundResource(R.drawable.white_border_background)
        llGender.setBackgroundResource(R.drawable.white_border_background)
        llAddress.setBackgroundResource(R.drawable.white_border_background)
        llCity.setBackgroundResource(R.drawable.white_border_background)
        StateLL.setBackgroundResource(R.drawable.white_border_background)
        llZipCode.setBackgroundResource(R.drawable.white_border_background)
        llCountry.setBackgroundResource(R.drawable.white_border_background)
        llUserName.setBackgroundResource(R.drawable.white_border_background)

        tvFirstName.visibility = View.GONE
        tvLastName.visibility = View.GONE
        tvEmail.visibility = View.GONE
        tvMobileNumber.visibility = View.GONE
        TvGender.visibility = View.GONE
        tvAddress.visibility = View.GONE
        tvCity.visibility = View.GONE
        tvState.visibility = View.GONE
        tvZip.visibility = View.GONE
        tvCountry.visibility = View.GONE
        tvUserName.visibility = View.GONE

        tvFirstName.text = ""
        tvLastName.text = ""
        tvEmail.text = ""
        tvMobileNumber.text = ""
        TvGender.text = ""
        tvAddress.text = ""
        tvCity.text = ""
        tvState.text = ""
        tvZip.text = ""
        tvUserName.text = ""
        tvCountry.text = ""


        if (etFirstName.text.isEmpty()) {
            tvFirstName.visibility = View.VISIBLE
            tvFirstName.text = context.getString(R.string.first_name_enter)
            tvFirstName.setTextColor(Color.parseColor("#C63636"))
            firstNameLL.setBackgroundResource(R.drawable.errordrawable)
        } else if (etFirstName.text.length < 2) {
            tvFirstName.visibility = View.VISIBLE
            tvFirstName.text = context.getString(R.string.valid_first_name_enter)
            tvFirstName.setTextColor(Color.parseColor("#C63636"))
            firstNameLL.setBackgroundResource(R.drawable.errordrawable)
        } else if (etLastName.text.isEmpty()) {
            tvLastName.visibility = View.VISIBLE
            tvLastName.text = context.getString(R.string.last_name_enter)
            tvLastName.setTextColor(Color.parseColor("#C63636"))
            lastNameLL.setBackgroundResource(R.drawable.errordrawable)
        } else if (etLastName.text.length < 2) {
            tvLastName.visibility = View.VISIBLE
            tvLastName.text = context.getString(R.string.valid_last_name_enter)
            tvLastName.setTextColor(Color.parseColor("#C63636"))
            lastNameLL.setBackgroundResource(R.drawable.errordrawable)
        } else if (etUserName.text.isEmpty()) {
            tvUserName.visibility = View.VISIBLE
            tvUserName.text = context.getString(R.string.enter_user_name_valid)
            tvUserName.setTextColor(Color.parseColor("#C63636"))
            llUserName.setBackgroundResource(R.drawable.errordrawable)
        } else if (etMail.text.isEmpty()) {
            tvEmail.visibility = View.VISIBLE
            tvEmail.text = context.getString(R.string.email_enter)
            tvEmail.setTextColor(Color.parseColor("#C63636"))
            llEmail.setBackgroundResource(R.drawable.errordrawable)

        } else if (!etMail.text.matches(Regex(emailPattern))) {
            tvEmail.visibility = View.VISIBLE
            tvEmail.text = context.getString(R.string.email_valid)
            tvEmail.setTextColor(Color.parseColor("#C63636"))
            llEmail.setBackgroundResource(R.drawable.errordrawable)

        } else if (etMobileNumber.text.isEmpty()) {
            tvMobileNumber.visibility = View.VISIBLE
            tvMobileNumber.text = context.getString(R.string.enter_phone)
            tvMobileNumber.setTextColor(Color.parseColor("#C63636"))
            llMobileNumber.setBackgroundResource(R.drawable.errordrawable)

        } else if (etMobileNumber.text.length < 10) {
            tvMobileNumber.visibility = View.VISIBLE
            tvMobileNumber.text = context.getString(R.string.enter_valid_phone)
            tvMobileNumber.setTextColor(Color.parseColor("#C63636"))
            llMobileNumber.setBackgroundResource(R.drawable.errordrawable)

        } else if (genderSpinner.selectedItem.equals("Select gender")) {

            TvGender.visibility = View.VISIBLE
            TvGender.text = context.getString(R.string.select_gender)
            TvGender.setTextColor(Color.parseColor("#C63636"))
            llGender.setBackgroundResource(R.drawable.errordrawable)
        } else if (etAddress.text.isEmpty()) {

            tvAddress.visibility = View.VISIBLE
            tvAddress.text = context.getString(R.string.enter_address)
            tvAddress.setTextColor(Color.parseColor("#C63636"))
            llAddress.setBackgroundResource(R.drawable.errordrawable)


        } else if (etAddress.text.length < 5) {

            tvAddress.visibility = View.VISIBLE
            tvAddress.text = context.getString(R.string.enter_address_valid)
            tvAddress.setTextColor(Color.parseColor("#C63636"))
            llAddress.setBackgroundResource(R.drawable.errordrawable)

        } else if (etCountry.text.isEmpty()) {
            tvCountry.visibility = View.VISIBLE
            tvCountry.text = context.getString(R.string.select_country)
            tvCountry.setTextColor(Color.parseColor("#C63636"))
            llCountry.setBackgroundResource(R.drawable.errordrawable)
        } else if (etState.text.isEmpty()) {
            tvState.visibility = View.VISIBLE
            tvState.text = context.getString(R.string.state)
            tvState.setTextColor(Color.parseColor("#C63636"))
            StateLL.setBackgroundResource(R.drawable.errordrawable)
        } else if (etCity.text.isEmpty()) {
            tvCity.visibility = View.VISIBLE
            tvCity.text = context.getString(R.string.select_city)
            tvCity.setTextColor(Color.parseColor("#C63636"))
            llCity.setBackgroundResource(R.drawable.errordrawable)
        } else {
            firstNameLL.setBackgroundResource(R.drawable.white_border_background)
            lastNameLL.setBackgroundResource(R.drawable.white_border_background)
            llEmail.setBackgroundResource(R.drawable.white_border_background)
            llMobileNumber.setBackgroundResource(R.drawable.white_border_background)
            llGender.setBackgroundResource(R.drawable.white_border_background)
            llAddress.setBackgroundResource(R.drawable.white_border_background)
            llCity.setBackgroundResource(R.drawable.white_border_background)
            StateLL.setBackgroundResource(R.drawable.white_border_background)
            llZipCode.setBackgroundResource(R.drawable.white_border_background)
            llCountry.setBackgroundResource(R.drawable.white_border_background)

            tvFirstName.visibility = View.GONE
            tvLastName.visibility = View.GONE
            tvEmail.visibility = View.GONE
            tvMobileNumber.visibility = View.GONE
            TvGender.visibility = View.GONE
            tvAddress.visibility = View.GONE
            tvCity.visibility = View.GONE
            tvState.visibility = View.GONE
            tvZip.visibility = View.GONE
            tvCountry.visibility = View.GONE

            tvFirstName.text = ""
            tvLastName.text = ""
            tvEmail.text = ""
            tvMobileNumber.text = ""
            TvGender.text = ""
            tvAddress.text = ""
            tvCity.text = ""
            tvState.text = ""
            tvZip.text = ""
            tvCountry.text = ""


        }


    }


    fun editProfileVendor(
        etFirstName: EditText,
        tvFirstName: TextView,
        firstNameLL: LinearLayout,
        etLastName: EditText,
        tvLastName: TextView,
        lastNameLL: LinearLayout,
        etMail: EditText,
        tvEmail: TextView,
        llEmail: LinearLayout,
        etMobileNumber: EditText,
        tvMobileNumber: TextView,
        llMobileNumber: LinearLayout,
        genderSpinner: Spinner,
        TvGender: TextView,
        llGender: LinearLayout,
        etAddress: EditText,
        tvAddress: TextView,
        llAddress: LinearLayout,
        etCity: TextView,
        tvCity: TextView,
        llCity: LinearLayout,
        etState: TextView,
        tvState: TextView,
        StateLL: LinearLayout,
        etZipCode: EditText,
        tvZip: TextView,
        llZipCode: LinearLayout,
        etCountry: TextView,
        tvCountry: TextView,
        llCountry: LinearLayout,
        context: Context
    ) {

        firstNameLL.setBackgroundResource(R.drawable.white_border_background)
        lastNameLL.setBackgroundResource(R.drawable.white_border_background)
        llEmail.setBackgroundResource(R.drawable.white_border_background)
        llMobileNumber.setBackgroundResource(R.drawable.white_border_background)
        llGender.setBackgroundResource(R.drawable.white_border_background)
        llAddress.setBackgroundResource(R.drawable.white_border_background)
        llCity.setBackgroundResource(R.drawable.white_border_background)
        StateLL.setBackgroundResource(R.drawable.white_border_background)
        llZipCode.setBackgroundResource(R.drawable.white_border_background)
        llCountry.setBackgroundResource(R.drawable.white_border_background)

        tvFirstName.visibility = View.GONE
        tvLastName.visibility = View.GONE
        tvEmail.visibility = View.GONE
        tvMobileNumber.visibility = View.GONE
        TvGender.visibility = View.GONE
        tvAddress.visibility = View.GONE
        tvCity.visibility = View.GONE
        tvState.visibility = View.GONE
        tvZip.visibility = View.GONE
        tvCountry.visibility = View.GONE

        tvFirstName.text = ""
        tvLastName.text = ""
        tvEmail.text = ""
        tvMobileNumber.text = ""
        TvGender.text = ""
        tvAddress.text = ""
        tvCity.text = ""
        tvState.text = ""
        tvZip.text = ""
        tvCountry.text = ""


        if (etFirstName.text.isEmpty()) {
            tvFirstName.visibility = View.VISIBLE
            tvFirstName.text = context.getString(R.string.first_name_enter)
            tvFirstName.setTextColor(Color.parseColor("#C63636"))
            firstNameLL.setBackgroundResource(R.drawable.errordrawable)
        } else if (etFirstName.text.length < 2) {
            tvFirstName.visibility = View.VISIBLE
            tvFirstName.text = context.getString(R.string.valid_first_name_enter)
            tvFirstName.setTextColor(Color.parseColor("#C63636"))
            firstNameLL.setBackgroundResource(R.drawable.errordrawable)
        } else if (etLastName.text.isEmpty()) {
            tvLastName.visibility = View.VISIBLE
            tvLastName.text = context.getString(R.string.last_name_enter)
            tvLastName.setTextColor(Color.parseColor("#C63636"))
            lastNameLL.setBackgroundResource(R.drawable.errordrawable)
        } else if (etLastName.text.length < 2) {
            tvLastName.visibility = View.VISIBLE
            tvLastName.text = context.getString(R.string.valid_last_name_enter)
            tvLastName.setTextColor(Color.parseColor("#C63636"))
            lastNameLL.setBackgroundResource(R.drawable.errordrawable)
        } else if (etMail.text.isEmpty()) {
            tvEmail.visibility = View.VISIBLE
            tvEmail.text = context.getString(R.string.email_enter)
            tvEmail.setTextColor(Color.parseColor("#C63636"))
            llEmail.setBackgroundResource(R.drawable.errordrawable)

        } else if (!etMail.text.matches(Regex(emailPattern))) {
            tvEmail.visibility = View.VISIBLE
            tvEmail.text = context.getString(R.string.email_valid)
            tvEmail.setTextColor(Color.parseColor("#C63636"))
            llEmail.setBackgroundResource(R.drawable.errordrawable)

        } else if (etMobileNumber.text.isEmpty()) {
            tvMobileNumber.visibility = View.VISIBLE
            tvMobileNumber.text = context.getString(R.string.enter_phone)
            tvMobileNumber.setTextColor(Color.parseColor("#C63636"))
            llMobileNumber.setBackgroundResource(R.drawable.errordrawable)

        } else if (etMobileNumber.text.length < 10) {
            tvMobileNumber.visibility = View.VISIBLE
            tvMobileNumber.text = context.getString(R.string.enter_valid_phone)
            tvMobileNumber.setTextColor(Color.parseColor("#C63636"))
            llMobileNumber.setBackgroundResource(R.drawable.errordrawable)

        } else if (genderSpinner.selectedItem.equals("Select gender")) {

            TvGender.visibility = View.VISIBLE
            TvGender.text = context.getString(R.string.select_gender)
            TvGender.setTextColor(Color.parseColor("#C63636"))
            llGender.setBackgroundResource(R.drawable.errordrawable)
        } else if (etAddress.text.isEmpty()) {

            tvAddress.visibility = View.VISIBLE
            tvAddress.text = context.getString(R.string.enter_address)
            tvAddress.setTextColor(Color.parseColor("#C63636"))
            llAddress.setBackgroundResource(R.drawable.errordrawable)


        } else if (etAddress.text.length < 5) {

            tvAddress.visibility = View.VISIBLE
            tvAddress.text = context.getString(R.string.enter_address_valid)
            tvAddress.setTextColor(Color.parseColor("#C63636"))
            llAddress.setBackgroundResource(R.drawable.errordrawable)

        } else if (etCountry.text.isEmpty()) {
            tvCountry.visibility = View.VISIBLE
            tvCountry.text = context.getString(R.string.select_country)
            tvCountry.setTextColor(Color.parseColor("#C63636"))
            llCountry.setBackgroundResource(R.drawable.errordrawable)
        } else if (etState.text.isEmpty()) {
            tvState.visibility = View.VISIBLE
            tvState.text = context.getString(R.string.state)
            tvState.setTextColor(Color.parseColor("#C63636"))
            StateLL.setBackgroundResource(R.drawable.errordrawable)
        }  else if (etCity.text.isEmpty()) {
            tvCity.visibility = View.VISIBLE
            tvCity.text = context.getString(R.string.select_city)
            tvCity.setTextColor(Color.parseColor("#C63636"))
            llCity.setBackgroundResource(R.drawable.errordrawable)
        } else {
            firstNameLL.setBackgroundResource(R.drawable.white_border_background)
            lastNameLL.setBackgroundResource(R.drawable.white_border_background)
            llEmail.setBackgroundResource(R.drawable.white_border_background)
            llMobileNumber.setBackgroundResource(R.drawable.white_border_background)
            llGender.setBackgroundResource(R.drawable.white_border_background)
            llAddress.setBackgroundResource(R.drawable.white_border_background)
            llCity.setBackgroundResource(R.drawable.white_border_background)
            StateLL.setBackgroundResource(R.drawable.white_border_background)
            llZipCode.setBackgroundResource(R.drawable.white_border_background)
            llCountry.setBackgroundResource(R.drawable.white_border_background)

            tvFirstName.visibility = View.GONE
            tvLastName.visibility = View.GONE
            tvEmail.visibility = View.GONE
            tvMobileNumber.visibility = View.GONE
            TvGender.visibility = View.GONE
            tvAddress.visibility = View.GONE
            tvCity.visibility = View.GONE
            tvState.visibility = View.GONE
            tvZip.visibility = View.GONE
            tvCountry.visibility = View.GONE

            tvFirstName.text = ""
            tvLastName.text = ""
            tvEmail.text = ""
            tvMobileNumber.text = ""
            TvGender.text = ""
            tvAddress.text = ""
            tvCity.text = ""
            tvState.text = ""
            tvZip.text = ""
            tvCountry.text = ""


        }


    }


    fun forgotPasswordByPhone(
        etMobileNumber: EditText,
        mobileLL: LinearLayout,
        mobileTV: TextView,
        context: Context
    ) {
        val Mobile = etMobileNumber.text.toString()

        mobileTV.visibility = View.GONE
        mobileTV.text = ""
        mobileLL.setBackgroundResource(R.drawable.white_border_background)


        if (Mobile.isEmpty()) {
            mobileTV.visibility = View.VISIBLE
            mobileTV.text = context.getString(R.string.enter_phone)
            mobileTV.setTextColor(Color.parseColor("#C63636"))
            mobileLL.setBackgroundResource(R.drawable.errordrawable)

        } else if (Mobile.length < 10) {
            mobileTV.visibility = View.VISIBLE
            mobileTV.text = context.getString(R.string.enter_valid_phone)
            mobileTV.setTextColor(Color.parseColor("#C63636"))
            mobileLL.setBackgroundResource(R.drawable.errordrawable)

        } else {
            mobileTV.visibility = View.GONE
            mobileTV.text = ""
            mobileLL.setBackgroundResource(R.drawable.white_border_background)
        }
    }


    fun forgotPasswordByMail(
        etMobileNumber: EditText,
        emailLL: LinearLayout,
        emailTv: TextView,
        context: Context
    ) {
        val email = etMobileNumber.text.toString()

        emailTv.visibility = View.GONE
        emailTv.text = ""
        emailLL.setBackgroundResource(R.drawable.white_border_background)

        if (email.isEmpty()) {
            emailTv.visibility = View.VISIBLE
            emailTv.text = context.getString(R.string.email_enter)
            emailTv.setTextColor(Color.parseColor("#C63636"))
            emailLL.setBackgroundResource(R.drawable.errordrawable)

        } else if (!email.matches(Regex(emailPattern))) {
            emailTv.visibility = View.VISIBLE
            emailTv.text = context.getString(R.string.email_valid)
            emailTv.setTextColor(Color.parseColor("#C63636"))
            emailLL.setBackgroundResource(R.drawable.errordrawable)

        } else {
            emailTv.visibility = View.GONE
            emailTv.text = ""
            emailLL.setBackgroundResource(R.drawable.white_border_background)
        }


    }


    fun otpVerification(
        et_1: EditText,
        ET1: RelativeLayout,
        et_2: EditText,
        ET2: RelativeLayout,
        et_3: EditText,
        ET3: RelativeLayout,
        et_4: EditText,
        ET4: RelativeLayout,
        et_5: EditText,
        ET5: RelativeLayout,
        et_6: EditText,
        ET6: RelativeLayout,
        OTP: TextView,
        context: Context
    ) {
        if (et_1.text.isEmpty() && et_2.text.isEmpty() && et_3.text.isEmpty() && et_4.text.isEmpty()
            && et_5.text.isEmpty() && et_6.text.isEmpty()
        ) {

            OTP.visibility = View.VISIBLE
            OTP.text = context.getString(R.string.otp_verification)
            ET1.setBackgroundResource(R.drawable.errordrawable)
            ET2.setBackgroundResource(R.drawable.errordrawable)
            ET3.setBackgroundResource(R.drawable.errordrawable)
            ET4.setBackgroundResource(R.drawable.errordrawable)
            ET5.setBackgroundResource(R.drawable.errordrawable)
            ET6.setBackgroundResource(R.drawable.errordrawable)
        } else if (et_1.text.isEmpty()) {

            OTP.visibility = View.VISIBLE
            OTP.text = context.getString(R.string.valid_otp_verification)
            ET1.setBackgroundResource(R.drawable.errordrawable)

        } else if (et_2.text.isEmpty()) {

            OTP.visibility = View.VISIBLE
            OTP.text = context.getString(R.string.valid_otp_verification)
            ET1.setBackgroundResource(R.drawable.white_border_background)
            ET2.setBackgroundResource(R.drawable.errordrawable)
            ET3.setBackgroundResource(R.drawable.errordrawable)
            ET4.setBackgroundResource(R.drawable.errordrawable)
            ET5.setBackgroundResource(R.drawable.errordrawable)
            ET6.setBackgroundResource(R.drawable.errordrawable)

        } else if (et_3.text.isEmpty()) {

            OTP.visibility = View.VISIBLE
            OTP.text = context.getString(R.string.valid_otp_verification)
            ET1.setBackgroundResource(R.drawable.white_border_background)
            ET2.setBackgroundResource(R.drawable.white_border_background)
            ET3.setBackgroundResource(R.drawable.errordrawable)
            ET4.setBackgroundResource(R.drawable.errordrawable)
            ET5.setBackgroundResource(R.drawable.errordrawable)
            ET6.setBackgroundResource(R.drawable.errordrawable)
        } else if (et_4.text.isEmpty()) {

            OTP.visibility = View.VISIBLE
            OTP.text = context.getString(R.string.valid_otp_verification)
            ET1.setBackgroundResource(R.drawable.white_border_background)
            ET2.setBackgroundResource(R.drawable.white_border_background)
            ET3.setBackgroundResource(R.drawable.white_border_background)
            ET4.setBackgroundResource(R.drawable.errordrawable)
            ET5.setBackgroundResource(R.drawable.errordrawable)
            ET6.setBackgroundResource(R.drawable.errordrawable)

        } else if (et_5.text.isEmpty()) {

            OTP.visibility = View.VISIBLE
            OTP.text = context.getString(R.string.valid_otp_verification)
            ET1.setBackgroundResource(R.drawable.white_border_background)
            ET2.setBackgroundResource(R.drawable.white_border_background)
            ET3.setBackgroundResource(R.drawable.white_border_background)
            ET4.setBackgroundResource(R.drawable.white_border_background)
            ET5.setBackgroundResource(R.drawable.errordrawable)
            ET6.setBackgroundResource(R.drawable.errordrawable)

        } else if (et_6.text.isEmpty()) {

            OTP.visibility = View.VISIBLE
            OTP.text = context.getString(R.string.valid_otp_verification)
            ET1.setBackgroundResource(R.drawable.white_border_background)
            ET2.setBackgroundResource(R.drawable.white_border_background)
            ET3.setBackgroundResource(R.drawable.white_border_background)
            ET4.setBackgroundResource(R.drawable.white_border_background)
            ET5.setBackgroundResource(R.drawable.white_border_background)
            ET6.setBackgroundResource(R.drawable.errordrawable)

        } else {
            OTP.text = ""
            OTP.visibility = View.GONE
            ET1.setBackgroundResource(R.drawable.white_border_background)
            ET2.setBackgroundResource(R.drawable.white_border_background)
            ET3.setBackgroundResource(R.drawable.white_border_background)
            ET4.setBackgroundResource(R.drawable.white_border_background)
            ET5.setBackgroundResource(R.drawable.white_border_background)
            ET6.setBackgroundResource(R.drawable.white_border_background)


        }

    }


    fun changePassword(
        etNewPassword: EditText,
        llPassWord: LinearLayout,
        tvNewPassword: TextView,
        etConfirmPassword: EditText,
        llConfirmPassWord: LinearLayout,
        tvConfirmPassword: TextView,
        context: Context
    ) {

        val Pass = etNewPassword.text.toString()
        val ConfirmPass = etConfirmPassword.text.toString()

        tvNewPassword.visibility = View.GONE
        tvConfirmPassword.visibility = View.GONE
        tvNewPassword.text = ""
        tvConfirmPassword.text = ""
        llPassWord.setBackgroundResource(R.drawable.white_border_background)
        llConfirmPassWord.setBackgroundResource(R.drawable.white_border_background)

        if (Pass.isEmpty()) {
            tvNewPassword.visibility = View.VISIBLE
            tvNewPassword.text = context.getString(R.string.enter_password)
            llPassWord.setBackgroundResource(R.drawable.errordrawable)
        } else if (Pass.length < 6) {
            tvNewPassword.visibility = View.VISIBLE
            tvNewPassword.text = context.getString(R.string.validPassword)
            llPassWord.setBackgroundResource(R.drawable.errordrawable)
        } else if (ConfirmPass.isEmpty()) {
            tvConfirmPassword.visibility = View.VISIBLE
            tvConfirmPassword.text = context.getString(R.string.password_confirm)
            llConfirmPassWord.setBackgroundResource(R.drawable.errordrawable)
        } else if (ConfirmPass != Pass) {
            tvConfirmPassword.visibility = View.VISIBLE
            tvConfirmPassword.text = context.getString(R.string.ConfirmPasswordMatch)
            llConfirmPassWord.setBackgroundResource(R.drawable.errordrawable)
        } else {
            tvNewPassword.visibility = View.GONE
            tvConfirmPassword.visibility = View.GONE
            tvNewPassword.text = ""
            tvConfirmPassword.text = ""
            llPassWord.setBackgroundResource(R.drawable.white_border_background)
            llConfirmPassWord.setBackgroundResource(R.drawable.white_border_background)
        }


    }


    fun passwordChange(
        etOldPassword: EditText,
        llOldPassword: LinearLayout,
        tvOldPassword: TextView,
        etNewPassword: EditText,
        llPassWord: LinearLayout,
        tvNewPassword: TextView,
        etConfirmPassword: EditText,
        llConfirmPassWord: LinearLayout,
        tvConfirmPassword: TextView,
        context: Context
    ) {

        val oldPass = etOldPassword.text.toString()
        val Pass = etNewPassword.text.toString()
        val ConfirmPass = etConfirmPassword.text.toString()

        tvNewPassword.visibility = View.GONE
        tvConfirmPassword.visibility = View.GONE
        tvOldPassword.visibility = View.GONE
        tvOldPassword.text = ""
        tvNewPassword.text = ""
        tvConfirmPassword.text = ""
        llOldPassword.setBackgroundResource(R.drawable.white_border_background)
        llPassWord.setBackgroundResource(R.drawable.white_border_background)
        llConfirmPassWord.setBackgroundResource(R.drawable.white_border_background)

        if (oldPass.isEmpty()) {
            tvOldPassword.visibility = View.VISIBLE
            tvOldPassword.text = context.getString(R.string.old_password_enter)
            llOldPassword.setBackgroundResource(R.drawable.errordrawable)
        } else if (Pass.isEmpty()) {
            tvNewPassword.visibility = View.VISIBLE
            tvNewPassword.text = context.getString(R.string.new_password_enter)
            llPassWord.setBackgroundResource(R.drawable.errordrawable)
        } else if (Pass.length < 6) {
            tvNewPassword.visibility = View.VISIBLE
            tvNewPassword.text = context.getString(R.string.validPassword)
            llPassWord.setBackgroundResource(R.drawable.errordrawable)
        } else if (ConfirmPass.isEmpty()) {
            tvConfirmPassword.visibility = View.VISIBLE
            tvConfirmPassword.text = context.getString(R.string.enterConfirmPassword)
            llConfirmPassWord.setBackgroundResource(R.drawable.errordrawable)
        } else if (ConfirmPass != Pass) {
            tvConfirmPassword.visibility = View.VISIBLE
            tvConfirmPassword.text = context.getString(R.string.ConfirmPasswordMatch)
            llConfirmPassWord.setBackgroundResource(R.drawable.errordrawable)
        } else {
            tvNewPassword.visibility = View.GONE
            tvConfirmPassword.visibility = View.GONE
            tvOldPassword.visibility = View.GONE
            tvOldPassword.text = ""
            tvNewPassword.text = ""
            tvConfirmPassword.text = ""
            llPassWord.setBackgroundResource(R.drawable.white_border_background)
            llConfirmPassWord.setBackgroundResource(R.drawable.white_border_background)
            llOldPassword.setBackgroundResource(R.drawable.white_border_background)
        }


    }


    fun addPet(
        etPetName: EditText,
        llPetName: LinearLayout,
        tvPetName: TextView,
        txtLastSeen: TextView,
        llLastSeen: LinearLayout,
        tvDateOfBirth: TextView,
        etType: TextView,
        llType: LinearLayout,
        tvType: TextView,
        genderSpinner: Spinner,
        llGender: LinearLayout,
        tvGender: TextView,
        etColor: EditText,
        llColor: LinearLayout,
        tvColor: TextView,
        etBreed: TextView,
        llBreed: LinearLayout,
        tvBreed: TextView,
        etPeculiarity: EditText,
        llPeculiarity: LinearLayout,
        tvPeculiarity: TextView,
        etName: EditText,
        llName: LinearLayout,
        tvName: TextView,
        etAddress: EditText,
        llAddress: LinearLayout,
        tvAddress: TextView,
        etEmail: EditText,
        llEmail: LinearLayout,
        tvEmail: TextView,
        etContact: EditText,
        llContact: LinearLayout,
        tvContact: TextView,
        context: Context,
        imageArray: ArrayList<String>,
        llAddImage: LinearLayout,
        tvImage: TextView
    ) {

        tvPetName.visibility = View.GONE
        tvPetName.text = ""
        llPetName.setBackgroundResource(R.drawable.white_border_background)

        tvDateOfBirth.visibility = View.GONE
        tvDateOfBirth.text = ""
        llLastSeen.setBackgroundResource(R.drawable.white_border_background)

        tvType.visibility = View.GONE
        tvType.text = ""
        llType.setBackgroundResource(R.drawable.white_border_background)

        tvGender.visibility = View.GONE
        tvGender.text = ""
        llGender.setBackgroundResource(R.drawable.white_border_background)

        tvColor.visibility = View.GONE
        tvColor.text = ""
        llColor.setBackgroundResource(R.drawable.white_border_background)

        tvBreed.visibility = View.GONE
        tvBreed.text = ""
        llBreed.setBackgroundResource(R.drawable.white_border_background)

        tvPeculiarity.visibility = View.GONE
        tvPeculiarity.text = ""
        llPeculiarity.setBackgroundResource(R.drawable.white_border_background)

        tvName.visibility = View.GONE
        tvName.text = ""
        llName.setBackgroundResource(R.drawable.white_border_background)

        tvAddress.visibility = View.GONE
        tvAddress.text = ""
        llAddress.setBackgroundResource(R.drawable.white_border_background)

        tvEmail.visibility = View.GONE
        tvEmail.text = ""
        llEmail.setBackgroundResource(R.drawable.white_border_background)

        tvContact.visibility = View.GONE
        tvContact.text = ""
        llContact.setBackgroundResource(R.drawable.white_border_background)

        tvImage.visibility = View.GONE
        tvImage.text = ""
        llAddImage.setBackgroundResource(R.drawable.white_border_background)



        if (etPetName.text.isEmpty()) {
            tvPetName.visibility = View.VISIBLE
            tvPetName.text = "*Please enter pet name."
            llPetName.setBackgroundResource(R.drawable.errordrawable)
        } else if (imageArray.size == 0) {
            tvImage.isVisible = true
            tvImage.text = context.getString(R.string.select_image)
            llAddImage.setBackgroundResource(R.drawable.errordrawable)
        } else if (txtLastSeen.text.isEmpty()) {
            tvDateOfBirth.visibility = View.VISIBLE
            tvDateOfBirth.text = context.getString(R.string.select_date)
            llLastSeen.setBackgroundResource(R.drawable.errordrawable)
        } else if (genderSpinner.selectedItem.equals("Select gender")) {
            tvGender.visibility = View.VISIBLE
            tvGender.text = context.getString(R.string.select_gender)
            llGender.setBackgroundResource(R.drawable.errordrawable)
        } else if (etColor.text.isEmpty()) {
            tvColor.visibility = View.VISIBLE
            tvColor.text = context.getString(R.string.color_name)
            llColor.setBackgroundResource(R.drawable.errordrawable)
        } else if (etBreed.text.isEmpty()) {
            tvBreed.visibility = View.VISIBLE
            tvBreed.text = context.getString(R.string.breed_name)
            llBreed.setBackgroundResource(R.drawable.errordrawable)
        } else if (etPeculiarity.text.isEmpty()) {
            tvPeculiarity.visibility = View.VISIBLE
            tvPeculiarity.text = context.getString(R.string.peculiarity_name)
            llPeculiarity.setBackgroundResource(R.drawable.errordrawable)
        } else if (etName.text.isEmpty()) {
            tvName.visibility = View.VISIBLE
            tvName.text = context.getString(R.string.name_)
            llName.setBackgroundResource(R.drawable.errordrawable)
        } else if (etAddress.text.isEmpty()) {
            tvAddress.visibility = View.VISIBLE
            tvAddress.text = context.getString(R.string.enter_address)
            llAddress.setBackgroundResource(R.drawable.errordrawable)
        } else if (etEmail.text.isEmpty()) {
            tvEmail.visibility = View.VISIBLE
            tvEmail.text = context.getString(R.string.email_enter)
            llEmail.setBackgroundResource(R.drawable.errordrawable)
        } else if (!etEmail.text.matches(Regex(emailPattern))) {
            tvEmail.visibility = View.VISIBLE
            tvEmail.text = context.getString(R.string.email_valid)
            llEmail.setBackgroundResource(R.drawable.errordrawable)
        } else if (etContact.text.isEmpty()) {
            tvContact.visibility = View.VISIBLE
            tvContact.text = context.getString(R.string.enter_phone)
            llContact.setBackgroundResource(R.drawable.errordrawable)
        } else if (etContact.text.length <= 9) {
            tvContact.visibility = View.VISIBLE
            tvContact.text = context.getString(R.string.enter_valid_phone)
            llContact.setBackgroundResource(R.drawable.errordrawable)
        } else {

            tvPetName.visibility = View.GONE
            tvPetName.text = ""
            llPetName.setBackgroundResource(R.drawable.white_border_background)

            tvDateOfBirth.visibility = View.GONE
            tvDateOfBirth.text = ""
            llLastSeen.setBackgroundResource(R.drawable.white_border_background)

            tvType.visibility = View.GONE
            tvType.text = ""
            llType.setBackgroundResource(R.drawable.white_border_background)

            tvGender.visibility = View.GONE
            tvGender.text = ""
            llGender.setBackgroundResource(R.drawable.white_border_background)

            tvColor.visibility = View.GONE
            tvColor.text = ""
            llColor.setBackgroundResource(R.drawable.white_border_background)

            tvBreed.visibility = View.GONE
            tvBreed.text = ""
            llBreed.setBackgroundResource(R.drawable.white_border_background)

            tvPeculiarity.visibility = View.GONE
            tvPeculiarity.text = ""
            llPeculiarity.setBackgroundResource(R.drawable.white_border_background)

            tvName.visibility = View.GONE
            tvName.text = ""
            llName.setBackgroundResource(R.drawable.white_border_background)

            tvAddress.visibility = View.GONE
            tvAddress.text = ""
            llAddress.setBackgroundResource(R.drawable.white_border_background)

            tvEmail.visibility = View.GONE
            tvEmail.text = ""
            llEmail.setBackgroundResource(R.drawable.white_border_background)

            tvContact.visibility = View.GONE
            tvContact.text = ""
            llContact.setBackgroundResource(R.drawable.white_border_background)
        }


    }


    fun addPetCheck(
        etPetName: EditText,
        llPetName: LinearLayout,
        tvPetName: TextView,
        txtLastSeen: TextView,
        llLastSeen: LinearLayout,
        tvDateOfBirth: TextView,
        etType: TextView,
        llType: LinearLayout,
        tvType: TextView,
        genderSpinner: Spinner,
        llGender: LinearLayout,
        tvGender: TextView,
        etColor: EditText,
        llColor: LinearLayout,
        tvColor: TextView,
        etBreed: TextView,
        llBreed: LinearLayout,
        tvBreed: TextView,
        etPeculiarity: EditText,
        llPeculiarity: LinearLayout,
        tvPeculiarity: TextView,
        etName: EditText,
        llName: LinearLayout,
        tvName: TextView,
        etAddress: EditText,
        llAddress: LinearLayout,
        tvAddress: TextView,
        etEmail: EditText,
        llEmail: LinearLayout,
        tvEmail: TextView,
        etContact: EditText,
        llContact: LinearLayout,
        tvContact: TextView,
        context: Context,
        imageArray: ArrayList<String>,
        llAddImage: LinearLayout,
        tvImage: TextView,
        etTrackerId: EditText,
        llTrackerId: LinearLayout,
        tvTrackerId: TextView
    ) {

        tvTrackerId.visibility = View.GONE
        tvTrackerId.text = ""
        llTrackerId.setBackgroundResource(R.drawable.white_border_background)

        tvPetName.visibility = View.GONE
        tvPetName.text = ""
        llPetName.setBackgroundResource(R.drawable.white_border_background)

        tvDateOfBirth.visibility = View.GONE
        tvDateOfBirth.text = ""
        llLastSeen.setBackgroundResource(R.drawable.white_border_background)

        tvType.visibility = View.GONE
        tvType.text = ""
        llType.setBackgroundResource(R.drawable.white_border_background)

        tvGender.visibility = View.GONE
        tvGender.text = ""
        llGender.setBackgroundResource(R.drawable.white_border_background)

        tvColor.visibility = View.GONE
        tvColor.text = ""
        llColor.setBackgroundResource(R.drawable.white_border_background)

        tvBreed.visibility = View.GONE
        tvBreed.text = ""
        llBreed.setBackgroundResource(R.drawable.white_border_background)

        tvPeculiarity.visibility = View.GONE
        tvPeculiarity.text = ""
        llPeculiarity.setBackgroundResource(R.drawable.white_border_background)

        tvName.visibility = View.GONE
        tvName.text = ""
        llName.setBackgroundResource(R.drawable.white_border_background)

        tvAddress.visibility = View.GONE
        tvAddress.text = ""
        llAddress.setBackgroundResource(R.drawable.white_border_background)

        tvEmail.visibility = View.GONE
        tvEmail.text = ""
        llEmail.setBackgroundResource(R.drawable.white_border_background)

        tvContact.visibility = View.GONE
        tvContact.text = ""
        llContact.setBackgroundResource(R.drawable.white_border_background)

        tvImage.visibility = View.GONE
        tvImage.text = ""
        llAddImage.setBackgroundResource(R.drawable.white_border_background)



        if (etPetName.text.isEmpty()) {
            tvPetName.visibility = View.VISIBLE
            tvPetName.text = "*Please enter pet name."
            llPetName.setBackgroundResource(R.drawable.errordrawable)
        } else if (imageArray.size == 0) {
            tvImage.isVisible = true
            tvImage.text = context.getString(R.string.select_image)
            llAddImage.setBackgroundResource(R.drawable.errordrawable)
        } else if (txtLastSeen.text.isEmpty()) {
            tvDateOfBirth.visibility = View.VISIBLE
            tvDateOfBirth.text = context.getString(R.string.select_date)
            llLastSeen.setBackgroundResource(R.drawable.errordrawable)
        } else if (etType.text.isEmpty()) {
            tvType.visibility = View.VISIBLE
            tvType.text = context.getString(R.string.enter_type)
            llType.setBackgroundResource(R.drawable.errordrawable)
        } else if (genderSpinner.selectedItem.equals("Select gender")) {
            tvGender.visibility = View.VISIBLE
            tvGender.text = context.getString(R.string.select_gender)
            llGender.setBackgroundResource(R.drawable.errordrawable)
        } else if (etColor.text.isEmpty()) {
            tvColor.visibility = View.VISIBLE
            tvColor.text = context.getString(R.string.color_name)
            llColor.setBackgroundResource(R.drawable.errordrawable)
        } else if (etBreed.text.isEmpty()) {
            tvBreed.visibility = View.VISIBLE
            tvBreed.text = context.getString(R.string.breed_name)
            llBreed.setBackgroundResource(R.drawable.errordrawable)
        } else if (etPeculiarity.text.isEmpty()) {
            tvPeculiarity.visibility = View.VISIBLE
            tvPeculiarity.text = context.getString(R.string.peculiarity_name)
            llPeculiarity.setBackgroundResource(R.drawable.errordrawable)
        } else if (etName.text.isEmpty()) {
            tvName.visibility = View.VISIBLE
            tvName.text = context.getString(R.string.name_)
            llName.setBackgroundResource(R.drawable.errordrawable)
        } else if (etAddress.text.isEmpty()) {
            tvAddress.visibility = View.VISIBLE
            tvAddress.text = context.getString(R.string.enter_address)
            llAddress.setBackgroundResource(R.drawable.errordrawable)
        } else if (etEmail.text.isEmpty()) {
            tvEmail.visibility = View.VISIBLE
            tvEmail.text = context.getString(R.string.email_enter)
            llEmail.setBackgroundResource(R.drawable.errordrawable)
        } else if (!etEmail.text.matches(Regex(emailPattern))) {
            tvEmail.visibility = View.VISIBLE
            tvEmail.text = context.getString(R.string.email_valid)
            llEmail.setBackgroundResource(R.drawable.errordrawable)
        } else if (etContact.text.isEmpty()) {
            tvContact.visibility = View.VISIBLE
            tvContact.text = context.getString(R.string.enter_phone)
            llContact.setBackgroundResource(R.drawable.errordrawable)
        } else if (etContact.text.length <= 9) {
            tvContact.visibility = View.VISIBLE
            tvContact.text = context.getString(R.string.enter_valid_phone)
            llContact.setBackgroundResource(R.drawable.errordrawable)
        } else if (etTrackerId.text.isEmpty()) {
            tvTrackerId.visibility = View.VISIBLE
            tvTrackerId.text = "*Please Enter tracker Id."
            llTrackerId.setBackgroundResource(R.drawable.errordrawable)
        } else {

            tvPetName.visibility = View.GONE
            tvPetName.text = ""
            llPetName.setBackgroundResource(R.drawable.white_border_background)

            tvDateOfBirth.visibility = View.GONE
            tvDateOfBirth.text = ""
            llLastSeen.setBackgroundResource(R.drawable.white_border_background)

            tvType.visibility = View.GONE
            tvType.text = ""
            llType.setBackgroundResource(R.drawable.white_border_background)

            tvGender.visibility = View.GONE
            tvGender.text = ""
            llGender.setBackgroundResource(R.drawable.white_border_background)

            tvColor.visibility = View.GONE
            tvColor.text = ""
            llColor.setBackgroundResource(R.drawable.white_border_background)

            tvBreed.visibility = View.GONE
            tvBreed.text = ""
            llBreed.setBackgroundResource(R.drawable.white_border_background)

            tvPeculiarity.visibility = View.GONE
            tvPeculiarity.text = ""
            llPeculiarity.setBackgroundResource(R.drawable.white_border_background)

            tvName.visibility = View.GONE
            tvName.text = ""
            llName.setBackgroundResource(R.drawable.white_border_background)

            tvAddress.visibility = View.GONE
            tvAddress.text = ""
            llAddress.setBackgroundResource(R.drawable.white_border_background)

            tvEmail.visibility = View.GONE
            tvEmail.text = ""
            llEmail.setBackgroundResource(R.drawable.white_border_background)

            tvContact.visibility = View.GONE
            tvContact.text = ""
            llContact.setBackgroundResource(R.drawable.white_border_background)
        }


    }

    fun addPetDetails(
        etPetName: EditText,
        llPetName: LinearLayout,
        tvPetName: TextView,
        txtDateOfBirth: TextView,
        llDateOfBirth: LinearLayout,
        tvDateOfBirth: TextView,
        etOrigin: EditText,
        llOrigin: LinearLayout,
        tvOrigin: TextView,
        genderSpinner: Spinner,
        llGender: LinearLayout,
        tvGender: TextView,
        context: Context,
        size: Int,
        imagesView: TextView,
        llAddImage: LinearLayout,
        llAddress: LinearLayout,
        etAddress: EditText,
        tvAddress: TextView
    ) {

        tvAddress.visibility = View.GONE
        tvAddress.text = ""
        llAddress.setBackgroundResource(R.drawable.white_border_background)


        tvPetName.visibility = View.GONE
        tvPetName.text = ""
        llPetName.setBackgroundResource(R.drawable.white_border_background)

        tvDateOfBirth.visibility = View.GONE
        tvDateOfBirth.text = ""
        llDateOfBirth.setBackgroundResource(R.drawable.white_border_background)

        tvOrigin.visibility = View.GONE
        tvOrigin.text = ""
        llOrigin.setBackgroundResource(R.drawable.white_border_background)

        tvGender.visibility = View.GONE
        tvGender.text = ""
        llGender.setBackgroundResource(R.drawable.white_border_background)

        imagesView.visibility = View.GONE
        imagesView.text = ""
        llAddImage.setBackgroundResource(R.drawable.white_border_background)



        if (etPetName.text.isEmpty()) {
            tvPetName.visibility = View.VISIBLE
            tvPetName.text = context.getString(R.string.pet_name_enter)
            llPetName.setBackgroundResource(R.drawable.errordrawable)
        } else if (size == 0) {
            imagesView.visibility = View.VISIBLE
            imagesView.text = context.getString(R.string.select_image)
            imagesView.setTextColor(Color.parseColor("#C63636"))
            llAddImage.setBackgroundResource(R.drawable.errordrawable)
        } else if (txtDateOfBirth.text.isEmpty()) {
            tvDateOfBirth.visibility = View.VISIBLE
            tvDateOfBirth.text = context.getString(R.string.date_of_birth_select)
            llDateOfBirth.setBackgroundResource(R.drawable.errordrawable)
        } else if (etOrigin.text.isEmpty()) {
            tvOrigin.visibility = View.VISIBLE
            tvOrigin.text = context.getString(R.string.origin_enter)
            llOrigin.setBackgroundResource(R.drawable.errordrawable)
        } else if (etAddress.text.isEmpty()) {
            tvAddress.visibility = View.VISIBLE
            tvAddress.text = context.getString(R.string.enter_address)
            llAddress.setBackgroundResource(R.drawable.errordrawable)
        } else if (genderSpinner.selectedItem.equals("Select gender")) {
            tvGender.visibility = View.VISIBLE
            tvGender.text = context.getString(R.string.select_gender)
            llGender.setBackgroundResource(R.drawable.errordrawable)
        } else {
            tvPetName.visibility = View.GONE
            tvPetName.text = ""
            llPetName.setBackgroundResource(R.drawable.white_border_background)

            tvDateOfBirth.visibility = View.GONE
            tvDateOfBirth.text = ""
            llDateOfBirth.setBackgroundResource(R.drawable.white_border_background)

            tvOrigin.visibility = View.GONE
            tvOrigin.text = ""
            llOrigin.setBackgroundResource(R.drawable.white_border_background)

            tvGender.visibility = View.GONE
            tvGender.text = ""
            llGender.setBackgroundResource(R.drawable.white_border_background)

        }


    }

    fun addEvent(
        etEventName: EditText,
        llEventName: LinearLayout,
        tvEventName: TextView,
        etAddress: EditText,
        addressLL: LinearLayout,
        tvAddress: TextView,
        etCountry: TextView,
        llCountry: LinearLayout,
        tvCountry: TextView,
        etState: TextView,
        stateLL: LinearLayout,
        tvState: TextView,
        etCity: TextView,
        llCity: LinearLayout,
        tvCity: TextView,
        txtDateOfBirth: TextView,
        llDateOfBirth: LinearLayout,
        tvDateOfBirth: TextView,
        txtTime: TextView,
        llTime: LinearLayout,
        tvTimeError: TextView,
        etCaption: EditText,
        llDescription: LinearLayout,
        tvDescription: TextView,
        context: Context,
        size: Int,
        imagesView: TextView,
        llAddImage: LinearLayout
    ) {


        llEventName.setBackgroundResource(R.drawable.white_border_background)
        addressLL.setBackgroundResource(R.drawable.white_border_background)
        llCountry.setBackgroundResource(R.drawable.white_border_background)
        stateLL.setBackgroundResource(R.drawable.white_border_background)
        llCity.setBackgroundResource(R.drawable.white_border_background)
        llDateOfBirth.setBackgroundResource(R.drawable.white_border_background)
        llDescription.setBackgroundResource(R.drawable.white_border_background)
        llAddImage.setBackgroundResource(R.drawable.white_border_background)
        llTime.setBackgroundResource(R.drawable.white_border_background)

        tvCountry.visibility = View.GONE
        tvEventName.text = ""

        tvAddress.visibility = View.GONE
        tvAddress.text = ""

        tvCountry.visibility = View.GONE
        tvCountry.text = ""

        tvState.visibility = View.GONE
        tvState.text = ""

        tvCity.visibility = View.GONE
        tvCity.text = ""

        tvDateOfBirth.visibility = View.GONE
        tvDateOfBirth.text = ""

        tvTimeError.visibility = View.GONE
        tvTimeError.text = ""

        tvDescription.visibility = View.GONE
        imagesView.visibility = View.GONE
        tvDescription.text = ""
        imagesView.text = ""


        if (etEventName.text.isEmpty()) {
            tvEventName.visibility = View.VISIBLE
            tvEventName.text = context.getString(R.string.enter_event)
            tvEventName.setTextColor(Color.parseColor("#C63636"))
            llEventName.setBackgroundResource(R.drawable.errordrawable)
        } else if (size == 0) {
            imagesView.visibility = View.VISIBLE
            imagesView.text = context.getString(R.string.select_image)
            imagesView.setTextColor(Color.parseColor("#C63636"))
            llAddImage.setBackgroundResource(R.drawable.errordrawable)
        } else if (etAddress.text.isEmpty()) {
            tvAddress.visibility = View.VISIBLE
            tvAddress.text = context.getString(R.string.enter_address)
            tvAddress.setTextColor(Color.parseColor("#C63636"))
            addressLL.setBackgroundResource(R.drawable.errordrawable)


        } else if (etAddress.text.length < 3) {
            tvAddress.visibility = View.VISIBLE
            tvAddress.text = context.getString(R.string.enter_address_valid)
            tvAddress.setTextColor(Color.parseColor("#C63636"))
            addressLL.setBackgroundResource(R.drawable.errordrawable)

        } else if (etCountry.text.isEmpty()) {
            tvCountry.visibility = View.VISIBLE
            tvCountry.text = context.getString(R.string.select_country)
            tvCountry.setTextColor(Color.parseColor("#C63636"))
            llCountry.setBackgroundResource(R.drawable.errordrawable)
        } else if (etState.text.isEmpty()) {
            tvState.visibility = View.VISIBLE
            tvState.text = context.getString(R.string.select_state)
            tvState.setTextColor(Color.parseColor("#C63636"))
            stateLL.setBackgroundResource(R.drawable.errordrawable)
        } else if (etCity.text.isEmpty()) {
            tvCity.visibility = View.VISIBLE
            tvCity.text = context.getString(R.string.select_city)
            tvCity.setTextColor(Color.parseColor("#C63636"))
            llCity.setBackgroundResource(R.drawable.errordrawable)
        } else if (txtDateOfBirth.text.isEmpty()) {
            tvDateOfBirth.visibility = View.VISIBLE
            tvDateOfBirth.text = context.getString(R.string.date_event)
            tvDateOfBirth.setTextColor(Color.parseColor("#C63636"))
            llDateOfBirth.setBackgroundResource(R.drawable.errordrawable)
        } else if (txtTime.text.isEmpty()) {
            tvTimeError.visibility = View.VISIBLE
            tvTimeError.text = "*Please select time for event."
            tvTimeError.setTextColor(Color.parseColor("#C63636"))
            llTime.setBackgroundResource(R.drawable.errordrawable)
        } else if (etCaption.text.isEmpty()) {
            tvDescription.visibility = View.VISIBLE
            tvDescription.text = context.getString(R.string.enter_description)
            tvDescription.setTextColor(Color.parseColor("#C63636"))
            llDescription.setBackgroundResource(R.drawable.errordrawable)
        }


    }


    fun signUpVendor(
        etFirstName: EditText,
        tvFirstName: TextView,
        firstNameLL: LinearLayout,
        etLastName: EditText,
        tvLastName: TextView,
        lastNameLL: LinearLayout,
        etMail: EditText,
        tvEmail: TextView,
        llEmail: LinearLayout,
        etMobileNumber: EditText,
        tvMobileNumber: TextView,
        llMobileNumber: LinearLayout,
        genderSpinner: Spinner,
        tvGender: TextView,
        llGender: LinearLayout,
        etAddress: EditText,
        tvAddress: TextView,
        llAddress: LinearLayout,
        etCity: TextView,
        tvCity: TextView,
        llCity: LinearLayout,
        etState: TextView,
        tvState: TextView,
        stateLL: LinearLayout,
        etZipCode: EditText,
        tvZip: TextView,
        llZipCode: LinearLayout,
        etCountry: TextView,
        tvCountry: TextView,
        llCountry: LinearLayout,
        etPassword: EditText,
        tvPassword: TextView,
        llPassword: LinearLayout,
        registerTandC: CheckBox,
        tvTerms: TextView,
        tvDocument: TextView,
        context: Context,
        profilepic: String,
        userProfile: CircleImageView,
        llVendorType: LinearLayout,
        vendorType1: Spinner,
        tvVendorType: TextView,
        txtDateOfBirth: TextView,
        llDateOfBirth: LinearLayout,
        tvDateOfBirth: TextView
    ) {

        firstNameLL.setBackgroundResource(R.drawable.white_border_background)
        lastNameLL.setBackgroundResource(R.drawable.white_border_background)
        llEmail.setBackgroundResource(R.drawable.white_border_background)
        llMobileNumber.setBackgroundResource(R.drawable.white_border_background)
        llGender.setBackgroundResource(R.drawable.white_border_background)
        llAddress.setBackgroundResource(R.drawable.white_border_background)
        llCity.setBackgroundResource(R.drawable.white_border_background)
        stateLL.setBackgroundResource(R.drawable.white_border_background)
        llZipCode.setBackgroundResource(R.drawable.white_border_background)
        llCountry.setBackgroundResource(R.drawable.white_border_background)
        llPassword.setBackgroundResource(R.drawable.white_border_background)
        llVendorType.setBackgroundResource(R.drawable.white_border_background)
        llDateOfBirth.setBackgroundResource(R.drawable.white_border_background)
        userProfile.borderColor = Color.parseColor("#6FCFB9")

        tvFirstName.visibility = View.GONE
        tvLastName.visibility = View.GONE
        tvEmail.visibility = View.GONE
        tvMobileNumber.visibility = View.GONE
        tvGender.visibility = View.GONE
        tvAddress.visibility = View.GONE
        tvCity.visibility = View.GONE
        tvState.visibility = View.GONE
        tvZip.visibility = View.GONE
        tvCountry.visibility = View.GONE
        tvPassword.visibility = View.GONE
        tvTerms.visibility = View.GONE
        tvVendorType.visibility = View.GONE
        tvDateOfBirth.visibility = View.GONE


        tvFirstName.text = ""
        tvLastName.text = ""
        tvEmail.text = ""
        tvMobileNumber.text = ""
        tvGender.text = ""
        tvAddress.text = ""
        tvCity.text = ""
        tvState.text = ""
        tvZip.text = ""
        tvCountry.text = ""
        tvPassword.text = ""
        tvTerms.text = ""
        tvVendorType.text = ""
        tvDateOfBirth.text = ""



        if (etFirstName.text.isEmpty()) {
            tvFirstName.visibility = View.VISIBLE
            tvFirstName.text = context.getString(R.string.first_name_enter)
            tvFirstName.setTextColor(Color.parseColor("#C63636"))
            firstNameLL.setBackgroundResource(R.drawable.errordrawable)
        } else if (etFirstName.text.length < 3) {
            tvFirstName.visibility = View.VISIBLE
            tvFirstName.text = context.getString(R.string.valid_first_name_enter)
            tvFirstName.setTextColor(Color.parseColor("#C63636"))
            firstNameLL.setBackgroundResource(R.drawable.errordrawable)
        } else if (etLastName.text.isEmpty()) {
            tvLastName.visibility = View.VISIBLE
            tvLastName.text = context.getString(R.string.last_name_enter)
            tvLastName.setTextColor(Color.parseColor("#C63636"))
            lastNameLL.setBackgroundResource(R.drawable.errordrawable)
        } else if (etLastName.text.length < 2) {
            tvLastName.visibility = View.VISIBLE
            tvLastName.text = context.getString(R.string.valid_last_name_enter)
            tvLastName.setTextColor(Color.parseColor("#C63636"))
            lastNameLL.setBackgroundResource(R.drawable.errordrawable)
        } else if (profilepic.isEmpty()) {
            userProfile.borderColor = Color.parseColor("#C63636")
        } else if (etMail.text.isEmpty()) {
            tvEmail.visibility = View.VISIBLE
            tvEmail.text = context.getString(R.string.email_enter)
            tvEmail.setTextColor(Color.parseColor("#C63636"))
            llEmail.setBackgroundResource(R.drawable.errordrawable)

        } else if (!etMail.text.matches(Regex(emailPattern))) {
            tvEmail.visibility = View.VISIBLE
            tvEmail.text = context.getString(R.string.email_valid)
            tvEmail.setTextColor(Color.parseColor("#C63636"))
            llEmail.setBackgroundResource(R.drawable.errordrawable)

        } else if (etMobileNumber.text.isEmpty()) {
            tvMobileNumber.visibility = View.VISIBLE
            tvMobileNumber.text = context.getString(R.string.enter_phone)
            tvMobileNumber.setTextColor(Color.parseColor("#C63636"))
            llMobileNumber.setBackgroundResource(R.drawable.errordrawable)

        } else if (etMobileNumber.text.length < 10) {
            tvMobileNumber.visibility = View.VISIBLE
            tvMobileNumber.text = context.getString(R.string.enter_valid_phone)
            tvMobileNumber.setTextColor(Color.parseColor("#C63636"))
            llMobileNumber.setBackgroundResource(R.drawable.errordrawable)

        } else if (genderSpinner.selectedItem.equals("Select gender")) {

            tvGender.visibility = View.VISIBLE
            tvGender.text = context.getString(R.string.select_gender)
            tvGender.setTextColor(Color.parseColor("#C63636"))
            llGender.setBackgroundResource(R.drawable.errordrawable)
        }else if (txtDateOfBirth.text.isEmpty()) {

            tvDateOfBirth.visibility = View.VISIBLE
            tvDateOfBirth.text = context.getString(R.string.date_of_birth_select)
            tvDateOfBirth.setTextColor(Color.parseColor("#C63636"))
            llDateOfBirth.setBackgroundResource(R.drawable.errordrawable)

        }  else if (etAddress.text.isEmpty()) {

            tvAddress.visibility = View.VISIBLE
            tvAddress.text = context.getString(R.string.enter_address)
            tvAddress.setTextColor(Color.parseColor("#C63636"))
            llAddress.setBackgroundResource(R.drawable.errordrawable)


        } else if (etAddress.text.length < 5) {

            tvAddress.visibility = View.VISIBLE
            tvAddress.text = context.getString(R.string.enter_address_valid)
            tvAddress.setTextColor(Color.parseColor("#C63636"))
            llAddress.setBackgroundResource(R.drawable.errordrawable)

        } else if (etCountry.text.isEmpty()) {
            tvCountry.visibility = View.VISIBLE
            tvCountry.text = context.getString(R.string.select_country)
            tvCountry.setTextColor(Color.parseColor("#C63636"))
            llCountry.setBackgroundResource(R.drawable.errordrawable)
        } else if (etState.text.isEmpty()) {
            tvState.visibility = View.VISIBLE
            tvState.text = context.getString(R.string.select_state)
            tvState.setTextColor(Color.parseColor("#C63636"))
            stateLL.setBackgroundResource(R.drawable.errordrawable)
        } else if (etCity.text.isEmpty()) {
            tvCity.visibility = View.VISIBLE
            tvCity.text = context.getString(R.string.select_city)
            tvCity.setTextColor(Color.parseColor("#C63636"))
            llCity.setBackgroundResource(R.drawable.errordrawable)
        } else if (vendorType1.selectedItem.equals("Select vendor type")) {

            tvVendorType.visibility = View.VISIBLE
            tvVendorType.text = context.getString(R.string.vendor_type_select)
            tvVendorType.setTextColor(Color.parseColor("#C63636"))
            llVendorType.setBackgroundResource(R.drawable.errordrawable)
        } else if (etPassword.text.isEmpty()) {
            tvPassword.visibility = View.VISIBLE
            tvPassword.text = context.getString(R.string.enter_password)
            tvPassword.setTextColor(Color.parseColor("#C63636"))
            llPassword.setBackgroundResource(R.drawable.errordrawable)
        } else if (etPassword.text.length < 6) {
            tvPassword.visibility = View.VISIBLE
            tvPassword.text = context.getString(R.string.validPassword)
            tvPassword.setTextColor(Color.parseColor("#C63636"))
            llPassword.setBackgroundResource(R.drawable.errordrawable)
        } else if (!registerTandC.isChecked) {
            tvTerms.visibility = View.VISIBLE
            tvTerms.text = context.getString(R.string.accept_term_condition)
        } else {

        }


    }


    fun addProductValidation(
        etProductName: EditText,
        llProductName: LinearLayout,
        tvProductName: TextView,
        llAddImage: LinearLayout,
        imagesView: TextView,
        imageArray: ArrayList<String>,
        etPetType: TextView,
        llPetType: LinearLayout,
        tvPetType: TextView,
        etProductCategory: TextView,
        llProductCategory: LinearLayout,
        tvProductCategory: TextView,
        subCategoryLL: LinearLayout,
        etSubCategory: TextView,
        tvSubCategory: TextView,
        llProductWeight: LinearLayout,
        etProductWeight: EditText,
        tvProductWeight: TextView,
        llProductPrice: LinearLayout,
        etProductPrice: EditText,
        tvProductPrice: TextView,
        llProductAddress: LinearLayout,
        etProductAddress: EditText,
        tvProductAddress: TextView,
        llDescription: LinearLayout,
        etCaption: EditText,
        tvProductDescription: TextView,
        context: Context
    ) {

        llProductName.setBackgroundResource(R.drawable.white_border_background)
        llAddImage.setBackgroundResource(R.drawable.white_border_background)
        llPetType.setBackgroundResource(R.drawable.white_border_background)
        llProductCategory.setBackgroundResource(R.drawable.white_border_background)
        subCategoryLL.setBackgroundResource(R.drawable.white_border_background)
        llProductWeight.setBackgroundResource(R.drawable.white_border_background)
        llProductPrice.setBackgroundResource(R.drawable.white_border_background)
        llProductAddress.setBackgroundResource(R.drawable.white_border_background)
        llDescription.setBackgroundResource(R.drawable.white_border_background)

        tvProductName.isVisible = false
        imagesView.isVisible = false
        tvPetType.isVisible = false
        tvProductCategory.isVisible = false
        tvSubCategory.isVisible = false
        tvProductWeight.isVisible = false
        tvProductPrice.isVisible = false
        tvProductAddress.isVisible = false
        tvProductDescription.isVisible = false

        tvProductName.text = ""
        imagesView.text = ""
        tvPetType.text = ""
        tvProductCategory.text = ""
        tvSubCategory.text = ""
        tvProductWeight.text = ""
        tvProductPrice.text = ""
        tvProductAddress.text = ""
        tvProductDescription.text = ""



        if (etProductName.text.isEmpty()) {
            tvProductName.isVisible = true
            tvProductName.text = context.getString(R.string.enter_product_name_vali)
            tvProductName.setTextColor(Color.parseColor("#C63636"))
            llProductName.setBackgroundResource(R.drawable.errordrawable)
        } else if (imageArray.isEmpty()) {
            imagesView.isVisible = true
            imagesView.text = context.getString(R.string.select_image)
            imagesView.setTextColor(Color.parseColor("#C63636"))
            llAddImage.setBackgroundResource(R.drawable.errordrawable)
        } else if (etPetType.text.isEmpty()) {
            tvPetType.isVisible = true
            tvPetType.text = context.getString(R.string.select_pet_tpe)
            tvPetType.setTextColor(Color.parseColor("#C63636"))
            llPetType.setBackgroundResource(R.drawable.errordrawable)
        } else if (etProductCategory.text.isEmpty()) {
            tvProductCategory.isVisible = true
            tvProductCategory.text = context.getString(R.string.enter_product_category_vali)
            tvProductCategory.setTextColor(Color.parseColor("#C63636"))
            llProductCategory.setBackgroundResource(R.drawable.errordrawable)
        } else if (etSubCategory.text.isEmpty()) {
            tvSubCategory.isVisible = true
            tvSubCategory.text = context.getString(R.string.enter_product_sub_category_vali)
            tvSubCategory.setTextColor(Color.parseColor("#C63636"))
            subCategoryLL.setBackgroundResource(R.drawable.errordrawable)
        } else if (etProductWeight.text.isEmpty()) {
            tvProductWeight.isVisible = true
            tvProductWeight.text = context.getString(R.string.enter_product_weight_vali)
            tvProductWeight.setTextColor(Color.parseColor("#C63636"))
            llProductWeight.setBackgroundResource(R.drawable.errordrawable)
        } else if (etProductPrice.text.isEmpty()) {
            tvProductPrice.isVisible = true
            tvProductPrice.text = context.getString(R.string.enter_product_price_vali)
            tvProductPrice.setTextColor(Color.parseColor("#C63636"))
            llProductPrice.setBackgroundResource(R.drawable.errordrawable)
        } else if (etProductAddress.text.isEmpty()) {
            tvProductAddress.isVisible = true
            tvProductAddress.text = context.getString(R.string.enter_address_new_vali)
            tvProductAddress.setTextColor(Color.parseColor("#C63636"))
            llProductAddress.setBackgroundResource(R.drawable.errordrawable)
        } else if (etCaption.text.isEmpty()) {
            tvProductDescription.isVisible = true
            tvProductDescription.text = context.getString(R.string.enter_description)
            tvProductDescription.setTextColor(Color.parseColor("#C63636"))
            llDescription.setBackgroundResource(R.drawable.errordrawable)
        }


    }


    fun addServiceValidation(
        etServiceName: EditText,
        llProductName: LinearLayout,
        tvServiceName: TextView,
        llAddImage: LinearLayout,
        imagesView: TextView,
        imageArray: ArrayList<String>,
        etPetType: TextView,
        llPetType: LinearLayout,
        tvPetType: TextView,
        etServiceCategory: TextView,
        llServiceCategory: LinearLayout,
        tvServiceCategory: TextView,
        subCategoryLL: LinearLayout,
        etSubCategory: TextView,
        tvSubCategory: TextView,
        llServiceExperience: LinearLayout,
        etServiceExperience: EditText,
        tvServiceExperience: TextView,
        llServicePrice: LinearLayout,
        etServicePrice: EditText,
        tvServicePrice: TextView,
        llServiceAddress: LinearLayout,
        etServiceAddress: EditText,
        tvServiceAddress: TextView,
        llDescription: LinearLayout,
        etCaption: EditText,
        tvServiceDescription: TextView,
        context: Context
    ) {
        llProductName.setBackgroundResource(R.drawable.white_border_background)
        llAddImage.setBackgroundResource(R.drawable.white_border_background)
        llPetType.setBackgroundResource(R.drawable.white_border_background)
        llServiceCategory.setBackgroundResource(R.drawable.white_border_background)
        subCategoryLL.setBackgroundResource(R.drawable.white_border_background)
        llServicePrice.setBackgroundResource(R.drawable.white_border_background)
        llServiceExperience.setBackgroundResource(R.drawable.white_border_background)
        llServiceAddress.setBackgroundResource(R.drawable.white_border_background)
        llDescription.setBackgroundResource(R.drawable.white_border_background)

        tvServiceName.isVisible = false
        imagesView.isVisible = false
        tvPetType.isVisible = false
        tvServiceCategory.isVisible = false
        tvSubCategory.isVisible = false
        tvServicePrice.isVisible = false
        tvServiceExperience.isVisible = false
        tvServiceAddress.isVisible = false
        tvServiceDescription.isVisible = false

        tvServiceName.text = ""
        imagesView.text = ""
        tvPetType.text = ""
        tvServiceCategory.text = ""
        tvSubCategory.text = ""
        tvServicePrice.text = ""
        tvServiceExperience.text = ""
        tvServiceAddress.text = ""
        tvServiceDescription.text = ""



        if (etServiceName.text.isEmpty()) {
            tvServiceName.isVisible = true
            tvServiceName.text = context.getString(R.string.enter_service_name_vali)
            tvServiceName.setTextColor(Color.parseColor("#C63636"))
            llProductName.setBackgroundResource(R.drawable.errordrawable)
        } else if (imageArray.isEmpty()) {
            imagesView.isVisible = true
            imagesView.text = context.getString(R.string.select_image)
            imagesView.setTextColor(Color.parseColor("#C63636"))
            llAddImage.setBackgroundResource(R.drawable.errordrawable)
        } else if (etPetType.text.isEmpty()) {
            tvPetType.isVisible = true
            tvPetType.text = context.getString(R.string.select_pet_tpe)
            tvPetType.setTextColor(Color.parseColor("#C63636"))
            llPetType.setBackgroundResource(R.drawable.errordrawable)
        } else if (etServiceCategory.text.isEmpty()) {
            tvServiceCategory.isVisible = true
            tvServiceCategory.text = context.getString(R.string.enter_product_service_vali)
            tvServiceCategory.setTextColor(Color.parseColor("#C63636"))
            llServiceCategory.setBackgroundResource(R.drawable.errordrawable)
        } else if (etSubCategory.text.isEmpty()) {
            tvSubCategory.isVisible = true
            tvSubCategory.text = context.getString(R.string.enter_product_service_sub_vali)
            tvSubCategory.setTextColor(Color.parseColor("#C63636"))
            subCategoryLL.setBackgroundResource(R.drawable.errordrawable)
        } else if (etServicePrice.text.isEmpty()) {
            tvServicePrice.isVisible = true
            tvServicePrice.text = context.getString(R.string.service_price_vali)
            tvServicePrice.setTextColor(Color.parseColor("#C63636"))
            llServicePrice.setBackgroundResource(R.drawable.errordrawable)
        } else if (etServiceAddress.text.isEmpty()) {
            tvServiceAddress.isVisible = true
            tvServiceAddress.text = context.getString(R.string.enter_address_new_vali)
            tvServiceAddress.setTextColor(Color.parseColor("#C63636"))
            llServiceAddress.setBackgroundResource(R.drawable.errordrawable)
        } else if (etCaption.text.isEmpty()) {
            tvServiceDescription.isVisible = true
            tvServiceDescription.text = context.getString(R.string.enter_description)
            tvServiceDescription.setTextColor(Color.parseColor("#C63636"))
            llDescription.setBackgroundResource(R.drawable.errordrawable)

        }
    }


    fun doctorsRegistration(
        firstNameLL: LinearLayout,
        etFirstName: EditText,
        tvFirstName: TextView,
        middleNameLL: LinearLayout,
        etMiddleName: EditText,
        tvMiddleName: TextView,
        lastNameLL: LinearLayout,
        etLastName: EditText,
        tvLastName: TextView,
        primaryLanguageLL: LinearLayout,
        etPrimaryLanguage: EditText,
        tvPrimaryLanguage: TextView,
        llMobileNumber: LinearLayout,
        etMobileNumber: EditText,
        tvMobileNumber: TextView,
        llGender: LinearLayout,
        genderSpinner: Spinner,
        tvGender: TextView,
        etClinic: EditText,
        clinicLL: LinearLayout,
        tvClinic: TextView,
        llCountry: LinearLayout,
        etCountry: TextView,
        tvCountry: TextView,
        stateLL: LinearLayout,
        etState: TextView,
        tvState: TextView,
        llZipCode: LinearLayout,
        etZipCode: EditText,
        tvZip: TextView,
        llCity: LinearLayout,
        etCity: TextView,
        tvCity: TextView,
        llFrom: LinearLayout,
        etFrom: TextView,
        tvFrom: TextView,
        llEmergencyNumber: LinearLayout,
        etEmergencyNumber: EditText,
        tvEmergencyNumber: TextView,
        llSpecialization: LinearLayout,
        etSpecialization: TextView,
        tvSpecialization: TextView,
        llPractice: LinearLayout,
        etPractice: EditText,
        tvPractice: TextView,
        llCollege: LinearLayout,
        etCollege: EditText,
        tvCollege: TextView,
        uploadDocumentLL: LinearLayout,
        uploadDocumentTv: TextView,
        tvDocument: TextView,
        selectEndDate: RelativeLayout,
        expirationDate: TextView,
        tvEndDate: TextView,
        context: Activity,
        llDegreeType: LinearLayout,
        degreeTypeSpinner: Spinner,
        tvDegreeType: TextView,
        uploadDocumentLL1: LinearLayout,
        uploadDocumentTv1: EditText,
        tvDocument1: TextView,
        selectEndDate1: RelativeLayout,
        expirationDate1: TextView,
        tvEndDate1: TextView,
        permitLL: LinearLayout,
        permitTv: EditText,
        tvPermit: TextView,
        selectEndDatePermit: RelativeLayout,
        expirationDatePermit: TextView,
        tvEndDatePermit: TextView
    ): Boolean {

        clearvalidations(
            firstNameLL,
            tvFirstName,
            lastNameLL,
            tvLastName,
            primaryLanguageLL,
            tvPrimaryLanguage,
            llMobileNumber,
            tvMobileNumber,
            llGender,
            tvGender,
            clinicLL,
            tvClinic,
            llCountry,
            tvCountry,
            stateLL,
            tvState,
            llZipCode,
            tvZip,
            llCity,
            tvCity,
            llFrom,
            tvFrom,
            llEmergencyNumber,
            tvEmergencyNumber,
            llSpecialization,
            tvSpecialization,
            llPractice,
            tvPractice,
            llCollege,
            tvCollege,
            uploadDocumentLL,
            tvDocument,
            selectEndDate,
            tvEndDate,selectEndDatePermit,
            tvEndDatePermit,llDegreeType,
            tvDegreeType,
            permitLL,tvPermit
        )
        if (etPrimaryLanguage.text.isEmpty()) {
            tvPrimaryLanguage.isVisible = true
            tvPrimaryLanguage.text = context.getString(R.string.primary_language_enter)
            primaryLanguageLL.setBackgroundResource(R.drawable.errordrawable)
            return false
        }  else if (etClinic.text.isEmpty()) {
            tvClinic.isVisible = true
            tvClinic.text = context.getString(R.string.enter_address_clinic)
            clinicLL.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (etFrom.text.isEmpty()) {
            tvFrom.isVisible = true
            tvFrom.text = context.getString(R.string.select_clinic_hours_valid)
            tvFrom.setTextColor(Color.parseColor("#C63636"))
            llFrom.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (etSpecialization.text.isEmpty()) {
            tvSpecialization.isVisible = true
            tvSpecialization.text = context.getString(R.string.select_specialization_valid)
            tvSpecialization.setTextColor(Color.parseColor("#C63636"))
            llSpecialization.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (etPractice.text.isEmpty()) {
            tvPractice.isVisible = true
            tvPractice.text = context.getString(R.string.enter_years_of_practice_valid)
            tvPractice.setTextColor(Color.parseColor("#C63636"))
            llPractice.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (etCollege.text.isEmpty()) {
            tvCollege.isVisible = true
            tvCollege.text = context.getString(R.string.enter_college_valid)
            tvCollege.setTextColor(Color.parseColor("#C63636"))
            llCollege.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (degreeTypeSpinner.selectedItem == "Select degree type") {
            tvDegreeType.isVisible = true
            tvDegreeType.text = context.getString(R.string.select_degree_type)
            tvDegreeType.setTextColor(Color.parseColor("#C63636"))
            llDegreeType.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (uploadDocumentTv.text.isEmpty()) {
            tvDocument.isVisible = true
            tvDocument.text = context.getString(R.string.select_certificate)
            tvDocument.setTextColor(Color.parseColor("#C63636"))
            uploadDocumentLL.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (expirationDate.text.isEmpty()) {
            tvEndDate.isVisible = true
            tvEndDate.text = context.getString(R.string.expiration_date_valid)
            tvEndDate.setTextColor(Color.parseColor("#C63636"))
            selectEndDate.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (permitTv.text.isEmpty()) {
            tvPermit.isVisible = true
            tvPermit.text = context.getString(R.string.expiration_date_valid)
            tvPermit.setTextColor(Color.parseColor("#C63636"))
            permitLL.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (expirationDatePermit.text.isEmpty()) {
            tvEndDatePermit.isVisible = true
            tvEndDatePermit.text = context.getString(R.string.expiration_date_valid)
            tvEndDatePermit.setTextColor(Color.parseColor("#C63636"))
            selectEndDatePermit.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else {
            clearvalidations(
                firstNameLL,
                tvFirstName,
                lastNameLL,
                tvLastName,
                primaryLanguageLL,
                tvPrimaryLanguage,
                llMobileNumber,
                tvMobileNumber,
                llGender,
                tvGender,
                clinicLL,
                tvClinic,
                llCountry,
                tvCountry,
                stateLL,
                tvState,
                llZipCode,
                tvZip,
                llCity,
                tvCity,
                llFrom,
                tvFrom,
                llEmergencyNumber,
                tvEmergencyNumber,
                llSpecialization,
                tvSpecialization,
                llPractice,
                tvPractice,
                llCollege,
                tvCollege,
                uploadDocumentLL,
                tvDocument,
                selectEndDate,
                tvEndDate,selectEndDatePermit,
                tvEndDatePermit,llDegreeType,
                tvDegreeType,
                permitLL,tvPermit
            )
            return true
        }
    }


    fun clearvalidations(
        firstNameLL: LinearLayout,
        tvFirstName: TextView,
        lastNameLL: LinearLayout,
        tvLastName: TextView,
        primaryLanguageLL: LinearLayout,
        tvPrimaryLanguage: TextView,
        llMobileNumber: LinearLayout,
        tvMobileNumber: TextView,
        llGender: LinearLayout,
        tvGender: TextView,
        clinicLL: LinearLayout,
        tvClinic: TextView,
        llCountry: LinearLayout,
        tvCountry: TextView,
        stateLL: LinearLayout,
        tvState: TextView,
        llZipCode: LinearLayout,
        tvZip: TextView,
        llCity: LinearLayout,
        tvCity: TextView,
        llFrom: LinearLayout,
        tvFrom: TextView,
        llEmergencyNumber: LinearLayout,
        tvEmergencyNumber: TextView,
        llSpecialization: LinearLayout,
        tvSpecialization: TextView,
        llPractice: LinearLayout,
        tvPractice: TextView,
        llCollege: LinearLayout,
        tvCollege: TextView,
        uploadDocumentLL: LinearLayout,
        tvDocument: TextView,
        selectEndDate: RelativeLayout,
        tvEndDate: TextView,
        selectEndDatePermit: RelativeLayout,
        tvEndDatePermit: TextView,
        llDegreeType: LinearLayout,
        tvDegreeType: TextView,
        permitLL: LinearLayout,
        tvPermit: TextView,
    ) {
        tvDegreeType.isVisible = false
        tvEndDate.isVisible = false
        tvPermit.isVisible = false
        tvEndDate.isVisible = false
        tvDocument.isVisible = false
        tvCollege.isVisible = false
        tvPractice.isVisible = false
        tvSpecialization.isVisible = false
        tvEmergencyNumber.isVisible = false
        tvFrom.isVisible = false
        tvCity.isVisible = false
        tvZip.isVisible = false
        tvState.isVisible = false
        tvCountry.isVisible = false
        tvClinic.isVisible = false
        tvGender.isVisible = false
        tvMobileNumber.isVisible = false
        tvPrimaryLanguage.isVisible = false
        tvLastName.isVisible = false
        tvFirstName.isVisible = false
        tvEndDatePermit.isVisible = false

        selectEndDatePermit.setBackgroundResource(R.drawable.white_border_background)
        llDegreeType.setBackgroundResource(R.drawable.white_border_background)
        permitLL.setBackgroundResource(R.drawable.white_border_background)
        llCollege.setBackgroundResource(R.drawable.white_border_background)
        uploadDocumentLL.setBackgroundResource(R.drawable.white_border_background)
        selectEndDate.setBackgroundResource(R.drawable.white_border_background)
        llCollege.setBackgroundResource(R.drawable.white_border_background)
        llPractice.setBackgroundResource(R.drawable.white_border_background)
        llSpecialization.setBackgroundResource(R.drawable.white_border_background)
        llEmergencyNumber.setBackgroundResource(R.drawable.white_border_background)
        llFrom.setBackgroundResource(R.drawable.white_border_background)
        llCity.setBackgroundResource(R.drawable.white_border_background)
        llZipCode.setBackgroundResource(R.drawable.white_border_background)
        stateLL.setBackgroundResource(R.drawable.white_border_background)
        llCountry.setBackgroundResource(R.drawable.white_border_background)
        clinicLL.setBackgroundResource(R.drawable.white_border_background)
        llGender.setBackgroundResource(R.drawable.white_border_background)
        llMobileNumber.setBackgroundResource(R.drawable.white_border_background)
        primaryLanguageLL.setBackgroundResource(R.drawable.white_border_background)
        lastNameLL.setBackgroundResource(R.drawable.white_border_background)
        firstNameLL.setBackgroundResource(R.drawable.white_border_background)
    }



    fun makeAppointmentValidation(
        etPetName: EditText,
        llPetName: LinearLayout,
        tvPetName: TextView,
        etCell: EditText,
        cellLL: LinearLayout,
        tvCell: TextView,
        etLandLine: EditText,
        landLineLL: LinearLayout,
        tvLandLine: TextView,
        etFaxNumber: EditText,
        llFaxNumber: LinearLayout,
        tvFaxNumber: TextView,
        appointmentType: Spinner,
        llAppointmentType: LinearLayout,
        tvAppointmentType: TextView,
        vaccinationDate: TextView,
        llVaccinationDate: RelativeLayout,
        tvVaccinationDate: TextView,
        etReasonForVisit: EditText,
        llReasonForVisit: LinearLayout,
        tvReasonForVisit: TextView,
        etOtherMedicalConditions: EditText,
        llOtherMedicalConditions: LinearLayout,
        tvOtherMedicalConditions: TextView,
        etSymptoms: TextView,
        llSymptoms: LinearLayout,
        tvSymptoms: TextView,
        etCurrentMeditation: EditText,
        llCurrentMeditation: LinearLayout,
        tvCurrentMeditation: TextView,
        etDosage: EditText,
        llDosage: LinearLayout,
        tvDosage: TextView,
        etDiet: EditText,
        llDiet: LinearLayout,
        tvDiet: TextView,
        etAppointment: TextView,
        llAppointment: LinearLayout,
        tvAppointment: TextView,
        context: Context,
        llGender: LinearLayout,
        genderSpinner: Spinner,
        tvGender: TextView,
        llAboutGender: LinearLayout,
        aboutGenderSpinner: Spinner,
        tvAboutGender: TextView,
        llHowHear: LinearLayout,
        howHear1: Spinner,
        tvHowHear: TextView
    ):Boolean{

        tvPetName.text = ""
        tvPetName.isVisible = false
        tvCell.text = ""
        tvCell.isVisible = false
        tvLandLine.text = ""
        tvLandLine.isVisible = false
        tvFaxNumber.text = ""
        tvFaxNumber.isVisible = false
        tvAppointmentType.text = ""
        tvAppointmentType.isVisible = false
        tvVaccinationDate.text = ""
        tvVaccinationDate.isVisible = false
        tvReasonForVisit.text = ""
        tvReasonForVisit.isVisible = false
        tvOtherMedicalConditions.text = ""
        tvOtherMedicalConditions.isVisible = false
        tvSymptoms.text = ""
        tvSymptoms.isVisible = false
        tvCurrentMeditation.text = ""
        tvCurrentMeditation.isVisible = false
        tvDosage.text = ""
        tvDosage.isVisible = false
        tvDiet.text = ""
        tvDiet.isVisible = false
        tvAppointment.text = ""
        tvAppointment.isVisible = false
        tvGender.isVisible = false
        tvAboutGender.isVisible = false
        tvHowHear.isVisible = false

        llPetName.setBackgroundResource(R.drawable.white_border_background)
        cellLL.setBackgroundResource(R.drawable.white_border_background)
        landLineLL.setBackgroundResource(R.drawable.white_border_background)
        llFaxNumber.setBackgroundResource(R.drawable.white_border_background)
        llAppointmentType.setBackgroundResource(R.drawable.white_border_background)
        llVaccinationDate.setBackgroundResource(R.drawable.white_border_background)
        llReasonForVisit.setBackgroundResource(R.drawable.white_border_background)
        llOtherMedicalConditions.setBackgroundResource(R.drawable.white_border_background)
        llSymptoms.setBackgroundResource(R.drawable.white_border_background)
        llCurrentMeditation.setBackgroundResource(R.drawable.white_border_background)
        llDosage.setBackgroundResource(R.drawable.white_border_background)
        llDiet.setBackgroundResource(R.drawable.white_border_background)
        llAppointment.setBackgroundResource(R.drawable.white_border_background)
        llHowHear.setBackgroundResource(R.drawable.white_border_background)
        llGender.setBackgroundResource(R.drawable.white_border_background)
        llAboutGender.setBackgroundResource(R.drawable.white_border_background)



        if (etPetName.text.isEmpty()) {
            tvPetName.visibility = View.VISIBLE
            tvPetName.text = context.getString(R.string.pet_name_enter)
            tvPetName.setTextColor(Color.parseColor("#C63636"))
            llPetName.setBackgroundResource(R.drawable.errordrawable)
            return false
        }else if (etCell.text.isEmpty()) {
            tvCell.visibility = View.VISIBLE
            tvCell.text = context.getString(R.string.enter_cell_number_valid)
            tvCell.setTextColor(Color.parseColor("#C63636"))
            cellLL.setBackgroundResource(R.drawable.errordrawable)
            return false
        }else if (appointmentType.selectedItem.equals("Select appointment type")) {

            tvAppointmentType.visibility = View.VISIBLE
            tvAppointmentType.text = context.getString(R.string.select_appointment_validation)
            tvAppointmentType.setTextColor(Color.parseColor("#C63636"))
            llAppointmentType.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (howHear1.selectedItem.equals("Select how did you hear")) {

            tvHowHear.visibility = View.VISIBLE
            tvHowHear.text = context.getString(R.string.how_hear_validation)
            tvHowHear.setTextColor(Color.parseColor("#C63636"))
            llHowHear.setBackgroundResource(R.drawable.errordrawable)
            return false
        }else if (genderSpinner.selectedItem.equals("Select gender")) {

            tvGender.visibility = View.VISIBLE
            tvGender.text = context.getString(R.string.select_gender)
            tvGender.setTextColor(Color.parseColor("#C63636"))
            llGender.setBackgroundResource(R.drawable.errordrawable)
            return false
        }else if (aboutGenderSpinner.selectedItem.equals("Select about gender")) {

            tvAboutGender.visibility = View.VISIBLE
            tvAboutGender.text = context.getString(R.string.gender_about_validation)
            tvAboutGender.setTextColor(Color.parseColor("#C63636"))
            llAboutGender.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (vaccinationDate.text.isEmpty()) {
            tvVaccinationDate.visibility = View.VISIBLE
            tvVaccinationDate.text = context.getString(R.string.select_vaccination_date_validation)
            tvVaccinationDate.setTextColor(Color.parseColor("#C63636"))
            llVaccinationDate.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (etReasonForVisit.text.isEmpty()) {
            tvReasonForVisit.visibility = View.VISIBLE
            tvReasonForVisit.text = context.getString(R.string.reson_visiting)
            tvReasonForVisit.setTextColor(Color.parseColor("#C63636"))
            llReasonForVisit.setBackgroundResource(R.drawable.errordrawable)
            return false
        } else if (etOtherMedicalConditions.text.isEmpty()) {
            tvOtherMedicalConditions.visibility = View.VISIBLE
            tvOtherMedicalConditions.text = context.getString(R.string.medical_conditions_validation)
            tvOtherMedicalConditions.setTextColor(Color.parseColor("#C63636"))
            llOtherMedicalConditions.setBackgroundResource(R.drawable.errordrawable)
            return false
        }else if (etSymptoms.text.isEmpty()) {
            tvSymptoms.visibility = View.VISIBLE
            tvSymptoms.text = context.getString(R.string.symptom_validation)
            tvSymptoms.setTextColor(Color.parseColor("#C63636"))
            llSymptoms.setBackgroundResource(R.drawable.errordrawable)
            return false
        }else if (etCurrentMeditation.text.isEmpty()) {
            tvCurrentMeditation.visibility = View.VISIBLE
            tvCurrentMeditation.text = context.getString(R.string.current_medication_validation)
            tvCurrentMeditation.setTextColor(Color.parseColor("#C63636"))
            llCurrentMeditation.setBackgroundResource(R.drawable.errordrawable)
            return false
        }else if (etDosage.text.isEmpty()) {
            tvDosage.visibility = View.VISIBLE
            tvDosage.text = context.getString(R.string.current_dosage_validation)
            tvDosage.setTextColor(Color.parseColor("#C63636"))
            llDosage.setBackgroundResource(R.drawable.errordrawable)
            return false
        }else if (etDiet.text.isEmpty()) {
            tvDiet.visibility = View.VISIBLE
            tvDiet.text = context.getString(R.string.current_diet_validation)
            tvDiet.setTextColor(Color.parseColor("#C63636"))
            llDiet.setBackgroundResource(R.drawable.errordrawable)
            return false
        }else if (etAppointment.text.isEmpty()) {
            tvAppointment.visibility = View.VISIBLE
            tvAppointment.text = context.getString(R.string.date_time_validation)
            tvAppointment.setTextColor(Color.parseColor("#C63636"))
            llAppointment.setBackgroundResource(R.drawable.errordrawable)
            return false
        }else{

            tvPetName.text = ""
            tvPetName.isVisible = false
            tvCell.text = ""
            tvCell.isVisible = false
            tvLandLine.text = ""
            tvLandLine.isVisible = false
            tvFaxNumber.text = ""
            tvFaxNumber.isVisible = false
            tvAppointmentType.text = ""
            tvAppointmentType.isVisible = false
            tvVaccinationDate.text = ""
            tvVaccinationDate.isVisible = false
            tvReasonForVisit.text = ""
            tvReasonForVisit.isVisible = false
            tvOtherMedicalConditions.text = ""
            tvOtherMedicalConditions.isVisible = false
            tvSymptoms.text = ""
            tvSymptoms.isVisible = false
            tvCurrentMeditation.text = ""
            tvCurrentMeditation.isVisible = false
            tvDosage.text = ""
            tvDosage.isVisible = false
            tvDiet.text = ""
            tvDiet.isVisible = false
            tvAppointment.text = ""
            tvAppointment.isVisible = false

            llPetName.setBackgroundResource(R.drawable.white_border_background)
            cellLL.setBackgroundResource(R.drawable.white_border_background)
            landLineLL.setBackgroundResource(R.drawable.white_border_background)
            llFaxNumber.setBackgroundResource(R.drawable.white_border_background)
            llAppointmentType.setBackgroundResource(R.drawable.white_border_background)
            llVaccinationDate.setBackgroundResource(R.drawable.white_border_background)
            llReasonForVisit.setBackgroundResource(R.drawable.white_border_background)
            llOtherMedicalConditions.setBackgroundResource(R.drawable.white_border_background)
            llSymptoms.setBackgroundResource(R.drawable.white_border_background)
            llCurrentMeditation.setBackgroundResource(R.drawable.white_border_background)
            llDosage.setBackgroundResource(R.drawable.white_border_background)
            llDiet.setBackgroundResource(R.drawable.white_border_background)
            llAppointment.setBackgroundResource(R.drawable.white_border_background)




            return true
        }


    }






}