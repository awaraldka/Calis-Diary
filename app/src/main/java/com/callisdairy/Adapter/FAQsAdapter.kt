package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.R
import com.callisdairy.api.response.FaqResult
import com.callisdairy.databinding.FaqsModellayoutBinding

class FAQsAdapter (var context: Context,
                                 var data:ArrayList<FaqResult>
):
    RecyclerView.Adapter<FAQsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FaqsModellayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val listData = data[position]

            holder.binding.txtHeading.text = listData.question
            holder.binding.txtContent.text = Html.fromHtml(listData.answer)

            holder.binding.txtContent.visibility = if (listData.expand) View.VISIBLE else View.GONE
            holder.binding.txtHeading.setOnClickListener {
                listData.expand = !listData.expand
                notifyDataSetChanged()

            }

            if (listData.expand){
                holder.binding.txtHeading.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_keyboard_arrow_down_24, 0)
            }else{
                holder.binding.txtHeading.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_forward, 0)
            }

        }catch (_:Exception){

        }

    }

    override fun getItemCount(): Int {
        return data.size
//        return 5
    }


    inner class ViewHolder(val binding: FaqsModellayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


}