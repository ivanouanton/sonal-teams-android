package com.waveneuro.data.model.request.treatment;

import com.asif.abase.data.model.BaseModel;
import com.google.gson.annotations.SerializedName;
import com.waveneuro.utils.DateUtil;

import java.io.Serializable;
import java.util.Calendar;

public class AddTreatmentRequest extends BaseModel implements Serializable {

    @SerializedName("completed")
    boolean completed;

    @SerializedName("eeg_id")
    Long eegId;

    @SerializedName("questionnaire")
    Questionnaire questionnaire;

    @SerializedName("treatment_date")
    Long treatmentDate;

    @SerializedName("sonal_id")
    Long sonalId;

    @SerializedName("protocol_id")
    Long protocolId;

    public AddTreatmentRequest() {
        completed = true;
        questionnaire = new Questionnaire();
        treatmentDate = System.currentTimeMillis();
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Long getEegId() {
        return eegId;
    }

    public void setEegId(Long eegId) {
        this.eegId = eegId;
    }

    public void setTreatmentDate() {
        Calendar calendar = Calendar.getInstance();
        this.treatmentDate = System.currentTimeMillis();
    }

    public Long getSonalId() {
        return sonalId;
    }

    public void setSonalId(Long sonalId) {
        this.sonalId = sonalId;
    }

    public Long getProtocolId() {
        return protocolId;
    }

    public void setProtocolId(Long protocolId) {
        this.protocolId = protocolId;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    @Override
    public String toString() {
        return "AddTreatmentRequest{" +
                "completed=" + completed +
                ", eegId='" + eegId + '\'' +
                ", questionnaire=" + questionnaire +
                ", treatmentDate='" + treatmentDate + '\'' +
                '}';
    }
}
