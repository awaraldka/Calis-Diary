package com.callisdairy.AdapterVendors

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.RemoveImage
import com.callisdairy.ModalClass.AppointmentData
import com.callisdairy.ModalClass.BlockUserData
import com.callisdairy.ModalClass.addPetRequest
import com.callisdairy.ModalClass.feedbackDetails
import com.callisdairy.R
import com.callisdairy.Utils.DateFormat
import com.callisdairy.Vendor.Activities.CommonContainerActivity
import com.callisdairy.Vendor.Activities.DoctorRoleActivity
import com.callisdairy.api.request.mediaUrls
import com.callisdairy.api.response.Appointment
import com.callisdairy.api.response.UserFeedBackListDocs
import com.callisdairy.databinding.AppointmentListBinding
import com.callisdairy.databinding.BlockUnblockClientBinding
import com.callisdairy.databinding.FeedbackClientBinding
import com.callisdairy.databinding.ViewSelectedValueBinding
import com.callisdairy.extension.setSafeOnClickListener
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale

class FeedbackListAdapter(val context: Context, var dataFeedback: List<UserFeedBackListDocs>):RecyclerView.Adapter<FeedbackListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FeedbackClientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val binding = holder.binding
            dataFeedback[position].apply {
                Glide.with(context).load(userId.profilePic).placeholder(R.drawable.placeholder).into(binding.userProfilePicPublic)
                Glide.with(context).load(userId.petPic).placeholder(R.drawable.placeholder_pet).into(binding.petImage)
                binding.Name.text = userId.name
                binding.rating.text =  "Rating: $rating"
                binding.date.text = DateFormat.getDate(createdAt)


                binding.ServiceView.setSafeOnClickListener {
                    val myObject = feedbackDetails(userId.name,rating.toString(),message,DateFormat.getDate(createdAt)!!,userId.profilePic,userId.petPic)
                    val intent = Intent(context, CommonContainerActivity::class.java)
                    intent.putExtra("flag", "Feedback details")
                    intent.putExtra("myObject", myObject)
                    context.startActivity(intent)
                }


            }

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return dataFeedback.size
    }

    inner class ViewHolder(val binding: FeedbackClientBinding) : RecyclerView.ViewHolder(binding.root)
}