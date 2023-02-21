package com.waveneuro.ui.session.session

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.ap.ble.BluetoothManager
import com.ap.ble.BluetoothManager.DeviceConnectionCallback
import com.ap.ble.data.BleDevice
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.waveneuro.R
import com.waveneuro.data.DataManager
import com.waveneuro.data.analytics.AnalyticsManager
import com.waveneuro.databinding.DialogInfoBinding
import com.waveneuro.databinding.DialogPopupBinding
import com.waveneuro.databinding.DialogPopupWithCheckboxBinding
import com.waveneuro.databinding.FragmentSessionBinding
import com.waveneuro.ui.base.BaseActivity
import com.waveneuro.ui.dashboard.DashboardCommand
import com.waveneuro.ui.session.complete.SessionCompleteCommand
import com.waveneuro.ui.session.precautions.PrecautionsBottomSheet
import com.waveneuro.ui.session.session.SessionViewEffect.*
import com.waveneuro.ui.session.session.SessionViewEvent.*
import com.waveneuro.ui.session.session.SessionViewState.*
import com.waveneuro.utils.CountDownTimer
import com.waveneuro.utils.CountDownTimer.OnCountDownListener
import com.waveneuro.views.sessionProgressDialogBuilder
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class SessionActivity : BaseActivity(), OnCountDownListener, DeviceConnectionCallback {

    @Inject
    lateinit var sessionCompleteCommand: SessionCompleteCommand
    @Inject
    lateinit var dashboardCommand: DashboardCommand
    @Inject
    lateinit var viewModel: SessionViewModel
    @Inject
    lateinit var dataManager: DataManager
    @Inject
    lateinit var analyticsManager: AnalyticsManager

    private lateinit var binding: FragmentSessionBinding

    private var readyDialog: AlertDialog? = null
    private var precautionsWarningDialog: AlertDialog? = null
    private var preparingDialog: AlertDialog? = null

    private var treatmentLength: String? = null
    private var protocolFrequency: String? = null
    private var sonalId: String? = null
    private var treatmentLengthMinutes = 0
    private var ble6Value = ""
    private var sessionTimer = CountDownTimer(0, 10, 1, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent()?.inject(this)
        binding = FragmentSessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(SessionCommand.SONAL_ID)) {
            sonalId = intent.getStringExtra(SessionCommand.SONAL_ID)
        }
        if (intent.hasExtra(SessionCommand.TREATMENT_LENGTH)) {
            treatmentLength = intent.getStringExtra(SessionCommand.TREATMENT_LENGTH)
            try {
                treatmentLengthMinutes = treatmentLength!!.toInt() / 60
            } catch (e: Exception) {
                e.printStackTrace()
                finish()
            }
        }
        if (intent.hasExtra(SessionCommand.PROTOCOL_FREQUENCY)) {
            protocolFrequency = intent.getStringExtra(SessionCommand.PROTOCOL_FREQUENCY)
        }

        setView()
        setObserver()
        setPauseResumeText(false)
        setBleListeners()

        if (viewModel.isPrepareDialogShowed.value.not())
            viewModel.processEvent(SessionViewEvent.Initializing)
    }

    private fun setView() {
        preparingDialog = sessionProgressDialogBuilder()
        with(binding) {
            clPrecautions.setOnClickListener {
                val precautionsBottomSheet = PrecautionsBottomSheet.newInstance()
                precautionsBottomSheet.show(supportFragmentManager, "")
            }
            btnStartSession.setOnClickListener {
                btnStartSession.visibility = View.INVISIBLE
                startSession()
            }
            ivInfo.setOnClickListener {
                showInfoDialog()
            }
        }
    }

    private fun setBleListeners() {
        BluetoothManager.getInstance().getSixthCharacteristic(object : BluetoothManager.Callback {
            override fun invoke(args: String) {
                Timber.e("SESSION_EVENT :: BLE_6_VALUE :: %s", args)
                try {
                    val properties = JSONObject()
                    try {
                        properties.put("ble_value", args)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                if (args == ble6Value) {
                    return
                }

                when (args) {
                    BluetoothManager.START_SESSION -> if (ble6Value == BluetoothManager.PAUSE_SESSION) {
                        viewModel.processEvent(SessionViewEvent.ResumeSession)
                    } else {
                        viewModel.processEvent(StartSession)
                    }
                    BluetoothManager.PAUSE_SESSION -> viewModel.processEvent(
                        DevicePaused
                    )
                    BluetoothManager.END_SESSION -> viewModel.processEvent(EndSession)
                    BluetoothManager.ERROR -> viewModel.processEvent(
                        DeviceError(
                            "Uh Oh!",
                            "Error detected on device"
                        )
                    )
                    "01" -> {}
                    BluetoothManager.INACTIVE -> viewModel.processEvent(
                        DeviceError(
                            "Session Ended",
                            "You manually stopped the device."
                        )
                    )
                    else -> {}
                }
                ble6Value = args
                BluetoothManager.getInstance().registerBatteryLevelChangedCallback(
                    viewModel.batteryLevelChangeCallback)
            }

            override fun invoke(args: List<BleDevice>) {
                // follow the interface
            }

            override fun invoke(args: Array<String>) {
                // follow the interface
            }
        })

        BluetoothManager.getInstance().registerDeviceConnectionCallback(this)
    }

    private fun setObserver() {
        with(viewModel) {
            data.observe(this@SessionActivity, Observer { viewState ->
                when(viewState) {
                    is Success -> onSuccess(viewState.item)
                    is Failure -> onFailure(viewState.error)
                    is Loading -> {
                        if (viewState.loading) {
                            displayWait("Loading...", null)
                        } else {
                            removeWait()
                        }
                    }
                    is LocateDevice -> {
                        binding.tvSessionTimer.visibility = View.VISIBLE
                        binding.tvSessionTimer.text = "30:00"
                        binding.tvStopSessionInfo.visibility = View.VISIBLE
                        binding.llControlInfo.visibility = View.GONE
                    }
                    is SessionStarted -> {
                        if (readyDialog != null) readyDialog?.dismiss()
                        binding.btnStartSession.visibility = View.GONE
                        binding.tvSessionTimer.visibility = View.VISIBLE
                        binding.tvStopSessionInfo.visibility = View.VISIBLE
                        binding.llControlInfo.visibility = View.VISIBLE
                        startCountdown()
                    }
                    is SessionViewState.ResumeSession -> {
                        setPauseResumeText(false)
                        binding.tvPaused.visibility = View.GONE
                        binding.ivPause.setImageResource(R.drawable.ic_pause_session)
                        binding.tvSessionTimer.visibility = View.VISIBLE
                        binding.tvStopSessionInfo.visibility = View.VISIBLE
                        binding.llControlInfo.visibility = View.VISIBLE
                        resumeCountdown()
                    }
                    is SessionEnded -> {
                        stopCountdown()
                        showSessionCompleteDialog()
                    }
                    is SessionPaused -> {
                        binding.ivPause.setImageResource(R.drawable.ic_resume_session)
                        setPauseResumeText(true)
                        binding.tvPaused.visibility = View.VISIBLE
                        pauseCountdown()
                    }
                    is ErrorSession -> showEndSessionDialog(viewState.title, viewState.message)
                    is ErrorSending -> showErrorSendingDialog()
                    else -> {}
                }
            })
            viewEffect.observe(this@SessionActivity, Observer { viewEffect ->
                when (viewEffect) {
                    is Back -> showCloseSessionDialog()
                    is InitializeBle -> setDeviceInitChars()
                    is ShowLoader -> preparingDialog?.show()
                    is HideLoader -> {
                        preparingDialog?.dismiss()
                        setPrepareIsShowed(true)
                    }
                }
            })
            currentClient.observe(this@SessionActivity, Observer { client ->
                client?.let { binding.tvTitle.text = it }
            })
            batteryLevel.observe(this@SessionActivity, Observer { batteryLevel ->
                if (batteryLevel in CRITICAL_BATTERY..LOW_BATTERY && !isLowDialogShowed.value)
                    showBatteryDialog(R.string.error_battery_low, LOW_BATTERY)
                else if (batteryLevel <= CRITICAL_BATTERY && !isCriticalDialogShowed.value)
                    showBatteryDialog(R.string.error_battery_critical, CRITICAL_BATTERY)

                if (batteryLevel > CRITICAL_BATTERY && isCriticalDialogShowed.value)
                    setCriticalIsShowed(false)
                if (batteryLevel > LOW_BATTERY && isLowDialogShowed.value)
                    setLowIsShowed(false)
            })
        }
    }

    private fun setPauseResumeText(isPaused: Boolean) {
        binding.tvPauseSessionInfo.text = if (isPaused) getString(R.string.heading_resume_session)
            else getString(R.string.heading_pause_session)
    }

    private fun startSession() {
        if (dataManager.precautionsDisplayed) {
            showStartSessionDialog()
            viewModel.processEvent(Start)
        } else {
            showPrecautionsWarningDialog()
        }
    }

    private fun showBatteryDialog(@StringRes message: Int, type: Byte) {
        val binding = DialogPopupBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(this, R.style.PopUp).setView(binding.root)

        with(binding) {
            tvTitle.setText(R.string.warning_title)
            tvContent.setText(message)
            btnPrimary.setText(R.string.continue_button)
            btnPrimary.setOnClickListener { readyDialog?.dismiss() }
        }

        if (readyDialog?.isShowing == true) readyDialog?.dismiss()

        if (preparingDialog?.isShowing != true) {
            readyDialog = builder.create()
            readyDialog?.show()
            when(type) {
                LOW_BATTERY -> viewModel.setLowIsShowed(true)
                CRITICAL_BATTERY -> viewModel.setCriticalIsShowed(true)
            }
        }
    }

    private fun showCloseSessionDialog() {
        if (!isFinishing) {
            MaterialAlertDialogBuilder(this)
                .setTitle("Are you sure?")
                .setMessage("You want to leave session?")
                .setNegativeButton(
                    "No"
                ) { _: DialogInterface?, _: Int -> }
                .setPositiveButton(
                    "Yes"
                ) { _: DialogInterface?, _: Int ->
                    if (!sessionTimer.isFinished) sessionTimer.pause()
                    dashboardCommand.navigate()
                }
                .setCancelable(false)
                .show()
        }
    }

    private fun showEndSessionDialog(title: String, message: String) {
        if (!isFinishing) {
            val binding = DialogPopupBinding.inflate(layoutInflater)
            val builder = MaterialAlertDialogBuilder(this, R.style.PopUp).setView(binding.root)

            with(binding) {
                ivPrimary.visibility = View.GONE
                tvTitle.text = title
                tvContent.text = message
                btnPrimary.text = "Exit Session"
                btnPrimary.setOnClickListener {
                    if (!sessionTimer.isFinished) sessionTimer.pause()
                    dashboardCommand.navigate()
                }
            }

            readyDialog = builder.create()
            readyDialog?.show()
        }
    }

    private fun showErrorSendingDialog() {
        val binding = DialogPopupBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(this, R.style.PopUp).setView(binding.root)
        val dialog = builder.create()

        with(binding) {
            tvTitle.setText(R.string.error_sending_report)
            btnPrimary.setText(R.string.retry)
            btnPrimary.setOnClickListener {
                viewModel.processEvent(
                    DeviceError("Session Ended", "")
                )
            }
            tvContent.visibility = View.GONE
        }

        dialog.show()
    }

    private fun showSessionCompleteDialog() {
        val binding = DialogPopupBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(this, R.style.PopUp).setView(binding.root)

        with(binding) {
            ivPrimary.visibility = View.GONE
            tvTitle.setText(R.string.congratulations)
            tvContent.setText(R.string.session_done)
            btnPrimary.setText(R.string.go_home)
            btnPrimary.setOnClickListener { v: View? -> dashboardCommand.navigate() }
        }

        readyDialog = builder.create()
        readyDialog?.show()
    }

    private fun showStartSessionDialog() {
        val binding = DialogPopupBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(this, R.style.PopUp).setView(binding.root)
        readyDialog = builder.create()

        with(binding) {
            Glide.with(this@SessionActivity).load(R.drawable.turn_on).into(ivPrimary)
            tvTitle.visibility = View.GONE
            tvContent.setText(R.string.press_center_button)
            btnPrimary.visibility = View.GONE
        }

        readyDialog?.setCanceledOnTouchOutside(false)
        readyDialog?.show()
    }

    private fun showPrecautionsWarningDialog() {
        val binding = DialogPopupWithCheckboxBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(this, R.style.PopUp).setView(binding.root)
        precautionsWarningDialog = builder.create()

        with(binding) {
            tvTitle.setText(R.string.warning)
            tvContent.setText(R.string.warning_message)
            btnPrimary.setText(R.string.continue_button)
            btnPrimary.setOnClickListener {
                if (dontShowAgain.isChecked) {
                    dataManager.setPrecautionsDisplayed()
                }
                precautionsWarningDialog?.hide()
                showStartSessionDialog()
                viewModel.processEvent(Start)
            }
        }

        precautionsWarningDialog?.setCanceledOnTouchOutside(false)
        precautionsWarningDialog?.show()
    }

    private fun showInfoDialog() {
        val binding = DialogInfoBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(this, R.style.PopUp).setView(binding.root)
        val dialog = builder.create()
        val client = viewModel.currentClient.value

        with(binding) {
            tvDeviceIdValue.text = sonalId
            tvClientValue.text = client
            ivClose.setOnClickListener { dialog.dismiss() }
        }

        viewModel.batteryLevel.observe(this, Observer { batteryLevel ->
            binding.tvBatteryValue.text = "${batteryLevel}%"
        })

        if (client != null)
            dialog.show()
    }

    private fun setDeviceInitChars() {
        BluetoothManager.getInstance().sendFrequencyData(
            protocolFrequency,
            treatmentLength,
            object : BluetoothManager.Callback {
                override fun invoke(args: String) {
                    // follow the interface
                }
                override fun invoke(args: List<BleDevice>) {
                    // follow the interface
                }
                override fun invoke(args: Array<String>) {
                    // follow the interface
                }
            })
        Timber.e(
            "SESSION_VARS T :: %s :: %s",
            treatmentLength,
            BluetoothManager.getInstance().getTreatmentLength(treatmentLength)
        )
        Timber.e(
            "SESSION_VARS F :: %s :: %s",
            protocolFrequency,
            BluetoothManager.getInstance().getFrequency(protocolFrequency)
        )
    }

    private fun startCountdown() {
        if (!sessionTimer.isFinished) {
            sessionTimer.pause()
        }
        sessionTimer = CountDownTimer(treatmentLengthMinutes.toLong(), 0, 1, this)
        sessionTimer.start(false)
    }

    private fun pauseCountdown() {
        if (!sessionTimer.isFinished) {
            sessionTimer.pause()
        }
    }

    private fun resumeCountdown() {
        if (!sessionTimer.isFinished) {
            sessionTimer.pause()
        }
        Timber.e("SESSION :: %s", sessionTimer.minutesTillCountDown)
        sessionTimer = CountDownTimer(
            sessionTimer.minutesTillCountDown,
            sessionTimer.secondsTillCountDown, 1, this
        )
        sessionTimer.start(false)
    }

    private fun stopCountdown() {
        if (!sessionTimer.isFinished) {
            sessionTimer.pause()
        }
    }

    override fun onCountDownActive(time: String) {
        binding.tvSessionTimer.post {
            val m = time.split(":").toTypedArray()[0].toInt()
            val s = time.split(":").toTypedArray()[1].toInt()
            val t = m * 60 + s
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                binding.pbProgress.setProgress((1800 - t) * 100 / 1800 + 1, true)
            } else {
                binding.pbProgress.progress = (1800 - t) * 100 / 1800 + 1
            }
            binding.tvSessionTimer.text = time
        }
    }

    override fun onCountDownFinished() {
        binding.tvSessionTimer.post {
            binding.tvSessionTimer.text = "00:00"
            ble6Value = "04"
            viewModel.processEvent(SessionViewEvent.EndSession)
        }
    }

    // Device connection callback
    override fun onConnected(bleDevice: BleDevice) {
        // follow the interface
    }
    override fun onCharacterises(value: String) {
        // follow the interface
    }
    override fun onDisconnected() {
        Timber.e("SESSION DISCONNECTED CALLBACK")
        if (!sessionTimer.isFinished) {
            sessionTimer.pause()
        }
        viewModel.processEvent(
            DeviceError(
                "Session Ended",
                "You manually stopped the device."
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        BluetoothManager.getInstance().unregisterDeviceConnectionCallback(this)
        BluetoothManager.getInstance().unregisterBatteryLevelChangedCallback(
            viewModel.batteryLevelChangeCallback)
    }

    companion object {
        private const val LOW_BATTERY: Byte = 20
        private const val CRITICAL_BATTERY: Byte = 10
    }
}