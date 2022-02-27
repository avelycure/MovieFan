package com.avelycure.movie_info.presentation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import com.avelycure.data.constants.RequestConstants
import com.avelycure.domain.constants.MovieConstants.YT_FRAGMENT_PARAMETER_VIDEO_PATH
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragmentX

class YTFragment : YouTubePlayerSupportFragmentX(), YouTubePlayer.OnInitializedListener {
    private lateinit var videoPath: String

    companion object {
        fun getInstance(videoPath: String) = YTFragment().apply {
            arguments = bundleOf(YT_FRAGMENT_PARAMETER_VIDEO_PATH to videoPath)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.getString(YT_FRAGMENT_PARAMETER_VIDEO_PATH)?.let {
            videoPath = it
            Log.d("mytag", "OnAttach")
        }
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?,
        youTubePlayer: YouTubePlayer?,
        b: Boolean
    ) {
        Log.d("mytag", "Success")
        youTubePlayer?.cueVideo(videoPath)
        youTubePlayer?.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
    }

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider?,
        youTubeInitializationResult: YouTubeInitializationResult?
    ) {
        Log.d("mytag", "Failed" + youTubeInitializationResult?.name)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initialize(RequestConstants.YOUTUBE_API_KEY, this)
    }
}