package com.avelycure.data.remote.service.movie

import com.avelycure.data.constants.RequestConstants
import com.avelycure.data.remote.dto.movie_info.MovieInfoDto
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.utils.io.errors.*

class MovieInfoService(
    private val client: HttpClient
) {
    suspend fun getMovieDetail(id: Int): MovieInfoDto {
        return try {
            client.get {
                with(RequestConstants) {
                    url("$BASE_URL/movie/$id?api_key=$API_KEY&append_to_response=$CREDITS,$MOVIE_IMAGES,$SIMILAR_MOVIES")
                }
            }
        } catch (e: RedirectResponseException) {
            throw Exception("Further action needs to be taken in order to complete the request")
        } catch (e: ClientRequestException) {
            throw Exception("The request contains bad syntax or cannot be fulfilled")
        } catch (e: ServerResponseException) {
            throw Exception("The server failed to fulfil an apparently valid request")
        } catch (e: IOException) {
            throw IOException("No internet connection")
        } catch (e: Exception) {
            throw Exception(" Unknown error occurred")
        }
    }
}