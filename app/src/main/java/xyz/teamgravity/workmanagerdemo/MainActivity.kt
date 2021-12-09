package xyz.teamgravity.workmanagerdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import xyz.teamgravity.workmanagerdemo.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {
        private const val WORK_ONE = "workOne"
        private const val WORK_TWO = "workTwo"
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var workManager: WorkManager
    private lateinit var workOne: WorkRequest
    private lateinit var workTwo: WorkRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lateInIt()
        button()
    }

    private fun lateInIt() {
        workManager = WorkManager.getInstance(applicationContext)

        // each 15 minutes, this work gets executed
        workOne = PeriodicWorkRequestBuilder<RandomNumberGeneratorWorker>(15, TimeUnit.MINUTES)
            .addTag(WORK_ONE)
            .build()

        // when there is wifi and device is charging, this work gets executed
        val twoConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresCharging(true)
            .build()

        workTwo = OneTimeWorkRequestBuilder<RandomNumberGeneratorWorker>()
            .addTag(WORK_TWO)
            .setConstraints(twoConstraints)
            .build()
    }

    private fun button() {
        onWorkOneStart()
        onWorkOneStop()
        onWorkTwoStart()
        onWorkTwoStop()
    }

    private fun onWorkOneStart() {
        binding.workOneStartB.setOnClickListener {
            workManager.enqueue(workOne)
        }
    }

    private fun onWorkOneStop() {
        binding.workOneStopB.setOnClickListener {
            workManager.cancelAllWorkByTag(WORK_ONE)
        }
    }

    private fun onWorkTwoStart() {
        binding.workTwoStartB.setOnClickListener {
            workManager.enqueue(workTwo)
        }
    }

    private fun onWorkTwoStop() {
        binding.workTwoStopB.setOnClickListener {
            workManager.cancelAllWorkByTag(WORK_TWO)
        }
    }
}