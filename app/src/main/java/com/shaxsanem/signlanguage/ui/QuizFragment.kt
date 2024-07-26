package com.shaxsanem.signlanguage.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shaxsanem.signlanguage.R
import com.shaxsanem.signlanguage.databinding.FragmentQuizBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QuizFragment : Fragment(R.layout.fragment_quiz) {

    private val binding by viewBinding(FragmentQuizBinding::bind)
    private val navArgs by navArgs<QuizFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupListeners() {

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.cardBySign.setOnClickListener {
            findNavController().navigate(
                QuizFragmentDirections.actionQuizFragmentToQuizBySignFragment()
            )
        }
        binding.cardWrongRight.setOnClickListener {
            findNavController().navigate(
                QuizFragmentDirections.actionQuizFragmentToWrongRightFragment(navArgs.groupName)
            )
        }

    }

}