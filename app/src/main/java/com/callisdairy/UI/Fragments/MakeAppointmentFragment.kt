package com.callisdairy.UI.Fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.Adapter.SelectADateAdaptor
import com.callisdairy.Adapter.SymptomsSelectedAdapter
import com.callisdairy.Interface.AppointmentListener
import com.callisdairy.Interface.GetTime
import com.callisdairy.ModalClass.ProblemSymptoms
import com.callisdairy.R
import com.callisdairy.Utils.DateFormat
import com.callisdairy.Utils.DialogUtils
import com.callisdairy.Utils.Progresss
import com.callisdairy.Utils.Resource
import com.callisdairy.Utils.SavedPrefManager
import com.callisdairy.Validations.FormValidations
import com.callisdairy.api.request.AddAppointmentRequest
import com.callisdairy.api.response.AppointmentDateResult
import com.callisdairy.databinding.FragmentMakeAppointmentBinding
import com.callisdairy.extension.androidExtension
import com.callisdairy.extension.setSafeOnClickListener
import com.callisdairy.viewModel.MakeAppointmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar


@AndroidEntryPoint
class MakeAppointmentFragment : Fragment(), GetTime, AppointmentListener {

    private var _binding: FragmentMakeAppointmentBinding? = null
    private val binding get() = _binding!!
    lateinit var backTitle: ImageView
    var token = ""
    var vetId = ""
    var appointmentType = ""

    lateinit var adapter : SelectADateAdaptor

    private lateinit var dialog: Dialog
    private lateinit var recyclerView: RecyclerView
    var currentOrSelectedDate = ""
    var dataList: List<AppointmentDateResult> = listOf()
    var positioncount :Int=0
    var appointDate = ""
    var appointTime = ""
    var flag=false
    var datePicker: DatePickerDialog? = null




    var dataSymptoms : List<ProblemSymptoms> = listOf()

    private lateinit var dialogSymptoms: Dialog
    private lateinit var recyclerViewSymptoms: RecyclerView
    private lateinit var tickSymptoms: LinearLayout
    lateinit var adapterSymptoms: SymptomsSelectedAdapter

    var symptomsArray = ArrayList<String>()






    private val viewModel: MakeAppointmentViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentMakeAppointmentBinding.inflate(layoutInflater, container, false)
        backTitle = activity?.findViewById(R.id.backTitle)!!

        arguments?.getString("vetId")?.let { vetId =it }


        token= SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.Token).toString()
        setSpinnerValues()
        val petType = SavedPrefManager.getStringPreferences(requireContext(),SavedPrefManager.profileType).toString()


        binding.etPetType.text = petType

        binding.etPetName.addTextChangedListener(textWatcher)
        binding.etCell.addTextChangedListener(textWatcher)
        binding.etReasonForVisit.addTextChangedListener(textWatcher)
        binding.etOtherMedicalConditions.addTextChangedListener(textWatcher)
        binding.etSymptoms.addTextChangedListener(textWatcher)
        binding.etCurrentMeditation.addTextChangedListener(textWatcher)
        binding.etDosage.addTextChangedListener(textWatcher)
        binding.etDiet.addTextChangedListener(textWatcher)
        binding.etAppointment.addTextChangedListener(textWatcher)




        backTitle.setSafeOnClickListener {
            activity?.finishAfterTransition()
            parentFragmentManager.popBackStack()
        }

        dataSymptoms = emptyList()
        dataSymptoms = listOf(
            ProblemSymptoms("Behavior problems",false),
            ProblemSymptoms("Bleeding Gums",false),
            ProblemSymptoms("Breathing Problems",false),
            ProblemSymptoms("Coughing",false),
            ProblemSymptoms("Diarrhea",false),
            ProblemSymptoms("Gagging",false),
            ProblemSymptoms("Head shaking",false),
            ProblemSymptoms("Lack of appetite",false),
            ProblemSymptoms("Limping",false),
            ProblemSymptoms("Loss of balance",false),
            ProblemSymptoms("Scooting",false),
            ProblemSymptoms("Scratching",false),
            ProblemSymptoms("Seems depressed",false),
            ProblemSymptoms("Sneezing",false),
            ProblemSymptoms("Thirst and/or urination increase",false),
            ProblemSymptoms("Vomiting",false),
            ProblemSymptoms("Weakness",false),
            ProblemSymptoms("Other",false)
        )





        binding.llAppointment.setSafeOnClickListener {
            openPopUp()
        }

        binding.llSymptoms.setSafeOnClickListener {
            openPopUpForSymptoms()
        }

        binding.llVaccinationDate.setOnClickListener {
           DateFormat.dateSelector(requireContext(),binding.vaccinationDate)
        }

        binding.createButton.setSafeOnClickListener {
            if(FormValidations.makeAppointmentValidation(
                binding.etPetName,binding.llPetName,binding.tvPetName,binding.etCell,binding.CellLL,
                binding.tvCell,binding.etLandLine,binding.landLineLL,binding.tvLandLine,binding.etFaxNumber,binding.llFaxNumber,
                binding.tvFaxNumber,binding.appointmentType,binding.llAppointmentType,binding.TvAppointmentType,binding.vaccinationDate,binding.llVaccinationDate,binding.tvVaccinationDate,
                binding.etReasonForVisit,binding.llReasonForVisit,binding.tvReasonForVisit,binding.etOtherMedicalConditions,binding.llOtherMedicalConditions,binding.tvOtherMedicalConditions,
                binding.etSymptoms,binding.llSymptoms,binding.tvSymptoms,binding.etCurrentMeditation,binding.llCurrentMeditation,binding.tvCurrentMeditation,
                binding.etDosage,binding.llDosage,binding.tvDosage,binding.etDiet,binding.llDiet,binding.tvDiet,binding.etAppointment,binding.llAppointment,binding.tvAppointment,
            requireContext(),binding.llGender,binding.genderSpinner,binding.tvGender,binding.llAboutGender,binding.aboutGenderSpinner,binding.tvAboutGender,binding.llHowHear,binding.HowHear,binding.TvHowHear)){


                val request  = AddAppointmentRequest()

                request.petName = binding.etPetName.text.toString()
                request.mobileNumber = binding.etCell.text.toString()
                request.landLine = binding.etLandLine.text.toString()
                request.faxNumber = binding.etFaxNumber.text.toString()
                request.consultingType = appointmentType
                request.vaccinationDate = binding.vaccinationDate.text.toString()
                request.reasonForVisit = binding.etReasonForVisit.text.toString()
                request.medicalCondition = binding.etOtherMedicalConditions.text.toString()
                request.symptoms = symptomsArray
                request.currentMedication = binding.etCurrentMeditation.text.toString()
                request.dose = binding.etDosage.text.toString()
                request.diet = binding.etDiet.text.toString()
                request.appointmentDate = binding.etAppointment.text.toString().split(" ")[0]
                request.slot = binding.etAppointment.text.toString().split(" ")[1]
                request.docId = vetId
                request.aboutGender = binding.aboutGenderSpinner.selectedItem.toString()
                request.howHere = binding.HowHear.selectedItem.toString()
                request.gender = binding.genderSpinner.selectedItem.toString()


                viewModel.bookAppointmentApi(token,request)


            }
        }





        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeGetAllSlotsResponse()
        observeAddAppointmentResponse()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finishAfterTransition()
                parentFragmentManager.popBackStack()
            }

        })
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("InflateParams", "SetTextI18n")
    fun openPopUp() {

        try {
            val bindingPopUp = LayoutInflater.from(requireContext()).inflate(R.layout.book_appointment, null)
            dialog = DialogUtils().createDialog(requireContext(), bindingPopUp.rootView, 0)!!
            recyclerView = bindingPopUp.findViewById(R.id.popup_recyclerView)
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation

            viewModel.getAllAppointmentListApi(token,DateFormat.getCurrentDate(),vetId)
            val llDate = bindingPopUp.findViewById<RelativeLayout>(R.id.llDate)
            val appointmentDate = bindingPopUp.findViewById<TextView>(R.id.appointmentDate)
            val dialogBackButton = bindingPopUp.findViewById<ImageView>(R.id.BackButton)
            val tick = bindingPopUp.findViewById<ImageView>(R.id.tick)
            dialogBackButton.setOnClickListener { dialog.dismiss() }
            tick.setOnClickListener {
                binding.etAppointment.text = "${appointDate.split("T").getOrNull(0)} $appointTime"
                dialog.dismiss()
            }


            val myCalendar = Calendar.getInstance()
            val day = myCalendar.get(Calendar.DAY_OF_MONTH)
            val year = myCalendar.get(Calendar.YEAR)
            val month = myCalendar.get(Calendar.MONTH)

            llDate.setOnClickListener {
                datePicker = DatePickerDialog(
                    requireContext(), R.style.DatePickerTheme,
                    { _, year, month, dayOfMonth ->
                        currentOrSelectedDate = "$year-${month + 1}-$dayOfMonth"
                        appointmentDate.text = "$year-${month + 1}-$dayOfMonth"
                        viewModel.getAllAppointmentListApi(token, currentOrSelectedDate, vetId)
                        dataList = emptyList()
                    }, year, month, day
                )

                datePicker!!.datePicker.minDate = myCalendar.timeInMillis
                datePicker!!.show()
            }

            currentOrSelectedDate = DateFormat.getCurrentDate()


            appointmentDate.text = currentOrSelectedDate






            dialog.show()

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }







    private fun observeGetAllSlotsResponse() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._getAllSlots.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.statusCode == 200) {
                                Progresss.stop()
                                try {
                                    dataList = response.data.result
                                    setListAdapter(response.data.result)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            response.message?.let { message ->
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


    private fun setListAdapter(List: List<AppointmentDateResult>) {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = SelectADateAdaptor(requireContext(), List, this,this)
        recyclerView.adapter = adapter
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    override fun appointmentListenerSet(position: Int, subposition: Int) {

        for (i in dataList.indices) {
            val slotTimes = dataList[i].allSlotTimes
            for (j in slotTimes.indices) {
                val slotTime = slotTimes[j]
                slotTime.flag = (i == position && j == subposition)
                if (slotTime.flag!!) {
                    appointTime = slotTime.time
                    appointDate = dataList[i].date
                }
            }
        }

       setListAdapter(dataList)


        positioncount = position
    }



    override fun getTime(time: String, appointment_date: String) {

    }



//    Handle problem and meditation pop up

    private fun openPopUpForSymptoms() {
        try {
            val bindingPopup = LayoutInflater.from(requireContext()).inflate(R.layout.pop_lists, null)
            dialogSymptoms = DialogUtils().createDialog(requireContext(), bindingPopup.rootView, 0)!!
            recyclerViewSymptoms = bindingPopup.findViewById(R.id.popup_recyclerView)
            tickSymptoms = bindingPopup.findViewById(R.id.tick)
            recyclerViewSymptoms.layoutManager = LinearLayoutManager(requireContext())



            setAdapterSymptoms(dataSymptoms)


            tickSymptoms.isVisible = true

            val title = bindingPopup.findViewById<TextView>(R.id.popupTitle)
            title.text = getString(R.string.symptoms_problems)
            val backButton = bindingPopup.findViewById<ImageView>(R.id.BackButton)
            backButton.setOnClickListener { dialogSymptoms.dismiss() }

            val searchEditText = bindingPopup.findViewById<EditText>(R.id.search_bar_edittext_popuplist)


            tickSymptoms.setSafeOnClickListener {
                val selectedItems = dataSymptoms.filter { it.isSelected }

                if (selectedItems.isEmpty()) {
                    androidExtension.alertBox("Please select at least one item",requireContext())
                    return@setSafeOnClickListener
                }

                symptomsArray.clear()
                for (i in selectedItems.indices) {
                    symptomsArray.add(selectedItems[i].other.trim())
                }

                binding.etSymptoms.text = symptomsArray.joinToString(", ")
                dialogSymptoms.dismiss()
            }



            dialogSymptoms.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    private fun setAdapterSymptoms(result: List<ProblemSymptoms>) {
        recyclerViewSymptoms.layoutManager = LinearLayoutManager(requireContext())
        adapterSymptoms = SymptomsSelectedAdapter(requireContext(), result)
        recyclerViewSymptoms.adapter = adapterSymptoms
    }



    private fun observeAddAppointmentResponse() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel._addAppointmentData.collectLatest { response ->

                    when (response) {

                        is Resource.Success -> {
                            if (response.data?.responseCode == 200) {
                                Progresss.stop()
                                try {
                                    androidExtension.alertBoxFinishFragment(response.data.responseMessage,requireContext(),parentFragmentManager,requireActivity())
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }

                        is Resource.Error -> {
                            Progresss.stop()
                            response.message?.let { message ->
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


    private fun setSpinnerValues() {
        val valuesA = arrayOf("Select about gender","Neutered", "Spayed", "None")
        val valuesB = arrayOf("Select about gender","Spayed", "None")

        val firstSpinnerAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.genderPet, android.R.layout.simple_spinner_item)
        firstSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.genderSpinner.adapter = firstSpinnerAdapter

        val secondSpinnerAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item)
        secondSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.aboutGenderSpinner.adapter = secondSpinnerAdapter

        binding.genderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                when (parent.getItemAtPosition(position).toString()) {
                    "Male" -> {
                        secondSpinnerAdapter.clear()
                        secondSpinnerAdapter.addAll(valuesA.toMutableList())

                    }
                    "Female" -> {
                        secondSpinnerAdapter.clear()
                        secondSpinnerAdapter.addAll(valuesB.toMutableList())
                    }
                    else -> {
                        secondSpinnerAdapter.clear()
                        binding.llGender.setBackgroundResource(R.drawable.white_border_background)
                        binding.tvGender.visibility = View.GONE
                        binding.tvGender.text = ""
                    }
                }
                secondSpinnerAdapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        binding.aboutGenderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedValue = parent.getItemAtPosition(position).toString()

                if (selectedValue != "Select about gender"){
                    binding.llAboutGender.setBackgroundResource(R.drawable.white_border_background)
                    binding.tvAboutGender.visibility = View.GONE
                    binding.tvAboutGender.text = ""

                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        binding.appointmentType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val item = parent.getItemAtPosition(pos)

                if (item != "Select appointment type") {
                    appointmentType = if (item == "In Person"){
                        "IN-PERSON"
                    }else{
                        "TELEHEALTH"
                    }

                    binding.llAppointmentType.setBackgroundResource(R.drawable.white_border_background)
                    binding.TvAppointmentType.visibility = View.GONE
                    binding.TvAppointmentType.text = ""
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.HowHear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                val item = parent.getItemAtPosition(pos)

                if (item != "Select how did you hear") {
                    binding.llHowHear.setBackgroundResource(R.drawable.white_border_background)
                    binding.TvHowHear.visibility = View.GONE
                    binding.TvHowHear.text = ""
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }



    }


    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s != null) {
                if (s.length == 1 && s.toString().startsWith("0")) {
                    s.clear();
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            FormValidations.makeAppointmentValidation(
                binding.etPetName,binding.llPetName,binding.tvPetName,binding.etCell,binding.CellLL,
                binding.tvCell,binding.etLandLine,binding.landLineLL,binding.tvLandLine,binding.etFaxNumber,binding.llFaxNumber,
                binding.tvFaxNumber,binding.appointmentType,binding.llAppointmentType,binding.TvAppointmentType,binding.vaccinationDate,binding.llVaccinationDate,binding.tvVaccinationDate,
                binding.etReasonForVisit,binding.llReasonForVisit,binding.tvReasonForVisit,binding.etOtherMedicalConditions,binding.llOtherMedicalConditions,binding.tvOtherMedicalConditions,
                binding.etSymptoms,binding.llSymptoms,binding.tvSymptoms,binding.etCurrentMeditation,binding.llCurrentMeditation,binding.tvCurrentMeditation,
                binding.etDosage,binding.llDosage,binding.tvDosage,binding.etDiet,binding.llDiet,binding.tvDiet,binding.etAppointment,binding.llAppointment,binding.tvAppointment,
                requireContext(),binding.llGender,binding.genderSpinner,binding.tvGender,binding.llAboutGender,binding.aboutGenderSpinner,binding.tvAboutGender,binding.llHowHear,binding.HowHear,binding.TvHowHear)
        }
    }




}