package com.waveneuro.ui.session.session;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;

import com.ap.ble.BluetoothManager;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textview.MaterialTextView;
import com.waveneuro.R;
import com.waveneuro.data.DataManager;
import com.waveneuro.data.analytics.AnalyticsManager;
import com.waveneuro.ui.base.BaseActivity;
import com.waveneuro.ui.dashboard.DashboardCommand;
import com.waveneuro.ui.session.complete.SessionCompleteCommand;
import com.waveneuro.ui.session.precautions.PrecautionsBottomSheet;
import com.waveneuro.utils.CountDownTimer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class SessionActivity extends BaseActivity implements CountDownTimer.OnCountDownListener {

    @BindView(R.id.tv_session_timer)
    MaterialTextView tvSessionTimer;

    @BindView(R.id.tv_pause_session_info)
    MaterialTextView tvPauseSessionInfo;

    @BindView(R.id.tv_stop_session_info)
    MaterialTextView tvStopSessionInfo;

    @BindView(R.id.cl_container_session_info)
    ConstraintLayout clContainerSessionInfo;

    @BindView(R.id.btn_start_session)
    Button btnStartSession;

    @BindView(R.id.pb_progress)
    CircularProgressIndicator pbProgress;

    @BindView(R.id.ll_control_info)
    LinearLayout llConrolInfo;

    @BindView(R.id.iv_pause)
    ImageView ivPause;

    @BindView(R.id.tv_paused)
    TextView tvPaused;

    @BindView(R.id.cl_precautions)
    ConstraintLayout clPrecautions;

    AlertDialog readyDialog;

    @Inject
    SessionCompleteCommand sessionCompleteCommand;
    @Inject
    DashboardCommand dashboardCommand;

    @Inject
    SessionViewModel sessionViewModel;

    @Inject
    DataManager dataManager;

    String treatmentLength = null;
    String protocolFrequency = null;
    String sonalId = null;
    int treatmentLengthMinutes = 0;

    private String ble6Value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.fragment_session);
        ButterKnife.bind(this);

        setView();
        setObserver();

        if (getIntent().hasExtra(SessionCommand.SONAL_ID)) {
            sonalId = getIntent().getStringExtra(SessionCommand.SONAL_ID);
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

        stopSpanText();
        pauseSpanText(false);

        setBleListeners();
    }


    private void setView() {
        clPrecautions.setOnClickListener(v -> {
            PrecautionsBottomSheet precautionsBottomSheet = PrecautionsBottomSheet.newInstance();
            precautionsBottomSheet.show(this.getSupportFragmentManager(), "");
        });
    }

    @Inject
    AnalyticsManager analyticsManager;

    private void setBleListeners() {

        BluetoothManager.getInstance().getSixthCharacteristic(new BluetoothManager.Callback() {
            @Override
            public void invoke(String args) {
                Timber.e("SESSION_EVENT :: BLE_6_VALUE :: %s", args);
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
                    case BluetoothManager.START_SESSION:
                        if (ble6Value.equals(BluetoothManager.PAUSE_SESSION)) {
                            sessionViewModel.processEvent(new SessionViewEvent.ResumeSession());
                        } else {
                            sessionViewModel.processEvent(new SessionViewEvent.StartSession());
                        }
                        break;
                    case BluetoothManager.PAUSE_SESSION:
                        sessionViewModel.processEvent(new SessionViewEvent.DevicePaused());
                        break;
                    case BluetoothManager.END_SESSION:
                        sessionViewModel.processEvent(new SessionViewEvent.EndSession());
                        break;
                    case BluetoothManager.ERROR:
                        sessionViewModel.processEvent(new SessionViewEvent.DeviceError("Uh Oh!",
                                "Error detected on device"));
                        break;
                    case "01":
                        break;
                    case BluetoothManager.INACTIVE:
                        sessionViewModel.processEvent(new SessionViewEvent.DeviceError("Session Ended",
                                "You manually stopped the device."));
                        break;
                    default:
                }
                ble6Value = args;
            }

            @Override
            public void invoke(List<com.ap.ble.data.BleDevice> args) {
                // follow the interface
            }

            @Override
            public void invoke(String[] args) {
                // follow the interface
            }
        });


        BluetoothManager.getInstance().registerDeviceConnectionCallback(deviceConnectionCallback);
    }

    BluetoothManager.DeviceConnectionCallback deviceConnectionCallback = new BluetoothManager.DeviceConnectionCallback() {
        @Override
        public void onConnected(com.ap.ble.data.BleDevice bleDevice) {
            // follow the interface
        }

        @Override
        public void onCharacterises(String value) {
            // follow the interface
        }

        @Override
        public void onDisconnected() {
            Timber.e("SESSION DISCONNECTED CALLBACK");
            if (!sessionTimer.isFinished()) {
                sessionTimer.pause();
            }
            sessionViewModel.processEvent(new SessionViewEvent.DeviceError("Session Ended",
                    "You manually stopped the device."));
        }
    };

    private void setObserver() {
        this.sessionViewModel.getData().observe(this, sessionViewStateObserver);
        this.sessionViewModel.getViewEffect().observe(this, sessionViewEffectObserver);
    }

    private void pauseSpanText(boolean isPaused) {
        SpannableString spannableString = null;
        if (isPaused) {
            spannableString = new SpannableString(getString(R.string.resume_session_info));
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            spannableString = new SpannableString(getString(R.string.pause_session_info));
            spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tvPauseSessionInfo.setText(spannableString);
    }

    private void stopSpanText() {
        SpannableString spannableString = new SpannableString(getString(R.string.cancel_session_info));
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvStopSessionInfo.setText(spannableString);
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
            if (loading.getLoading()) {
                displayWait("Loading...", null);
            }
            else {
                removeWait();
            }
        } else if (viewState instanceof SessionViewState.LocateDevice) {
            tvSessionTimer.setVisibility(View.VISIBLE);
            tvSessionTimer.setText("30:00");
            tvStopSessionInfo.setVisibility(View.VISIBLE);
            llConrolInfo.setVisibility(View.GONE);
        } else if (viewState instanceof SessionViewState.SessionStarted) {
            if (readyDialog != null) readyDialog.dismiss();
            btnStartSession.setVisibility(View.GONE);
            tvSessionTimer.setVisibility(View.VISIBLE);
            tvStopSessionInfo.setVisibility(View.VISIBLE);
            llConrolInfo.setVisibility(View.VISIBLE);
            startCountdown();
        } else if (viewState instanceof SessionViewState.ResumeSession) {
            pauseSpanText(false);
            tvPaused.setVisibility(View.GONE);
            ivPause.setImageResource(R.drawable.ic_pause_session);
            tvSessionTimer.setVisibility(View.VISIBLE);
            tvStopSessionInfo.setVisibility(View.VISIBLE);
            llConrolInfo.setVisibility(View.VISIBLE);
            resumeCountdown();
        } else if (viewState instanceof SessionViewState.SessionEnded) {
            stopCountdown();
            launchSessionCompleteScreen();
        } else if (viewState instanceof SessionViewState.SessionPaused) {
            ivPause.setImageResource(R.drawable.ic_resume_session);
            pauseSpanText(true);
            tvPaused.setVisibility(View.VISIBLE);
            pauseCountdown();
        } else if (viewState instanceof SessionViewState.ErrorSession) {
            SessionViewState.ErrorSession state = (SessionViewState.ErrorSession) viewState;
            showEndSessionDialog(state.getTitle(), state.getMessage());
        } else if (viewState instanceof SessionViewState.ErrorSending) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.PopUp);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_popup, viewGroup, false);
            TextView tvTitle = dialogView.findViewById(R.id.tv_title);
            TextView tvContent = dialogView.findViewById(R.id.tv_content);
            Button btnPrimary = dialogView.findViewById(R.id.btn_primary);
            ImageView ivPrimary = dialogView.findViewById(R.id.iv_primary);
            tvTitle.setText(R.string.error_sending_report);
            btnPrimary.setText(R.string.retry);
            btnPrimary.setOnClickListener(v -> sessionViewModel.processEvent(new SessionViewEvent.DeviceError("Session Ended", "")));
            tvContent.setVisibility(View.GONE);
            builder.setView(dialogView);
            final AlertDialog dialog = builder.create();
            dialog.show();
        }
    };

    Observer<SessionViewEffect> sessionViewEffectObserver = viewEffect -> {
        if (viewEffect instanceof SessionViewEffect.Back) {
            showCloseSessionDialog();
        } else if (viewEffect instanceof SessionViewEffect.InitializeBle) {
            setDeviceInitChars();
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
            final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.PopUp);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_popup, viewGroup, false);
            TextView tvTitle = dialogView.findViewById(R.id.tv_title);
            TextView tvContent = dialogView.findViewById(R.id.tv_content);
            Button btnPrimary = dialogView.findViewById(R.id.btn_primary);
            ImageView ivPrimary = dialogView.findViewById(R.id.iv_primary);
            ivPrimary.setVisibility(View.GONE);
            tvTitle.setText(title);
            tvContent.setText(message);
            btnPrimary.setText("Exit Session");
            btnPrimary.setOnClickListener(v -> {
                if (!sessionTimer.isFinished())
                    sessionTimer.pause();
                dashboardCommand.navigate();
            });
            builder.setView(dialogView);
            readyDialog = builder.create();
            readyDialog.show();
        }
    }

    private void launchSessionCompleteScreen() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.PopUp);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_popup, viewGroup, false);
        TextView tvTitle = dialogView.findViewById(R.id.tv_title);
        TextView tvContent = dialogView.findViewById(R.id.tv_content);
        Button btnPrimary = dialogView.findViewById(R.id.btn_primary);
        ImageView ivPrimary = dialogView.findViewById(R.id.iv_primary);
        ivPrimary.setVisibility(View.GONE);
        tvTitle.setText(R.string.congratulations);
        tvContent.setText(R.string.session_done);
        btnPrimary.setText(R.string.go_home);
        btnPrimary.setOnClickListener(v -> dashboardCommand.navigate());
        builder.setView(dialogView);
        readyDialog = builder.create();
        readyDialog.show();
    }


    private void setDeviceInitChars() {
        BluetoothManager.getInstance().sendFrequencyData(protocolFrequency, treatmentLength, new BluetoothManager.Callback() {
            @Override
            public void invoke(String args) {
                // follow the interface
            }

            @Override
            public void invoke(List<com.ap.ble.data.BleDevice> args) {
                // follow the interface
            }

            @Override
            public void invoke(String[] args) {
                // follow the interface
            }
        });
        Timber.e("SESSION_VARS T :: %s :: %s", treatmentLength, BluetoothManager.getInstance().getTreatmentLength(treatmentLength));
        Timber.e("SESSION_VARS F :: %s :: %s", protocolFrequency, BluetoothManager.getInstance().getFrequency(protocolFrequency));
    }

    private CountDownTimer sessionTimer = new CountDownTimer(0, 10, 1, this);

    private void startCountdown() {
        if (!sessionTimer.isFinished()) {
            sessionTimer.pause();
        }
        sessionTimer = new CountDownTimer(treatmentLengthMinutes, 0, 1, this);
        sessionTimer.start(false);
    }

    private void pauseCountdown() {
        if (!sessionTimer.isFinished()) {
            sessionTimer.pause();
        }
    }

    private void resumeCountdown() {
        if (!sessionTimer.isFinished()) {
            sessionTimer.pause();
        }
        Timber.e("SESSION :: %s", sessionTimer.getMinutesTillCountDown());
        sessionTimer = new CountDownTimer(sessionTimer.getMinutesTillCountDown(),
                sessionTimer.getSecondsTillCountDown(), 1, this);
        sessionTimer.start(false);
    }

    private void stopCountdown() {
        if (!sessionTimer.isFinished()) {
            sessionTimer.pause();
        }
    }

    @Override
    public void onCountDownActive(String time) {
        tvSessionTimer.post(() -> {
            int m = Integer.parseInt(time.split(":")[0]);
            int s = Integer.parseInt(time.split(":")[1]);
            int t = m * 60 + s;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                pbProgress.setProgress(((1800 - t) * 100) / 1800 + 1, true);
            } else {
                pbProgress.setProgress(((1800 - t) * 100) / 1800 + 1);
            }

            tvSessionTimer.setText(time);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BluetoothManager.getInstance().unregisterDeviceConnectionCallback(deviceConnectionCallback);
    }

    @OnClick(R.id.btn_start_session)
    public void onClickStartSession() {
        btnStartSession.setVisibility(View.INVISIBLE);
        startSession();
        this.sessionViewModel.processEvent(new SessionViewEvent.Start());
    }

    private void showStartSessionPopup() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.PopUp);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_popup, viewGroup, false);
        TextView tvTitle = dialogView.findViewById(R.id.tv_title);
        TextView tvContent = dialogView.findViewById(R.id.tv_content);
        Button btnPrimary = dialogView.findViewById(R.id.btn_primary);
        ImageView ivPrimary = dialogView.findViewById(R.id.iv_primary);
        ivPrimary.setImageResource(R.drawable.ic_press_button);
        tvTitle.setVisibility(View.GONE);
        tvContent.setText(R.string.press_center_button);
        btnPrimary.setVisibility(View.GONE);
        builder.setView(dialogView);
        readyDialog = builder.create();
        readyDialog.setCanceledOnTouchOutside(false);
        readyDialog.show();
    }

    private void startSession() {
        if (dataManager.getPrecautionsDisplayed()) {
            showStartSessionPopup();
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.PopUp);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_popup_with_checkbox, viewGroup, false);
            TextView tvTitle = dialogView.findViewById(R.id.tv_title);
            tvTitle.setText(R.string.warning_title);
            TextView tvContent = dialogView.findViewById(R.id.tv_content);
            tvContent.setText(R.string.warning_message);
            Button btnPrimary = dialogView.findViewById(R.id.btn_primary);
            btnPrimary.setText(R.string.continue_button);
            CheckBox cbDontShowAgain = dialogView.findViewById(R.id.dont_show_again);
            builder.setView(dialogView);
            AlertDialog precautionsWarningDialog = builder.create();
            precautionsWarningDialog.setCanceledOnTouchOutside(false);
            precautionsWarningDialog.show();

            btnPrimary.setOnClickListener(v -> {
                if (cbDontShowAgain.isChecked()) {
                    dataManager.setPrecautionsDisplayed();
                }
                precautionsWarningDialog.hide();
                showStartSessionPopup();
            });
        }
    }
}