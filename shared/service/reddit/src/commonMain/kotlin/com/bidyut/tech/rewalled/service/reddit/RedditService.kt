package com.bidyut.tech.rewalled.service.reddit

import com.bidyut.tech.rewalled.model.Filter
import com.bidyut.tech.rewalled.service.reddit.json.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments
import kotlinx.io.IOException

class RedditService(
    private val client: HttpClient,
) {
    suspend fun getPosts(
        subreddit: String,
        filter: Filter,
        limit: Int = 20,
        count: Int = 0,
        after: String? = null,
        before: String? = null,
    ): Result<Response> {
        return try {
            val response: HttpResponse = client.submitForm(
                url = URLBuilder(baseUrl)
                    .appendPathSegments("r", subreddit, "$filter.json")
                    .buildString(),
                formParameters = Parameters.build {
                    append("limit", limit.toString())
                    if (count > 0) append("count", count.toString())
                    after?.let { append("after", it) }
                    before?.let { append("before", it) }
                },
                encodeInQuery = true,
            )
            when (response.status) {
                HttpStatusCode.OK -> Result.success(response.body())
                else -> Result.failure(IllegalStateException(response.body() as String))
            }
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    suspend fun getSearch(
        query: String,
        limit: Int = 20,
        count: Int = 0,
        after: String? = null,
        before: String? = null,
    ): Result<Response> {
        return try {
            val response: HttpResponse = client.submitForm(
                url = URLBuilder(baseUrl)
                    .appendPathSegments("search.json")
                    .buildString(),
                formParameters = Parameters.build {
                    append("type", "link")
                    append("q", query)
                    append("limit", limit.toString())
                    if (count > 0) append("count", count.toString())
                    after?.let { append("after", it) }
                    before?.let { append("before", it) }
                },
                encodeInQuery = true,
            )
            when (response.status) {
                HttpStatusCode.OK -> Result.success(response.body())
                else -> Result.failure(IllegalStateException(response.body() as String))
            }
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    companion object {
        private const val baseUrl = "https://www.reddit.com/"
    }
}
