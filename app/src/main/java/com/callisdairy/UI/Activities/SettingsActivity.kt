package com.callisdairy.UI.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.callisdairy.Interface.CommonDialogInterface
import com.callisdairy.R
import com.callisdairy.Socket.SocketManager
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Vendor.ChooseTypeActivity
import com.callisdairy.databinding.ActivitySettingsBinding
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.NotificationViewModel
import com.callisdairy.extension.androidExtension
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity(), CommonDialogInterface {

    private lateinit var binding : ActivitySettingsBinding
    lateinit var socketInstance: SocketManager

    var from = ""
    var accountType = ""

    private val viewModel : NotificationViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.attributes.windowAnimations = R.style.Fade

        intent?.getStringExtra("from")?.let { from = it }


        socketInstance = SocketManager.getInstance(this)
        val userId = SavedPrefManager.getStringPreferences(this, SavedPrefManager.userId).toString()
        socketInstance.onlineUser(userId)

        binding.BackClick.setOnClickListener {
            finishAfterTransition()

        }


        if (from == "vendor"){
            binding.Accountprivacy.isVisible = false
        }



        binding.changePasswordButton.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }


        binding.languageSettingButton.setOnClickListener {
            val intent = Intent(this, ChangeLanguageActivity::class.java)
            if (from == "vendor"){
                intent.putExtra("from","vendor")
            }
            startActivity(intent)
        }


        binding.Accountprivacy.setOnClickListener {
            val intent = Intent(this, CommonActivityForViewActivity::class.java)
            intent.putExtra("flag","Account")
            startActivity(intent)
        }


        binding.DeleteAccount.setSafeOnClickListener{
            accountType = "delete"
            androidExtension.showDialogDeleteAccount(this,getString(R.string.are_you_sure_you_want_to_delete),getString(R.string.delete),this)
        }

        binding.DeactivateAccount.setSafeOnClickListener{
            accountType = "deactivate"
            androidExtension.showDialogDeleteAccount(this,getString(R.string.are_you_sure_you_want_to_deactivate),getString(R.string.deactivate),this)
        }



        observeResponseAccountDelete()
        observeResponseAccountDeactivate()



    }

    override fun commonWork() {
        val token = SavedPrefManager.getStringPreferences(this,SavedPrefManager.Token).toString()
        if (accountType == "delete"){
            viewModel.deleteAccountApi(token)
        }
        if (accountType == "deactivate"){
            viewModel.deactivateAccountApi(token)
        }

    }

    private fun observeResponseAccountDelete() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._deleteAccountData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                try {
                                    val intent = Intent(this@SettingsActivity, ChooseTypeActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                        }

                        is Resource.Loading -> {

                        }

                        is Resource.Empty -> {
                        }

                    }

                }

            }

        }


    }

    private fun observeResponseAccountDeactivate() {

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._deactivateAccountData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                try {
                                    val intent = Intent(this@SettingsActivity, ChooseTypeActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                        }

                        is Resource.Loading -> {

                        }

                        is Resource.Empty -> {
                        }

                    }

                }

            }

        }


    }




}