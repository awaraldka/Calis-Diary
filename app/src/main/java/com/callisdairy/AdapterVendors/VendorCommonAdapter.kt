package com.callisdairy.AdapterVendors

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Interface.VendorProductClick
import com.callisdairy.Utils.DateFormat
import com.callisdairy.api.response.ProductsListDocs
import com.callisdairy.databinding.CommonLayoutBinding

class VendorCommonAdapter(
    var context: Context, var data: ArrayList<ProductsListDocs>,val click: VendorProductClick
):
    RecyclerView.Adapter<VendorCommonAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CommonLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {

            data[position].apply {
                holder.binding.productName.text = productName
                holder.binding.productId.text = productGenerateId
                holder.binding.categoryName.text = categoryId!!.categoryName
                holder.binding.subCategoryName.text = subCategoryId!!.subCategoryName
                holder.binding.date.text = DateFormat.dateFormatEvent(createdAt)?.replace("am", "AM")?.replace("pm", "PM")
                holder.binding.productStatus.text = approveStatus

                if (approveStatus!!.lowercase() == "approved"){
                    holder.binding.productStatus.setTextColor(android.graphics.Color.parseColor("#49CC90"))
                }else{
                    holder.binding.productStatus.setTextColor(android.graphics.Color.parseColor("#FF0000"))

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