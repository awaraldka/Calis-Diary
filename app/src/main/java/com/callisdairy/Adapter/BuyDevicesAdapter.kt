package com.callisdairy.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.ModalClass.TrackingDevice
import com.callisdairy.databinding.BuyDevicesListBinding
import com.callisdairy.extension.setSafeOnClickListener

class BuyDevicesAdapter (var context: Context,
                         var data:List<TrackingDevice>
):
    RecyclerView.Adapter<BuyDevicesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = BuyDevicesListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
         data[position].apply {
             holder.binding.text.text = name



             holder.binding.click.setSafeOnClickListener {



                 val uri = Uri.parse(url) // missing 'http://' will cause crashed
                 val intent = Intent(Intent.ACTION_VIEW, uri)
                 context.startActivity(intent)
             }
         }


        }catch (_:Exception){

        }

    }

    override fun getItemCount(): Int {
        return data.size
//        return 5
    }


    inner class ViewHolder(val binding: BuyDevicesListBinding) :
        RecyclerView.ViewHolder(binding.root)


}