package com.avelycure.data.remote.service.movie

import com.avelycure.data.constants.RequestConstants
import com.avelycure.data.remote.dto.movie.MovieResponse
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.utils.io.errors.*

class SearchMovieService(
    private val client: HttpClient
) {
    suspend fun getMovieByName(query: String, page: Int): MovieResponse {
        return try {
            client.get {
                with(RequestConstants) {
                    url("$BASE_URL/search/movie?api_key=$API_KEY&query=$query&page=$page")
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