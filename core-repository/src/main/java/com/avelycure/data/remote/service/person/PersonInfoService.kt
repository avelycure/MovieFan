package com.avelycure.data.remote.service.person

import com.avelycure.data.constants.RequestConstants
import com.avelycure.data.remote.dto.person.ResponsePersonInfo
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import java.io.IOException

class PersonInfoService(
    private val client: HttpClient
) {
    suspend fun getPersonInfo(id: Int): ResponsePersonInfo {
        return try {
            client.get {
                with(RequestConstants) {
                    url("$BASE_URL/person/$id?api_key=$API_KEY&append_to_response=$PERSON_IMAGES")
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