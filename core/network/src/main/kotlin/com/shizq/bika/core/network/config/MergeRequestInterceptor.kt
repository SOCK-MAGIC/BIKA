package com.shizq.bika.core.network.config

import android.net.Uri
import android.webkit.URLUtil
import coil3.intercept.Interceptor
import coil3.request.ImageRequest
import coil3.request.ImageResult
import kotlinx.coroutines.CompletableDeferred
import java.util.concurrent.ConcurrentHashMap

/**
 * Merge the same requests to avoid multiple parallel network requests for the same URL.
 *
 * You can set the [mergeFilter] to determine which requests need to be merged,
 * the default is to merge the network requests.
 *
 * You can set the [keyGenerator] to generate the key for the request,
 * the default is to use the request data as the key.
 *
 * Fix the [issue](https://github.com/coil-kt/coil/issues/1461).
 *
 * @param mergeFilter A function that determines which requests need to be merged.
 * @param keyGenerator A function that generates the key for the request.
 * @author airsaid
 */
class MergeRequestInterceptor(
    private val mergeFilter: (ImageRequest) -> Boolean = { it.isNetUrlRequest() },
    private val keyGenerator: (ImageRequest) -> String = { it.generateKey() },
) : Interceptor {

    private val pendingRequests = ConcurrentHashMap<String, CompletableDeferred<ImageResult>>()

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val req = chain.request
        if (!mergeFilter(req)) {
            return chain.proceed()
        }

        // If there is a pending request, we should wait for it
        val requestKey = keyGenerator(req)
        val existingRequest = pendingRequests[requestKey]
        return if (existingRequest != null) {
            try {
                existingRequest.await()
            } catch (ignore: Exception) {
                // Ignore the exception to keep the request chain running
            }
            // We can't return the result of the existing request directly,
            // because it will not be shown in the UI for the same result.
            // Therefore, we should transfer the request and return a different cached result
            chain.proceed()
        } else {
            val deferred = CompletableDeferred<ImageResult>()
            pendingRequests[requestKey] = deferred
            try {
                // Proceed with the actual request
                val result = chain.proceed()
                deferred.complete(result)
                result
            } catch (e: Exception) {
                deferred.completeExceptionally(e)
                throw e
            } finally {
                // Remove the request from the pending map
                pendingRequests.remove(requestKey)
            }
        }
    }
}

private fun ImageRequest.isNetUrlRequest(): Boolean {
    val data = data
    if (data is Uri) {
        return URLUtil.isNetworkUrl(data.toString())
    }

    if (data is String) {
        return URLUtil.isNetworkUrl(data)
    }
    return false
}

private fun ImageRequest.generateKey(): String {
    return data.toString()
}
