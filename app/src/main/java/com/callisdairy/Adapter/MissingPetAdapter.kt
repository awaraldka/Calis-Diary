package com.callisdairy.Adapter

import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Interface.MissingPest
import com.callisdairy.ModalClass.descriptionImage
import com.callisdairy.UI.Activities.AddMissingPetActivity
import com.callisdairy.Utils.DateFormat
import com.callisdairy.api.response.MissingPetDocs
import com.callisdairy.databinding.MissingPetModellayoutBinding
import com.callisdairy.extension.setSafeOnClickListener

class MissingPetAdapter(
    val context: Context,
    val dataMissingPet: ArrayList<MissingPetDocs>,
    val click: MissingPest,
) :
    RecyclerView.Adapter<MissingPetAdapter.ViewHolder>() {
    lateinit var imageAdaptor: ImageSliderAdaptor
    var data: ArrayList<descriptionImage> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            MissingPetModellayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val listData = dataMissingPet[position]

            holder.binding.petName.text  = listData.petName
            holder.binding.userName.text = listData.userDetails.name
            holder.binding.address.text = listData.userDetails.address
            holder.binding.timePost.text = DateFormat.covertTimeOtherFormat(listData.createdAt)



            holder.binding.llClick.setOnClickListener {
                click.viewMissingPet(listData._id)
            }

            holder.binding.EditPet.isVisible = false
            holder.binding.DeleteEvent.isVisible = false
            holder.binding.trackingLl.isVisible = true

            holder.binding.indicator.isVisible = listData.petImage.size > 1

            imageAdaptor = ImageSliderAdaptor(context, listData.petImage)
            holder.binding.storeViewpager.adapter = imageAdaptor
            holder.binding.indicator.setViewPager(holder.binding.storeViewpager)





            holder.binding.share.setSafeOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, listData.petImage.getOrNull(0))
                    putExtra(Intent.EXTRA_TEXT, "Cali's Diary")
                    putExtra(Intent.EXTRA_TEXT, "https://calisdiary.com/")
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                context.startActivity(shareIntent)
            }


            holder.binding.trackTv.setOnClickListener {
                try {
                    click.trackPet(listData.trackerID)
//                    click.trackPet("357445100070479")
                }catch (e:Exception){
//                    Toast.makeText(context, "You don't have device is so you can not ", Toast.LENGTH_SHORT).show()
                }

            }



        } catch (_: Exception) {

        }
    }

    override fun getItemCount(): Int {

        return dataMissingPet.size
    }


    inner class ViewHolder(val binding: MissingPetModellayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


}