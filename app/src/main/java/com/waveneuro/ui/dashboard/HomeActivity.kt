package com.waveneuro.ui.dashboard

import android.os.Bundle
import android.view.MenuItem
import com.waveneuro.R
import com.waveneuro.databinding.ActivityHomeBinding
import com.waveneuro.ui.base.BaseActivity
import com.waveneuro.ui.dashboard.home.HomeFragment
import com.waveneuro.ui.dashboard.home.MoreFragment

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent()?.inject(this)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnNavigationItemSelectedListener(::onNavItemSelected)
        binding.bottomNavigation.selectedItemId = R.id.bottom_navigation_home
    }

    private fun onNavItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.bottom_navigation_home -> {
                HomeFragment.newInstance()
            }
            R.id.bottom_navigation_more -> {
                MoreFragment.newInstance()
            }
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
//                addFragment(R.id.fr_home, DeviceFragment.newInstance())
//            }
//        }
//    }

    companion object {
        const val TAB_HOME = 0
        const val TAB_DEVICE = 1
        const val TAB_HISTORY = 2
        const val TAB_MORE = 3
    }
}