package com.callisdairy.UI.Activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.Adapter.BuyDevicesAdapter
import com.callisdairy.ModalClass.TrackingDevice
import com.callisdairy.R
import com.callisdairy.databinding.ActivityTrackingDeviceBinding
import com.callisdairy.extension.setSafeOnClickListener

class TrackingDeviceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrackingDeviceBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrackingDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade


        binding.BackClicks.setSafeOnClickListener {
            finishAfterTransition()


        }



        val data = listOf(
            TrackingDevice("Garmin", "https://www.garmin.com/en-US/c/outdoor-recreation/sporting-dog-tracking-training-devices/"),
            TrackingDevice("Tractive", "https://tractive.com/"),
            TrackingDevice("Fi-Smart", "https://tryfi.com/learn"),
            TrackingDevice("Halo", "https://www.halocollar.com/"),
            TrackingDevice("Whistle", "https://www.whistle.com/products/whistle-health-gps-plus-dog-tracker-activity-monitor?variant=39481877168176"),
            TrackingDevice("Apple Air Tag", "https://spotonfence.com/products/spoton-gps-fence"),
            TrackingDevice("Jiobit", "https://www.jiobit.com/product")
        )
        setChatAdapter(data)


        binding.ChatClick.setOnClickListener {
            val intent = Intent(this, ChatsActivity::class.java)
            startActivity(intent)
        }





    }

    private fun setChatAdapter(datalist : List<TrackingDevice>) {
        binding.recyclerList.layoutManager = LinearLayoutManager(this)
        val deviceAdapter = BuyDevicesAdapter(this, datalist)
        binding.recyclerList.adapter = deviceAdapter
    }



}