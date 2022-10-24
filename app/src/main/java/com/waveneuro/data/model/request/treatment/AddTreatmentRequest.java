package com.waveneuro.data.model.request.treatment;

import com.asif.abase.data.model.BaseModel;
import com.google.gson.annotations.SerializedName;
import com.waveneuro.utils.DateUtil;

import java.io.Serializable;
import java.util.Calendar;

public class AddTreatmentRequest extends BaseModel implements Serializable {

    @SerializedName("eeg_id")
    Long eegId;

    @SerializedName("finished_at")
    Long finishedAt;

    @SerializedName("is_completed")
    boolean completed;

    @SerializedName("patient_id")
    Long patientId;

    @SerializedName("protocol_id")
    Long protocolId;

    @SerializedName("sonal_id")
    String sonalId;

    public Long getEegId() {
        return eegId;
    }

    public void setEegId(Long eegId) {
        this.eegId = eegId;
    }

    public Long getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Long finishedAt) {
        this.finishedAt = finishedAt;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(Long protocolId) {
        this.protocolId = protocolId;
    }

    public String getSonalId() {
        return sonalId;
    }

    public void setSonalId(String sonalId) {
        this.sonalId = sonalId;
    }

    public AddTreatmentRequest() {
    }

    public AddTreatmentRequest(Long eegId, Long finishedAt, boolean completed, Long patientId, Long protocolId, String sonalId) {
        this.eegId = eegId;
        this.finishedAt = finishedAt;
        this.completed = completed;
        this.patientId = patientId;
        this.protocolId = protocolId;
        this.sonalId = sonalId;
    }
}
