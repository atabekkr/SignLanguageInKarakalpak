package com.shaxsanem.signlanguage.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.shaxsanem.signlanguage.R
import com.shaxsanem.signlanguage.data.db.SLDao
import com.shaxsanem.signlanguage.data.models.Word
import com.shaxsanem.signlanguage.databinding.FragmentWordOverviewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class WordOverviewFragment : Fragment(R.layout.fragment_word_overview) {

    private val binding by viewBinding(FragmentWordOverviewBinding::bind)
    private val navArgs by navArgs<WordOverviewFragmentArgs>()

    @Inject
    lateinit var dao: SLDao

    private var word: Word? = null
    private var isFavourite = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()
        setupListeners()
        playVideo()

    }

    private fun loadData() {
        lifecycleScope.launch {
            word = withContext(Dispatchers.IO) {
                dao.getWordByContent(navArgs.content)
            }
            binding.tvName.text = word?.name
            isFavourite = word?.isFavourite ?: false
            setStar()
        }
    }

    private fun setupListeners() {

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.btnFav.setOnClickListener {
            isFavourite = !isFavourite
            setStar()
            lifecycleScope.launch {
                word?.copy(isFavourite = isFavourite)?.let { it1 -> dao.updateIsFavourite(it1) }
            }
        }

    }

    private fun playVideo() {
        val mediaController = MediaController(requireContext())
        binding.textureView.setMediaController(mediaController)
        val rawFile = requireContext().resources.getIdentifier(
            navArgs.content,
            "raw",
            requireContext().packageName
        )
        binding.textureView.setVideoURI(
            Uri.parse("android.resource://${requireActivity().packageName}/$rawFile")
        )
        binding.textureView.setMediaController(null)
        binding.textureView.setOnPreparedListener {
            it.isLooping = true
        }
        binding.textureView.start()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setStar() {
        binding.ivStar.setImageDrawable(
            if (isFavourite) requireContext().resources.getDrawable(
                R.drawable.ic_filled_star
            ) else requireContext().resources.getDrawable(R.drawable.ic_outlined_start)
        )
    }
}
