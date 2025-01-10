package com.callisdairy.extension

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.airbnb.lottie.LottieAnimationView
import com.callisdairy.Interface.AddPetInterface
import com.callisdairy.Interface.AsPerGoPay
import com.callisdairy.Interface.CancelAppointmentInterface
import com.callisdairy.Interface.CommonDialogInterface
import com.callisdairy.Interface.DeleteClick
import com.callisdairy.Interface.Finish
import com.callisdairy.Interface.FinishBack
import com.callisdairy.Interface.Logout
import com.callisdairy.Interface.ReportPost
import com.callisdairy.Interface.petProfile
import com.callisdairy.R
import com.callisdairy.UI.Activities.Login
import com.callisdairy.Utils.DateFormat
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Validations.FormValidations.finish
import com.callisdairy.pdfreader.PdfQuality
import com.callisdairy.pdfreader.PdfRendererView
import com.callisdairy.pdfreader.PdfViewerActivity.Companion.engine

object androidExtension {


    fun showDialogLogout(context: Context, message: String, title: String, click: Logout) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.logout_popup)
        val yesBtn = dialog.findViewById<LinearLayout>(R.id.llYesButton)
        val NoBtn = dialog.findViewById<LinearLayout>(R.id.llNoButton)
        val tvHeader = dialog.findViewById<TextView>(R.id.tvHeader)
        val tvText = dialog.findViewById<TextView>(R.id.tvText)
        dialog.window!!.attributes.windowAnimations = R.style.Fade



        dialog.setCancelable(false)
        NoBtn.setSafeOnClickListener {
            dialog.dismiss()
        }
        yesBtn.setSafeOnClickListener {
            dialog.cancel()
            click.logoutUser()

        }
        dialog.show()
        val window = dialog.window
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun cancelAppointment(context: Context, message: String, title: String, click: CancelAppointmentInterface) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.cancel_appointment)
        val yesBtn = dialog.findViewById<LinearLayout>(R.id.llYesButton)
        val startDate = dialog.findViewById<TextView>(R.id.startDate)
        val selectStartDate = dialog.findViewById<RelativeLayout>(R.id.selectStartDate)
        val NoBtn = dialog.findViewById<LinearLayout>(R.id.llNoButton)
        dialog.window!!.attributes.windowAnimations = R.style.Fade

        startDate.text = DateFormat.getCurrentDate()

        dialog.setCancelable(false)


        selectStartDate.setSafeOnClickListener {
            DateFormat.dateSelectorFutureDates(context,startDate)
        }


        NoBtn.setSafeOnClickListener {
            dialog.dismiss()
        }
        yesBtn.setSafeOnClickListener {
            dialog.cancel()
            click.commonWork(startDate.text.toString())
        }
        dialog.show()
        val window = dialog.window
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        )
    }

    fun showDialogDeleteAccount(
        context: Context,
        message: String,
        title: String,
        click: CommonDialogInterface
    ) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.logout_popup)
        val yesBtn = dialog.findViewById<LinearLayout>(R.id.llYesButton)
        val NoBtn = dialog.findViewById<LinearLayout>(R.id.llNoButton)
        val tvHeader = dialog.findViewById<TextView>(R.id.tvHeader)
        val tvText = dialog.findViewById<TextView>(R.id.tvText)
        dialog.window!!.attributes.windowAnimations = R.style.Fade


        tvText.text = message
        tvHeader.text = title



        dialog.setCancelable(false)
        NoBtn.setSafeOnClickListener {
            dialog.dismiss()
        }
        yesBtn.setSafeOnClickListener {
            click.commonWork()
            dialog.dismiss()
        }
        dialog.show()
        val window = dialog.window
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        )
    }

    fun showDialogAddProfile(context: Context, click: petProfile) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.add_profile)
        val yesBtn = dialog.findViewById<LinearLayout>(R.id.llYesButton)
        val NoBtn = dialog.findViewById<LinearLayout>(R.id.llNoButton)
        dialog.window!!.attributes.windowAnimations = R.style.Fade



        dialog.setCancelable(false)
        NoBtn.setSafeOnClickListener {
            dialog.dismiss()
            click.notAddingNow()
        }
        yesBtn.setSafeOnClickListener {
            dialog.cancel()
            click.addProfile()
        }
        dialog.show()
        val window = dialog.window
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        )
    }


    fun alertBox(message: String, context: Context) {
        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        builder.setIcon(R.mipmap.app_icon)
        builder.setTitle("Cali's Dairy")
        builder.setMessage(message)
        alertDialog?.window?.attributes?.windowAnimations = R.style.Fade

        builder.setPositiveButton("ok") { dialogInterface, which ->
            alertDialog!!.dismiss()
        }
        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog!!.show()
    }


    fun alertBoxFinish(message: String, context: Context,click:FinishBack) {
        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        builder.setIcon(R.mipmap.app_icon)
        alertDialog?.window?.attributes?.windowAnimations = R.style.Fade
        builder.setTitle("Cali's Dairy")
        builder.setMessage(message)
        builder.setPositiveButton("Yes") { _, _ ->
            alertDialog!!.dismiss()
            click.finishBackActivity()
        }

        builder.setNegativeButton("No") { _, _ ->
            alertDialog!!.dismiss()
        }

        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog!!.show()
    }


    fun alertBoxSendToLogin(message: String, context: Context) {
        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        builder.setIcon(R.mipmap.app_icon)
        alertDialog?.window?.attributes?.windowAnimations = R.style.Fade
        builder.setTitle("Cali's Dairy")

        builder.setMessage(message)
        builder.setPositiveButton("ok") { dialogInterface, which ->
            SavedPrefManager.saveStringPreferences(context, SavedPrefManager.Token, "token")
            val intent = Intent(context, Login::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
            alertDialog!!.dismiss()
            finish()
        }
        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog.show()
    }


    fun alertBoxDelete(message: String, context: Context, click: DeleteClick) {
        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        builder.setIcon(R.mipmap.app_icon)
        builder.setTitle("Cali's Dairy")
        builder.setMessage(message)
        alertDialog?.window?.attributes?.windowAnimations = R.style.Fade

        builder.setNegativeButton("No") { _, _ ->
            alertDialog!!.dismiss()
        }

        builder.setPositiveButton("Yes") { _, _ ->
            alertDialog!!.dismiss()
            click.deleteItem()
        }



        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog.show()
    }

    fun alertBoxCommon(message: String, context: Context, click: CommonDialogInterface) {
        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        builder.setIcon(R.mipmap.app_icon)
        builder.setTitle("Cali's Dairy")
        builder.setMessage(message)
        alertDialog?.window?.attributes?.windowAnimations = R.style.Fade
        builder.setPositiveButton("Yes") { _, _ ->
            alertDialog!!.dismiss()
            click.commonWork()
        }

        builder.setNegativeButton("No") { _, _ ->
            alertDialog!!.dismiss()
        }

        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog.show()
    }

    fun alertBoxFinishActivity(message: String, context: Context, click: Finish) {
        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        builder.setIcon(R.mipmap.app_icon)
        builder.setTitle("Cali's Dairy")
        builder.setMessage(message)
        alertDialog?.window?.attributes?.windowAnimations = R.style.Fade
        builder.setPositiveButton("ok") { _, _ ->
            click.finishActivity()
            alertDialog!!.dismiss()

        }
        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog.show()
    }

    fun addPriceDialog(context: Context, click: AddPetInterface, currency: String) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.add_price)
        val yesBtn = dialog.findViewById<LinearLayout>(R.id.DoneButton)
        val PetPrice = dialog.findViewById<EditText>(R.id.PetPrice)
        val llPetPrice = dialog.findViewById<LinearLayout>(R.id.llPetPrice)
        val tvPet = dialog.findViewById<TextView>(R.id.tvPet)
        val price = dialog.findViewById<TextView>(R.id.price)
        val noBtn = dialog.findViewById<LinearLayout>(R.id.CancelButton)

        dialog.window!!.attributes.windowAnimations = R.style.Fade

        price.text = "${context.getString(R.string.price)}($currency)"



        PetPrice.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (s.toString().length == 1 && s.toString().startsWith("0")) {
                    s.clear()
                }
            }
        })


        yesBtn.setSafeOnClickListener {
            if (PetPrice.text.isEmpty()) {
                tvPet.isVisible = true
                tvPet.text = "*Please Enter Price"
                llPetPrice.setBackgroundResource(R.drawable.errordrawable)

            } else {
                tvPet.isVisible = false
                tvPet.text = ""
                llPetPrice.setBackgroundResource(R.drawable.white_border_background)
                dialog.dismiss()
                click.petPrice(PetPrice.text.toString())
            }


        }
        noBtn.setSafeOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        dialog.setCancelable(false)
        val window = dialog.window
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun addMorePetProfile(context: Context,click:AsPerGoPay) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.pay_as_per_go)
        val yesBtn = dialog.findViewById<LinearLayout>(R.id.DoneButton)
        val noBtn = dialog.findViewById<LinearLayout>(R.id.CancelButton)

        dialog.window!!.attributes.windowAnimations = R.style.Fade




        yesBtn.setSafeOnClickListener {
            click.payNow()
            dialog.dismiss()

        }
        noBtn.setSafeOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
        dialog.setCancelable(false)
        val window = dialog.window
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    fun LottieAnimationView.initLoader(isLoading: Boolean) {
        visibility = if (isLoading) {
            playAnimation()
            View.VISIBLE
        } else {
            pauseAnimation()
            animation?.reset()
            View.GONE
        }
    }


    fun postReportDialog(context: Context, position: Int, _id: String, click: ReportPost) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.sell_pet_design)

        val yesBtn = dialog.findViewById<LinearLayout>(R.id.DoneButton)
        val noBtn = dialog.findViewById<LinearLayout>(R.id.CancelButton)
        val etCaption = dialog.findViewById<EditText>(R.id.etCaption)

        dialog.window!!.attributes.windowAnimations = R.style.Fade


        noBtn.setSafeOnClickListener {
            dialog.dismiss()
        }


        yesBtn.setSafeOnClickListener {
            if (etCaption.text.toString().isEmpty()){
                Toast.makeText(context, "Describe your reason", Toast.LENGTH_SHORT).show()
            }else{
                click.reportPost(etCaption.text.toString(),position,_id)
                dialog.dismiss()
            }
        }



        dialog.show()
        dialog.setCancelable(false)
        val window = dialog.window
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayoutCompat.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


    fun alertBoxFinishFragment(
        message: String,
        context: Context,
        parentFragmentManager: FragmentManager,
        activity: FragmentActivity
    ) {
        var alertDialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        builder.setIcon(R.mipmap.app_icon)
        builder.setTitle("Cali's Dairy")
        builder.setMessage(message)
        alertDialog?.window?.attributes?.windowAnimations = R.style.Fade
        builder.setPositiveButton("ok") { _, _ ->
            parentFragmentManager.popBackStack()
            activity.finishAfterTransition()
            alertDialog!!.dismiss()

        }
        alertDialog = builder.create()
        alertDialog!!.setCancelable(false)
        alertDialog.show()
    }


    fun initPdfViewer(fileUrl: String,context: Context, certificateImage: PdfRendererView) {
        if (fileUrl.isEmpty()) return alertBox("Please Upload Another Document",context)


        try {
            certificateImage.initWithUrl(
                fileUrl,
                PdfQuality.NORMAL,
                engine
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }





}