package com.waveneuro.ui.session.history;

public class Session {
        public String name;
        public String rd;
        public String sd;
        public Boolean isCompleted;

        public Session(String name, String rd, String sd, Boolean isCompleted) {
            this.name = name;
            this.rd = rd;
            this.sd = sd;
            this.isCompleted = isCompleted;
        }
    }