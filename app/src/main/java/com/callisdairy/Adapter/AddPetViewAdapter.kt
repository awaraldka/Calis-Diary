package com.callisdairy.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.RemoveImage
import com.callisdairy.ModalClass.addPetRequest
import com.callisdairy.api.request.mediaUrls
import com.callisdairy.databinding.ViewSelectedValueBinding
import java.util.ArrayList

class AddPetViewAdapter(val context: Context, var mArrayUri: ArrayList<mediaUrls>, val click: RemoveImage):RecyclerView.Adapter<AddPetViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewSelectedValueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val listData = mArrayUri[position]
            Glide.with(context).load(listData.media.mediaUrlMobile).into(holder.binding.image)

            holder.binding.removeButton.setOnClickListener {
                click.deleteImage(position)
            }

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return mArrayUri.size
    }

    inner class ViewHolder(val binding: ViewSelectedValueBinding) : RecyclerView.ViewHolder(binding.root)
}