package uz.sher.currency


import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.hilt.android.AndroidEntryPoint
import uz.sher.currency.databinding.ActivityMainBinding
import uz.sher.currency.ui.vm.MainViewModel
import uz.sher.currency.util.Localization.changeLan
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var appUpdateManager: AppUpdateManager
    private val REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)
        changeLan(mainViewModel.getLang(), this)

        checkForUpdates()
        showFeedbackDialog()
        appUpdateManager!!.registerListener(
            installStateUpdatedListener
        )
    }
    private var installStateUpdatedListener =
        InstallStateUpdatedListener { installState: InstallState ->
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                Toast.makeText(
                    this,
                    "Downloaded, Restart the app in 5 seconds",
                    Toast.LENGTH_SHORT
                ).show()
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({ appUpdateManager!!.completeUpdate() }, 5000)
//            showCompletedUpdate();
            }
        }

    private fun showFeedbackDialog() {
        val reviewManager = ReviewManagerFactory.create(this)
        reviewManager.requestReviewFlow().addOnCompleteListener { it: Task<ReviewInfo?> ->
            if (it.isSuccessful) {
                reviewManager.launchReviewFlow(this, it.result!!)
            }
        }
    }

    private fun checkForUpdates() {
        appUpdateManager!!.appUpdateInfo.addOnSuccessListener { info: AppUpdateInfo ->
            val isUpdateAvailable =
                info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateAllowed = info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            if (isUpdateAvailable && isUpdateAllowed) {
                try {
                    appUpdateManager!!.startUpdateFlowForResult(
                        info,
                        AppUpdateType.FLEXIBLE,
                        this,
                        REQUEST_CODE
                    )
                } catch (e: IntentSender.SendIntentException) {
                    throw RuntimeException(e)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                Log.e("Error", "Nimadir xato ketdi")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        appUpdateManager!!.unregisterListener(installStateUpdatedListener)
    }


}