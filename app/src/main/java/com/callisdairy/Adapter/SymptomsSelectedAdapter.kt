package com.callisdairy.Adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.callisdairy.ModalClass.ProblemSymptoms
import com.callisdairy.databinding.SumptomsProblemBinding
import java.lang.Exception

class SymptomsSelectedAdapter(
    private val context: Context,
    private val data: List<ProblemSymptoms>
) : RecyclerView.Adapter<SymptomsSelectedAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SumptomsProblemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            val binding = holder.binding
            data[position].apply {
                binding.name.text = name

                binding.tagPeopleCK.isChecked = isSelected

                // Set the initial visibility of the EditText based on the isSelected and name properties
                binding.otherSelected.isVisible = isSelected && name == "Other"
                binding.etReasonForVisit.setText(other)

                binding.tagPeopleCK.setOnCheckedChangeListener { _, isChecked ->
                    isSelected = isChecked
                    binding.otherSelected.isVisible = isChecked && name == "Other"
                    other = name
                }

                binding.etReasonForVisit.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        other = s.toString()
                    }

                    override fun afterTextChanged(s: Editable?) {
                    }
                })



            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(val binding: SumptomsProblemBinding) : RecyclerView.ViewHolder(binding.root)


}
