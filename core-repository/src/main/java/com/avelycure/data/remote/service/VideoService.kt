package com.avelycure.data.remote.service

import com.avelycure.data.remote.dto.video.VideoResponseDto
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.utils.io.errors.*

class VideoService(
    private val client: HttpClient
) {
    suspend fun getVideos(id: Int): VideoResponseDto {
        return try {
            client.get {
                with(com.avelycure.data.constants.RequestConstants) {
                    url("$BASE_URL/movie/$id/videos?api_key=$API_KEY")
                }
            }
        } catch (e: RedirectResponseException) {
            throw Exception("Further action needs to be taken in order to complete the request")
        } catch (e: ClientRequestException) {
            throw Exception("The request contains bad syntax or cannot be fulfilled")
        } catch (e: ServerResponseException) {
            throw Exception("The server failed to fulfil an apparently valid request")
        } catch (e: IOException) {
            throw Exception("No internet connection")
        } catch (e: Exception) {
            throw Exception(" Unknown error occurred")
        }
    }
}