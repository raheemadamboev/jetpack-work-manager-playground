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
    private lateinit var workFour: WorkRequest

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
        workFour()
    }

    private fun button() {
        onWorkOneStart()
        onWorkOneStop()
        onWorkTwoStart()
        onWorkTwoStop()
        onWorkThreeStart()
        onWorkThreeStop()
        onWorkFourStart()
        onWorkFourStop()
        onWorkChainStart()
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
                    else -> Unit
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

    private fun workFour() {
        binding.apply {
            workFour = OneTimeWorkRequest.from(ProgressCoroutineWorker::class.java)
            workFourProgress.max = 100

            workManager.getWorkInfoByIdLiveData(workFour.id).observe(this@MainActivity) { workInfo ->
                workInfo?.let {
                    val progress = workInfo.progress.getInt(ProgressCoroutineWorker.PROGRESS_EXTRA, 0)
                    workFourStatusT.text = progress.toString()
                    workFourProgress.progress = progress
                }
            }
        }
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

    private fun onWorkFourStart() {
        binding.workFourStartB.setOnClickListener {
            workManager.enqueue(workFour)
        }
    }

    private fun onWorkFourStop() {
        binding.workFourStopB.setOnClickListener {
            workManager.cancelWorkById(workFour.id)
        }
    }

    private fun onWorkChainStart() {
        binding.workChainStartB.setOnClickListener {
            workManager
                .beginWith(listOf(workTwo as OneTimeWorkRequest, workFour  as OneTimeWorkRequest))
                .then(workThree as OneTimeWorkRequest)
                .enqueue()
        }
    }
}