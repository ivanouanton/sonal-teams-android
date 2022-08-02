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
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.waveneuro.R;
import com.waveneuro.data.model.entity.User;
import com.waveneuro.injection.component.DaggerFragmentComponent;
import com.waveneuro.injection.module.FragmentModule;
import com.waveneuro.ui.base.BaseActivity;
import com.waveneuro.ui.base.BaseFragment;
import com.waveneuro.ui.dashboard.DashBoardViewModel;
import com.waveneuro.ui.dashboard.DashboardViewState;
import com.waveneuro.ui.dashboard.HomeActivity;
import com.waveneuro.ui.dashboard.more.WebCommand;
import com.waveneuro.ui.session.session.SessionCommand;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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


    @BindView(R.id.iv_profile_image)
    ImageView ivProfileImage;

    @BindView(R.id.tv_profile_image_initials)
    MaterialTextView tvProfileImageInitials;

    @BindView(R.id.tv_username)
    MaterialTextView tvUsername;

    @BindView(R.id.tv_label_welcome)
    MaterialTextView tvLabelWelcome;

    @BindView(R.id.cv_device)
    MaterialCardView cvDevice;

    @BindView(R.id.btn_start_session)
    MaterialButton btnStartSession;

    @BindView(R.id.cv_bubble_error)
    MaterialCardView cvBubbleError;

    @BindView(R.id.iv_bubble_error)
    ImageView ivBubbleError;

    @BindView(R.id.tv_bubble_error_text)
    MaterialTextView tvBubbleErrorText;

    @BindView(R.id.spin_kit)
    SpinKitView spinKitView;

    @BindView(R.id.cv_next_session)
    MaterialCardView cvNextSession;

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
        homeViewModel.getDeviceData().observe(this.getViewLifecycleOwner(), homeViewStateObserver);
        homeViewModel.getUserData().observe(this.getViewLifecycleOwner(), homeUserViewStateObserver);
        homeViewModel.getProtocolData().observe(this.getViewLifecycleOwner(), homeProtocolViewStateObserver);
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

    Observer<HomeDeviceViewState> homeViewStateObserver = viewState -> {
        if (viewState instanceof HomeDeviceViewState.PairDevice) {
            btnStartSession.setText(getString(R.string.pair_device));
        } else if (viewState instanceof HomeDeviceViewState.StartSession) {
//            btnStartSession.setText(getString(R.string.start_session));
        }
    };

    Observer<HomeUserViewState> homeUserViewStateObserver = viewState -> {
        if (viewState instanceof HomeUserViewState.Success) {
            HomeUserViewState.Success success = (HomeUserViewState.Success) viewState;
            setUserDetail(success.getItem());
        }
    };

    Observer<HomeProtocolViewState> homeProtocolViewStateObserver = viewState -> {
        if (viewState instanceof HomeProtocolViewState.Success) {
            HomeProtocolViewState.Success success = (HomeProtocolViewState.Success) viewState;
            spinKitView.setVisibility(View.GONE);
            cvDevice.setVisibility(View.VISIBLE);
            setProtocolData();
        } else if (viewState instanceof HomeProtocolViewState.Failure) {
            spinKitView.setVisibility(View.GONE);
        } else if (viewState instanceof HomeProtocolViewState.ProtocolNotFound) {

            spinKitView.setVisibility(View.GONE);
            btnStartSession.setVisibility(View.GONE);
            showMissingProtocolError();

            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).enableDeviceTab(false);
            }
        } else if (viewState instanceof HomeProtocolViewState.Loading) {
            //TODO Create Dialog
            HomeProtocolViewState.Loading loading = (HomeProtocolViewState.Loading) viewState;
            if (loading.getLoading()) {
                spinKitView.setVisibility(View.VISIBLE);
            } else {
                spinKitView.setVisibility(View.GONE);
            }
        }
    };

    void showMissingProtocolError(){


        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.PopUp);
        ViewGroup viewGroup = getView().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_popup, viewGroup, false);
        TextView tvTitle = dialogView.findViewById(R.id.tv_title);
        TextView tvContent = dialogView.findViewById(R.id.tv_content);
        Button btnPrimary = dialogView.findViewById(R.id.btn_primary);
        ImageView ivPrimary = dialogView.findViewById(R.id.iv_primary);
        ivPrimary.setVisibility(View.GONE);
        tvTitle.setText(R.string.protocol_not_active);
        tvContent.setText(R.string.visit_sonal_website);
        btnPrimary.setVisibility(View.INVISIBLE);

        SpannableString spannableString = new SpannableString(getString(R.string.visit_sonal_website));

        spannableString.setSpan(new UnderlineSpan(), 13, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                redirectToSonalWebsite();
            }
        }, 13, 34, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gray_dim_dark)), 13, 34, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        tvContent.setText(spannableString);
        tvContent.setClickable(true);
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void setUserDetail(User user) {
        // DONE Which name need to display
        if (TextUtils.isEmpty(user.getName())) {
            tvUsername.setText("");
        } else {
            tvLabelWelcome.setVisibility(View.VISIBLE);
            TextViewCompat.setAutoSizeTextTypeWithDefaults(tvUsername, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
            tvUsername.setVisibility(View.VISIBLE);
            tvUsername.setText(user.getName());
        }
        if (TextUtils.isEmpty(user.getImageThumbnailUrl())) {
            if (TextUtils.isEmpty(user.getName())) {
                tvProfileImageInitials.setText("");
                return;
            }
            String[] strArray = user.getName().split(" ");
            StringBuilder builder = new StringBuilder();
            if (strArray.length > 0) {
                builder.append(strArray[0], 0, 1);
            }
            if (strArray.length > 1) {
                builder.append(strArray[1], 0, 1);
            }
//            if (strArray.length > 2) {
//                builder.append(strArray[2], 0, 1);
//            }
            tvProfileImageInitials.setText(builder.toString().toUpperCase());
            ivProfileImage.setVisibility(View.VISIBLE);
            tvProfileImageInitials.setVisibility(View.VISIBLE);
        } else {
            ivProfileImage.setVisibility(View.VISIBLE);
            // TODO Visible when Image is loaded
            Glide.with(this)
                    .load(user.getImageThumbnailUrl())
                    .into(ivProfileImage);
        }
    }

    private void setProtocolData() {

    }

    Observer<HomeViewEffect> homeViewEffectObserver = viewEffect -> {
        if (viewEffect instanceof HomeViewEffect.BackRedirect) {
        } else if (viewEffect instanceof HomeViewEffect.DeviceRedirect) {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).switchToDevice();
            }
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

    @OnClick(R.id.btn_start_session)
    public void onClickStartSession() {
        checkPermissions();
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

    private void hideNextSession() {
        cvNextSession.setVisibility(View.GONE);
    }

    private void hideBubbleError() {
        cvBubbleError.setVisibility(View.GONE);
        ivBubbleError.setVisibility(View.GONE);
        tvBubbleErrorText.setVisibility(View.GONE);
    }

    private void showBubbleError(@BubbleError int bubbleErrorType) {
        cvBubbleError.setVisibility(View.VISIBLE);
        ivBubbleError.setVisibility(View.VISIBLE);
        tvBubbleErrorText.setVisibility(View.VISIBLE);
        switch (bubbleErrorType) {
            case BUBBLE_NO_INTERNET:
                ivBubbleError.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                        R.drawable.ic_no_connection));
                tvBubbleErrorText.setText("Re-connect to internet");
                break;
            case BUBBLE_DEVICE_NOT_CONNECTED:
                ivBubbleError.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                        R.drawable.ic_bluetooth_square));
                tvBubbleErrorText.setText("Device not connected");
                break;
            case BUBBLE_LOW_BATTERY:
                ivBubbleError.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                        R.drawable.ic_battery));
                tvBubbleErrorText.setText("Low battery");
                break;
            case BUBBLE_LOW_BATTERY_PLUG:
                ivBubbleError.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                        R.drawable.ic_battery));
                tvBubbleErrorText.setText("Low battery - plug to use Nest");
                break;
            case BUBBLE_LOW_BATTERY_KEEP_PLUG:
                ivBubbleError.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                        R.drawable.ic_battery));
                tvBubbleErrorText.setText("Low battery - keep Nest plugged in");
                break;
        }
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