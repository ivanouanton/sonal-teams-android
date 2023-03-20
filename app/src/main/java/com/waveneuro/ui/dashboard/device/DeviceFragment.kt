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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.ap.ble.BluetoothManager
import com.ap.ble.BluetoothManager.DeviceConnectionCallback
import com.ap.ble.callback.BleScanCallback
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.waveneuro.R
import com.waveneuro.databinding.FragmentDeviceBinding
import com.waveneuro.domain.model.ble.BleDevice
import com.waveneuro.domain.model.user.UserInfo
import com.waveneuro.ui.base.fragment.BaseViewModelFragment
import com.waveneuro.ui.dashboard.DashBoardViewModelImpl
import com.waveneuro.ui.dashboard.DashboardActivity
import com.waveneuro.ui.dashboard.DashboardViewEvent
import com.waveneuro.ui.dashboard.DashboardViewState
import com.waveneuro.ui.dashboard.DashboardViewState.Connect
import com.waveneuro.ui.dashboard.DashboardViewState.Disconnect
import com.waveneuro.ui.dashboard.device.DeviceViewEvent.LocateDeviceNextClicked
import com.waveneuro.ui.dashboard.device.DeviceViewEvent.NoDeviceFound
import com.waveneuro.ui.dashboard.device.DeviceViewState.*
import com.waveneuro.ui.dashboard.device.adapter.DeviceAdapter
import com.waveneuro.ui.dashboard.device.viewmodel.DeviceViewModel
import com.waveneuro.ui.dashboard.device.viewmodel.DeviceViewModelImpl
import com.waveneuro.ui.dashboard.home.HomeFragment
import com.waveneuro.ui.session.how_to.HowToActivity
import com.waveneuro.ui.session.session.SessionActivity
import com.waveneuro.utils.ext.getAppComponent
import com.waveneuro.utils.ext.toast
import timber.log.Timber

class DeviceFragment : BaseViewModelFragment<FragmentDeviceBinding, DeviceViewModel>() {

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

    override fun initBinding(container: ViewGroup?): FragmentDeviceBinding =
        FragmentDeviceBinding.inflate(layoutInflater)

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        setObserver()
        binding?.let { binding ->
            with(binding) {
                deviceAdapter = DeviceAdapter(requireContext(), ::onClickDevice)
                binding.rvDeviceAvailable.adapter = deviceAdapter
                deviceAdapter.submitList(deviceList)

                operatingAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate)
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
                        //TODO check logic probably set bottom bar
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fr_container, HomeFragment.newInstance())
                            .commit()
//                        startActivity(DashboardActivity.newIntent(requireContext()))
                    }
                }
                tvFirstTime.setOnClickListener {
                    launchHowToActivity()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.processEvent(DeviceViewEvent.Start)
        if (!viewModel.onboardingDisplayed) {
            startActivity(HowToActivity.newIntent(requireContext()))
        }
    }

    private fun setObserver() {
        binding?.let { binding ->
            viewModel.data.removeObservers(viewLifecycleOwner)
            viewModel.viewEffect.removeObservers(viewLifecycleOwner)
            viewModel.data.observe(viewLifecycleOwner, Observer { viewState ->
                if (lifecycle.currentState != Lifecycle.State.RESUMED) {
                    return@Observer
                }
                isSearching = false
                when (viewState) {
                    is Success -> {
                        binding.llLocateDevice.visibility = View.GONE
                        binding.cvDeviceAvailable.visibility = View.VISIBLE
                        binding.llContainerDevice.visibility = View.GONE
                    }
                    is InitLocateDevice -> {
                        setUserDetail(viewState.user)
                        with(binding) {
                            tvLabelWelcome.text = getString(R.string.ensure_your_device_powered_up)
                            tvLabelWelcome.visibility = View.VISIBLE
                            ivDevice.visibility = View.VISIBLE
                            tvLocateDeviceInfo.visibility = View.VISIBLE
                            llLocateDevice.visibility = View.VISIBLE
                            cvDeviceAvailable.visibility = View.GONE
                            llContainerDevice.visibility = View.GONE
                            Glide.with(requireContext()).load(R.drawable.turn_on).into(ivDevice)
                        }
                    }
                    is LocateDevice -> {
                        with(binding) {
                            tvLabelWelcome.text = getString(R.string.ensure_your_device_powered_up)
                            tvLabelWelcome.visibility = View.VISIBLE
                            Glide.with(requireContext()).load(R.drawable.turn_on).into(ivDevice)
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
                                    requireContext(),
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
                            Glide.with(requireContext()).load(R.drawable.searching).into(ivDeviceScanning)
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
            viewModel.viewEffect.observe(viewLifecycleOwner, Observer { viewEffect: DeviceViewEffect? ->
                if (viewEffect is DeviceViewEffect.SessionRedirect) {
                    launchSessionScreen(
                        viewEffect.treatmentLength,
                        viewEffect.protocolFrequency,
                        viewEffect.sonalId
                    )
                }
            })
            dashBoardViewModel.data.observe(requireActivity()) { dashboardViewState: DashboardViewState? ->
                Timber.i("DEVICE_DASHBOARD :: onChanged: received freshObject")
                if (dashboardViewState != null) {
                    if (dashboardViewState is Connect) {
                        viewModel.processEvent(DeviceViewEvent.Connected)
                    } else if (dashboardViewState is Disconnect) {
                        viewModel.processEvent(DeviceViewEvent.Disconnected)
                    }
                }
            }
        }
    }

    private fun setUserDetail(user: UserInfo) {
        // DONE Which name need to display
        //TODO initials on image
//        if (TextUtils.isEmpty(user.imageThumbnailUrl)) {
//            val strArray = user.name?.split(" ")?.toTypedArray() ?: arrayOf()
//            val builder = StringBuilder()
//            if (strArray.isNotEmpty()) {
//                builder.append(strArray[0], 0, 1)
//            }
//            if (strArray.size > 1) {
//                builder.append(strArray[1], 0, 1)
//            }
//        }
    }

    private fun launchHowToActivity() {
        startActivity(HowToActivity.newIntent(requireContext()))
    }

    private fun launchPairingSuccessfulDialog() {
        val builder = MaterialAlertDialogBuilder(requireContext(), R.style.PopUp)
        val viewGroup = view?.findViewById<ViewGroup>(android.R.id.content)
        val dialogView =
            LayoutInflater.from(context).inflate(R.layout.dialog_popup, viewGroup, false)
        val tvTitle = dialogView.findViewById<TextView>(R.id.tv_title)
        val tvContent = dialogView.findViewById<TextView>(R.id.tv_content)
        val btnPrimary = dialogView.findViewById<Button>(R.id.btn_primary)
        tvTitle.setText(R.string.pairing_successful)
        tvContent.setText(R.string.pairing_successful_description)
        btnPrimary.setText(R.string.proceed_to_session)
        builder.setView(dialogView)
        val ad = builder.create()
        btnPrimary.setOnClickListener {
            viewModel.processEvent(DeviceViewEvent.StartSessionClicked)
        }
        ad.show()
    }

    private fun launchSessionScreen(
        treatmentLength: String,
        protocolFrequency: String,
        sonalId: String
    ) {
        if (TextUtils.isEmpty(treatmentLength) || TextUtils.isEmpty(protocolFrequency)) {
            requireActivity().toast("Treatment data not available.")
        }
        startActivity(SessionActivity.newIntent(
            requireContext(), treatmentLength, protocolFrequency, sonalId
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
                override fun onCharacterises(value: String?) {}
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
                        requireActivity().toast("No device found.")
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
            Toast.makeText(requireActivity(), "Please turn on Bluetooth first", Toast.LENGTH_LONG)
                .show()
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
            val permissionCheck = ContextCompat.checkSelfPermission(requireActivity(), permission)
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted(permission)
            } else {
                permissionDeniedList.add(permission)
            }
        }
        if (permissionDeniedList.isNotEmpty()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
                AlertDialog.Builder(requireActivity())
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
                            requireActivity(),
                            deniedPermissions,
                            REQUEST_CODE_PERMISSION_LOCATION
                        )
                    }
                    .setCancelable(false)
                    .show()
            } else {
                val deniedPermissions = permissionDeniedList.toTypedArray()
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    deniedPermissions,
                    REQUEST_CODE_PERMISSION_BLUETOOTH
                )
            }
        }
    }

    private fun onPermissionGranted(permission: String) {
        when (permission) {
            Manifest.permission.ACCESS_FINE_LOCATION -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
                AlertDialog.Builder(requireActivity())
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
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
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

        fun newInstance(): DeviceFragment {
            return DeviceFragment()
        }
    }

}