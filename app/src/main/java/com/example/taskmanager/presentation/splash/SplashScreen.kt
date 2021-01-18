package com.example.taskmanager.presentation.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.transition.doOnEnd
import com.example.taskmanager.R
import com.example.taskmanager.presentation.authentication.AuthenticationActivity
import com.example.taskmanager.presentation.dashboard.home.DashboardActivity
import com.example.taskmanager.utils.launchActivity
import kotlinx.android.synthetic.main.splash_screeen.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class SplashScreen : AppCompatActivity() {

    val splashViewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screeen)

        splashViewModel.tokenLivedata.observe(this) {

            Handler(Looper.getMainLooper()).postDelayed(kotlinx.coroutines.Runnable {
                if (it.isNotEmpty()) {
                    launchActivity<DashboardActivity>()
                    finish()
                    return@Runnable
                }
                launchActivity<AuthenticationActivity>()
                finish()
            }, 1000)
        }

        Handler(Looper.getMainLooper()).postDelayed(kotlinx.coroutines.Runnable {
            val constraintSet = ConstraintSet()
            val transition = ChangeBounds()
            transition.interpolator = DecelerateInterpolator()
            transition.duration = 2000
            transition.doOnEnd {
               splashViewModel.readUserInfo()
            }
            TransitionManager.beginDelayedTransition(root, transition)
            constraintSet.clone(this, R.layout.splash_screen_constraint)
            constraintSet.applyTo(root)
        }, 100)


    }

}