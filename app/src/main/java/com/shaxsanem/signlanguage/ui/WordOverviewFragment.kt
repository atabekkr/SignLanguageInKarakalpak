package com.shaxsanem.signlanguage.ui

import android.annotation.SuppressLint
import android.content.res.AssetFileDescriptor
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Surface
import android.view.TextureView
import android.view.View
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
class WordOverviewFragment : Fragment(R.layout.fragment_word_overview),
    TextureView.SurfaceTextureListener {

    private val binding by viewBinding(FragmentWordOverviewBinding::bind)
    private val navArgs by navArgs<WordOverviewFragmentArgs>()

    @Inject
    lateinit var dao: SLDao

    private var word: Word? = null
    private var isFavourite = false

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var assetFileDescriptor: AssetFileDescriptor

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadData()
        setupListeners()

        binding.textureView.surfaceTextureListener = this
    }

    private fun loadData() {
        lifecycleScope.launch {
            word = withContext(Dispatchers.IO) {
                dao.getWordByContent(navArgs.content)
            }
            binding.tvName.text = word?.name
            isFavourite = word?.isFavourite ?: false
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setupListeners() {

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }

        binding.btnFav.setOnClickListener {
            binding.ivStar.setImageDrawable(
                if (isFavourite) requireContext().resources.getDrawable(
                    R.drawable.ic_filled_star
                ) else requireContext().resources.getDrawable(R.drawable.ic_outlined_start)
            )
            isFavourite = !isFavourite
        }

    }

    override fun onSurfaceTextureAvailable(
        surfaceTexture: SurfaceTexture,
        width: Int,
        height: Int,
    ) {
        lifecycleScope.launch {
            try {
                assetFileDescriptor = withContext(Dispatchers.IO) {
                    requireActivity().assets.openFd(navArgs.content + ".mp4")
                }
                initializeMediaPlayer(surfaceTexture)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun initializeMediaPlayer(surfaceTexture: SurfaceTexture) {
        try {
            val surface = Surface(surfaceTexture)
            mediaPlayer = MediaPlayer().apply {
                setDataSource(
                    assetFileDescriptor.fileDescriptor,
                    assetFileDescriptor.startOffset,
                    assetFileDescriptor.length
                )
                setSurface(surface)
                isLooping = true
                setOnPreparedListener { it.start() }
                setOnErrorListener { _, _, _ -> false }
                prepareAsync()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onSurfaceTextureSizeChanged(
        surfaceTexture: SurfaceTexture,
        width: Int,
        height: Int,
    ) {
    }

    override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        return true
    }

    override fun onSurfaceTextureUpdated(surfaceTexture: SurfaceTexture) {}

    override fun onDestroyView() {
        super.onDestroyView()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
