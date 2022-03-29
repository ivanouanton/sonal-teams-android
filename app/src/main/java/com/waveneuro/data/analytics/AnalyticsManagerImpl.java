package com.waveneuro.data.analytics;

import android.content.Context;

import com.asif.abase.injection.qualifier.ApplicationContext;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONObject;

import javax.inject.Inject;

public class AnalyticsManagerImpl implements AnalyticsManager{

    public static final String MIXPANEL_API_TOKEN = "4ae4c1b4c948a7d8b0ec61fb6375ff6d";

    private MixpanelAPI mMixpanel;

    @Inject
    public AnalyticsManagerImpl(@ApplicationContext Context context){
        mMixpanel = MixpanelAPI.getInstance(context, MIXPANEL_API_TOKEN);
    }

    @Override
    public void sendEvent(String eventName, JSONObject properties, int destination) {
        switch (destination) {
            case AnalyticsManager.MIX_PANEL:
                mMixpanel.track(eventName, properties);
                break;
        }
    }
}
