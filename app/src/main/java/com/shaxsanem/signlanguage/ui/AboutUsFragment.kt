package com.shaxsanem.signlanguage.ui

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shaxsanem.signlanguage.R
import com.shaxsanem.signlanguage.databinding.FragmentAboutUsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutUsFragment: Fragment(R.layout.fragment_about_us) {

    private val binding by viewBinding(FragmentAboutUsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.tvAboutUs.movementMethod = ScrollingMovementMethod()
    }

}