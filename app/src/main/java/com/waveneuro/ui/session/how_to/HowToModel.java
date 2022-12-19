package com.waveneuro.ui.session.how_to;

import com.waveneuro.R;

public enum HowToModel {

    STEP1(R.string.how_to_step1, R.drawable.power_switch),
    STEP2(R.string.how_to_step2, R.drawable.turn_on),
    STEP3(R.string.how_to_step3, R.drawable.searching),
    STEP4(R.string.how_to_step4, R.drawable.ic_onboarding_4),
    STEP5(R.string.how_to_step5, R.drawable.ic_onboarding_5),
    STEP6(R.string.how_to_step6, R.drawable.ic_onboarding_6),;

    private int titleRes;
    private int drawableRes;

    HowToModel(int titleResId, int layoutResId) {
        titleRes = titleResId;
        drawableRes = layoutResId;
    }

    public int getTitleRes() {
        return titleRes;
    }

    public int getDrawableRes() {
        return drawableRes;
    }
}