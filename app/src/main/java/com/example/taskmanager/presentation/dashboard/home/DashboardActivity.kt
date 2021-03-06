package com.example.taskmanager.presentation.dashboard.home

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.taskmanager.R
import com.example.taskmanager.data.models.TaskModel
import com.example.taskmanager.presentation.BaseActivity
import com.example.taskmanager.presentation.dashboard.addTask.AddTaskActivity
import com.example.taskmanager.presentation.dashboard.home.filter.FilterFragment
import com.example.taskmanager.presentation.dashboard.home.taskDetail.TaskDetailFragment
import com.example.taskmanager.presentation.dashboard.profile.ProfileActivity
import com.example.taskmanager.utils.CommonUtils
import com.example.taskmanager.utils.ConstUtils
import com.example.taskmanager.utils.launchActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel


class DashboardActivity : BaseActivity<DashboardViewModel>() {

    private var isQueryChanged = false
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

        dashboardViewModel.getUserInfo()

        lifecycleScope.launchWhenStarted {
            dashboardViewModel.userInfoFlow.collect {
                tv_dashboard_user_name.text =
                    "${resources.getString(R.string.user_welcome)} ${it.name}"
                fab_add_task_dashboard.visibility =
                    if (it.role == ConstUtils.ROLE_USER) View.GONE else View.VISIBLE
            }
        }

        iv_menu_dashboard.setOnClickListener {
            if (mBottomSheetBehavior?.state == BottomSheetBehavior.STATE_COLLAPSED)
                openBottomSheet()
            else
                closeBottomSheet()
//            if (menu_items.visibility == View.INVISIBLE) {
//                val animate = TranslateAnimation(
//                    0f,  // fromXDelta
//                    0f,  // toXDelta
//                    0f,  // fromYDelta
//                    menu_items.height.toFloat()
//                ) // toYDelta
//
//                animate.duration = 500
//                animate.fillAfter = true
//                animate.setAnimationListener(object : Animation.AnimationListener {
//                    override fun onAnimationStart(p0: Animation?) {
//                    }
//
//                    override fun onAnimationEnd(p0: Animation?) {
//                        menu_items.visibility = View.VISIBLE
//                    }
//
//                    override fun onAnimationRepeat(p0: Animation?) {
//                    }
//
//                })
//                coordinatorLayout.startAnimation(animate)
//            }else{
//                menu_items.visibility = View.INVISIBLE
//                val animate = TranslateAnimation(
//                    0f,  // fromXDelta
//                    0f,  // toXDelta
//                    menu_items.height.toFloat(),  // fromYDelta
//                    0f
//                ) // toYDelta
//
//                animate.duration = 500
//                animate.fillAfter = true
//                coordinatorLayout.startAnimation(animate)
//            }
        }

        fab_add_task_dashboard.setOnClickListener {
            launchActivity<AddTaskActivity>()
        }

        menu_item_profile.setOnClickListener {
            launchActivity<ProfileActivity>()
        }
        supportFragmentManager.addFragmentOnAttachListener { fragmentManager, fragment ->
            fab_add_task_dashboard.visibility = View.GONE
        }
        menu_item_filetr.setOnClickListener {
            val filterFragment = FilterFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.frl_dashboard, filterFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun getViewModel(): DashboardViewModel = dashboardViewModel

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.dashboard_toolbar, menu)
        super.onCreateOptionsMenu(menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView: SearchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String): Boolean {
                val taskModel = TaskModel(taskName = text)
                dashboardViewModel.searchTask(taskModel)
                isQueryChanged = true
                return true
            }

            override fun onQueryTextChange(text: String): Boolean {
                return true
            }

        })
        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                if (isQueryChanged) {
                    dashboardViewModel.getUserTasks()
                    isQueryChanged = false
                }
                return true
            }

        })

        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        return true
    }

    fun closeBottomSheet() {
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
        menu_items.visibility = View.VISIBLE
    }

    fun openBottomSheet() {
        mBottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        menu_items.visibility = View.INVISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (supportFragmentManager.backStackEntryCount == 0 && getViewModel().userInfoFlow.value.role == "admin")
            fab_add_task_dashboard.visibility = View.VISIBLE
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

                menu_items.viewTreeObserver
                    .addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            menu_items.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            mBottomSheetBehavior?.setPeekHeight(
                                CommonUtils.getScreenHeight(this@DashboardActivity) - (toolbar.layoutParams.height + menu_items.height + 50),
                                true
                            )
                        }
                    })

            }
        }
    }

}

