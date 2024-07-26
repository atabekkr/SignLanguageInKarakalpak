package com.shaxsanem.signlanguage.ui

import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.MediaController
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.shaxsanem.signlanguage.R
import com.shaxsanem.signlanguage.data.db.SLDao
import com.shaxsanem.signlanguage.data.models.Word
import com.shaxsanem.signlanguage.databinding.BottomSheetResultBinding
import com.shaxsanem.signlanguage.databinding.FragmentQuizBySignBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class QuizBySignFragment : Fragment(R.layout.fragment_quiz_by_sign) {

    private val binding by viewBinding(FragmentQuizBySignBinding::bind)

    @Inject
    lateinit var dao: SLDao

    private var words: List<Word> = listOf()
    private var word: Word? = null

    private var currentAnswerId = 0
    private var result = 0

    private var selectText: String = ""
    private var selectButton: Button? = null

    private var dialog: BottomSheetDialog? = null

    private var options = mutableSetOf<String>()

    private var listOfVariantButton = listOf<Button>()

    private var isSelected = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            words = dao.getWordsByGroupName("number")

            binding.progressBar.max = words.size
            setQuestions(currentAnswerId)
            binding.progressBar.max = words.size
        }

        binding.apply {

            btnVariant1.setOnClickListener {
                optionSelected(btnVariant1)
            }
            btnVariant2.setOnClickListener {
                optionSelected(btnVariant2)
            }
            btnVariant3.setOnClickListener {
                optionSelected(btnVariant3)
            }
            btnVariant4.setOnClickListener {
                optionSelected(btnVariant4)
            }

            btnCheck.setOnClickListener {
                if (isSelected) {
                    checkAnswer()
                }
            }

            ivCancel.setOnClickListener { findNavController().popBackStack() }
        }

    }

    private fun setQuestions(testId: Int) {

        word = words[testId]

        playVideo(word!!.content)

        listOfVariantButton = listOf(
            binding.btnVariant1,
            binding.btnVariant2,
            binding.btnVariant3,
            binding.btnVariant4
        )

        options.add(word!!.name)
        words.shuffled().forEach {
            if (options.size < 4) {
                options.add(it.name)
            }
        }

        val shuffledOptions = options.shuffled()
        listOfVariantButton.forEachIndexed { index, button ->
            button.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.transparent)
            )
            button.text = shuffledOptions.elementAt(index)
        }

    }

    private fun optionSelected(button: Button) {

        listOfVariantButton.forEach {
            it.backgroundTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.transparent)
            )
        }

        button.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), R.color.color_option_selected)
        )

        selectText = button.text.toString()
        selectButton = button
        isSelected = true

    }

    private fun checkAnswer() {

        isSelected = false

        selectButton?.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), R.color.color_error)
        )

        listOfVariantButton.forEach {
            if (it.text == word!!.name) {
                it.backgroundTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.color_right)
                )
            }
        }

        showBottomSheetResult(selectText == word!!.name)

    }

    private fun showBottomSheetResult(isCorrect: Boolean) {
        binding.progressBar.progress++
        options.clear()
        val dialogView = layoutInflater.inflate(R.layout.bottom_sheet_result, null)
        dialog = BottomSheetDialog(
            requireContext(),
            R.style.BottomSheetDialogTheme
        )
        dialog?.setContentView(dialogView)
        dialog?.setCancelable(false)

        val binding = BottomSheetResultBinding.bind(dialogView)

        if (isCorrect) {
            result++
            binding.tvStatus.text = getString(R.string.right)
            binding.tvStatus.setTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.color_right)
                )
            )
        } else {
            binding.tvStatus.text = getString(R.string.wrong)
            binding.tvStatus.setTextColor(
                ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.color_error)
                )
            )
        }

        binding.btnNext.setOnClickListener {
            if (currentAnswerId == words.size - 1) {
                val resultInPercent = (result.toDouble() / words.size.toDouble()) * 100
                findNavController().navigate(
                    QuizBySignFragmentDirections.actionQuizBySignFragmentToResultFragment(
                        resultInPercent.toInt()
                    )
                )
            } else {
                lifecycleScope.launch {
                    currentAnswerId++
                    setQuestions(currentAnswerId)
                }
            }
            dialog?.dismiss()
        }


        dialog?.show()

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
        binding.videoView.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        currentAnswerId = 0
        result = 0
        binding.progressBar.progress = 0

    }

}