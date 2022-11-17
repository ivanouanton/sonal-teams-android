package com.waveneuro.ui.dashboard.edit_client;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.model.request.patient.PatientRequest;
import com.waveneuro.data.model.response.patient.PatientResponse;
import com.waveneuro.domain.usecase.patient.UpdatePatientUseCase;
import com.waveneuro.ui.dashboard.home.HomeClientsViewState;

import javax.inject.Inject;

public class EditClientViewModel extends ViewModel {


    private final UpdatePatientUseCase updatePatientUseCase;

    public MutableLiveData<HomeClientsViewState> getDataPatientsLive() {
        return mDataPatientsLive;
    }

    private final MutableLiveData<HomeClientsViewState> mDataPatientsLive = new MutableLiveData<>();

    @Inject
    public EditClientViewModel(UpdatePatientUseCase updatePatientUseCase) {
        this.updatePatientUseCase = updatePatientUseCase;
    }

    public void updateClient(int id, String firstName, String lastName, String dob, boolean isMale, String email) {
        PatientRequest request = new PatientRequest();
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setBirthday(dob);
        //request.setMale(isMale);
        request.setEmail(email);
        //mDataProtocolLive.postValue(new HomeProtocolViewState.Loading(true));
        this.updatePatientUseCase.execute(request, id, new UseCaseCallback() {
            @Override
            public void onSuccess(Object response) {
                PatientResponse patientResponse = (PatientResponse) response;
                mDataPatientsLive.postValue(new HomeClientsViewState.PatientSuccess(patientResponse, true));

            }

            @Override
            public void onError(Throwable throwable) {
                int x = 0;

            }

            @Override
            public void onFinish() {

            }
        });
    }

    public interface OnClientUpdated{
        void onClientUpdated();
    }

}