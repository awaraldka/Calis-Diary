package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.LocalizationClick
import com.callisdairy.ModalClass.language
import com.callisdairy.databinding.LanguageModellayoutBinding

class LanguageAdapter (var context: Context, var data:ArrayList<language>, val click: LocalizationClick):
    RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {


    var selectItem:Int = 0
    var flag = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LanguageModellayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val listData = data[position]

            Glide.with(context).load(listData.image).into(holder.binding.img1)
            holder.binding.txtName.text = listData.language
            holder.binding.secondName.text = listData.code

            holder.binding.radioButton.isChecked = listData.selectedLanguage == holder.binding.secondName.text


            if(flag) {
                holder.binding.radioButton.isChecked = selectItem == position
            }

            holder.binding.mainClick.setOnClickListener {
                click.getLanguage(listData.code)
                flag = true
                selectItem=position
                notifyDataSetChanged()
            }

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return data.size

    }


    inner class ViewHolder(val binding: LanguageModellayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


}