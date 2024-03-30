package com.callisdairy.AdapterVendors

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Interface.VendorProductClick
import com.callisdairy.R
import com.callisdairy.Utils.DateFormat
import com.callisdairy.api.response.MyPetListDocs
import com.callisdairy.databinding.CommonLayoutBinding

class VendorPetAdapter(
    var context: Context, var data: ArrayList<MyPetListDocs>, val click: VendorProductClick
):
    RecyclerView.Adapter<VendorPetAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CommonLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            holder.binding.firstName.text = context.getString(R.string.pet_name)
            holder.binding.secondName.text =  context.getString(R.string.pet_breed)
            holder.binding.thirdName.text =  context.getString(R.string.purchased_store)
            holder.binding.fourthName.text =  context.getString(R.string.language)
            holder.binding.fifthName.text =  context.getString(R.string.dob)
            holder.binding.sixthName.text = context.getString(R.string.created_at)
            holder.binding.statusForPet.isVisible = true



            data[position].apply {
                holder.binding.productName.text = petName?.takeIf { it.isNotBlank() }?: "NA"
                holder.binding.productId.text = breed?.takeIf { it.isNotBlank() }?: "NA"
                holder.binding.categoryName.text = purchesStore?.takeIf { it.isNotBlank() }?: "NA"
                holder.binding.subCategoryName.text = language?.takeIf { it.isNotBlank() }?: "NA"
                holder.binding.date.text = DateFormat.petDobDateFormat(dob)?.takeIf { it.isNotBlank() }?: "NA"
                holder.binding.productStatus.text = DateFormat.dateFormatEvent(createdAt)?.replace("am", "AM")?.replace("pm", "PM")?.takeIf { it.isNotBlank() }?: "NA"


                if (isMarketPlace){
                    holder.binding.etstatus.text = "PUBLISHED"
                    holder.binding.etstatus.setTextColor(android.graphics.Color.parseColor("#49CC90"))

                }else{
                    holder.binding.etstatus.text = "UN-PUBLISHED"
                    holder.binding.etstatus.setTextColor(android.graphics.Color.parseColor("#FF0000"))
                }


                holder.binding.viewProduct.setOnClickListener {
                    click.viewProduct(_id!!)

                }



            }


        }catch (_:Exception){

        }
    }

    override fun getItemCount(): Int {
        return data.size
}


    inner class ViewHolder(val binding: CommonLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


}