package com.waveneuro.ui.dashboard.device;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ap.ble.BluetoothManager;
import com.ap.ble.callback.BleScanCallback;
import com.ap.ble.data.BleDevice;
import com.asif.abase.view.RecyclerViewWithEmpty;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.waveneuro.R;
import com.waveneuro.data.model.entity.User;
import com.waveneuro.injection.component.DaggerFragmentComponent;
import com.waveneuro.injection.module.FragmentModule;
import com.waveneuro.ui.adapter.DeviceAdapter;
import com.waveneuro.ui.adapter.device.DeviceDelegate;
import com.waveneuro.ui.adapter.device.OnDeviceItemClickListener;
import com.waveneuro.ui.base.BaseActivity;
import com.waveneuro.ui.base.BaseListFragment;
import com.waveneuro.ui.dashboard.DashBoardViewModel;
import com.waveneuro.ui.dashboard.DashboardViewEvent;
import com.waveneuro.ui.dashboard.DashboardViewState;
import com.waveneuro.ui.dashboard.HomeActivity;
import com.waveneuro.ui.session.session.SessionCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class DeviceFragment extends BaseListFragment implements OnDeviceItemClickListener {

    private static final int REQUEST_CODE_OPEN_GPS = 1;
    private static final int REQUEST_CODE_PERMISSION_LOCATION = 2;
    private static final int REQUEST_CODE_PERMISSION_BLUETOOTH = 3;

    @BindView(R.id.iv_back)
    ImageView ivBack;

    @BindView(R.id.tv_label_welcome)
    MaterialTextView tvLabelWelcome;

    // Locate Device
    @BindView(R.id.ll_locate_device)
    LinearLayout cvLocateDevice;
    @BindView(R.id.tv_locate_device_info)
    MaterialTextView tvLocateDeviceInfo;
    @BindView(R.id.btn_locate_device)
    MaterialButton btnLocateDevice;
    @BindView(R.id.iv_device)
    ImageView ivDevice;
    @BindView(R.id.tv_first_time)
    TextView tvFirstTime;
    @BindView(R.id.tv_scanning_device_info)
    TextView tvScanningDeviceInfo;
    @BindView(R.id.tv_label_scanning)
    TextView tvLabelScanning;


    // Locating Device
    @BindView(R.id.ll_container_device)
    LinearLayout llContainerDevice;


    // Devices
    @BindView(R.id.cv_device_available)
    MaterialCardView cvDeviceAvailable;
    @BindView(R.id.rv_device_available)
    RecyclerViewWithEmpty rvDeviceAvailable;

    DeviceAdapter deviceAdapter;

    @Inject
    SessionCommand sessionCommand;

    @Inject
    DeviceViewModel deviceViewModel;

    private DashBoardViewModel dashBoardViewModel;


    private DeviceFragment() {
    }

    public static DeviceFragment newInstance() {
        return new DeviceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        fragmentComponent = DaggerFragmentComponent.builder()
                .activityComponent(((BaseActivity) getActivity()).activityComponent())
                .fragmentModule(new FragmentModule(this))
                .build();

        fragmentComponent.inject(this);
        super.onCreate(savedInstanceState);
        dashBoardViewModel = ViewModelProviders.of(getActivity()).get(DashBoardViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device, container, false);
        ButterKnife.bind(this, view);
        setView();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setObserver();
        this.deviceViewModel.processEvent(new DeviceViewEvent.Start());
    }

    private void setView() {
        rvDeviceAvailable.setLayoutManager(new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL, false));
        deviceAdapter = new DeviceAdapter.Builder()
                .setList(new ArrayList<>())
                .setDelegate(new DeviceDelegate())
                .setOnNoteListener(this::onClickDevice)
                .create();
        rvDeviceAvailable.setAdapter(deviceAdapter);
        initializeList(null, null, deviceAdapter);

        operatingAnim = AnimationUtils.loadAnimation(this.requireContext(), R.anim.rotate);
        operatingAnim.setInterpolator(new LinearInterpolator());

        tvFirstTime.setPaintFlags(tvFirstTime.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void setObserver() {
        deviceViewModel.getData().removeObservers(getViewLifecycleOwner());
        deviceViewModel.getViewEffect().removeObservers(getViewLifecycleOwner());
        deviceViewModel.getData().observe(getViewLifecycleOwner(), notesViewStateObserver);
        deviceViewModel.getViewEffect().observe(getViewLifecycleOwner(), notesViewEffectObserver);

        dashBoardViewModel.getData().observe(requireActivity(), dashboardViewState -> {
            Timber.i("DEVICE_DASHBOARD :: onChanged: received freshObject");
            if (dashboardViewState != null) {
                if (dashboardViewState instanceof DashboardViewState.Connect) {
                    deviceViewModel.processEvent(new DeviceViewEvent.Connected());
                } else if (dashboardViewState instanceof DashboardViewState.Disconnect) {
                    deviceViewModel.processEvent(new DeviceViewEvent.Disconnected());
                }
            }
        });

    }

    Observer<DeviceViewState> notesViewStateObserver = viewState -> {
        if (getLifecycle().getCurrentState() != Lifecycle.State.RESUMED) {
            return;
        }
        if (viewState instanceof DeviceViewState.Success) {
            cvLocateDevice.setVisibility(View.GONE);
            cvDeviceAvailable.setVisibility(View.VISIBLE);
            llContainerDevice.setVisibility(View.GONE);
            DeviceViewState.Success success = (DeviceViewState.Success) viewState;
//            loadNotesData(success.getItem());
        } else if (viewState instanceof DeviceViewState.Failure) {

        } else if (viewState instanceof DeviceViewState.Loading) {
            DeviceViewState.Loading loading = (DeviceViewState.Loading) viewState;
            if (loading.getLoading()) {
                displayListProgress();
            } else {
                removeListProgress();
            }
        } else if (viewState instanceof DeviceViewState.InitLocateDevice) {
            DeviceViewState.InitLocateDevice initLocateDevice = (DeviceViewState.InitLocateDevice) viewState;
            setUserDetail(initLocateDevice.getUser());
            ivBack.setVisibility(View.INVISIBLE);
            tvLabelWelcome.setText(getString(R.string.ensure_your_device_powered_up));
            tvLabelWelcome.setVisibility(View.VISIBLE);
            ivDevice.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    R.drawable.ic_sonal_device_power));
            ivDevice.setVisibility(View.VISIBLE);
            tvLocateDeviceInfo.setVisibility(View.VISIBLE);
            cvLocateDevice.setVisibility(View.VISIBLE);
            cvDeviceAvailable.setVisibility(View.GONE);
            llContainerDevice.setVisibility(View.GONE);
        } else if (viewState instanceof DeviceViewState.LocateDevice) {
            ivBack.setVisibility(View.INVISIBLE);
            tvLabelWelcome.setText(getString(R.string.ensure_your_device_powered_up));
            tvLabelWelcome.setVisibility(View.VISIBLE);
            ivDevice.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    R.drawable.ic_sonal_device_power));
            ivDevice.setVisibility(View.VISIBLE);
            cvLocateDevice.setVisibility(View.VISIBLE);
            cvDeviceAvailable.setVisibility(View.GONE);
            llContainerDevice.setVisibility(View.GONE);
        } else if (viewState instanceof DeviceViewState.LocateDeviceNext) {
            ivBack.setVisibility(View.INVISIBLE);
            tvLabelWelcome.setText(getString(R.string.activate_your_device));
            tvLabelWelcome.setVisibility(View.VISIBLE);
            ivDevice.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                    R.drawable.img_device_sonal_connector_with_wave));
            ivDevice.setVisibility(View.VISIBLE);
            tvLocateDeviceInfo.setVisibility(View.VISIBLE);
            cvLocateDevice.setVisibility(View.VISIBLE);
            cvDeviceAvailable.setVisibility(View.GONE);
            llContainerDevice.setVisibility(View.GONE);
        } else if (viewState instanceof DeviceViewState.Searching) {
            ivBack.setVisibility(View.VISIBLE);
            tvLabelWelcome.setVisibility(View.GONE);
            cvLocateDevice.setVisibility(View.GONE);
            cvDeviceAvailable.setVisibility(View.GONE);
            tvScanningDeviceInfo.setVisibility(View.VISIBLE);
            tvLabelScanning.setVisibility(View.VISIBLE);
            llContainerDevice.setVisibility(View.VISIBLE);
            locateDevice();
        } else if (viewState instanceof DeviceViewState.Connecting) {
            DeviceViewState.Connecting connecting = (DeviceViewState.Connecting) viewState;
            connectToDevice(connecting.getBleDevice());
        } else if (viewState instanceof DeviceViewState.Connected) {
            ivBack.setVisibility(View.INVISIBLE);
            launchPairingSuccessfulDialog();
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                tvLocatingDevice.setTextAppearance(R.style.BaseText_Subtitle_Green);
//            } else {
//                tvLocatingDevice.setTextAppearance(getContext(), R.style.BaseText_Subtitle_Green);
//            }
//            tvLocatingDevice.setText("Device Connected");
            cvLocateDevice.setVisibility(View.GONE);
            cvDeviceAvailable.setVisibility(View.GONE);
            llContainerDevice.setVisibility(View.VISIBLE);

//            launchHomeScreen();
        } else if (viewState instanceof DeviceViewState.Searched) {
            ivBack.setVisibility(View.VISIBLE);
            cvLocateDevice.setVisibility(View.VISIBLE);
            cvDeviceAvailable.setVisibility(View.VISIBLE);
            llContainerDevice.setVisibility(View.GONE);
        }
    };

    private void setUserDetail(User user) {
        // DONE Which name need to display
        if (TextUtils.isEmpty(user.getImageThumbnailUrl())) {
            String[] strArray = user.getName().split(" ");
            StringBuilder builder = new StringBuilder();
            if (strArray.length > 0) {
                builder.append(strArray[0], 0, 1);
            }
            if (strArray.length > 1) {
                builder.append(strArray[1], 0, 1);
            }
        }
    }

    private void launchFirstTimeDialog(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.PopUp);
        ViewGroup viewGroup = getView().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_popup, viewGroup, false);
        TextView tvTitle = dialogView.findViewById(R.id.tv_title);
        TextView tvContent = dialogView.findViewById(R.id.tv_content);
        Button btnPrimary = dialogView.findViewById(R.id.btn_primary);
        tvTitle.setText(R.string.first_time_using);
        tvTitle.setTextSize(24);
        tvContent.setText(R.string.make_sure_device_ready);
        btnPrimary.setText(R.string.ok);
        builder.setView(dialogView);
        final AlertDialog ad = builder.create();
        btnPrimary.setOnClickListener(v -> ad.dismiss());
        ad.show();

    }

    private void launchPairingSuccessfulDialog(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.PopUp);
        ViewGroup viewGroup = getView().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_popup, viewGroup, false);
        TextView tvTitle = dialogView.findViewById(R.id.tv_title);
        TextView tvContent = dialogView.findViewById(R.id.tv_content);
        Button btnPrimary = dialogView.findViewById(R.id.btn_primary);
        tvTitle.setText(R.string.pairing_successful);
        tvContent.setText(R.string.pairing_successful_description);
        btnPrimary.setText(R.string.proceed_to_session);
        builder.setView(dialogView);
        final AlertDialog ad = builder.create();
        btnPrimary.setOnClickListener(v -> this.deviceViewModel.processEvent(new DeviceViewEvent.StartSessionClicked()));
        ad.show();

    }

    private void launchHomeScreen(boolean launchWithDelay) {
        new Handler().postDelayed(() -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).switchToHome();
            }
        }, (launchWithDelay ? 1_500 : 0));
    }

    private void launchHomeScreen() {
        launchHomeScreen(false);
    }

    private void launchSessionScreen(String treatmentLength, String protocolFrequency, String sonalId) {
        if(TextUtils.isEmpty(treatmentLength) || TextUtils.isEmpty(protocolFrequency)) {
            Toast.makeText(requireActivity(), "Treatment data not available.", Toast.LENGTH_SHORT).show();
            return;
        }
        sessionCommand.navigate(treatmentLength, protocolFrequency, sonalId);
    }

    private void connectToDevice(com.waveneuro.data.model.entity.BleDevice bleDevice) {
        BluetoothManager.getInstance().getDeviceName(bleDevice.getMac(), bleDevice.getName(), new BluetoothManager.DeviceConnectionCallback() {

            @Override
            public void onConnected(BleDevice bleDevice) {
                if(requireActivity() instanceof BaseActivity) {
                    ((BaseActivity) getActivity()).removeWait();
                }
                deviceViewModel.processEvent(new DeviceViewEvent.Connected());
                deviceViewModel.setDeviceId(bleDevice.getName());
                dashBoardViewModel.processEvent(
                        new DashboardViewEvent.Connected(
                                new com.waveneuro.data.model.entity.BleDevice(bleDevice)));
            }

            @Override
            public void onCharacterises(String value) {

            }

            @Override
            public void onDisconnected() {
                if(requireActivity() instanceof BaseActivity) {
                    ((BaseActivity) getActivity()).removeWait();
                }
                deviceViewModel.processEvent(new DeviceViewEvent.Disconnected());
                dashBoardViewModel.processEvent(new DashboardViewEvent.Disconnected());
            }
        });
    }

//    BluetoothManager bluetoothManager;
    private Animation operatingAnim;

    private void locateDevice() {
//        if (bluetoothManager == null)
//            bluetoothManager = new BluetoothManager();
        BluetoothManager.getInstance().deviceList(new BleScanCallback() {
            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                Log.e("DEVICE_LIST", "" + Arrays.toString(scanResultList.toArray()));
                if (scanResultList.size() > 0) {
                    List<com.waveneuro.data.model.entity.BleDevice> bleDevices = new ArrayList<>();
                    for (int i = 0; i < scanResultList.size(); i++) {
                        bleDevices.add(new com.waveneuro.data.model.entity.BleDevice(scanResultList.get(i)));
                    }
                    loadBleDevices(bleDevices);
                    deviceViewModel.processEvent(new DeviceViewEvent.Searched());
                } else {
                    deviceViewModel.processEvent(new DeviceViewEvent.NoDeviceFound());
                    Toast.makeText(requireActivity(), "No device found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onScanStarted(boolean success) {

            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                Log.e("DEVICES", "" + bleDevice.getName() + " :: " + bleDevice.getKey());
            }
        });
    }

    Observer<DeviceViewEffect> notesViewEffectObserver = viewEffect -> {
        if (viewEffect instanceof DeviceViewEffect.SessionRedirect) {
            DeviceViewEffect.SessionRedirect sessionRedirect = (DeviceViewEffect.SessionRedirect) viewEffect;
            launchSessionScreen(sessionRedirect.getTreatmentLength(), sessionRedirect.getProtocolFrequency(), sessionRedirect.getSonalId());
        }
    };

    private void loadBleDevices(List<com.waveneuro.data.model.entity.BleDevice> item) {
        addAll(item);
    }

    @OnClick(R.id.btn_locate_device)
    public void onClickLocateDevice() {
        checkPermissions();
        this.deviceViewModel.processEvent(new DeviceViewEvent.LocateDevice());
    }

    @OnClick(R.id.iv_back)
    public void onClickBack() {
        // TODO Do with processEvent
        launchHomeScreen();
        this.deviceViewModel.processEvent(new DeviceViewEvent.Start());
    }

    @OnClick(R.id.tv_first_time)
    public void onFirstTimeUsing() {
        launchFirstTimeDialog();
    }

    @Override
    public void onClickDevice(com.waveneuro.data.model.entity.BleDevice data) {
        if(requireActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).displayWait();
        }
        this.deviceViewModel.processEvent(new DeviceViewEvent.DeviceClicked(data));
    }

    private void checkPermissions() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(requireActivity(), "Please turn on Bluetooth first", Toast.LENGTH_LONG).show();
            return;
        }

//        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
        List<String> permissions = new ArrayList<>();
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        } else {
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT);
            permissions.add(Manifest.permission.BLUETOOTH_SCAN);
        }
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
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
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
            } else {
                String[] deniedPermissions = permissionDeniedList.toArray(new String[permissionDeniedList.size()]);
                ActivityCompat.requestPermissions(requireActivity(), deniedPermissions, REQUEST_CODE_PERMISSION_BLUETOOTH);
            }


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
                    this.deviceViewModel.processEvent(new DeviceViewEvent.LocateDeviceNextClicked());
                }
                break;
            case Manifest.permission.BLUETOOTH_CONNECT:
            case Manifest.permission.BLUETOOTH_SCAN:
                Timber.e("PERMISSION_GRANTED :: %s", permission);
                this.deviceViewModel.processEvent(new DeviceViewEvent.LocateDeviceNextClicked());
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
            case REQUEST_CODE_PERMISSION_BLUETOOTH:
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