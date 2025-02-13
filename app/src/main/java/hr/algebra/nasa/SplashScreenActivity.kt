package hr.algebra.nasa

import android.animation.AnimatorInflater
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import hr.algebra.nasa.databinding.ActivitySplashScreenBinding
import hr.algebra.nasa.api.AnimalWorker
import hr.algebra.nasa.framework.applyAnimation
import hr.algebra.nasa.framework.callDelayed
import hr.algebra.nasa.framework.getBooleanPreference
import hr.algebra.nasa.framework.isOnline
import hr.algebra.nasa.framework.startActivity

private const val DELAY = 3000L
const val DATA_IMPORTED = "hr.algebra.nasa.data_imported"
private const val REQUEST_CODE_NOTIFICATIONS = 123
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAnimations()
        redirect()
    }



    @SuppressLint("ResourceType")
    private fun startAnimations() {
        val rainbowAnimator = AnimatorInflater.loadAnimator(this, R.anim.rainbow)
        rainbowAnimator.setTarget(binding.tvSplash)
        rainbowAnimator.start()

        binding.ivSplash.applyAnimation(R.anim.jump_up_down)

    }

    private fun redirect() {

        if (getBooleanPreference(DATA_IMPORTED)) {
            callDelayed(DELAY) { startActivity<HostActivity>() }

        } else {

            if (isOnline()) {

                WorkManager.getInstance(this).apply {
                    enqueueUniqueWork(
                        DATA_IMPORTED,
                        ExistingWorkPolicy.KEEP,
                        OneTimeWorkRequest.Companion.from(AnimalWorker::class.java)
                    )
                }

            } else {
                binding.tvSplash.text = getString(R.string.no_internet)
                callDelayed(DELAY) { finish() }
            }
        }
    }

}