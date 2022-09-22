package com.waveneuro.data;

public class Config {
    private static final String PROTOCOL = "http://";
    public static final String PROTOCOL_S = "https://";
    public static final String HOSTS[] = new String[]{
            "api.wn.repmvbx.xyz"
    };

    public static final String BASE_URL = PROTOCOL_S + HOSTS[0] + "/";

    public static final String SONAL_URL = PROTOCOL_S + "www.waveneuro.com";
    public static final String FAQ_URL = PROTOCOL_S + "www.waveneuro.com";
    public static final String SUPPORT_URL = PROTOCOL_S + "waveneurohelp.zendesk.com/hc/en-us/requests/new";
    public static final String TERMS_OF_USE_URL = PROTOCOL_S + "www.waveneuro.com/pages/privacy";
    public static final String PRIVACY_POLICY_URL = PROTOCOL_S + "www.waveneuro.com/pages/privacy";
    public static final String CONTACT_US_URL = PROTOCOL_S + "www.waveneuro.com/contact";

    public static final String BOOK_CONSULTATION_URL = PROTOCOL_S + "waveneurocardiff.com/products/brain-scan";
    public static final String FIND_OUT_MORE_URL = PROTOCOL_S + "waveneuro.com/pages/about-us";

    public static final int CONNECT_TIMEOUT = 100;
    public static final int READ_TIMEOUT = 100;
    public static final int WRITE_TIMEOUT = 60 * 5;
}
