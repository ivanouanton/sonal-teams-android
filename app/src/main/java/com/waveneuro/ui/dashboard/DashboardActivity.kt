package com.waveneuro.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.waveneuro.R
import com.waveneuro.databinding.ActivityDashboardBinding
import com.waveneuro.ui.base.activity.BaseViewModelActivity
import com.waveneuro.ui.dashboard.home.HomeFragment
import com.waveneuro.ui.dashboard.more.MoreFragment
import com.waveneuro.utils.ext.getAppComponent

class DashboardActivity : BaseViewModelActivity<ActivityDashboardBinding, DashboardViewModel>() {

    override val viewModel: DashBoardViewModelImpl by viewModels {
        getAppComponent().dashboardViewModelFactory()
    }

    override fun initBinding(): ActivityDashboardBinding =
        ActivityDashboardBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.bottomNavigation.setOnNavigationItemSelectedListener(::onNavItemSelected)
        binding.bottomNavigation.selectedItemId = R.id.bottom_navigation_home
    }

    private fun onNavItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bottom_navigation_home -> HomeFragment.newInstance()
            R.id.bottom_navigation_more -> MoreFragment.newInstance()
            else -> null
        }?.let { fragment ->
            supportFragmentManager.beginTransaction()
                .replace(R.id.fr_container, fragment)
                .commit()

            return true
        }

        return false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

//    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (data != null && data.hasExtra(SessionHistoryActivity.START_SESSION)) {
//            if (data.getBooleanExtra(SessionHistoryActivity.START_SESSION, false)) {
//                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
//                addContentView(R.id.fr_container, DeviceFragment.newInstance())
//            }
//        }
//    }

    companion object {
        fun newIntent(context: Context) = Intent(context, DashboardActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

}