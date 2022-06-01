package com.waveneuro.ui.session.session;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import com.ap.ble.BleManager;
import com.ap.ble.BluetoothManager;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.waveneuro.R;
import com.waveneuro.data.analytics.AnalyticsManager;
import com.waveneuro.data.model.entity.BleDevice;
import com.waveneuro.ui.base.BaseActivity;
import com.waveneuro.ui.dashboard.DashboardCommand;
import com.waveneuro.ui.session.complete.SessionCompleteCommand;
import com.waveneuro.utils.CountDownTimer;
import com.waveneuro.utils.MonospaceSpan;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class SessionActivity extends BaseActivity implements CountDownTimer.OnCountDownListener {

    @BindView(R.id.tv_label_session_length)
    MaterialTextView tvSessionLengthInfo;

    @BindView(R.id.tv_session_length)
    MaterialTextView tvSessionLength;

    @BindView(R.id.tv_session_timer)
    MaterialTextView tvSessionTimer;

    @BindView(R.id.tv_stop_session_info)
    MaterialTextView tvStopSessionInfo;

    @BindView(R.id.cv_locate_device)
    MaterialCardView cvLocateDevice;

    @BindView(R.id.cv_device_initializing)
    MaterialCardView cvDeviceInitializing;

    @BindView(R.id.iv_circle_connected)
    ShapeableImageView ivCircleConnected;

    @BindView(R.id.tv_connect_status)
    MaterialTextView tvConnectStatus;

    @BindView(R.id.ll_container_session_info)
    LinearLayout llContainerSessionInfo;

    @BindView(R.id.tv_locate_device_info)
    MaterialTextView tvLocateDeviceInfo;

    @BindView(R.id.spin_kit_main)
    SpinKitView spinKitViewMain;


    @Inject
    SessionCompleteCommand sessionCompleteCommand;
    @Inject
    DashboardCommand dashboardCommand;

    @Inject
    SessionViewModel sessionViewModel;

    String treatmentLength = null;
    String protocolFrequency = null;
    int treatmentLengthMinutes = 0;

    BleDevice bleDevice = null;
//    BluetoothManager bluetoothManager;

    private String ble6Value = "";

//    public BluetoothManager getBluetoothManager() {
////        return new BluetoothManagerSimulation(this);
//        return new BluetoothManager(this);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_session);
        ButterKnife.bind(this);

        setView();
        setObserver();

        this.sessionViewModel.processEvent(new SessionViewEvent.Start());

        if (getIntent().hasExtra(SessionCommand.BLE_DEVICE)) {
            bleDevice = (BleDevice) getIntent().getSerializableExtra(SessionCommand.BLE_DEVICE);
        } else {
//            finish();
            //TODO Display restart pairing message and send to home
        }
        if (getIntent().hasExtra(SessionCommand.TREATMENT_LENGTH)) {
            treatmentLength = getIntent().getStringExtra(SessionCommand.TREATMENT_LENGTH);
            try {
                treatmentLengthMinutes = Integer.parseInt(treatmentLength) / 60;
            } catch (Exception e) {
                e.printStackTrace();
                finish();
            }
        }
        if (getIntent().hasExtra(SessionCommand.PROTOCOL_FREQUENCY)) {
            protocolFrequency = getIntent().getStringExtra(SessionCommand.PROTOCOL_FREQUENCY);
        }

        setBleListeners();
    }

    private void setView() {
        bluetoothSpanText();
    }

    private void bluetoothSpanText() {
        SpannableString spannableString = new SpannableString(getString(R.string.session_start_info));
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 10, 54, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvLocateDeviceInfo.setText(spannableString);
    }

    @Inject
    AnalyticsManager analyticsManager;

    private void setBleListeners() {

//        if (bluetoothManager == null)
//            bluetoothManager = getBluetoothManager();
        BluetoothManager.getInstance().getSixthCharacteristic(new BluetoothManager.Callback() {
            @Override
            public void invoke(String args) {
                Timber.e("SESSION_EVENT :: BLE_6_VALUE :: %s", args);
//                Toast.makeText(SessionActivity.this, "SESSION_EVENT :: BLE_6_VALUE :: "+ args,
//                        Toast.LENGTH_SHORT).show();
                try {
                    JSONObject properties = new JSONObject();
                    try {
                        properties.put("ble_value", args);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    analyticsManager.sendEvent("BLE_VALUE", properties, AnalyticsManager.MIX_PANEL);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (args.equals(ble6Value)) {
                    return;
                }
                switch (args) {
                    case "02":
                        //DONE listen for characteristics 06 for value 02 for start
                        //DONE testing set manually, use BluetoothSimulator
                        if (ble6Value.equals("03")) {
                            sessionViewModel.processEvent(new SessionViewEvent.ResumeSession());
                        } else {
                            sessionViewModel.processEvent(new SessionViewEvent.StartSession());
                        }
                        break;
                    case "03":
                        //DONE listen for characteristics 06 for value 03 for paused
                        //DONE pause timer
                        sessionViewModel.processEvent(new SessionViewEvent.DevicePaused());
                        break;
                    case "04":
                        //DONE listen for characteristics 06 for value 04 for end
                        //DONE end timer
                        sessionViewModel.processEvent(new SessionViewEvent.EndSession());
                        break;
                    case "05":
                        //DONE listen for characteristics 06 for value 05 error
                        //DONE Display for error
                        //DONE Alert message
                        //TODO Refactor alert message sending code
                        sessionViewModel.processEvent(new SessionViewEvent.DeviceError("Oops!",
                                "There was an error during treatment!"));
                        break;
                    case "01":
                        //TODO Same as iOS
                        break;
                    case "00":
                        //TODO Same as iOS
                        //DONE Alert message
                        //TODO Refactor alert message sending code
                        sessionViewModel.processEvent(new SessionViewEvent.DeviceError("Session Ended",
                                "You manually stopped the device."));
                        break;
                    default:

                        break;
                }
                ble6Value = args;
            }

            @Override
            public void invoke(List<com.ap.ble.data.BleDevice> args) {

            }

            @Override
            public void invoke(String[] args) {

            }
        });


        BluetoothManager.getInstance().registerDeviceConnectionCallback(deviceConnectionCallback);
    }

    BluetoothManager.DeviceConnectionCallback deviceConnectionCallback = new BluetoothManager.DeviceConnectionCallback() {
        @Override
        public void onConnected(com.ap.ble.data.BleDevice bleDevice) {
            // TODO Manage using state
            tvConnectStatus.setText("Connected");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvConnectStatus.setTextAppearance(R.style.BaseText_Caption_Green);
            } else {
                tvConnectStatus.setTextAppearance(SessionActivity.this, R.style.BaseText_Caption_Green);
            }
            ivCircleConnected.setImageDrawable(new ColorDrawable(ContextCompat.getColor(SessionActivity.this, R.color.green)));
        }

        @Override
        public void onCharacterises(String value) {

        }

        @Override
        public void onDisconnected() {
            // TODO Manage using state
            Timber.e("SESSION DISCONNECTED CALLBACK");
            if (!sessionTimer.isFinished())
                sessionTimer.pause();
            tvConnectStatus.setText("Disconnected");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvConnectStatus.setTextAppearance(R.style.BaseText_Caption_Red);
            } else {
                tvConnectStatus.setTextAppearance(SessionActivity.this, R.style.BaseText_Caption_Red);
            }
            ivCircleConnected.setImageDrawable(new ColorDrawable(ContextCompat.getColor(SessionActivity.this, R.color.red)));
            sessionViewModel.processEvent(new SessionViewEvent.DeviceError("Session Ended",
                    "You manually stopped the device."));
        }
    };

    private void setObserver() {
        this.sessionViewModel.getData().observe(this, sessionViewStateObserver);
        this.sessionViewModel.getViewEffect().observe(this, sessionViewEffectObserver);
    }

    Observer<SessionViewState> sessionViewStateObserver = viewState -> {
        if (viewState instanceof SessionViewState.Success) {
            SessionViewState.Success success = (SessionViewState.Success) viewState;
            onSuccess(success.getItem());
        } else if (viewState instanceof SessionViewState.Failure) {
            SessionViewState.Failure error = (SessionViewState.Failure) viewState;
            onFailure(error.getError());
        } else if (viewState instanceof SessionViewState.Loading) {
            SessionViewState.Loading loading = ((SessionViewState.Loading) viewState);
            if (loading.getLoading())
                displayWait("Loading...", null);
            else
                removeWait();
        } else if (viewState instanceof SessionViewState.Initializing) {
            tvSessionLength.setText(treatmentLengthMinutes + " min");
            tvSessionLengthInfo.setVisibility(View.VISIBLE);
            tvSessionLength.setVisibility(View.VISIBLE);
            cvDeviceInitializing.setVisibility(View.VISIBLE);
            cvLocateDevice.setVisibility(View.GONE);
            tvSessionTimer.setVisibility(View.GONE);
            tvStopSessionInfo.setVisibility(View.GONE);
            llContainerSessionInfo.setVisibility(View.GONE);
        } else if (viewState instanceof SessionViewState.LocateDevice) {
            tvSessionLength.setText(treatmentLengthMinutes + " min");
            tvSessionLengthInfo.setVisibility(View.VISIBLE);
            tvSessionLength.setVisibility(View.VISIBLE);
            cvDeviceInitializing.setVisibility(View.GONE);
            cvLocateDevice.setVisibility(View.VISIBLE);
            tvSessionTimer.setVisibility(View.GONE);
            tvStopSessionInfo.setVisibility(View.GONE);
            llContainerSessionInfo.setVisibility(View.GONE);
        } else if (viewState instanceof SessionViewState.SessionStarted) {
            tvSessionLengthInfo.setVisibility(View.GONE);
            tvSessionLength.setVisibility(View.GONE);
            cvDeviceInitializing.setVisibility(View.GONE);
            cvLocateDevice.setVisibility(View.GONE);
            tvSessionTimer.setVisibility(View.VISIBLE);
            tvStopSessionInfo.setVisibility(View.GONE);
            llContainerSessionInfo.setVisibility(View.VISIBLE);
            startSession();
        } else if (viewState instanceof SessionViewState.ResumeSession) {
            tvSessionLengthInfo.setVisibility(View.GONE);
            tvSessionLength.setVisibility(View.GONE);
            cvDeviceInitializing.setVisibility(View.GONE);
            cvLocateDevice.setVisibility(View.GONE);
            tvSessionTimer.setVisibility(View.VISIBLE);
            tvStopSessionInfo.setVisibility(View.GONE);
            llContainerSessionInfo.setVisibility(View.VISIBLE);
            resumeSession();
        } else if (viewState instanceof SessionViewState.SessionEnded) {
            endSession();
            launchSessionCompleteScreen();
        } else if (viewState instanceof SessionViewState.SessionPaused) {
            pauseSession();
        } else if (viewState instanceof SessionViewState.ErrorSession) {
            SessionViewState.ErrorSession state = (SessionViewState.ErrorSession) viewState;
            showEndSessionDialog(state.getTitle(), state.getMessage());
        } else if (viewState instanceof SessionViewState.DeviceDisconnected) {
            tvConnectStatus.setText("Disconnected");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvConnectStatus.setTextAppearance(R.style.BaseText_Caption_Red);
            } else {
                tvConnectStatus.setTextAppearance(this, R.style.BaseText_Caption_Red);
            }
            ivCircleConnected.setImageDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.red)));
        }
    };

    private void launchSessionCompleteScreen() {
        sessionCompleteCommand.navigate();
        finish();
    }

    Observer<SessionViewEffect> sessionViewEffectObserver = viewEffect -> {
        if (viewEffect instanceof SessionViewEffect.Back) {
            showCloseSessionDialog();
        } else if (viewEffect instanceof SessionViewEffect.InitializeBle) {
            //DONE wait for 2 sec
            //DONE set length in device in 05 -> 02 & 0E -> length and freq
            setDeviceInitChars();
//            new Handler().postDelayed(() -> {
////                    sessionViewModel.processEvent(new SessionViewEvent.LocatingDevice());
//            }, 2000);

        }
    };

    private void showCloseSessionDialog() {
        if (!isFinishing()) {
            new AlertDialog.Builder(this)
                    .setTitle("Are you sure?")
                    .setMessage("You want to leave session?")
                    .setNegativeButton("No",
                            (dialog, which) -> {

                            })
                    .setPositiveButton("Yes",
                            (dialog, which) -> {
                                if (!sessionTimer.isFinished())
                                    sessionTimer.pause();
                                dashboardCommand.navigate();
                            })

                    .setCancelable(false)
                    .show();
        }
    }

    private void showEndSessionDialog(String title, String message) {
        if (!isFinishing()) {
            new AlertDialog.Builder(this)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton("Go To Home",
                            (dialog, which) -> {
                                if (!sessionTimer.isFinished())
                                    sessionTimer.pause();
                                dashboardCommand.navigate();
                            })

                    .setCancelable(false)
                    .show();
        }
    }


    private void setDeviceInitChars() {
//        if (bluetoothManager == null)
//            bluetoothManager = getBluetoothManager();
        //DONE check in RN for freq and length data
//        bluetoothManager.sendFrequencyData("10.5", "1800");
        displayWait();
//        spinKitViewMain.setVisibility(View.VISIBLE);
        BluetoothManager.getInstance().sendFrequencyData(protocolFrequency, treatmentLength, new BluetoothManager.Callback() {
            @Override
            public void invoke(String args) {
//                spinKitViewMain.setVisibility(View.GONE);
                removeWait();
                if ("true".equals(args)) {

                } else {

                }
//                sessionViewModel.processEvent(new SessionViewEvent.StartSession());
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        removeWait();
//                        if ("true".equals(args)) {
//                            Toast.makeText(SessionActivity.this, "Device initialized", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(SessionActivity.this, "Error in device initialization", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }, 20_000);

            }

            @Override
            public void invoke(List<com.ap.ble.data.BleDevice> args) {

            }

            @Override
            public void invoke(String[] args) {

            }
        });
        Timber.e("SESSION_VARS T :: %s :: %s", treatmentLength, BluetoothManager.getInstance().getTreatmentLength(treatmentLength));
        Timber.e("SESSION_VARS F :: %s :: %s", protocolFrequency, BluetoothManager.getInstance().getFrequency(protocolFrequency));
    }

    private CountDownTimer sessionTimer = new CountDownTimer(0, 10, 1, this);

    private void startSession() {
        if (!sessionTimer.isFinished())
            sessionTimer.pause();
        sessionTimer = new CountDownTimer(treatmentLengthMinutes, 0, 1, this);
        sessionTimer.start(false);

    }

    private void pauseSession() {
        if (!sessionTimer.isFinished())
            sessionTimer.pause();
    }

    private void resumeSession() {
        if (!sessionTimer.isFinished())
            sessionTimer.pause();
        Timber.e("SESSION :: %s", sessionTimer.getMinutesTillCountDown());
        sessionTimer = new CountDownTimer(sessionTimer.getMinutesTillCountDown(),
                sessionTimer.getSecondsTillCountDown(), 1, this);
        sessionTimer.start(false);
//            sessionTimer.start(true);
    }

    private void endSession() {
        if (!sessionTimer.isFinished())
            sessionTimer.pause();
    }

    @Override
    public void onCountDownActive(String time) {
        tvSessionTimer.post(() -> {
            SpannableString textMono = new SpannableString(time);
            textMono.setSpan(new MonospaceSpan(), 0, textMono.length(), 0);
            tvSessionTimer.setText(textMono);
        });
    }

    @Override
    public void onCountDownFinished() {
        tvSessionTimer.post(() -> {
            tvSessionTimer.setText("00:00");
            ble6Value = "04";
            sessionViewModel.processEvent(new SessionViewEvent.EndSession());
        });
    }

    @OnClick(R.id.iv_back)
    public void onBackClicked() {
        this.sessionViewModel.processEvent(new SessionViewEvent.BackClicked());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (bluetoothManager != null) {
        BluetoothManager.getInstance().unregisterDeviceConnectionCallback(deviceConnectionCallback);
//        }
    }
}