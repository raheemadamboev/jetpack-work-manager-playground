package xyz.teamgravity.workmanagerdemo

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlin.random.Random

class RandomNumberGeneratorWorker(
    context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        generateRandomNumber()
        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        raheem("$id stopped")
    }

    // each second generates random number from 0 to 100
    private fun generateRandomNumber() {
        var i = 0
        while (i < 5 && !isStopped) {
            try {
                Thread.sleep(1000)
                i++
                raheem("$id -> ${Random.nextInt(0, 100).toString()}")
            } catch (e: Exception) {
                raheem("Exception thrown in generateRandomNumber $id -> ${e.message}")
            }
        }
    }
}