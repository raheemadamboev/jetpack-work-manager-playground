package xyz.teamgravity.workmanagerdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import xyz.teamgravity.workmanagerdemo.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val workManager = WorkManager.getInstance(applicationContext)
        val workRequest = PeriodicWorkRequestBuilder<RandomNumberGeneratorWorker>(15, TimeUnit.MINUTES).build()
        workManager.enqueue(workRequest)
    }
}