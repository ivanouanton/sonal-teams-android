package com.waveneuro.ui.dashboard.device

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.ap.ble.BluetoothManager
import com.ap.ble.BluetoothManager.DeviceConnectionCallback
import com.ap.ble.callback.BleScanCallback
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.waveneuro.R
import com.waveneuro.databinding.ActivityDeviceBinding
import com.waveneuro.databinding.DialogPopupBinding
import com.waveneuro.domain.model.ble.BleDevice
import com.waveneuro.domain.model.user.UserInfo
import com.waveneuro.ui.base.activity.BaseViewModelActivity
import com.waveneuro.ui.dashboard.DashBoardViewModelImpl
import com.waveneuro.ui.dashboard.DashboardActivity
import com.waveneuro.ui.dashboard.DashboardViewEvent
import com.waveneuro.ui.dashboard.DashboardViewState.Connect
import com.waveneuro.ui.dashboard.DashboardViewState.Disconnect
import com.waveneuro.ui.dashboard.device.DeviceViewEvent.LocateDeviceNextClicked
import com.waveneuro.ui.dashboard.device.DeviceViewEvent.NoDeviceFound
import com.waveneuro.ui.dashboard.device.DeviceViewState.*
import com.waveneuro.ui.dashboard.device.adapter.DeviceAdapter
import com.waveneuro.ui.dashboard.device.viewmodel.DeviceViewModel
import com.waveneuro.ui.dashboard.device.viewmodel.DeviceViewModelImpl
import com.waveneuro.ui.session.how_to.HowToActivity
import com.waveneuro.ui.session.session.SessionActivity
import com.waveneuro.utils.ext.getAppComponent
import com.waveneuro.utils.ext.toast
import timber.log.Timber

class DeviceActivity : BaseViewModelActivity<ActivityDeviceBinding, DeviceViewModel>() {

    private val dashBoardViewModel: DashBoardViewModelImpl by viewModels {
        getAppComponent().dashboardViewModelFactory()
    }

    private lateinit var deviceAdapter: DeviceAdapter
    private val deviceList = mutableListOf<BleDevice>()

    private var isSearching = false
    private var operatingAnim: Animation? = null

    override val viewModel: DeviceViewModelImpl by viewModels {
        getAppComponent().deviceViewModelFactory()
    }

    override fun initBinding(): ActivityDeviceBinding =
        ActivityDeviceBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView()
        setObserver()

        viewModel.processEvent(DeviceViewEvent.Start)
        if (!viewModel.onboardingDisplayed) {
            startActivity(HowToActivity.newIntent(this))
        }
    }

    private fun setView() {
        with(binding) {
            deviceAdapter = DeviceAdapter(this@DeviceActivity, ::onClickDevice)
            binding.rvDeviceAvailable.adapter = deviceAdapter
            deviceAdapter.submitList(deviceList)

            operatingAnim = AnimationUtils.loadAnimation(this@DeviceActivity, R.anim.rotate)
            operatingAnim?.interpolator = LinearInterpolator()
            tvFirstTime.paintFlags = tvFirstTime.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            btnLocateDevice.setOnClickListener {
                checkPermissions()
                viewModel.processEvent(DeviceViewEvent.LocateDevice)
            }
            ivBack.setOnClickListener {
                if (isSearching) {
                    viewModel.processEvent(DeviceViewEvent.Start)
                } else {
                    startActivity(DashboardActivity.newIntent(this@DeviceActivity))
                }
            }
            tvFirstTime.setOnClickListener {
                launchHowToActivity()
            }
        }
    }

    private fun setObserver() {
        with(binding) {
            viewModel.data.observe(this@DeviceActivity, Observer { viewState ->
                isSearching = false
                when (viewState) {
                    is Success -> {
                        binding.llLocateDevice.visibility = View.GONE
                        binding.cvDeviceAvailable.visibility = View.VISIBLE
                        binding.llContainerDevice.visibility = View.GONE
                    }
                    is InitLocateDevice -> {
                        with(binding) {
                            tvLabelWelcome.text = getString(R.string.ensure_your_device_powered_up)
                            tvLabelWelcome.visibility = View.VISIBLE
                            ivDevice.visibility = View.VISIBLE
                            tvLocateDeviceInfo.visibility = View.VISIBLE
                            llLocateDevice.visibility = View.VISIBLE
                            cvDeviceAvailable.visibility = View.GONE
                            llContainerDevice.visibility = View.GONE
                            Glide.with(this@DeviceActivity).load(R.drawable.turn_on).into(ivDevice)
                        }
                    }
                    is LocateDevice -> {
                        with(binding) {
                            tvLabelWelcome.text = getString(R.string.ensure_your_device_powered_up)
                            tvLabelWelcome.visibility = View.VISIBLE
                            Glide.with(this@DeviceActivity).load(R.drawable.turn_on).into(ivDevice)
                            ivDevice.visibility = View.VISIBLE
                            llLocateDevice.visibility = View.VISIBLE
                            cvDeviceAvailable.visibility = View.GONE
                            llContainerDevice.visibility = View.GONE
                        }
                    }
                    is LocateDeviceNext -> {
                        with(binding) {
                            tvLabelWelcome.text = getString(R.string.activate_your_device)
                            tvLabelWelcome.visibility = View.VISIBLE
                            ivDevice.setImageDrawable(
                                ContextCompat.getDrawable(
                                    this@DeviceActivity,
                                    R.drawable.img_device_sonal_connector_with_wave
                                )
                            )
                            ivDevice.visibility = View.VISIBLE
                            tvLocateDeviceInfo.visibility = View.VISIBLE
                            llLocateDevice.visibility = View.VISIBLE
                            cvDeviceAvailable.visibility = View.GONE
                            llContainerDevice.visibility = View.GONE
                        }
                    }
                    is Searching -> {
                        isSearching = true
                        with(binding) {
                            tvLabelWelcome.visibility = View.GONE
                            llLocateDevice.visibility = View.GONE
                            cvDeviceAvailable.visibility = View.GONE
                            tvScanningDeviceInfo.visibility = View.VISIBLE
                            tvLabelScanning.visibility = View.VISIBLE
                            llContainerDevice.visibility = View.VISIBLE
                            Glide.with(this@DeviceActivity).load(R.drawable.searching).into(ivDeviceScanning)
                        }
                        locateDevice()
                    }
                    is Connecting -> {
                        isSearching = true
                        connectToDevice(viewState.bleDevice)
                    }
                    is Connected -> {
                        launchPairingSuccessfulDialog()
                        with(binding) {
                            llLocateDevice.visibility = View.GONE
                            cvDeviceAvailable.visibility = View.GONE
                            llContainerDevice.visibility = View.VISIBLE
                        }
                    }
                    is Searched -> {
                        isSearching = true
                        with(binding) {
                            llLocateDevice.visibility = View.VISIBLE
                            cvDeviceAvailable.visibility = View.VISIBLE
                            llContainerDevice.visibility = View.GONE
                        }
                    }
                    else -> {}
                }
            })
            viewModel.viewEffect.observe(this@DeviceActivity, Observer { viewEffect ->
                if (viewEffect is DeviceViewEffect.SessionRedirect) {
                    launchSessionScreen(
                        viewEffect.treatmentLength,
                        viewEffect.protocolFrequency,
                        viewEffect.sonalId
                    )
                }
            })
            dashBoardViewModel.data.observe(this@DeviceActivity, Observer { dashboardViewState ->
                Timber.i("DEVICE_DASHBOARD :: onChanged: received freshObject")
                if (dashboardViewState != null) {
                    if (dashboardViewState is Connect) {
                        viewModel.processEvent(DeviceViewEvent.Connected)
                    } else if (dashboardViewState is Disconnect) {
                        viewModel.processEvent(DeviceViewEvent.Disconnected)
                    }
                }
            })
        }
    }

    private fun launchHowToActivity() {
        startActivity(HowToActivity.newIntent(this))
    }

    private fun launchPairingSuccessfulDialog() {
        val binding = DialogPopupBinding.inflate(layoutInflater)
        val builder = MaterialAlertDialogBuilder(this, R.style.PopUp).setView(binding.root)
        val dialog = builder.create()

        with(binding) {
            tvTitle.setText(R.string.pairing_successful)
            tvContent.setText(R.string.pairing_successful_description)
            btnPrimary.setText(R.string.proceed_to_session)
            btnPrimary.setOnClickListener {
                dialog.dismiss()
                viewModel.processEvent(DeviceViewEvent.StartSessionClicked)
            }
        }

        dialog.show()
    }

    private fun launchSessionScreen(
        treatmentLength: String,
        protocolFrequency: String,
        sonalId: String
    ) {
        if (TextUtils.isEmpty(treatmentLength) || TextUtils.isEmpty(protocolFrequency)) {
            toast("Treatment data not available.")
        }
        startActivity(SessionActivity.newIntent(
            this, treatmentLength, protocolFrequency, sonalId
        ))
    }

    private fun connectToDevice(bleDevice: BleDevice) {
        BluetoothManager.getInstance()
            .getDeviceName(bleDevice.mac, bleDevice.name, object : DeviceConnectionCallback {
                override fun onConnected(bleDevice: com.ap.ble.data.BleDevice) {
                    showLoading(false)

                    viewModel.processEvent(DeviceViewEvent.Connected)
                    viewModel.processEvent(DeviceViewEvent.SetDeviceId(bleDevice.name))
                    dashBoardViewModel.processEvent(
                        DashboardViewEvent.Connected(
                            BleDevice(bleDevice.name, bleDevice.mac)
                        )
                    )
                }
                override fun onDisconnected() {
                    showLoading(false)
                    viewModel.processEvent(DeviceViewEvent.Disconnected)
                    dashBoardViewModel.processEvent(DashboardViewEvent.Disconnected)
                }
            })
    }

    private fun locateDevice() {
        BluetoothManager.getInstance().deviceList(object : BleScanCallback() {
            override fun onScanFinished(scanResultList: List<com.ap.ble.data.BleDevice>) {
                Log.e("DEVICE_LIST", "" + scanResultList.toTypedArray().contentToString())
                if (scanResultList.isNotEmpty()) {
                    val bleDevices = mutableListOf<BleDevice>()
                    for (device in scanResultList) {
                        bleDevices.add(BleDevice(device.name, device.mac))
                    }
                    loadBleDevices(bleDevices)
                    viewModel.processEvent(DeviceViewEvent.Searched)
                } else {
                    viewModel.processEvent(NoDeviceFound)
                    try {
                        toast("No device found.")
                    } catch (e: IllegalStateException) {
                        Timber.e(e.message)
                    }
                }
            }

            override fun onScanStarted(success: Boolean) {}
            override fun onScanning(bleDevice: com.ap.ble.data.BleDevice) {
                Log.e("DEVICES", "" + bleDevice.name + " :: " + bleDevice.key)
                val newDevice = BleDevice(bleDevice.name, bleDevice.mac)
                if (deviceList.contains(newDevice).not()) {
                    deviceList.add(newDevice)
                    if (viewModel.data.value !is Searched)
                        viewModel.processEvent(DeviceViewEvent.Searched)
                }
            }
        })
    }

    private fun loadBleDevices(list: List<BleDevice>) {
        deviceList.clear()
        deviceList.addAll(list)
        deviceAdapter.notifyDataSetChanged()
    }

    private fun onClickDevice(data: BleDevice) {
        showLoading(true)
        viewModel.processEvent(DeviceViewEvent.DeviceClicked(data))
    }

    private fun checkPermissions() {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!bluetoothAdapter.isEnabled) {
            toast("Please turn on Bluetooth first")
            return
        }
        val permissions: MutableList<String> = ArrayList()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
        }
        val permissionDeniedList: MutableList<String> = ArrayList()
        for (permission in permissions) {
            val permissionCheck = ContextCompat.checkSelfPermission(this, permission)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission)
            } else {
                permissionDeniedList.add(permission)
            }
        }
        if (permissionDeniedList.isNotEmpty()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                AlertDialog.Builder(this)
                    .setTitle("Location Permission Request")
                    .setMessage("WaveNeuro requires that Location permission be enabled to scan for Bluetooth Low Energy devices.\nPlease allow Location permission to continue.")
                    .setNegativeButton(
                        R.string.cancel
                    ) { dialog: DialogInterface?, which: Int -> }
                    .setPositiveButton(
                        "Allow"
                    ) { dialog: DialogInterface?, which: Int ->
                        val deniedPermissions = permissionDeniedList.toTypedArray()
                        ActivityCompat.requestPermissions(
                            this,
                            deniedPermissions,
                            REQUEST_CODE_PERMISSION_LOCATION
                        )
                    }
                    .setCancelable(false)
                    .show()
            } else {
                val deniedPermissions = permissionDeniedList.toTypedArray()
                ActivityCompat.requestPermissions(
                    this,
                    deniedPermissions,
                    REQUEST_CODE_PERMISSION_BLUETOOTH
                )
            }
        }
    }

    private fun onPermissionGranted(permission: String) {
        when (permission) {
            Manifest.permission.ACCESS_FINE_LOCATION -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
                AlertDialog.Builder(this)
                    .setTitle("Location Services not enabled")
                    .setMessage("Android requires that Location Services to enabled to scan for Bluetooth Low Energy devices.\nPlease enable Location Services in Settings to continue. ")
                    .setNegativeButton(
                        R.string.cancel
                    ) { dialog: DialogInterface?, which: Int -> }
                    .setPositiveButton(
                        "Setting"
                    ) { dialog: DialogInterface?, which: Int ->
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivityForResult(intent, REQUEST_CODE_OPEN_GPS)
                    }
                    .setCancelable(false)
                    .show()
            } else {
                viewModel.processEvent(LocateDeviceNextClicked)
            }
            Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN -> {
                Timber.e("PERMISSION_GRANTED :: %s", permission)
                viewModel.processEvent(LocateDeviceNextClicked)
            }
        }
    }

    private fun checkGPSIsOpen(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_PERMISSION_LOCATION -> if (grantResults.isNotEmpty()) {
                var i = 0
                while (i < grantResults.size) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        onPermissionGranted(permissions[i])
                    }
                    i++
                }
            }
            REQUEST_CODE_PERMISSION_BLUETOOTH -> if (grantResults.isNotEmpty()) {
                var i = 0
                while (i < grantResults.size) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        onPermissionGranted(permissions[i])
                    }
                    i++
                }
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_OPEN_GPS = 1
        private const val REQUEST_CODE_PERMISSION_LOCATION = 2
        private const val REQUEST_CODE_PERMISSION_BLUETOOTH = 3

        fun newIntent(context: Context): Intent = Intent(context, DeviceActivity::class.java)
    }

}