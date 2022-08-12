package com.waveneuro.ui.dashboard.home;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.github.ybq.android.spinkit.SpinKitView;
import com.waveneuro.R;
import com.waveneuro.injection.component.DaggerFragmentComponent;
import com.waveneuro.injection.module.FragmentModule;
import com.waveneuro.ui.base.BaseActivity;
import com.waveneuro.ui.base.BaseFragment;
import com.waveneuro.ui.dashboard.DashBoardViewModel;
import com.waveneuro.ui.dashboard.DashboardViewState;
import com.waveneuro.ui.dashboard.more.WebCommand;
import com.waveneuro.ui.session.session.SessionCommand;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class HomeFragment extends BaseFragment {

    private static final int REQUEST_CODE_OPEN_GPS = 1;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;


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

    @Inject
    SessionCommand sessionCommand;
    @Inject
    WebCommand webCommand;

    @Inject
    HomeViewModel homeViewModel;

    private DashBoardViewModel dashBoardViewModel;

    private HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
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
            this.homeViewModel.processEvent(new HomeViewEvent.Start());
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setObserver();
    }

    private void setView() {

    }


    private void redirectToSonalWebsite() {
        webCommand.navigate(WebCommand.PAGE_SONAL);
    }

    private void setObserver() {
        homeViewModel.getUserData().observe(this.getViewLifecycleOwner(), homeUserViewStateObserver);
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

    private void checkPermissions() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(requireActivity(), "Please turn on Bluetooth first", Toast.LENGTH_LONG).show();
            return;
        }

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        List<String> permissionDeniedList = new ArrayList<>();
        for (String permission : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(requireActivity(), permission);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission);
            } else {
                permissionDeniedList.add(permission);
            }
        }
        if (!permissionDeniedList.isEmpty()) {
            new AlertDialog.Builder(requireActivity())
                    .setTitle("Location Permission Request")
                    .setMessage("WaveNeuro requires that Location permission be enabled to scan for Bluetooth Low Energy devices.\nPlease allow Location permission to continue.")
                    .setNegativeButton(R.string.cancel,
                            (dialog, which) -> {
                            })
                    .setPositiveButton("Allow",
                            (dialog, which) -> {
                                String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
                                ActivityCompat.requestPermissions(requireActivity(), deniedPermissions, REQUEST_CODE_PERMISSION_LOCATION);
                            })

                    .setCancelable(false)
                    .show();

        }
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