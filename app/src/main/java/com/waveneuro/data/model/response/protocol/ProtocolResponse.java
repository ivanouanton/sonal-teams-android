package com.waveneuro.data.model.response.protocol;

import com.asif.abase.data.model.BaseModel;
import com.google.gson.annotations.SerializedName;

public class ProtocolResponse extends BaseModel {

    @SerializedName("msg")
    String message;
    @SerializedName("error")
    String error;

    @SerializedName("eeg_id")
    String eegId;
    @SerializedName("id")
    String id;
    @SerializedName("modified_at")
    String modifiedAt;
    @SerializedName("protocol_date_processed")
    String protocolDateProcessed;
    @SerializedName("protocol_frequency")
    String protocolFrequency;
    @SerializedName("status")
    String status;
    @SerializedName("treatment_length")
    String treatmentLength;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getEegId() {
        return eegId;
    }

    public void setEegId(String eegId) {
        this.eegId = eegId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getProtocolDateProcessed() {
        return protocolDateProcessed;
    }

    public void setProtocolDateProcessed(String protocolDateProcessed) {
        this.protocolDateProcessed = protocolDateProcessed;
    }

    public String getProtocolFrequency() {
        return protocolFrequency;
    }

    public void setProtocolFrequency(String protocolFrequency) {
        this.protocolFrequency = protocolFrequency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTreatmentLength() {
        return treatmentLength;
    }

    public void setTreatmentLength(String treatmentLength) {
        this.treatmentLength = treatmentLength;
    }
}
