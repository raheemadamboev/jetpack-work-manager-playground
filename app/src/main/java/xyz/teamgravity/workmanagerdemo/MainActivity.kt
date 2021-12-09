package xyz.teamgravity.workmanagerdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import xyz.teamgravity.workmanagerdemo.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {
        private const val WORK_ONE = "workOne"
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var workManager: WorkManager
    private lateinit var workOne: WorkRequest

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
    }

    private fun button() {
        onWorkOneStart()
        onWorkOneStop()
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
}