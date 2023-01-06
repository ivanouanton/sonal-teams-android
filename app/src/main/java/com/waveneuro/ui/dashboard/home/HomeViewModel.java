package com.waveneuro.ui.dashboard.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.asif.abase.data.model.APIError;
import com.asif.abase.domain.base.UseCaseCallback;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.model.response.patient.PatientListResponse;
import com.waveneuro.data.model.response.patient.PatientResponse;
import com.waveneuro.data.model.response.protocol.ProtocolResponse;
import com.waveneuro.data.model.response.user.UserInfoResponse;
import com.waveneuro.domain.base.SingleLiveEvent;
import com.waveneuro.domain.usecase.patient.GetOrganizationsUseCase;
import com.waveneuro.domain.usecase.patient.GetPatientUseCase;
import com.waveneuro.domain.usecase.patient.GetPatientsUseCase;
import com.waveneuro.domain.usecase.protocol.GetLatestProtocolUseCase;
import com.waveneuro.domain.usecase.user.GetPersonalInfoUseCase;
import com.waveneuro.utils.ErrorUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class HomeViewModel extends ViewModel {

    @Inject
    ErrorUtil errorUtil;

    @Inject
    DataManager dataManager;

    private final MutableLiveData<HomeDeviceViewState> mDataDeviceLive = new MutableLiveData<>();
    private final MutableLiveData<HomeUserViewState> mDataUserLive = new MutableLiveData<>();
    private final MutableLiveData<HomeProtocolViewState> mDataProtocolLive = new MutableLiveData<>();
    private final SingleLiveEvent<HomeViewEffect> mDataViewEffect = new SingleLiveEvent<>();
    private final MutableLiveData<HomeClientsViewState> mDataPatientsLive = new MutableLiveData<>();

    private final GetLatestProtocolUseCase getLatestProtocolUseCase;
    private final GetPersonalInfoUseCase getPersonalInfoUseCase;
    private final GetPatientsUseCase getPatientsUseCase;
    private final GetOrganizationsUseCase getOrganizationsUseCase;
    private final GetPatientUseCase getPatientUseCase;

    private ArrayList<PatientListResponse.Patient> mPatientList = new ArrayList<>();

    public MutableLiveData<Integer> mPage = new MutableLiveData<>(1);

    @Inject
    public HomeViewModel(GetLatestProtocolUseCase getLatestProtocolUseCase,
                         GetPersonalInfoUseCase getPersonalInfoUseCase,
                         GetPatientsUseCase getPatientsUseCase,
                         GetPatientUseCase getPatientUseCase,
                         GetOrganizationsUseCase getOrganizationsUseCase) {
        this.getLatestProtocolUseCase = getLatestProtocolUseCase;
        this.getPersonalInfoUseCase = getPersonalInfoUseCase;
        this.getPatientsUseCase = getPatientsUseCase;
        this.getPatientUseCase = getPatientUseCase;
        this.getOrganizationsUseCase = getOrganizationsUseCase;
    }

    private List<PatientListResponse.Patient.Organization> organizations = new ArrayList<>();

    public void processEvent(HomeViewEvent viewEvent) {
        if (viewEvent instanceof HomeViewEvent.Start) {
            HomeViewEvent.Start start = (HomeViewEvent.Start) viewEvent;
            if (this.mDataDeviceLive.getValue() instanceof HomeDeviceViewState.StartSession) {
                return;
            }
            this.mDataDeviceLive.postValue(new HomeDeviceViewState.PairDevice());
            getUserDetails();
            getClients(start.getPage(), start.getStartsWith(), start.getFilters());
            getOrganizations();
        } else if (viewEvent instanceof HomeViewEvent.DeviceDisconnected) {
            this.mDataDeviceLive.postValue(new HomeDeviceViewState.PairDevice());
        } else if (viewEvent instanceof HomeViewEvent.DeviceConnected) {
            this.mDataDeviceLive.postValue(new HomeDeviceViewState.StartSession());
        } else if (viewEvent instanceof HomeViewEvent.StartSessionClicked) {
            this.mDataViewEffect.postValue(new HomeViewEffect.DeviceRedirect());
        }
    }

    private void getUserDetails() {
        if (dataManager.getUser() != null
                && dataManager.getUser().isNameAvailable()) {
            this.mDataUserLive.postValue(new HomeUserViewState.Success(dataManager.getUser()));
        } else {
            getPersonalInfo();
        }
    }

    private void getPersonalInfo() {
        this.getPersonalInfoUseCase.execute(new UseCaseCallback() {
            @Override
            public void onSuccess(Object response) {
                Timber.e("PROFILE_SUCCESS");
                if (response instanceof UserInfoResponse) {
                    dataManager.saveUser((UserInfoResponse) response);
                    mDataUserLive.postValue(new HomeUserViewState.Success(dataManager.getUser()));
                }
            }

            @Override
            public void onError(Throwable throwable) {
                Timber.e("PROFILE_FAILURE");
            }

            @Override
            public void onFinish() {

            }
        });
    }

    public void getClients(Integer page, String startsWith, Integer[] filters) {
        mDataProtocolLive.postValue(new HomeProtocolViewState.Loading(true));

        this.getPatientsUseCase.execute(page, startsWith, filters, new UseCaseCallback() {
            @Override
            public void onSuccess(Object response) {
                mDataProtocolLive.postValue(new HomeProtocolViewState.Loading(false));
                mPatientList.addAll(((PatientListResponse) response).getPatients());
                mDataPatientsLive.postValue(new HomeClientsViewState.Success(mPatientList));
            }

            @Override
            public void onError(Throwable throwable) {
                APIError error = errorUtil.parseError(throwable);
                mDataProtocolLive.postValue(new HomeProtocolViewState.Loading(false));
            }

            @Override
            public void onFinish() {

            }
        });
    }

    void getOrganizations(){
        this.getOrganizationsUseCase.execute(new UseCaseCallback() {
            @Override
            public void onSuccess(Object o) {
                organizations.addAll((List<PatientListResponse.Patient.Organization>) o);
                mDataPatientsLive.postValue(new HomeClientsViewState.OrganizationSuccess((List<PatientListResponse.Patient.Organization>) o));
            }

            @Override
            public void onError(Throwable throwable) {
                int c = 0;
            }

            @Override
            public void onFinish() {

            }
        });
    }

    void getClientWithId(int id) {
        mDataProtocolLive.postValue(new HomeProtocolViewState.Loading(true));
        this.getPatientUseCase.execute(id, new UseCaseCallback() {
            @Override
            public void onSuccess(Object response) {
                PatientResponse patientResponse = (PatientResponse) response;
                getLatestProtocolUseCase.execute(id, new UseCaseCallback<ProtocolResponse>() {
                    @Override
                    public void onSuccess(ProtocolResponse protocolResponse) {
                        dataManager.saveProtocolFrequency(protocolResponse.getProtocolFrequency());
                        dataManager.saveTreatmentLength(protocolResponse.getTreatmentLength());
                        dataManager.saveProtocolId(protocolResponse.getId());
                        dataManager.saveEegId(protocolResponse.getEegId());
                        dataManager.savePatientId((long) id);
                        mDataProtocolLive.postValue(new HomeProtocolViewState.Loading(false));
                        mDataPatientsLive.postValue(new HomeClientsViewState.PatientSuccess(patientResponse, true));
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        mDataProtocolLive.postValue(new HomeProtocolViewState.Loading(false));
                        mDataPatientsLive.postValue(new HomeClientsViewState.PatientSuccess(patientResponse, false));
                    }

                    @Override
                    public void onFinish() {
                        int x = 0;

                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
                APIError error = errorUtil.parseError(throwable);
                mDataProtocolLive.postValue(new HomeProtocolViewState.Loading(false));

            }

            @Override
            public void onFinish() {

            }
        });
    }

    void startSessionForClientWithId(int id) {
        mDataProtocolLive.postValue(new HomeProtocolViewState.Loading(true));

                getLatestProtocolUseCase.execute(id, new UseCaseCallback<ProtocolResponse>() {
                    @Override
                    public void onSuccess(ProtocolResponse protocolResponse) {
                        dataManager.saveProtocolFrequency(protocolResponse.getProtocolFrequency());
                        dataManager.saveTreatmentLength(protocolResponse.getTreatmentLength());
                        dataManager.saveProtocolId(protocolResponse.getId());
                        dataManager.saveEegId(protocolResponse.getEegId());
                        dataManager.savePatientId((long) id);
                        mDataProtocolLive.postValue(new HomeProtocolViewState.Loading(false));
                        mDataPatientsLive.postValue(new HomeClientsViewState.PatientSessionSuccess(id));
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    public MutableLiveData<HomeDeviceViewState> getDeviceData() {
        return mDataDeviceLive;
    }

    public MutableLiveData<HomeUserViewState> getUserData() {
        return mDataUserLive;
    }

    public MutableLiveData<HomeClientsViewState> getClientsData() {
        return mDataPatientsLive;
    }

    public LiveData<HomeProtocolViewState> getProtocolData() {
        return mDataProtocolLive;
    }

    public SingleLiveEvent<HomeViewEffect> getViewEffect() {
        return mDataViewEffect;
    }

    public void setNewPage(Integer newPage) {
        mPage.postValue(newPage);
        if (newPage == 1) {
            mPatientList.clear();
        }
    }
}
