package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.CategoryClick
import com.callisdairy.R
import com.callisdairy.api.response.ListCategoryDocs
import com.callisdairy.databinding.ProductCategoryLayoutBinding

class ProductListCategory(val context:Context, var data:ArrayList<ListCategoryDocs>,val click: CategoryClick):RecyclerView.Adapter<ProductListCategory.ViewHolder>() {

    var selectItem:Int = 0
    var flag = false

     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         val binding = ProductCategoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
         return ViewHolder(binding)
     }

     @RequiresApi(Build.VERSION_CODES.M)
     @SuppressLint("ResourceAsColor", "NotifyDataSetChanged")
     override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         val dataList = data[position]
         try{
             Glide.with(context).load(dataList.categoryImage).placeholder(R.drawable.notfound_image).into(holder.binding.ImageCategory)
             holder.binding.Description.text = dataList.categoryName



             if(flag){
                 if (selectItem == position) {
                     holder.binding.ImageCategory.isEnabled = false
                     holder.binding.ImageCategory.borderWidth = 5
                     holder.binding.ImageCategory.borderColor = context.getColor(R.color.themeColor)


                 } else {
                     holder.binding.ImageCategory.isEnabled = true
                     holder.binding.ImageCategory.borderWidth = 1
                     holder.binding.ImageCategory.borderColor = R.color.white
                 }
             }

             holder.binding.ImageCategory.setOnClickListener {
                 flag = true
                 selectItem=position
                 click.getCategoryValue(dataList._id)
                 notifyDataSetChanged()
             }

         }catch (e:Exception){
             e.printStackTrace()
         }
     }

     override fun getItemCount(): Int {
        return data.size
     }

    inner class ViewHolder(val binding: ProductCategoryLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


 }