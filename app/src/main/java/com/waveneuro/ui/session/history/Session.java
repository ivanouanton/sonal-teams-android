package com.waveneuro.ui.session.history;

public class Session {
        public String name;
        public String rd;
        public String sd;
        public String status;

        public Session(String name, String rd, String sd, String status) {
            this.name = name;
            this.rd = rd;
            this.sd = sd;
            this.status = status;
        }
    }