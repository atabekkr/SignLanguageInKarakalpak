package com.shaxsanem.signlanguage.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shaxsanem.signlanguage.R
import com.shaxsanem.signlanguage.data.db.SLDao
import com.shaxsanem.signlanguage.databinding.FragmentTopicBinding
import com.shaxsanem.signlanguage.ui.adapters.WordAdapter
import com.shaxsanem.signlanguage.utils.Constants.ALPHABET
import com.shaxsanem.signlanguage.utils.Constants.ALPHABET_PHOTO
import com.shaxsanem.signlanguage.utils.Constants.CONFIG
import com.shaxsanem.signlanguage.utils.Constants.FAMILY
import com.shaxsanem.signlanguage.utils.Constants.HUMAN
import com.shaxsanem.signlanguage.utils.Constants.INTRO
import com.shaxsanem.signlanguage.utils.Constants.NUMBER
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TopicFragment : Fragment(R.layout.fragment_topic) {

    private val binding by viewBinding(FragmentTopicBinding::bind)
    private val navArgs by navArgs<TopicFragmentArgs>()
    private val adapter = WordAdapter()

    @Inject
    lateinit var dao: SLDao

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupData()
        loadData()
        setupListeners()

    }

    private fun setupData() {

        binding.recyclerView.adapter = adapter

        when (navArgs.groupName) {
            ALPHABET -> {
                binding.ivTitle.setImageDrawable(requireContext().getDrawable(R.drawable.pic_alphabet2))
                binding.tvTitle.text = getString(R.string.alphabet_gif)
            }

            ALPHABET_PHOTO -> {
                binding.ivTitle.setImageDrawable(requireContext().getDrawable(R.drawable.pic_alphabet))
                binding.tvTitle.text = getString(R.string.alphabet_photo)
            }

            INTRO -> {
                binding.ivTitle.setImageDrawable(requireContext().getDrawable(R.drawable.pic_introduce))
                binding.tvTitle.text = getString(R.string.introducing)
            }

            HUMAN -> {
                binding.ivTitle.setImageDrawable(requireContext().getDrawable(R.drawable.pic_human2))
                binding.tvTitle.text = getString(R.string.human)
            }

            NUMBER -> {
                binding.ivTitle.setImageDrawable(requireContext().getDrawable(R.drawable.pic_numbers))
                binding.tvTitle.text = getString(R.string.digits)
            }

            CONFIG -> {
                binding.ivTitle.setImageDrawable(requireContext().getDrawable(R.drawable.pic_config))
                binding.tvTitle.text = getString(R.string.config)
            }

            FAMILY -> {
                binding.ivTitle.setImageDrawable(requireContext().getDrawable(R.drawable.pic_family))
                binding.tvTitle.text = getString(R.string.family)
            }
        }

    }

    private fun loadData() {

        lifecycleScope.launch {
            adapter.submitList(dao.getWordsByGroupName(navArgs.groupName))
        }

    }

    private fun setupListeners() {

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.btnGoToQuiz.setOnClickListener {
            findNavController().navigate(
                TopicFragmentDirections.actionTopicFragmentToQuizFragment(navArgs.groupName)
            )
        }

        binding.etSearch.addTextChangedListener { text ->

            binding.btnClear.isVisible = text?.isNotEmpty() == true

            val searchText = "$text%"

            lifecycleScope.launch {
                val list = dao.search(searchText, navArgs.groupName)
                adapter.submitList(list)
            }

        }

        binding.btnClear.setOnClickListener {
            binding.etSearch.text.clear()
        }

        adapter.setOnItemClickListener { content, groupName ->
            findNavController().navigate(
                TopicFragmentDirections.actionTopicFragmentToWordOverviewFragment(content, groupName)
            )
        }

    }

}