package com.avelycure.domain.models

import com.avelycure.domain.constants.ErrorCodes

/**
 * Class to store information about movie. Now it stores only key, but class is needed for easier
 * development in future
 */
data class VideoInfo(
    val key: String = ErrorCodes.ERROR_NO_TRAILER_CODE.toString()
)