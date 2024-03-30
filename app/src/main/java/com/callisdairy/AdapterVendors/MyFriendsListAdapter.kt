package com.callisdairy.AdapterVendors

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.callisdairy.Interface.ProfileFollowUnfollow
import com.callisdairy.Interface.UserProfileClick
import com.callisdairy.R
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.FollowersListDocs
import com.callisdairy.databinding.FollowListBinding

class MyFriendsListAdapter(
    var context: Context,
    val docs: ArrayList<FollowersListDocs>,
    val click: UserProfileClick,
    val followUnfollow: ProfileFollowUnfollow
) : RecyclerView.Adapter<MyFriendsListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FollowListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val followingData = docs[position]

            holder.binding.userName.text =  followingData.userName
            Glide.with(context).load(followingData.petPic).placeholder(R.drawable.placeholder_pet).into(holder.binding.petProfile)
            Glide.with(context).load(followingData.profilePic).placeholder(R.drawable.placeholder).into(holder.binding.userProfile)


            val userid = SavedPrefManager.getStringPreferences(context,SavedPrefManager.userId).toString()

            var privacyType = followingData.privacyType

            if (userid == followingData._id){
                holder.binding.FollowButton.isVisible = false
                holder.binding.RequestedButton.isVisible = false
                holder.binding.FollowingButton.isVisible = false
            }else{

                when (privacyType.lowercase()) {
                    "public" -> {
                        if (followingData.isFollowed) {
                            holder.binding.FollowButton.isVisible = false
                            holder.binding.RequestedButton.isVisible = false
                            holder.binding.FollowingButton.isVisible = true
                        }  else {
                            holder.binding.FollowButton.isVisible = true
                            holder.binding.RequestedButton.isVisible = false
                            holder.binding.FollowingButton.isVisible = false
                        }
                    }

                    "private" -> {
                        if (followingData.isFollowed && !followingData.isRequested) {
                            holder.binding.FollowButton.isVisible = false
                            holder.binding.RequestedButton.isVisible = false
                            holder.binding.FollowingButton.isVisible = true
                        } else  if (!followingData.isFollowed && followingData.isRequested) {
                            holder.binding.FollowButton.isVisible = false
                            holder.binding.RequestedButton.isVisible = true
                            holder.binding.FollowingButton.isVisible = false
                        } else {
                            holder.binding.FollowButton.isVisible = true
                            holder.binding.RequestedButton.isVisible = false
                            holder.binding.FollowingButton.isVisible = false
                        }
                    }
                }

            }



            holder.binding.FollowingButton.setOnClickListener {
                buttonHandler(holder,privacyType, followingData)
                followUnfollow.followUnfollowTwo(followingData._id, position, docs)
            }

            holder.binding.FollowButton.setOnClickListener {
                buttonHandler(holder, privacyType, followingData)
                followUnfollow.followUnfollowTwo(followingData._id, position, docs)
            }

            holder.binding.RequestedButton.setOnClickListener {
                buttonHandler(holder, privacyType, followingData)
                followUnfollow.followUnfollowTwo(followingData._id, position, docs)
            }

            holder.binding.viewProfile.setOnClickListener {
                click.userProfileListener(followingData._id, followingData.userName)
            }

            holder.binding.profileClick.setOnClickListener {
                click.userProfileListener(followingData._id, followingData.userName)
            }

        }catch (e:Exception){
            e.printStackTrace()
        }



    }

    private fun buttonHandler(
        holder: ViewHolder,
        privacyType: String,
        followingData: FollowersListDocs
    ) {
        when (privacyType.lowercase()) {
            "public" -> {
                if (holder.binding.FollowButton.isVisible) {
                    holder.binding.FollowButton.isVisible = false
                    holder.binding.RequestedButton.isVisible = false
                    holder.binding.FollowingButton.isVisible = true
                    followingData.isFollowed = true
                } else if (holder.binding.FollowingButton.isVisible) {
                    holder.binding.FollowButton.isVisible = true
                    holder.binding.RequestedButton.isVisible = false
                    holder.binding.FollowingButton.isVisible = false
                    followingData.isFollowed = false
                }
            }

            "private" -> {
                if (holder.binding.FollowButton.isVisible) {
                    holder.binding.FollowButton.isVisible = false
                    holder.binding.RequestedButton.isVisible = true
                    holder.binding.FollowingButton.isVisible = false
                    followingData.isFollowed = false
                    followingData.isRequested = true

                } else if (holder.binding.RequestedButton.isVisible) {
                    holder.binding.FollowButton.isVisible = true
                    holder.binding.RequestedButton.isVisible = false
                    holder.binding.FollowingButton.isVisible = false
                    followingData.isFollowed = false
                    followingData.isRequested = false
                } else if (holder.binding.FollowingButton.isVisible) {
                    holder.binding.FollowButton.isVisible = true
                    holder.binding.RequestedButton.isVisible = false
                    holder.binding.FollowingButton.isVisible = false
                    followingData.isFollowed = false
                    followingData.isRequested = false

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return docs.size

    }


    inner class ViewHolder(val binding: FollowListBinding) : RecyclerView.ViewHolder(binding.root) {

    }
}