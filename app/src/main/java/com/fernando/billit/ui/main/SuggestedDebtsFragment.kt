package com.fernando.billit.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fernando.billit.R
import dagger.android.support.DaggerFragment

class SuggestedDebtsFragment : DaggerFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_suggested_debts, container, false)


        return root
    }

}
