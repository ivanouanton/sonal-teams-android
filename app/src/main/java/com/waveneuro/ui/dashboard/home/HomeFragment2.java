///**/package com.waveneuro.ui.dashboard.home;
//
//import android.Manifest;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.LocationManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.Settings;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.IntDef;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProviders;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
//
//import com.github.ybq.android.spinkit.SpinKitView;
//import com.google.android.material.textfield.TextInputEditText;
//import com.google.android.material.textfield.TextInputLayout;
//import com.waveneuro.R;
//import com.waveneuro.data.model.response.patient.PatientListResponse;
//import com.waveneuro.data.model.response.patient.PatientResponse;
//import com.waveneuro.injection.component.DaggerFragmentComponent;
//import com.waveneuro.injection.module.FragmentModule;
//import com.waveneuro.ui.base.BaseActivity;
//import com.waveneuro.ui.base.BaseFragment;
//import com.waveneuro.ui.dashboard.DashBoardViewModel;
//import com.waveneuro.ui.dashboard.DashboardViewState;
//import com.waveneuro.ui.dashboard.home.bottom_sheet.edit_client.EditClientViewModel;
//import com.waveneuro.ui.dashboard.home.bottom_sheet.filters.FiltersBottomSheet;
//import com.waveneuro.ui.dashboard.home.adapter.ClientListAdapter;
//import com.waveneuro.ui.dashboard.home.bottom_sheet.view_client.ViewClientBottomSheet;
//import com.waveneuro.ui.dashboard.web.WebCommand;
//import com.waveneuro.ui.session.session.SessionCommand;
//
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import javax.inject.Inject;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import timber.log.Timber;
//
//public class HomeFragment2 extends BaseFragment implements ClientListAdapter.OnItemClickListener, EditClientViewModel.OnClientUpdated, OnFiltersChangedListener {
//
//    private static final int REQUEST_CODE_OPEN_GPS = 1;
//    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;
//
//    private ArrayList<PatientListResponse.Patient> currentList = new ArrayList<>();
//
//    @Override
//    public void onClientUpdated() {
//        this.homeViewModel.processEvent(new HomeViewEvent.Start(1, "", null));
//    }
//
//    @Override
//    public void onFiltersChanged(Integer[] ids) {
//        homeViewModel.filters.addAll(ids);
//        homeViewModel.getNewClients(etSearch.getText().toString());
//        this.homeViewModel.processEvent(new HomeViewEvent.Start(homeViewModel.getPage().getValue(), etSearch.getText().toString(), homeViewModel.filters));
//    }
//
//    @Retention(RetentionPolicy.SOURCE)
//    @IntDef({BUBBLE_NO_INTERNET, BUBBLE_DEVICE_NOT_CONNECTED, BUBBLE_LOW_BATTERY,
//            BUBBLE_LOW_BATTERY_PLUG, BUBBLE_LOW_BATTERY_KEEP_PLUG})
//    public @interface BubbleError {
//    }
//
//    public static final int BUBBLE_NO_INTERNET = 0;
//    public static final int BUBBLE_DEVICE_NOT_CONNECTED = 1;
//    public static final int BUBBLE_LOW_BATTERY = 2;
//    public static final int BUBBLE_LOW_BATTERY_PLUG = 3;
//    public static final int BUBBLE_LOW_BATTERY_KEEP_PLUG = 4;
//
//    @BindView(R.id.spin_kit)
//    SpinKitView spinKitView;
//
//    @BindView(R.id.tv_clients_list)
//    TextView tvClientsList;
//
//    @BindView(R.id.sr_clients)
//    SwipeRefreshLayout srClients;
//
//    @BindView(R.id.rvClients)
//    RecyclerView rvClients;
//
//    @BindView(R.id.tv_empty_result)
//    TextView tvEmptyResult;
//
//    @BindView(R.id.til_search)
//    TextInputLayout tilSearch;
//
//    @BindView(R.id.et_search)
//    TextInputEditText etSearch;
//
//    @BindView(R.id.pb_progress)
//    ProgressBar pbProgress;
//
//    @Inject
//    SessionCommand sessionCommand;
//    @Inject
//    WebCommand webCommand;
//
//    @Inject
//    HomeViewModel homeViewModel;
//
//    private DashBoardViewModel dashBoardViewModel;
//
//    protected ClientListAdapter mAdapter;
//    protected RecyclerView.LayoutManager mLayoutManager;
//    protected String[] mDataset;
//
//    FiltersBottomSheet filtersBottomSheet;
//
//
//    public HomeFragment2() {
//        // Required empty public constructor
//    }
//
//    public static HomeFragment2 newInstance() {
//        return new HomeFragment2();
//    }
//
//    @Override
//    public void onItemClick(PatientListResponse.Patient patient) {
//        homeViewModel.getClientWithId(patient.getId());
//    }
//
//    @Override
//    public void onStartSessionClick(PatientListResponse.Patient patient) {
//        homeViewModel.startSessionForClientWithId(patient.getId());
//    }
//
//    @Override
//    public void onListEnded() {
//        homeViewModel.getMoreClients(etSearch.getText().toString());
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        fragmentComponent = DaggerFragmentComponent.builder()
//                .activityComponent(((BaseActivity) getActivity()).activityComponent())
//                .fragmentModule(new FragmentModule(this))
//                .build();
//
//        fragmentComponent.inject(this);
//        super.onCreate(savedInstanceState);
//        dashBoardViewModel = ViewModelProviders.of(requireActivity()).get(DashBoardViewModel.class);
//    }
//
//    View view = null;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        if (view != null)
//            return view;
//        view = inflater.inflate(R.layout.fragment_home, container, false);
//        ButterKnife.bind(this, view);
//        setView();
//        if (dashBoardViewModel.getData().getValue() instanceof DashboardViewState.Connect) {
//            this.homeViewModel.processEvent(HomeViewEvent.DeviceConnected.INSTANCE);
//        } else {
//            this.homeViewModel.processEvent(new HomeViewEvent.Start(homeViewModel.getPage().getValue(), "", null));
//        }
//
//        mLayoutManager = new LinearLayoutManager(getActivity());
//        rvClients.setLayoutManager(mLayoutManager);
//
//        srClients.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                homeViewModel.getNewClients(etSearch.getText().toString());
//                srClients.setRefreshing(false);
//            }
//        });
//
//        etSearch.addTextChangedListener(
//                new TextWatcher() {
//                    private Timer timer = new Timer();
//                    private final long DELAY = 1000; // Milliseconds
//
//                    @Override
//                    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
//                    }
//
//                    @Override
//                    public void afterTextChanged(final Editable s) {
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
//                        timer.cancel();
//                        timer = new Timer();
//                        timer.schedule(
//                                new TimerTask() {
//                                    @Override
//                                    public void run() {
//                                        homeViewModel.getNewClients(charSequence.toString());
//                                    }
//                                },
//                                DELAY
//                        );
//                    }
//                });
//
//        mAdapter = new ClientListAdapter(this);
//        rvClients.setAdapter(mAdapter);
//        mAdapter.submitList(currentList);
//
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        setObserver();
//        spinKitView.setVisibility(View.INVISIBLE);
//
//        tilSearch.setEndIconOnClickListener(v -> {
//            if (filtersBottomSheet != null) {
//                filtersBottomSheet.show(getChildFragmentManager(), "");
//            }
//        });
//    }
//
//    private void setView() {
//
//    }
//
//    private void setObserver() {
//        homeViewModel.getUserData().observe(this.getViewLifecycleOwner(), homeUserViewStateObserver);
//        homeViewModel.getClientsData().observe(this.getViewLifecycleOwner(), homeClientsViewStateObserver);
//        homeViewModel.getViewEffect().observe(this.getViewLifecycleOwner(), homeViewEffectObserver);
//        homeViewModel.getProtocolData().observe(this.getViewLifecycleOwner(), homeProtocolDataObserver);
//
//        dashBoardViewModel.getData().observe(requireActivity(), dashboardViewState -> {
//            Timber.i("DEVICE_DASHBOARD :: onChanged: received freshObject");
//            if (dashboardViewState != null) {
//                if (dashboardViewState instanceof DashboardViewState.Connect) {
//                    homeViewModel.processEvent(HomeViewEvent.DeviceConnected.INSTANCE);
//                } else if (dashboardViewState instanceof DashboardViewState.Disconnect) {
//                    homeViewModel.processEvent(HomeViewEvent.DeviceDisconnected.INSTANCE);
//                }
//            }
//        });
//        homeViewModel.getPage().observe(this.getViewLifecycleOwner(), pageObserver);
//    }
//
//    Observer<HomeUserViewState> homeUserViewStateObserver = viewState -> {
//        if (viewState instanceof HomeUserViewState.Success) {
//            HomeUserViewState.Success success = (HomeUserViewState.Success) viewState;
//
//        }
//    };
//
//    Observer<HomeClientsViewState> homeClientsViewStateObserver = viewState -> {
//        if (viewState instanceof HomeClientsViewState.Success) {
//            HomeClientsViewState.Success success = (HomeClientsViewState.Success) viewState;
//            currentList.clear();
//            currentList.addAll(success.getPatientList());
//            mAdapter.notifyDataSetChanged();
//            if (success.getPatientList().isEmpty()) {
//                tvEmptyResult.setVisibility(View.VISIBLE);
//                rvClients.setVisibility(View.INVISIBLE);
//            } else {
//                tvEmptyResult.setVisibility(View.INVISIBLE);
//                rvClients.setVisibility(View.VISIBLE);
//            }
//        } else if (viewState instanceof HomeClientsViewState.PatientSuccess) {
//            HomeClientsViewState.PatientSuccess success = (HomeClientsViewState.PatientSuccess) viewState;
//
//            PatientResponse patient = success.getItem();
//            ViewClientBottomSheet viewClientBottomSheet = ViewClientBottomSheet.newInstance(this, patient, success.getTreatmentDataPresent());
//            viewClientBottomSheet.show(getChildFragmentManager(), "");
//
//        } else if (viewState instanceof HomeClientsViewState.OrganizationSuccess) {
//            HomeClientsViewState.OrganizationSuccess success = (HomeClientsViewState.OrganizationSuccess) viewState;
//            List<PatientListResponse.Patient.Organization> orgs = success.getItem();
//            filtersBottomSheet = FiltersBottomSheet.newInstance(orgs, homeViewModel.filters);
//            filtersBottomSheet.setListener(this);
//
//        } else if (viewState instanceof HomeClientsViewState.PatientSessionSuccess) {
//            //TODO uncomment
////            ((HomeActivity) requireActivity()).addFragment(R.id.fr_home, DeviceFragment.newInstance());
//
//        }
//    };
//
//    Observer<HomeViewEffect> homeViewEffectObserver = viewEffect -> {
//        if (viewEffect instanceof HomeViewEffect.BackRedirect) {
//        } else if (viewEffect instanceof HomeViewEffect.SessionRedirect) {
//            HomeViewEffect.SessionRedirect sessionRedirect = (HomeViewEffect.SessionRedirect) viewEffect;
//            launchSessionScreen(sessionRedirect.getTreatmentLength(), sessionRedirect.getProtocolFrequency(),
//                    sessionRedirect.getSonalId());
//        }
//    };
//
//    Observer<HomeProtocolViewState> homeProtocolDataObserver = protocol -> {
//        if (protocol instanceof HomeProtocolViewState.Loading) {
//            if (((HomeProtocolViewState.Loading) protocol).getLoading()) {
//                pbProgress.setVisibility(View.VISIBLE);
//            } else {
//                pbProgress.setVisibility(View.INVISIBLE);
//            }
//        }
//    };
//
//    Observer<Integer> pageObserver = new Observer<Integer>() {
//        @Override
//        public void onChanged(@Nullable final Integer newPage) {
//            if (newPage != null && newPage != 1) {
//                homeViewModel.processEvent(new HomeViewEvent.Start(newPage, etSearch.getText().toString(), homeViewModel.filters));
//            }
//        }
//    };
//
//    private void launchSessionScreen(String treatmentLength, String protocolFrequency, String sonalId) {
//        if (TextUtils.isEmpty(treatmentLength) || TextUtils.isEmpty(protocolFrequency)) {
//            Toast.makeText(requireActivity(), "Treatment data not available.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        sessionCommand.navigate(treatmentLength, protocolFrequency, sonalId);
//    }
//
//    private void onPermissionGranted(String permission) {
//        switch (permission) {
//            case Manifest.permission.ACCESS_FINE_LOCATION:
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
//                    new AlertDialog.Builder(requireActivity())
//                            .setTitle("Location Services not enabled")
//                            .setMessage("Android requires that Location Services to enabled to scan for Bluetooth Low Energy devices.\nPlease enable Location Services in Settings to continue. ")
//                            .setNegativeButton(R.string.cancel,
//                                    (dialog, which) -> {
//                                    })
//                            .setPositiveButton("Setting",
//                                    (dialog, which) -> {
//                                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                                        startActivityForResult(intent, REQUEST_CODE_OPEN_GPS);
//                                    })
//
//                            .setCancelable(false)
//                            .show();
//                } else {
//                    this.homeViewModel.processEvent(HomeViewEvent.StartSessionClicked.INSTANCE);
//                }
//                break;
//        }
//    }
//
//    private boolean checkGPSIsOpen() {
//        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
//        if (locationManager == null)
//            return false;
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//    }
//
//    @Override
//    public final void onRequestPermissionsResult(int requestCode,
//                                                 @NonNull String[] permissions,
//                                                 @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case REQUEST_CODE_PERMISSION_LOCATION:
//                if (grantResults.length > 0) {
//                    for (int i = 0; i < grantResults.length; i++) {
//                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                            onPermissionGranted(permissions[i]);
//                        }
//                    }
//                }
//                break;
//        }
//    }
//}