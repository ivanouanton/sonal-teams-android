package com.waveneuro.data.model.response.session;

import com.asif.abase.data.model.BaseModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SessionResponse extends BaseModel {


    @SerializedName("sessions")
    private List<Session> sessions;

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public SessionResponse(List<Session> sessions) {
        this.sessions = sessions;
    }

    public class Session {

        public Session(Double eegRecordedAt, Double finishedAt, boolean isCompleted, String sonalId) {
            this.eegRecordedAt = eegRecordedAt;
            this.finishedAt = finishedAt;
            this.isCompleted = isCompleted;
            this.sonalId = sonalId;
        }

        @SerializedName("eeg_recorded_at")
        public Double eegRecordedAt;

        @SerializedName("finished_at")
        public Double finishedAt;

        @SerializedName("is_completed")
        private boolean isCompleted;

        @SerializedName("sonal_id")
        private String sonalId;

        public Double getEegRecordedAt() {
            return eegRecordedAt;
        }

        public void setEegRecordedAt(Double eegRecordedAt) {
            this.eegRecordedAt = eegRecordedAt;
        }

        public Double getFinishedAt() {
            return finishedAt;
        }

        public void setFinishedAt(Double finishedAt) {
            this.finishedAt = finishedAt;
        }

        public boolean isCompleted() {
            return isCompleted;
        }

        public void setCompleted(boolean completed) {
            isCompleted = completed;
        }

        public String getSonalId() {
            return sonalId;
        }

        public void setSonalId(String sonalId) {
            this.sonalId = sonalId;
        }
    }
}
