package com.waveneuro.ui.session.session

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.ap.ble.BluetoothManager
import com.ap.ble.BluetoothManager.DeviceConnectionCallback
import com.ap.ble.data.BleDevice
import com.waveneuro.R
import com.waveneuro.data.DataManager
import com.waveneuro.data.analytics.AnalyticsManager
import com.waveneuro.databinding.DialogInfoBinding
import com.waveneuro.databinding.FragmentSessionBinding
import com.waveneuro.ui.base.BaseActivity
import com.waveneuro.ui.dashboard.DashboardCommand
import com.waveneuro.ui.session.complete.SessionCompleteCommand
import com.waveneuro.ui.session.precautions.PrecautionsBottomSheet
import com.waveneuro.ui.session.session.SessionViewEffect.InitializeBle
import com.waveneuro.ui.session.session.SessionViewEvent.DeviceError
import com.waveneuro.ui.session.session.SessionViewState.*
import com.waveneuro.utils.CountDownTimer
import com.waveneuro.utils.CountDownTimer.OnCountDownListener
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class SessionActivity : BaseActivity(), OnCountDownListener {

    @Inject
    lateinit var sessionCompleteCommand: SessionCompleteCommand
    @Inject
    lateinit var dashboardCommand: DashboardCommand
    @Inject
    lateinit var sessionViewModel: SessionViewModel
    @Inject
    lateinit var dataManager: DataManager
    @Inject
    lateinit var analyticsManager: AnalyticsManager

    private lateinit var binding: FragmentSessionBinding

    var readyDialog: AlertDialog? = null
    var precautionsWarningDialog: AlertDialog? = null
    var treatmentLength: String? = null
    var protocolFrequency: String? = null
    var sonalId: String? = null
    var treatmentLengthMinutes = 0
    private var ble6Value = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent()?.inject(this)
        binding = FragmentSessionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setView()
        setObserver()

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
        stopSpanText()
        pauseSpanText(false)
        setBleListeners()
    }

    private fun setView() {
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
                showDialogInfo()
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
                        sessionViewModel.processEvent(SessionViewEvent.ResumeSession)
                    } else {
                        sessionViewModel.processEvent(SessionViewEvent.StartSession)
                    }
                    BluetoothManager.PAUSE_SESSION -> sessionViewModel.processEvent(
                        SessionViewEvent.DevicePaused
                    )
                    BluetoothManager.END_SESSION -> sessionViewModel.processEvent(SessionViewEvent.EndSession)
                    BluetoothManager.ERROR -> sessionViewModel.processEvent(
                        DeviceError(
                            "Uh Oh!",
                            "Error detected on device"
                        )
                    )
                    "01" -> {}
                    BluetoothManager.INACTIVE -> sessionViewModel.processEvent(
                        DeviceError(
                            "Session Ended",
                            "You manually stopped the device."
                        )
                    )
                    else -> {}
                }
                ble6Value = args
            }

            override fun invoke(args: List<BleDevice>) {
                // follow the interface
            }

            override fun invoke(args: Array<String>) {
                // follow the interface
            }
        })

        BluetoothManager.getInstance().registerDeviceConnectionCallback(deviceConnectionCallback)
    }

    var deviceConnectionCallback: DeviceConnectionCallback = object : DeviceConnectionCallback {
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
            sessionViewModel.processEvent(
                DeviceError(
                    "Session Ended",
                    "You manually stopped the device."
                )
            )
        }
    }

    private fun setObserver() {
        sessionViewModel.data.observe(this, sessionViewStateObserver)
        sessionViewModel.viewEffect.observe(this, sessionViewEffectObserver)
    }

    private fun pauseSpanText(isPaused: Boolean) {
        var spannableString: SpannableString? = null
        if (isPaused) {
            spannableString = SpannableString(getString(R.string.resume_session_info))
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                10,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        } else {
            spannableString = SpannableString(getString(R.string.pause_session_info))
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                9,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        binding.tvPauseSessionInfo.text = spannableString
    }

    private fun stopSpanText() {
        val spannableString = SpannableString(getString(R.string.cancel_session_info))
        spannableString.setSpan(StyleSpan(Typeface.BOLD), 0, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvStopSessionInfo.text = spannableString
    }

    var sessionViewStateObserver = Observer { viewState: SessionViewState? ->
        if (viewState is SessionViewState.Success) {
            val (item) = viewState
            onSuccess(item)
        } else if (viewState is SessionViewState.Failure) {
            val (error) = viewState
            onFailure(error)
        } else if (viewState is SessionViewState.Loading) {
            val (loading) = viewState
            if (loading) {
                displayWait("Loading...", null)
            } else {
                removeWait()
            }
        } else if (viewState is SessionViewState.LocateDevice) {
            binding.tvSessionTimer.visibility = View.VISIBLE
            binding.tvSessionTimer.text = "30:00"
            binding.tvStopSessionInfo.visibility = View.VISIBLE
            binding.llControlInfo.visibility = View.GONE
        } else if (viewState is SessionStarted) {
            if (readyDialog != null) readyDialog?.dismiss()
            binding.btnStartSession.visibility = View.GONE
            binding.tvSessionTimer.visibility = View.VISIBLE
            binding.tvStopSessionInfo.visibility = View.VISIBLE
            binding.llControlInfo.visibility = View.VISIBLE
            startCountdown()
        } else if (viewState is SessionViewState.ResumeSession) {
            pauseSpanText(false)
            binding.tvPaused.visibility = View.GONE
            binding.ivPause.setImageResource(R.drawable.ic_pause_session)
            binding.tvSessionTimer.visibility = View.VISIBLE
            binding.tvStopSessionInfo.visibility = View.VISIBLE
            binding.llControlInfo.visibility = View.VISIBLE
            resumeCountdown()
        } else if (viewState is SessionEnded) {
            stopCountdown()
            launchSessionCompleteScreen()
        } else if (viewState is SessionPaused) {
            binding.ivPause.setImageResource(R.drawable.ic_resume_session)
            pauseSpanText(true)
            binding.tvPaused.visibility = View.VISIBLE
            pauseCountdown()
        } else if (viewState is ErrorSession) {
            val (title, message) = viewState
            showEndSessionDialog(title, message)
        } else if (viewState is ErrorSending) {
            val builder = AlertDialog.Builder(this, R.style.PopUp)
            val viewGroup = findViewById<ViewGroup>(android.R.id.content)
            val dialogView =
                LayoutInflater.from(this).inflate(R.layout.dialog_popup, viewGroup, false)
            val tvTitle = dialogView.findViewById<TextView>(R.id.tv_title)
            val tvContent = dialogView.findViewById<TextView>(R.id.tv_content)
            val btnPrimary = dialogView.findViewById<Button>(R.id.btn_primary)
            val ivPrimary = dialogView.findViewById<ImageView>(R.id.iv_primary)
            tvTitle.setText(R.string.error_sending_report)
            btnPrimary.setText(R.string.retry)
            btnPrimary.setOnClickListener { v: View? ->
                sessionViewModel.processEvent(
                    DeviceError(
                        "Session Ended",
                        ""
                    )
                )
            }
            tvContent.visibility = View.GONE
            builder.setView(dialogView)
            val dialog = builder.create()
            dialog.show()
        }
    }
    var sessionViewEffectObserver = Observer { viewEffect: SessionViewEffect? ->
        if (viewEffect is SessionViewEffect.Back) {
            showCloseSessionDialog()
        } else if (viewEffect is InitializeBle) {
            setDeviceInitChars()
        }
    }

    private fun showCloseSessionDialog() {
        if (!isFinishing) {
            AlertDialog.Builder(this)
                .setTitle("Are you sure?")
                .setMessage("You want to leave session?")
                .setNegativeButton(
                    "No"
                ) { dialog: DialogInterface?, which: Int -> }
                .setPositiveButton(
                    "Yes"
                ) { dialog: DialogInterface?, which: Int ->
                    if (!sessionTimer.isFinished) sessionTimer.pause()
                    dashboardCommand.navigate()
                }
                .setCancelable(false)
                .show()
        }
    }

    private fun showEndSessionDialog(title: String, message: String) {
        if (!isFinishing) {
            val builder = AlertDialog.Builder(this, R.style.PopUp)
            val viewGroup = findViewById<ViewGroup>(android.R.id.content)
            val dialogView =
                LayoutInflater.from(this).inflate(R.layout.dialog_popup, viewGroup, false)
            val tvTitle = dialogView.findViewById<TextView>(R.id.tv_title)
            val tvContent = dialogView.findViewById<TextView>(R.id.tv_content)
            val btnPrimary = dialogView.findViewById<Button>(R.id.btn_primary)
            val ivPrimary = dialogView.findViewById<ImageView>(R.id.iv_primary)
            ivPrimary.visibility = View.GONE
            tvTitle.text = title
            tvContent.text = message
            btnPrimary.text = "Exit Session"
            btnPrimary.setOnClickListener { v: View? ->
                if (!sessionTimer.isFinished) sessionTimer.pause()
                dashboardCommand.navigate()
            }
            builder.setView(dialogView)
            readyDialog = builder.create()
            readyDialog?.show()
        }
    }

    private fun launchSessionCompleteScreen() {
        val builder = AlertDialog.Builder(this, R.style.PopUp)
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_popup, viewGroup, false)
        val tvTitle = dialogView.findViewById<TextView>(R.id.tv_title)
        val tvContent = dialogView.findViewById<TextView>(R.id.tv_content)
        val btnPrimary = dialogView.findViewById<Button>(R.id.btn_primary)
        val ivPrimary = dialogView.findViewById<ImageView>(R.id.iv_primary)
        ivPrimary.visibility = View.GONE
        tvTitle.setText(R.string.congratulations)
        tvContent.setText(R.string.session_done)
        btnPrimary.setText(R.string.go_home)
        btnPrimary.setOnClickListener { v: View? -> dashboardCommand.navigate() }
        builder.setView(dialogView)
        readyDialog = builder.create()
        readyDialog?.show()
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

    private var sessionTimer = CountDownTimer(0, 10, 1, this)

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
            sessionViewModel.processEvent(SessionViewEvent.EndSession)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        BluetoothManager.getInstance().unregisterDeviceConnectionCallback(deviceConnectionCallback)
    }

    private fun showStartSessionPopup() {
        val builder = AlertDialog.Builder(this, R.style.PopUp)
        val viewGroup = findViewById<ViewGroup>(android.R.id.content)
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_popup, viewGroup, false)
        val tvTitle = dialogView.findViewById<TextView>(R.id.tv_title)
        val tvContent = dialogView.findViewById<TextView>(R.id.tv_content)
        val btnPrimary = dialogView.findViewById<Button>(R.id.btn_primary)
        val ivPrimary = dialogView.findViewById<ImageView>(R.id.iv_primary)
        ivPrimary.setImageResource(R.drawable.ic_press_button)
        tvTitle.visibility = View.GONE
        tvContent.setText(R.string.press_center_button)
        btnPrimary.visibility = View.GONE
        builder.setView(dialogView)
        readyDialog = builder.create()
        readyDialog?.setCanceledOnTouchOutside(false)
        readyDialog?.show()
    }

    private fun startSession() {
        if (dataManager.precautionsDisplayed) {
            showStartSessionPopup()
            sessionViewModel.processEvent(SessionViewEvent.Start)
        } else {
            val builder = AlertDialog.Builder(this, R.style.PopUp)
            val viewGroup = findViewById<ViewGroup>(android.R.id.content)
            val dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_popup_with_checkbox, viewGroup, false)
            val tvTitle = dialogView.findViewById<TextView>(R.id.tv_title)
            tvTitle.setText(R.string.warning_title)
            val tvContent = dialogView.findViewById<TextView>(R.id.tv_content)
            tvContent.setText(R.string.warning_message)
            val btnPrimary = dialogView.findViewById<Button>(R.id.btn_primary)
            btnPrimary.setText(R.string.continue_button)
            val cbDontShowAgain = dialogView.findViewById<CheckBox>(R.id.dont_show_again)
            builder.setView(dialogView)
            precautionsWarningDialog = builder.create()
            precautionsWarningDialog?.setCanceledOnTouchOutside(false)
            precautionsWarningDialog?.show()
            btnPrimary.setOnClickListener { v: View? ->
                if (cbDontShowAgain.isChecked) {
                    dataManager.setPrecautionsDisplayed()
                }
                precautionsWarningDialog?.hide()
                showStartSessionPopup()
                sessionViewModel.processEvent(SessionViewEvent.Start)
            }
        }
    }

    private fun showDialogInfo() {
        val binding = DialogInfoBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(this, R.style.PopUp).setView(binding.root)
        val dialog = builder.create()

        with(binding) {
            tvDeviceIdValue.text = "Sonal - 3438909"
            tvClientValue.text = "Ann Doe"
            tvBatteryValue.text = "87%"
            ivClose.setOnClickListener { dialog.dismiss() }
        }

        dialog.show()
    }

}