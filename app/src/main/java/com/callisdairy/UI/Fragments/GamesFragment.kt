package com.callisdairy.UI.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.callisdairy.R
import com.callisdairy.extension.setSafeOnClickListener


class GamesFragment : Fragment() {

    lateinit var back: ImageView
    lateinit var search: ImageView
    lateinit var chat: ImageView
    lateinit var mainTitle: TextView
    lateinit var Username: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_games, container, false)

        allIds()
        toolBarWithBottomTab()
        back.setSafeOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return  view

    }



    private fun allIds(){



        chat = activity?.findViewById(R.id.ChantClick)!!
        mainTitle = activity?.findViewById(R.id.MainTitle)!!
        back = activity?.findViewById(R.id.back)!!
        search = activity?.findViewById(R.id.SearchClick)!!
        Username = activity?.findViewById(R.id.Username)!!


    }

    private fun toolBarWithBottomTab(){
        mainTitle.visibility = View.VISIBLE
        chat.visibility = View.VISIBLE
        back.visibility = View.VISIBLE
        search.visibility = View.VISIBLE


        Username.text = getString(R.string.pet_forum)

    }
}