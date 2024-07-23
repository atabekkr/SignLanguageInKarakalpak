package com.shaxsanem.signlanguage.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shaxsanem.signlanguage.R
import com.shaxsanem.signlanguage.databinding.FragmentTopicBinding

class TopicFragment : Fragment(R.layout.fragment_topic) {

    private val binding by viewBinding(FragmentTopicBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()

    }

    private fun setupListeners() {

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.btnGoToQuiz.setOnClickListener {
            findNavController().navigate(
                TopicFragmentDirections.actionTopicFragmentToQuizFragment()
            )
        }

    }

}