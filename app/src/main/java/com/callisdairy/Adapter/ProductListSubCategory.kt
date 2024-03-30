package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Interface.SubCategoryClick
import com.callisdairy.R
import com.callisdairy.api.response.SubCategoryDocs
import com.callisdairy.databinding.SubCategoryBinding

class ProductListSubCategory(val context:Context, var data:ArrayList<SubCategoryDocs>, val click: SubCategoryClick):RecyclerView.Adapter<ProductListSubCategory.ViewHolder>() {

    var selectItem:Int = 0
    var flag = false
     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         val binding = SubCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
         return ViewHolder(binding)
     }

     @RequiresApi(Build.VERSION_CODES.M)
     @SuppressLint("UseCompatLoadingForDrawables")
     override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         val dataList = data[position]
         try{
             holder.binding.subCategoryName.text = dataList.subCategoryName

             holder.binding.subCategoryNameClick.setOnClickListener {
                 flag = true
                 selectItem=position
                 click.getSubCategoryValue(dataList._id,dataList.subCategoryName )
                 notifyDataSetChanged()
             }

             if(flag) {
                 if (selectItem == position) {
                     holder.binding.subCategoryNameClick.isEnabled = false
                     holder.binding.subCategoryNameClick.background = context.getDrawable(R.drawable.white_border_background)
                     holder.binding.subCategoryName.setTextColor(context.getColor(R.color.black))

                 } else {
                     holder.binding.subCategoryNameClick.isEnabled = true
                     holder.binding.subCategoryNameClick.background = context.getDrawable(R.drawable.button_background)
                     holder.binding.subCategoryName.setTextColor(context.getColor(R.color.white))

                 }
             }

         }catch (e:Exception){
             e.printStackTrace()
         }
     }

     override fun getItemCount(): Int {
        return data.size
     }

    inner class ViewHolder(val binding: SubCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)


 }