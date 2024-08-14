package com.shaxsanem.signlanguage.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shaxsanem.signlanguage.R
import com.shaxsanem.signlanguage.databinding.FragmentHomeBinding
import com.shaxsanem.signlanguage.utils.Constants.ALPHABET
import com.shaxsanem.signlanguage.utils.Constants.ALPHABET_PHOTO
import com.shaxsanem.signlanguage.utils.Constants.CONFIG
import com.shaxsanem.signlanguage.utils.Constants.FAMILY
import com.shaxsanem.signlanguage.utils.Constants.HUMAN
import com.shaxsanem.signlanguage.utils.Constants.INTRO
import com.shaxsanem.signlanguage.utils.Constants.NUMBER
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()

    }

    private fun setupListeners() {

        binding.ivInfo.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_aboutUsFragment)
        }

        binding.cardAlphabet.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToTopicFragment(ALPHABET)
            )
        }
        binding.cardAlphabetPhoto.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToTopicFragment(ALPHABET_PHOTO)
            )
        }
        binding.cardIntroducing.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToTopicFragment(INTRO)
            )
        }
        binding.cardHuman.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToTopicFragment(HUMAN)
            )
        }
        binding.cardNumber.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToTopicFragment(NUMBER)
            )
        }
        binding.cardConfig.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToTopicFragment(CONFIG)
            )
        }
        binding.cardFamily.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToTopicFragment(FAMILY)
            )
        }


        binding.cardFavourite.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_favouritesFragment
            )
        }

    }
}