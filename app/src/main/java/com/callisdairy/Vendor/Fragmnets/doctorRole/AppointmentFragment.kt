package com.callisdairy.Vendor.Fragmnets.doctorRole

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.callisdairy.AdapterVendors.AppointmentAdapter
import com.callisdairy.Interface.CancelAppointmentInterface
import com.callisdairy.ModalClass.dateImport
import com.callisdairy.R
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.api.response.Appointment
import com.callisdairy.databinding.FragmentAppointmentBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.DoctorAppointmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
class AppointmentFragment : Fragment(), CancelAppointmentInterface {

    private var _binding: FragmentAppointmentBinding? = null
    private val binding get() = _binding!!



    var appointmentData: List<Appointment> = listOf()
    var appointmentVirtualData: List<Appointment> = listOf()
    var flagTab = "InPerson"
    var token = ""
    var pages = 0
    var page = 1
    var limit = 10
    var dataLoadFlag = false
    var loaderFlag = true

    var pagesVirtual = 0
    var pageVirtual = 1
    var limitVirtual = 10
    var dataLoadFlagVirtual = false
    var loaderFlagVirtual = true

    var from = ""
    var isScreen = ""


    var arrayDate :List<Appointment> = listOf()


    private val viewModel: DoctorAppointmentViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppointmentBinding.inflate(layoutInflater, container, false)
        arguments?.getString("from")?.let { from = it }
        arguments?.getString("isScreen")?.let { isScreen = it }

        if (isScreen !="Appointment History"){
            findIdAndHandleTab()

        }else{
            binding.cancelAppointment.isVisible = false
        }
        token = SavedPrefManager.getStringPreferences(requireContext(), SavedPrefManager.Token).toString()


//        binding.addDateInCalander.setSafeOnClickListener {
//            getDates()
//        }
        if(from == "Completed TeleHealth" || from == "Total TeleHealth" || from == "Confirmed TeleHealth") {
            flagTab = "TeleHealth"
        }

        binding.pullToRefresh.setOnRefreshListener {
            binding.pullToRefresh.isRefreshing = false
            binding.DFsearch.setText("")
        }



        binding.cancelAppointment.setSafeOnClickListener {
            androidExtension.cancelAppointment(requireContext(),getString(R.string.cancel_appointment_msgs),"",this)
        }

        callApiBasedOnPageLanding(search = "")






        binding.DFsearch.addTextChangedListener(textWatcher)



        binding.InPersonButton.setSafeOnClickListener {
            flagTab = "InPerson"
            binding.InPersonButton.isEnabled = false
            binding.teleHeath.isEnabled = true
            binding.teleHeath.isFocusable = true
            binding.scrollView.scrollY = 0
            binding.DFsearch.text= SpannableStringBuilder()

            binding.ProgressBarScroll.isVisible = false
            binding.teleHeath.setBackgroundResource(R.drawable.border_background)
            binding.InPersonButton.setBackgroundResource(R.drawable.main_button_background)
            binding.txtTeleHealth.setTextColor(ContextCompat.getColor(requireContext(),R.color.themeColor))
            binding.txtInPerson.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

        }
        binding.teleHeath.setSafeOnClickListener {
            flagTab = "TeleHealth"
            binding.InPersonButton.isEnabled = true
            binding.InPersonButton.isFocusable = true
            binding.teleHeath.isEnabled = false
            binding.ProgressBarScroll.isVisible = false
            binding.DFsearch.text = SpannableStringBuilder()
            binding.scrollView.scrollY = 0
            binding.teleHeath.setBackgroundResource(R.drawable.main_button_background)
            binding.InPersonButton.setBackgroundResource(R.drawable.border_background)
            binding.txtTeleHealth.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            binding.txtInPerson.setTextColor(ContextCompat.getColor(requireContext(),R.color.themeColor))
        }




        binding.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
            when (flagTab) {

                "TeleHealth" -> {
                    if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                        dataLoadFlagVirtual = true
                        pageVirtual++
                        binding.ProgressBarScroll.visibility = View.VISIBLE
                        if (pageVirtual > limitVirtual) {
                            binding.ProgressBarScroll.visibility = View.GONE
                        } else {
                            callApiBasedOnPageLanding(search ="")
                        }
                    }
                }


                else -> {
                    if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                        dataLoadFlag = true
                        page++
                        binding.ProgressBarScroll.visibility = View.VISIBLE
                        if (page > pages) {
                            binding.ProgressBarScroll.visibility = View.GONE
                        } else {
                            callApiBasedOnPageLanding(search ="")
                        }
                    }
                }
            }


        })

        return binding.root
    }




    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeAppointmentListResponse()
        observeAppointmentListVirtualResponse()
        observeCancelAppointmentResponse()


        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (isScreen == "Appointment History") {
                        activity?.finishAfterTransition()

                    }
                    parentFragmentManager.popBackStack()
                }

            })


    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeAppointmentListResponse() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._appointmentListData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {

                            Progresss.stop()

                            if (response.data?.responseCode == 200) {
                                try {
                                    getDates()
                                    loaderFlag = false
                                    if (!dataLoadFlag) {
                                        appointmentData = emptyList()
                                    }

                                    appointmentData = appointmentData + response.data.result.docs

                                    if (appointmentData.isNotEmpty()) {

                                        binding.NotFound.isVisible = false
                                        binding.TeleHealthRecycler.isVisible = false
                                        binding.InPersonRecycler.isVisible = true

                                        pages = response.data.result.pages
                                        page = response.data.result.page
                                        setAdapterClinic(appointmentData)
                                    } else {
                                        binding.NotFound.isVisible = true
                                        binding.InPersonRecycler.isVisible = false
                                        binding.TeleHealthRecycler.isVisible = false
                                    }


                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            binding.NotFound.isVisible = true
                            binding.InPersonRecycler.isVisible = false
                            binding.TeleHealthRecycler.isVisible = false
                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
                            }

                        }

                        is Resource.Loading -> {
                            binding.NotFound.isVisible = false
                            if (loaderFlag) {
                                binding.InPersonRecycler.isVisible = false
                                Progresss.start(requireContext())
                            }
                        }

                        is Resource.Empty -> {
                        }

                    }

                }

            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeAppointmentListVirtualResponse() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._appointmentListVirtualData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                try {

                                    loaderFlagVirtual = false
                                    if (!dataLoadFlagVirtual) {
                                        appointmentVirtualData = emptyList()
                                    }

                                    appointmentVirtualData = appointmentVirtualData + response.data.result.docs

                                    getDates()

                                    if (appointmentVirtualData.isNotEmpty()) {

                                        binding.TeleHealthRecycler.isVisible = true
                                        binding.InPersonRecycler.isVisible = false
                                        binding.NotFound.isVisible = false

                                        pagesVirtual = response.data.result.pages
                                        pageVirtual = response.data.result.page
                                        setAdapterClinicTelehealth(appointmentVirtualData)

                                    } else {
                                        binding.InPersonRecycler.isVisible = false
                                        binding.TeleHealthRecycler.isVisible = false
                                        binding.NotFound.isVisible = true
                                    }


                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {

                            binding.InPersonRecycler.isVisible = false
                            binding.TeleHealthRecycler.isVisible = false
                            response.message?.let { message ->
                            }

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


    private fun setAdapterClinic(appointmentVirtualData: List<Appointment>) {
        binding.InPersonRecycler.layoutManager = LinearLayoutManager(requireContext())
        val adapter = AppointmentAdapter(requireContext(), appointmentVirtualData,from)
        binding.InPersonRecycler.adapter = adapter
    }

    private fun setAdapterClinicTelehealth(appointmentVirtualData: List<Appointment>) {
        binding.TeleHealthRecycler.layoutManager = LinearLayoutManager(requireContext())
        val adapter = AppointmentAdapter(requireContext(), appointmentVirtualData,from)
        binding.TeleHealthRecycler.adapter = adapter
    }


    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            if (s.toString().isEmpty()) {
                callApi("")
            } else {
                callApi(s.toString())
            }


        }

    }


    private fun callApi(search: String) {
        if (flagTab == "TeleHealth") {
            pageVirtual = 1
            limitVirtual = 10
            dataLoadFlagVirtual = false
            loaderFlagVirtual = true
            callApiBasedOnPageLanding(search)

        } else {
            page = 1
            limit = 10
            dataLoadFlag = false
            loaderFlag = false

            callApiBasedOnPageLanding(search)

        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDates() {
        arrayDate = appointmentVirtualData + appointmentData

        var userName = ""
        var petName = ""


        val calendarDates = mutableListOf<dateImport>()


        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")


        for (i in arrayDate.indices) {

            val appointmentDate = arrayDate[i].appointmentDate
            val appointmentTime = arrayDate[i].slot
            userName = arrayDate[i].userId.userName
            petName = arrayDate[i].petName


            val parsedDate = LocalDateTime.parse(appointmentDate, dateFormatter)
            val currentDateTime = LocalDateTime.now()

            val parsedTime = LocalTime.parse(appointmentTime, DateTimeFormatter.ofPattern("HH:mm"))

            val appointmentDateTime = LocalDateTime.of(parsedDate.toLocalDate(), parsedTime)
            val milliseconds = convertDateTimeToMilliseconds(appointmentDateTime.toString())


            if (appointmentDateTime.isAfter(currentDateTime)) {
                calendarDates.add(dateImport(milliseconds, arrayDate[i].consultingType,userName,petName))
            }
        }

        if (calendarDates.isNotEmpty()) {
            createCalendarEvent(requireContext(), calendarDates)
        } else {
//            Toast.makeText(requireContext(), "No Future Appointments", Toast.LENGTH_SHORT).show()
        }
    }


    private fun createCalendarEvent(context: Context, dates: List<dateImport>) {
        val calendarId = 1 // This is the ID of the calendar you want to add the event to

        for (date in dates) {
            val startMillis = date.Date
            val endMillis = startMillis + (60 * 60 * 1000) // Adding 1 hour to the start time

            val eventValues = ContentValues().apply {
                put(CalendarContract.Events.DTSTART, startMillis)
                put(CalendarContract.Events.DTEND, endMillis)
                put(CalendarContract.Events.TITLE, date.EventType)
                put(CalendarContract.Events.DESCRIPTION, "User Name:${date.userName} \n Pet Name:${date.petname}")
                put(CalendarContract.Events.CALENDAR_ID, calendarId)
                put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
            }

            val existingEventId = getExistingEventId(context, startMillis, endMillis)
            if (existingEventId != null) {
                // Update the existing event
                val updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, existingEventId)
                context.contentResolver.update(updateUri, eventValues, null, null)
//                Toast.makeText(context, "Event Updated", Toast.LENGTH_SHORT).show()
            } else {
                // Insert a new event
                val uri = context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, eventValues)
                if (uri != null) {
                    val eventId = uri.lastPathSegment?.toLongOrNull()
                    if (eventId != null) {
                        val reminderValues = ContentValues().apply {
                            put(CalendarContract.Reminders.MINUTES, 10) // Set the reminder time in minutes before the event
                            put(CalendarContract.Reminders.EVENT_ID, eventId)
                            put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
                        }
                        context.contentResolver.insert(CalendarContract.Reminders.CONTENT_URI, reminderValues)
//                        Toast.makeText(context, "Event Added", Toast.LENGTH_SHORT).show()
                    } else {
                        // Failed to get event ID
//                        Toast.makeText(context, "Failed to get event ID", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Failed to add event
//                    Toast.makeText(context, "Failed to add event", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("Range")
    private fun getExistingEventId(context: Context, startMillis: Long, endMillis: Long): Long? {
        val projection = arrayOf(CalendarContract.Events._ID)
        val selection = "${CalendarContract.Events.DTSTART} = ? AND ${CalendarContract.Events.DTEND} = ?"
        val selectionArgs = arrayOf(startMillis.toString(), endMillis.toString())

        context.contentResolver.query(
            CalendarContract.Events.CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )?.use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getLong(cursor.getColumnIndex(CalendarContract.Events._ID))
            }
        }

        return null
    }


    private fun findIdAndHandleTab() {


        val addVendors = activity?.findViewById<ImageView>(R.id.addVendors)!!
        val homeTv = activity?.findViewById<TextView>(R.id.homeTv)!!
        val productTv = activity?.findViewById<TextView>(R.id.productTv)!!
        val petTv = activity?.findViewById<TextView>(R.id.petTv)!!
        val serviceTv = activity?.findViewById<TextView>(R.id.serviceTv)!!
        val settingsTv = activity?.findViewById<TextView>(R.id.settingsTv)!!
        val DocotorTv = activity?.findViewById<TextView>(R.id.DocotorTv)!!

        val settingsView = activity?.findViewById<View>(R.id.settingsView)!!
        val serviceView = activity?.findViewById<View>(R.id.serviceView)!!
        val petsView = activity?.findViewById<View>(R.id.petsView)!!
        val productView = activity?.findViewById<View>(R.id.productView)!!
        val Homeview = activity?.findViewById<View>(R.id.Homeview)!!
        val DoctorView = activity?.findViewById<View>(R.id.DoctorView)!!



        homeTv.setTextColor(Color.parseColor("#757575"))
        productTv.setTextColor(Color.parseColor("#757575"))
        petTv.setTextColor(Color.parseColor("#757575"))
        serviceTv.setTextColor(Color.parseColor("#757575"))
        settingsTv.setTextColor(Color.parseColor("#757575"))
        DocotorTv.setTextColor(Color.parseColor("#6FCFB9"))

        addVendors.isVisible = false

        Homeview.isVisible = false
        productView.isVisible = false
        petsView.isVisible = false
        DoctorView.isVisible = true
        serviceView.isVisible = false
        settingsView.isVisible = false

    }

    private fun callApiBasedOnPageLanding(search: String) {
        val apiType: String
        val apiStatus: String

        when {
            isScreen == "Tab" -> {
                apiType = if (flagTab == "TeleHealth") "TELEHEALTH" else "IN-PERSON"
                apiStatus = "CONFIRMED"
            }
            isScreen == "Appointment History" -> {
                apiType = if (flagTab == "TeleHealth") "TELEHEALTH" else "IN-PERSON"
                apiStatus = "ALL"
                setBackVendorClickListener()
            }


            from == "Completed TeleHealth" || from == "Total TeleHealth" || from == "Confirmed TeleHealth"  -> {
                apiType = if (flagTab == "TeleHealth")  "TELEHEALTH"  else "IN-PERSON"
                apiStatus = when (from) {
                    "Completed TeleHealth" -> if (flagTab == "TeleHealth") "COMPLETED" else "COMPLETED"
                    "Total TeleHealth" -> "ALL"
                    else -> "CONFIRMED"
                }

                setTeleHealthEnabled()
            }
            else -> {
                apiType = if (flagTab == "TeleHealth")  "TELEHEALTH"  else "IN-PERSON"
                apiStatus = when (from) {
                    "Total InPerson" -> "ALL"
                    "Total Appointments" -> "ALL"
                    "Confirmed InPerson" ->  "CONFIRMED"
                    "Completed InPerson" -> "COMPLETED"
                    else ->  "COMPLETED"
                }
            }
        }

        if (apiType == "TELEHEALTH") {
            viewModel.appointmentListVirtualApi(token, search, pageVirtual, limitVirtual, apiType, apiStatus)
        } else {
            viewModel.appointmentListApi(token, search, page, limit, apiType, apiStatus)
        }
    }

    private fun setBackVendorClickListener() {
        val backVendor = activity?.findViewById<ImageView>(R.id.backVendor) ?: return
        backVendor.setSafeOnClickListener {
            activity?.finishAfterTransition()
            parentFragmentManager.popBackStack()
        }
    }

    private fun setTeleHealthEnabled() {
        binding.apply {
            teleHeath.setBackgroundResource(R.drawable.main_button_background)
            InPersonButton.setBackgroundResource(R.drawable.border_background)
            txtTeleHealth.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            txtInPerson.setTextColor(ContextCompat.getColor(requireContext(),R.color.themeColor))
        }
    }

    private fun convertDateTimeToMilliseconds(dateTime: String): Long {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val date = dateFormat.parse(dateTime)
        return date?.time ?: 0L
    }


    override fun commonWork(date:String) {
        viewModel.cancelAllAppointmentApi(token = token, date = date)
    }


    private fun observeCancelAppointmentResponse() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._cancelAllAppointmentData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            Progresss.stop()
                            if (response.data?.responseCode == 200) {
                                try {
                                    androidExtension.alertBox(response.data.responseMessage,requireContext())

                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            response.message?.let { message ->
                                androidExtension.alertBox(message, requireContext())
                            }
                        }

                        is Resource.Loading -> {
                            Progresss.start(requireContext())
                        }

                        is Resource.Empty -> {
                        }

                    }

                }
            }
        }
    }



}