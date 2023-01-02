package com.waveneuro.data.analytics;

public class AnalyticsEvent {

    private AnalyticsEvent() {}

    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";
    public static final String SESSION_STARTED = "Session Started";
    public static final String SESSION_PAUSED = "Session Paused";
    public static final String SESSION_COMPLETED = "Session Completed";
    public static final String SESSION_TERMINATED_EARLY = "Session Terminated Early";
}
