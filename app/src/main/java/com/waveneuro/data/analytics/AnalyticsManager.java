package com.waveneuro.data.analytics;

import androidx.annotation.IntDef;

import org.json.JSONObject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface AnalyticsManager {

    int MIX_PANEL = 1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MIX_PANEL})
    @interface AnalyticsDestination {
    }

    void sendEvent(String eventName, JSONObject properties, @AnalyticsDestination int destination);
}
