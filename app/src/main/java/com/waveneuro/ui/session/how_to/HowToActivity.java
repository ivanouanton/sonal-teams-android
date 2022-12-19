package com.waveneuro.ui.session.how_to;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.waveneuro.R;
import com.waveneuro.ui.base.BaseActivity;
import com.waveneuro.ui.session.precautions.PrecautionsBottomSheet;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class HowToActivity extends BaseActivity {
    @Inject
    HowToViewModel howToViewModel;

    @BindView(R.id.tv_skip_onboarding)
    TextView tvSkipOnboarding;

    @BindView(R.id.tv_page_indicator)
    TextView tvPageIndicator;

    @BindView(R.id.btn_got_it)
    MaterialButton btnGotIt;

    @BindView(R.id.tl_dots)
    TabLayout tlDots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to);

        activityComponent().inject(this);
        ButterKnife.bind(this);

        tvSkipOnboarding.setPaintFlags(tvSkipOnboarding.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new HowToAdapter(this));
        tlDots.setupWithViewPager(viewPager);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                tvPageIndicator.setText((position+1)+" of 6");
                if (position == 5) {
                    btnGotIt.setVisibility(View.VISIBLE);
                    tvSkipOnboarding.setText(R.string.list_of_precautions);
                    tvSkipOnboarding.setOnClickListener(v -> {
                        PrecautionsBottomSheet precautionsBottomSheet = PrecautionsBottomSheet.newInstance();
                        precautionsBottomSheet.show(getSupportFragmentManager(), "");
                    });

                } else {
                    btnGotIt.setVisibility(View.INVISIBLE);
                    tvSkipOnboarding.setText(R.string.skip_onboarding);
                    tvSkipOnboarding.setOnClickListener(v -> finish());

                }
            }
        });
    }

    @OnClick(R.id.btn_got_it)
    public void btnGotIt() {
        finish();
    }

    @OnClick(R.id.tv_skip_onboarding)
    public void onSkipClicked() {
        finish();
    }

}