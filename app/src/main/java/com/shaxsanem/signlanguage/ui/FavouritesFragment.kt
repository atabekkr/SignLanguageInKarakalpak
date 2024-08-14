package com.shaxsanem.signlanguage.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shaxsanem.signlanguage.R
import com.shaxsanem.signlanguage.data.db.SLDao
import com.shaxsanem.signlanguage.databinding.FragmentFavouritesBinding
import com.shaxsanem.signlanguage.ui.adapters.WordAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FavouritesFragment : Fragment(R.layout.fragment_favourites) {

    private val binding by viewBinding(FragmentFavouritesBinding::bind)
    private val adapter = WordAdapter()

    @Inject
    lateinit var dao: SLDao

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.adapter = adapter

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        lifecycleScope.launch {
            adapter.submitList(dao.getFavourites())
        }

        adapter.setOnItemClickListener { content, groupName ->
            findNavController().navigate(
                FavouritesFragmentDirections.actionFavouritesFragmentToWordOverviewFragment(content, groupName)
            )
        }

    }

}