package com.callisdairy.UI.Activities.subscription

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Interface.BuyPlan
import com.callisdairy.api.response.PlanListDoc
import com.callisdairy.databinding.PlanViewLayoutBinding

class PlanListAdapter(val context: Context, var data: List<PlanListDoc>,val click: BuyPlan):RecyclerView.Adapter<PlanListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PlanViewLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val binding = holder.binding
            data[position].apply {

                binding.name.text =  description

                binding.buyNow.setOnClickListener {
                    click.buy(id)
                }


            }




        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(val binding: PlanViewLayoutBinding) : RecyclerView.ViewHolder(binding.root)
}