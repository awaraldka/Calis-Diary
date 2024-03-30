package com.callisdairy.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.CategoryView
import com.callisdairy.ModalClass.MarketModalClass
import com.callisdairy.R
import com.callisdairy.databinding.CategoryItemBinding

class MarketCategoryAdapter(val context:Context, var data:ArrayList<MarketModalClass>, val click: CategoryView):RecyclerView.Adapter<MarketCategoryAdapter.ViewHolder>() {


     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         val binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
         return ViewHolder(binding)
     }

     override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         val dataList = data[position]
         try{
             Glide.with(context).load(dataList.Image).placeholder(R.drawable.notfound_image).into(holder.binding.ImageCategory)
             holder.binding.Description.text = dataList.Name

             holder.binding.ImageCategory.setOnClickListener {
                 click.petCategoryView(holder.binding.Description.text.toString())
             }


         }catch (e:Exception){
             e.printStackTrace()
         }
     }

     override fun getItemCount(): Int {
        return data.size
     }

    inner class ViewHolder(val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)


 }