package com.waveneuro.ui.dashboard.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.waveneuro.R;
import com.waveneuro.data.model.response.patient.PatientListResponse;
import com.waveneuro.data.model.response.patient.PatientResponse;
import com.waveneuro.injection.component.DaggerFragmentComponent;
import com.waveneuro.injection.module.FragmentModule;
import com.waveneuro.ui.base.BaseActivity;
import com.waveneuro.ui.base.BaseFragment;
import com.waveneuro.ui.dashboard.DashBoardViewModel;
import com.waveneuro.ui.dashboard.DashboardViewState;
import com.waveneuro.ui.dashboard.HomeActivity;
import com.waveneuro.ui.dashboard.device.DeviceFragment;
import com.waveneuro.ui.dashboard.edit_client.EditClientViewModel;
import com.waveneuro.ui.dashboard.filters.FiltersBottomSheet;
import com.waveneuro.ui.dashboard.more.WebCommand;
import com.waveneuro.ui.dashboard.view_client.ViewClientBottomSheet;
import com.waveneuro.ui.session.session.SessionCommand;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class HomeFragment extends BaseFragment implements ClientListAdapter.OnItemClickListener, EditClientViewModel.OnClientUpdated, OnFiltersChangedListener {

    private static final int REQUEST_CODE_OPEN_GPS = 1;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;

    private Integer[] filters;

    @Override
    public void onClientUpdated() {
        this.homeViewModel.processEvent(new HomeViewEvent.Start("", null));
    }

    @Override
    public void onFiltersChanged(Integer[] ids) {
        filters = ids;
        this.homeViewModel.processEvent(new HomeViewEvent.Start(etSearch.getText().toString(), filters));
    }


    @Retention(RetentionPolicy.SOURCE)
    @IntDef({BUBBLE_NO_INTERNET, BUBBLE_DEVICE_NOT_CONNECTED, BUBBLE_LOW_BATTERY,
            BUBBLE_LOW_BATTERY_PLUG, BUBBLE_LOW_BATTERY_KEEP_PLUG})
    public @interface BubbleError {
    }

    public static final int BUBBLE_NO_INTERNET = 0;
    public static final int BUBBLE_DEVICE_NOT_CONNECTED = 1;
    public static final int BUBBLE_LOW_BATTERY = 2;
    public static final int BUBBLE_LOW_BATTERY_PLUG = 3;
    public static final int BUBBLE_LOW_BATTERY_KEEP_PLUG = 4;

    @BindView(R.id.spin_kit)
    SpinKitView spinKitView;

    @BindView(R.id.tv_clients_list)
    TextView tvClientsList;

    @BindView(R.id.rvClients)
    RecyclerView rvClients;

    @BindView(R.id.tv_empty_result)
    TextView tvEmptyResult;

    @BindView(R.id.til_search)
    TextInputLayout tilSearch;

    @BindView(R.id.et_search)
    TextInputEditText etSearch;

    @Inject
    SessionCommand sessionCommand;
    @Inject
    WebCommand webCommand;

    @Inject
    HomeViewModel homeViewModel;

    private DashBoardViewModel dashBoardViewModel;

    protected ClientListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected String[] mDataset;

    FiltersBottomSheet filtersBottomSheet;


    private HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onItemClick(PatientListResponse.Patient patient) {

        homeViewModel.getClientWithId(patient.getId());
    }

    @Override
    public void onStartSessionClick(PatientListResponse.Patient patient) {
        ((HomeActivity)requireActivity()).addFragment(R.id.fr_home, DeviceFragment.newInstance());
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        fragmentComponent = DaggerFragmentComponent.builder()
                .activityComponent(((BaseActivity) getActivity()).activityComponent())
                .fragmentModule(new FragmentModule(this))
                .build();

        fragmentComponent.inject(this);
        super.onCreate(savedInstanceState);
        dashBoardViewModel = ViewModelProviders.of(requireActivity()).get(DashBoardViewModel.class);

    }

    View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (view != null)
            return view;
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        setView();
        if (dashBoardViewModel.getData().getValue() instanceof DashboardViewState.Connect) {
            this.homeViewModel.processEvent(new HomeViewEvent.DeviceConnected());
        } else {
            this.homeViewModel.processEvent(new HomeViewEvent.Start("", null));
        }

        mLayoutManager = new LinearLayoutManager(getActivity());
        rvClients.setLayoutManager(mLayoutManager);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                homeViewModel.getClients(charSequence.toString(), filters);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setObserver();
        spinKitView.setVisibility(View.INVISIBLE);

        tilSearch.setEndIconOnClickListener(v -> {
            if (filtersBottomSheet != null) {
                filtersBottomSheet.show(getChildFragmentManager(), "");
            }
        });
    }

    private void setView() {

    }


    private void setObserver() {
        homeViewModel.getUserData().observe(this.getViewLifecycleOwner(), homeUserViewStateObserver);
        homeViewModel.getClientsData().observe(this.getViewLifecycleOwner(), homeClientsViewStateObserver);
        homeViewModel.getViewEffect().observe(this.getViewLifecycleOwner(), homeViewEffectObserver);

        dashBoardViewModel.getData().observe(requireActivity(), dashboardViewState -> {
            Timber.i("DEVICE_DASHBOARD :: onChanged: received freshObject");
            if (dashboardViewState != null) {
                if (dashboardViewState instanceof DashboardViewState.Connect) {
                    homeViewModel.processEvent(new HomeViewEvent.DeviceConnected());
                } else if (dashboardViewState instanceof DashboardViewState.Disconnect) {
                    homeViewModel.processEvent(new HomeViewEvent.DeviceDisconnected());
                }
            }
        });
    }

    Observer<HomeUserViewState> homeUserViewStateObserver = viewState -> {
        if (viewState instanceof HomeUserViewState.Success) {
            HomeUserViewState.Success success = (HomeUserViewState.Success) viewState;

        }
    };

    Observer<HomeClientsViewState> homeClientsViewStateObserver = viewState -> {
        if (viewState instanceof HomeClientsViewState.Success) {
            HomeClientsViewState.Success success = (HomeClientsViewState.Success) viewState;
            mAdapter = new ClientListAdapter(success.getItem().getPatients());
            mAdapter.listener = this;
            rvClients.setAdapter(mAdapter);
            if (success.getItem().getPatients().isEmpty()) {
                tvEmptyResult.setVisibility(View.VISIBLE);
                rvClients.setVisibility(View.INVISIBLE);
            } else {
                tvEmptyResult.setVisibility(View.INVISIBLE);
                rvClients.setVisibility(View.VISIBLE);
            }
        } else if (viewState instanceof HomeClientsViewState.PatientSuccess) {
            HomeClientsViewState.PatientSuccess success = (HomeClientsViewState.PatientSuccess) viewState;

            PatientResponse patient = success.getItem();
            ViewClientBottomSheet viewClientBottomSheet =
                    ViewClientBottomSheet.newInstance(this, patient.getId(),
                            patient.getFirstName(),
                            patient.getLastName(),
                            patient.getBirthday(),
                            patient.isMale(),
                            patient.getEmail(),
                            patient.getUsername(),
                            patient.getOrganizationName(),
                            patient.isTosSigned()
                    );
            viewClientBottomSheet.show(getChildFragmentManager(), "");

        } else if (viewState instanceof HomeClientsViewState.OrganizationSuccess) {
            HomeClientsViewState.OrganizationSuccess success = (HomeClientsViewState.OrganizationSuccess) viewState;
            List<PatientListResponse.Patient.Organization> orgs = success.getItem();
            filtersBottomSheet = FiltersBottomSheet.newInstance(orgs, filters);
            filtersBottomSheet.setListener(this);

        }
    };

    Observer<HomeViewEffect> homeViewEffectObserver = viewEffect -> {
        if (viewEffect instanceof HomeViewEffect.BackRedirect) {
        } else if (viewEffect instanceof HomeViewEffect.SessionRedirect) {
            HomeViewEffect.SessionRedirect sessionRedirect = (HomeViewEffect.SessionRedirect) viewEffect;
            launchSessionScreen(sessionRedirect.getTreatmentLength(), sessionRedirect.getProtocolFrequency(), sessionRedirect.getSonalId());
        }
    };

    private void launchSessionScreen(String treatmentLength, String protocolFrequency, String sonalId) {
        if (TextUtils.isEmpty(treatmentLength) || TextUtils.isEmpty(protocolFrequency)) {
            Toast.makeText(requireActivity(), "Treatment data not available.", Toast.LENGTH_SHORT).show();
            return;
        }
        sessionCommand.navigate(treatmentLength, protocolFrequency, sonalId);
    }

    private void onPermissionGranted(String permission) {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
                    new AlertDialog.Builder(requireActivity())
                            .setTitle("Location Services not enabled")
                            .setMessage("Android requires that Location Services to enabled to scan for Bluetooth Low Energy devices.\nPlease enable Location Services in Settings to continue. ")
                            .setNegativeButton(R.string.cancel,
                                    (dialog, which) -> {
                                    })
                            .setPositiveButton("Setting",
                                    (dialog, which) -> {
                                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivityForResult(intent, REQUEST_CODE_OPEN_GPS);
                                    })

                            .setCancelable(false)
                            .show();
                } else {
                    this.homeViewModel.processEvent(new HomeViewEvent.StartSessionClicked());
                }
                break;
        }
    }

    private boolean checkGPSIsOpen() {
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null)
            return false;
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }


    @Override
    public final void onRequestPermissionsResult(int requestCode,
                                                 @NonNull String[] permissions,
                                                 @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION_LOCATION:
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            onPermissionGranted(permissions[i]);
                        }
                    }
                }
                break;
        }
    }


}