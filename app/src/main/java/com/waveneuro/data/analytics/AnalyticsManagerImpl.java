package com.waveneuro.data.analytics;

import android.content.Context;

import com.asif.abase.injection.qualifier.ApplicationContext;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.waveneuro.data.Config;

import org.json.JSONObject;

import javax.inject.Inject;

public class AnalyticsManagerImpl implements AnalyticsManager{

    public static final String MIXPANEL_API_TOKEN = Config.MIXPANEL_TOKEN;

    private MixpanelAPI mMixpanel;

    @Inject
    public AnalyticsManagerImpl(@ApplicationContext Context context){
        mMixpanel = MixpanelAPI.getInstance(context, MIXPANEL_API_TOKEN);
    }

    @Override
    public void sendEvent(String eventName, JSONObject properties, int destination) {
        if (destination == AnalyticsManager.MIX_PANEL) {
            mMixpanel.track(eventName, properties);
        }
    }
}
