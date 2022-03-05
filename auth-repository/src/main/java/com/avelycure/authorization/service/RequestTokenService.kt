package com.avelycure.authorization.service

import com.avelycure.authorization.constants.RequestConstants
import com.avelycure.authorization.dto.TokenDto
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import java.io.IOException

class RequestTokenService(
    private val client: HttpClient
) {
    suspend fun requestToken(): TokenDto {
        return try {
            client.get {
                with(RequestConstants) {
                    url("$BASE_URL/authentication/token/new?api_key=$API_KEY")
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