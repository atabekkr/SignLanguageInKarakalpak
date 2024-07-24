package com.shaxsanem.signlanguage.ui

import android.adservices.topics.Topic
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

    }

    private fun loadData() {

        lifecycleScope.launch {
            adapter.submitList(dao.getWords())
        }

    }

    private fun setupListeners() {

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.btnGoToQuiz.setOnClickListener {
            findNavController().navigate(
                TopicFragmentDirections.actionTopicFragmentToQuizFragment()
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

        adapter.setOnItemClickListener { content: String ->
            findNavController().navigate(
                TopicFragmentDirections.actionTopicFragmentToWordOverviewFragment(content)
            )
        }

    }

}