package com.waveneuro.data.analytics;

public class AnalyticsEvent {

    private AnalyticsEvent() {}

    public static final String LOGIN = "login";
    public static final String LOGOUT = "logout";
    public static final String SESSION_START = "session_start";
    public static final String SESSION_COMPLETED = "session_completed";
    public static final String SESSION_STARTED = "session_started";
    public static final String SESSION_ENDED_MANUALLY = "session_ended_manually";
    public static final String SESSION_PAUSED = "session_paused";
}
