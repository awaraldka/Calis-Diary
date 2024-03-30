package com.callisdairy.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.HomeStoryClick
import com.callisdairy.ModalClass.storymodel.StoryUser
import com.callisdairy.R
import com.callisdairy.UI.Activities.storyui.StoryActivity
import com.callisdairy.api.response.UserStoriesDoc
import com.callisdairy.databinding.HomeStoryViewBinding

class StoryHomeAdapter(
    val context: Context,
    val data: List<UserStoriesDoc>,
    var storyUser: ArrayList<StoryUser>?,
    var click: HomeStoryClick
) :
    RecyclerView.Adapter<StoryHomeAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            HomeStoryViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val listData = data[position]


            Glide.with(context).load(listData.profilePic).placeholder(R.drawable.placeholder_pet).into(holder.binding.userProfile)
            holder.binding.StoryViewName.text = listData.userName

            if(position == 0 && listData.stories.isEmpty()) {
                Glide.with(context).load(R.drawable.add_story).into(holder.binding.userProfile)
                Glide.with(context).load(listData.petPic).placeholder(R.drawable.placeholder_pet).into(holder.binding.StoryImage)
            }




            if (listData.stories.isNotEmpty()){
                val lastIndex = listData.stories.size - 1
                val lastImage = listData.stories[lastIndex]
                Glide.with(context).load(lastImage.image.thumbnail).placeholder(R.drawable.placeholder).into(holder.binding.StoryImage)

            }else{
                Glide.with(context).load(listData.petPic).placeholder(R.drawable.placeholder_pet).into(holder.binding.StoryImage)

            }






            holder.binding.MainLayout.setOnClickListener {
                if(listData.stories.isEmpty()) {
                    click.addStory()
                }else{

                        val intent = Intent(context, StoryActivity::class.java)
                        intent.putExtra("storyData", storyUser)
                        intent.putExtra("currentPosition", position)
                        context.startActivity(intent)



                }


            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(val binding: HomeStoryViewBinding) :
        RecyclerView.ViewHolder(binding.root)

}