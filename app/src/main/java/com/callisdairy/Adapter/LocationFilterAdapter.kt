package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Interface.GetFilterData
import com.callisdairy.ModalClass.MarketFilter
import com.callisdairy.databinding.FilterMarketBinding

class LocationFilterAdapter(var context: Context, val dataFilter: ArrayList<MarketFilter>, val click: GetFilterData):RecyclerView.Adapter<LocationFilterAdapter.ViewHolder>() {

    var selectItem:Int = 0
    var flag = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FilterMarketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {

            dataFilter[position].apply {
                holder.binding.txtName.text = name
                holder.binding.tagPeopleCK.isChecked = isFiltered

                if(flag){
                    holder.binding.tagPeopleCK.isChecked = selectItem == position
                }

                holder.binding.cardClickView.setOnClickListener {
                    flag = true
                    selectItem=position
                    notifyItemChanged(position)
                    notifyDataSetChanged()
                    click.getFilterData(holder.binding.txtName.text.toString())
                }


            }

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return dataFilter.size
    }

    inner class ViewHolder(val binding: FilterMarketBinding) : RecyclerView.ViewHolder(binding.root)
}