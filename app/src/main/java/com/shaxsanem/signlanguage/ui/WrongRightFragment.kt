package com.shaxsanem.signlanguage.ui

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
import com.shaxsanem.signlanguage.databinding.FragmentWrongRightBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WrongRightFragment : Fragment(R.layout.fragment_wrong_right) {

    private val binding by viewBinding(FragmentWrongRightBinding::bind)
    private val navArgs by navArgs<WrongRightFragmentArgs>()

    private var words: List<Word> = listOf()
    private var currentWord: Word? = null

    private var result = 0

    @Inject
    lateinit var dao: SLDao

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()
        setupListeners()

    }

    private fun loadData() {

        lifecycleScope.launch {
            words = dao.getWordsByGroupName(navArgs.groupName)
            setQuestion(questionId = 0)
            binding.progressBar.max = words.size
        }

    }

    private fun setupListeners() {

        binding.ivCancel.setOnClickListener { findNavController().popBackStack() }

        binding.btnTrue.setOnClickListener {
            if (currentWord.toString() == binding.tvName.text) {
                result++
            }
            checkingProgress()
        }
        binding.btnFalse.setOnClickListener {
            if (currentWord.toString() != binding.tvName.text) {
                result++
            }
            checkingProgress()
        }

    }

    private fun checkingProgress() {
        binding.progressBar.progress++
        if (binding.progressBar.progress != binding.progressBar.max) {
            setQuestion(binding.progressBar.progress)
        } else {
            val resultInPercent = (result.toDouble() / words.size.toDouble()) * 100
            findNavController().navigate(
                WrongRightFragmentDirections.actionWrongRightFragmentToResultFragment(
                    resultInPercent.toInt()
                )
            )
        }
    }

    private fun setQuestion(questionId: Int) {

        currentWord = words.getOrNull(questionId)
        val shuffleWords =
            listOf(words.random().name, words.random().name, currentWord?.name).shuffled()
        binding.tvName.text = shuffleWords[0]
        currentWord?.content?.let { playVideo(it) }

    }

    private fun playVideo(content: String) {
        val mediaController = MediaController(requireContext())
        binding.videoView.setMediaController(mediaController)
        val rawFile = requireContext().resources.getIdentifier(
            content,
            "raw",
            requireContext().packageName
        )
        binding.videoView.setVideoURI(
            Uri.parse("android.resource://${requireActivity().packageName}/$rawFile")
        )
        binding.videoView.setMediaController(null)
        binding.videoView.setOnPreparedListener {
            it.isLooping = true
            it.setVolume(0f, 0f)
        }
        binding.videoView.isSoundEffectsEnabled = false
        binding.videoView.start()
    }
}