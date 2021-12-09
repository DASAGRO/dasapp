package kz.das.dasaccounting.data.source.workmanagers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import kz.das.dasaccounting.domain.AwaitRequestInventoryRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class AwaitRequestWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params), KoinComponent {

    private val awaitRequestInventoryRepository: AwaitRequestInventoryRepository by inject()

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            var result: Result? = null
            awaitRequestInventoryRepository
                .initAwaitRequests()
                .catch {
                    result = Result.retry()
                }.collect {
                    awaitRequestInventoryRepository.removeAllAwaitRequests()
                    result = Result.success()
                }
            Log.d("awaitRequest", result.toString())
            return@withContext result!!
        } catch (error: Throwable) {
            return@withContext Result.retry()
        }
    }
}