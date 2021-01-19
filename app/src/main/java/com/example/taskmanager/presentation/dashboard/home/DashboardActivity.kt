package com.example.taskmanager.presentation.dashboard.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import com.example.taskmanager.R
import com.example.taskmanager.presentation.BaseActivity
import com.example.taskmanager.presentation.dashboard.profile.ProfileActivity
import com.example.taskmanager.utils.launchActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel


class DashboardActivity : BaseActivity<DashboardViewModel>() {

    private val dashboardViewModel: DashboardViewModel by viewModel()
    private var mBottomSheetBehavior: BottomSheetBehavior<View?>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        configureBackdrop()
        setup()
    }

    private fun setup() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
//       supportActionBar?.setDisplayHomeAsUpEnabled(true)
//       supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_home_24)
//       supportActionBar?.setHomeButtonEnabled(true)

        dashboardViewModel.getUserInfo()

        lifecycleScope.launchWhenStarted {
            dashboardViewModel.userInfoFlow.collect {
                tv_dashboard_user_name.text = "${resources.getString(R.string.user_welcome)} ${it.name}"
            }
        }

        iv_menu_dashboard.setOnClickListener{
            if (menu_items.visibility == View.INVISIBLE) {
                val animate = TranslateAnimation(
                    0f,  // fromXDelta
                    0f,  // toXDelta
                    0f,  // fromYDelta
                    menu_items.height.toFloat()
                ) // toYDelta

                animate.duration = 500
                animate.fillAfter = true
                animate.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(p0: Animation?) {
                    }

                    override fun onAnimationEnd(p0: Animation?) {
                        menu_items.visibility = View.VISIBLE
                    }

                    override fun onAnimationRepeat(p0: Animation?) {
                    }

                })
                coordinatorLayout.startAnimation(animate)
            }else{
                menu_items.visibility = View.INVISIBLE
                val animate = TranslateAnimation(
                    0f,  // fromXDelta
                    0f,  // toXDelta
                    menu_items.height.toFloat(),  // fromYDelta
                    0f
                ) // toYDelta

                animate.duration = 500
                animate.fillAfter = true
                coordinatorLayout.startAnimation(animate)
            }
        }

        menu_item_profile.setOnClickListener {
            launchActivity<ProfileActivity>()
        }

        menu_item_filetr.setOnClickListener {

        }
    }

    override fun getViewModel(): DashboardViewModel = dashboardViewModel

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.dashboard_toolbar, menu)
        super.onCreateOptionsMenu(menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView = searchItem?.actionView as SearchView


        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        return true
    }

    fun closeBottomSheet() {
//        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    fun openBottomSheet() {
//        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun configureBackdrop() {
        // Get the fragment reference
        val fragment = supportFragmentManager.findFragmentById(R.id.filter_fragment)

        fragment?.let {
            // Get the BottomSheetBehavior from the fragment view
            BottomSheetBehavior.from(it.view as View)?.let { bsb ->
                // Set the initial state of the BottomSheetBehavior to HIDDEN
                bsb.state = BottomSheetBehavior.STATE_EXPANDED

                // Set the trigger that will expand your view

                // Set the reference into class attribute (will be used latter)
                mBottomSheetBehavior = bsb
            }
        }
    }
}

