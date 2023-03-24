package com.waveneuro.ui.session.how_to

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.waveneuro.R
import com.waveneuro.databinding.ActivityHowToBinding
import com.waveneuro.ui.base.activity.BaseActivity
import com.waveneuro.ui.session.precautions.PrecautionsBottomSheet

class HowToActivity : BaseActivity<ActivityHowToBinding>() {

    override fun initBinding(): ActivityHowToBinding = ActivityHowToBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setView()
    }

    private fun setView() {
        with(binding) {
            btnGotIt.setOnClickListener {
                finish()
            }
            tvSkipOnboarding.setOnClickListener {
                finish()
            }
            tvSkipOnboarding.paintFlags = tvSkipOnboarding.paintFlags or Paint.UNDERLINE_TEXT_FLAG
            val viewPager = findViewById<View>(R.id.viewpager) as ViewPager
            viewPager.adapter = HowToAdapter(this@HowToActivity)
            tlDots.setupWithViewPager(viewPager)
            viewPager.addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {}
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {}
                override fun onPageSelected(position: Int) {
                    tvPageIndicator.text = (position + 1).toString() + " of 6"
                    if (position == 5) {
                        btnGotIt.visibility = View.VISIBLE
                        tvSkipOnboarding.setText(R.string.list_of_precautions)
                        tvSkipOnboarding.setOnClickListener {
                            val precautionsBottomSheet = PrecautionsBottomSheet.newInstance()
                            precautionsBottomSheet.show(supportFragmentManager, "")
                        }
                    } else {
                        btnGotIt.visibility = View.INVISIBLE
                        tvSkipOnboarding.setText(R.string.skip_onboarding)
                        tvSkipOnboarding.setOnClickListener { finish() }
                    }
                }
            })
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, HowToActivity::class.java)
    }

}