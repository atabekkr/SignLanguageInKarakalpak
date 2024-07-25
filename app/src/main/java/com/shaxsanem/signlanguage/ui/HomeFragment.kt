package com.shaxsanem.signlanguage.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shaxsanem.signlanguage.R
import com.shaxsanem.signlanguage.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()

    }

    private fun setupListeners() {

        binding.ivInfo.setOnClickListener { }

        binding.cardAlphabet.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToTopicFragment("number")
            )
        }

        binding.cardFavourite.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_favouritesFragment
            )
        }

    }
}