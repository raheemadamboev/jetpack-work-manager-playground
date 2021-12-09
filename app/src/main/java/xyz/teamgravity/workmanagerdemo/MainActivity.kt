package xyz.teamgravity.workmanagerdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import xyz.teamgravity.workmanagerdemo.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {
        private const val WORK_ONE = "workOne"
        private const val WORK_THREE = "workThree"
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var workManager: WorkManager
    private lateinit var workOne: WorkRequest
    private lateinit var workTwo: WorkRequest
    private lateinit var workThree: WorkRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lateInIt()
        works()
        button()
    }

    private fun lateInIt() {
        workManager = WorkManager.getInstance(applicationContext)
    }

    private fun works() {
        workOne()
        workTwo()
        workThree()
    }

    private fun button() {
        onWorkOneStart()
        onWorkOneStop()
        onWorkTwoStart()
        onWorkTwoStop()
        onWorkThreeStart()
        onWorkThreeStop()
    }

    private fun workOne() {
        // each 15 minutes, this work gets executed
        workOne = PeriodicWorkRequestBuilder<RandomNumberGeneratorWorker>(15, TimeUnit.MINUTES)
            .addTag(WORK_ONE)
            .build()
    }

    private fun workTwo() {
        binding.apply {
            // when there is wifi and device is charging, this work gets executed
            val twoConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresCharging(true)
                .build()

            workTwo = OneTimeWorkRequestBuilder<RandomNumberGeneratorWorker>()
                .setConstraints(twoConstraints)
                .build()

            // observe work
            workManager.getWorkInfoByIdLiveData(workTwo.id).observe(this@MainActivity) { workInfo ->
                when (workInfo?.state) {
                    WorkInfo.State.SUCCEEDED -> workTwoStatusT.text = "Work Two is succeeded"
                    WorkInfo.State.CANCELLED -> workTwoStatusT.text = "Work Two is cancelled"
                    WorkInfo.State.RUNNING -> workTwoStatusT.text = "Work Two is running..."
                }
            }
        }
    }

    private fun workThree() {
        // initial delayed work, after 15 seconds it gets executed
        workThree = OneTimeWorkRequestBuilder<RandomNumberGeneratorWorker>()
            .setInitialDelay(15, TimeUnit.SECONDS)
            .addTag(WORK_THREE)
            .setInputData(workDataOf(RandomNumberGeneratorWorker.ONE_EXTRA to "Tupac is the best"))
            .build()
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
            workManager.cancelWorkById(workTwo.id)
        }
    }

    private fun onWorkThreeStart() {
        binding.workThreeStartB.setOnClickListener {
            workManager.enqueue(workThree)
        }
    }

    private fun onWorkThreeStop() {
        binding.workThreeStopB.setOnClickListener {
            workManager.cancelAllWorkByTag(WORK_THREE)
        }
    }
}