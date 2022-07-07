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
    String eegId;

    @SerializedName("questionnaire")
    Questionnaire questionnaire;

    @SerializedName("treatment_date")
    String treatmentDate;

    public AddTreatmentRequest() {
        completed = true;
        questionnaire = new Questionnaire();
        treatmentDate = DateUtil.getCurrentDate();
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getEegId() {
        return eegId;
    }

    public void setEegId(String eegId) {
        this.eegId = eegId;
    }

    public void setTreatmentDate() {
        Calendar calendar = Calendar.getInstance();
        this.treatmentDate = DateUtil.formatDate(calendar.getTime(), DateUtil.PATTERN_ISO8601);
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
