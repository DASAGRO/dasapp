package kz.das.dasaccounting.core.extensions

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.*
import retrofit2.Response

abstract class NetworkSourceBound<RESULT, REQUEST>(val converter: (REQUEST) -> RESULT) {

    // asFlow extend method should be wrapped into try catch (-_-)
    fun asFlow() = flow<RESULT> {

        // Fetch latest data from api response
        try {
            val apiResponse = fetchFromRemote()

            // Parse body and collects
            val remote = apiResponse.body()

            // Check for response validation
            if (apiResponse.isSuccessful && remote != null) {
                // Save posts into the persistence storage
                saveRemoteData(remote)
                emit(converter(remote))
            }

        } catch (cause: Throwable) {
            fetchFromLocal()?.let {
                emit(it)
            }
            throw cause
        }

    }.catch { cause: Throwable ->
        throw cause
    }

    /**
     * Saves retrieved from remote into the persistence storage
     */
    @WorkerThread
    protected abstract suspend fun saveRemoteData(response: REQUEST)

    /**
     * Retrieves all data from persistence storage
     */
    @MainThread
    protected abstract fun fetchFromLocal(): RESULT?

    /**
     * Fetches [Response] from the remote end point if there is one
     */
    @MainThread
    protected abstract suspend fun fetchFromRemote(): Response<REQUEST>

}