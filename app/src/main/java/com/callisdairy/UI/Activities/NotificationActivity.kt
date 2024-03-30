package com.callisdairy.UI.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.Adapter.NotificationAdapter
import com.callisdairy.ModalClass.NotificationModelClass
import com.callisdairy.R
import com.callisdairy.databinding.ActivityNotificationBinding
import com.callisdairy.extension.setSafeOnClickListener


class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    var data : ArrayList<NotificationModelClass> = ArrayList()
    lateinit var Adapter : NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.back.setSafeOnClickListener {
            finishAfterTransition()
        }

//        data.add(NotificationModelClass(R.drawable.img11,R.drawable.img22,"Lorem ipsum dolor sit abcdefg hijklmano ","1m ago.",2))
//        data.add(NotificationModelClass(R.drawable.img11,R.drawable.img22,"Lorem ipsum dolor sit amet, elit. ","1m ago.",2))
//        data.add(NotificationModelClass(R.drawable.img11,R.drawable.img22,"Lorem ipsum dolor sit amet, elit. ","1m ago.",2))



//        binding.notificationRecycler.layoutManager = LinearLayoutManager(this)
//        Adapter = NotificationAdapter(this,data)
//        binding.notificationRecycler.adapter = Adapter

        binding.back.setSafeOnClickListener{
            finishAfterTransition()
        }


    }



}