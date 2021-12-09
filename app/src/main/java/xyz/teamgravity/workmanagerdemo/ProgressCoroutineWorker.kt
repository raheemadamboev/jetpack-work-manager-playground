package xyz.teamgravity.workmanagerdemo

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay

class ProgressCoroutineWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    companion object {
        const val PROGRESS_EXTRA = "Progress"
    }

    override suspend fun doWork(): Result {
        var i = 1
        while (i <= 10 && !isStopped) {
            delay(1000)
            setProgress(workDataOf(PROGRESS_EXTRA to i * 10))
            i++
        }
        return Result.success()
    }
}