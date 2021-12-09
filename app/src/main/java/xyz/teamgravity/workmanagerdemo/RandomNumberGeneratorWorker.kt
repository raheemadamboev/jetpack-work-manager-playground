package xyz.teamgravity.workmanagerdemo

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlin.random.Random

class RandomNumberGeneratorWorker(context: Context, workerParameters: WorkerParameters) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        generateRandomNumber()
        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()

    }

    private fun generateRandomNumber() {
        var i = 0
        while (i < 5) {
            try {
                Thread.sleep(1000)
                println("raheem: ${Random.nextInt(0, 100)}")
                i++
            } catch (e: Exception) {
                println("raheem: Exception in generateRandomNumber -> ${e.message}")
            }
        }
    }
}