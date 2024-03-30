package com.callisdairy.AdapterVendors

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.api.response.CountryList
import com.callisdairy.databinding.PetTypeBinding

class petTypeSelectAdapter(
    var context: Context,
    var data: List<CountryList>, ) : RecyclerView.Adapter<petTypeSelectAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PetTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val tagData = data[position]

            holder.binding.tagPeopleCK.isChecked = tagData.isSelected

            holder.binding.txtName.text = tagData.petCategoryName


            holder.binding.tagPeopleCK.setOnClickListener {
                tagData.isSelected = !tagData.isSelected
                if (!tagData.isSelected) {
                    tagData.productName = ""
                } else {
                    tagData.productName = tagData.petCategoryName
                }
                notifyDataSetChanged()
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterData(filteredList: List<CountryList>) {
        data = filteredList
        notifyDataSetChanged()
    }



    inner class ViewHolder(val binding: PetTypeBinding) :
        RecyclerView.ViewHolder(binding.root)
}