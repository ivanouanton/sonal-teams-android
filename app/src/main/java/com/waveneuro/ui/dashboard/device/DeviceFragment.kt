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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.ap.ble.BluetoothManager
import com.ap.ble.BluetoothManager.DeviceConnectionCallback
import com.ap.ble.callback.BleScanCallback
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.waveneuro.R
import com.waveneuro.data.model.entity.BleDevice
import com.waveneuro.data.model.entity.User
import com.waveneuro.databinding.FragmentDeviceBinding
import com.waveneuro.injection.component.DaggerFragmentComponent
import com.waveneuro.injection.module.FragmentModule
import com.waveneuro.ui.adapter.DeviceAdapter
import com.waveneuro.ui.adapter.device.DeviceDelegate
import com.waveneuro.ui.adapter.device.OnDeviceItemClickListener
import com.waveneuro.ui.base.BaseActivity
import com.waveneuro.ui.base.BaseListFragment
import com.waveneuro.ui.dashboard.DashBoardViewModel
import com.waveneuro.ui.dashboard.DashboardCommand
import com.waveneuro.ui.dashboard.DashboardViewEvent
import com.waveneuro.ui.dashboard.DashboardViewState
import com.waveneuro.ui.dashboard.DashboardViewState.Connect
import com.waveneuro.ui.dashboard.DashboardViewState.Disconnect
import com.waveneuro.ui.dashboard.device.DeviceViewEvent.LocateDeviceNextClicked
import com.waveneuro.ui.dashboard.device.DeviceViewEvent.NoDeviceFound
import com.waveneuro.ui.dashboard.device.DeviceViewState.*
import com.waveneuro.ui.session.how_to.HowToCommand
import com.waveneuro.ui.session.session.SessionCommand
import com.waveneuro.utils.ext.toast
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class DeviceFragment : BaseListFragment(), OnDeviceItemClickListener {

    @Inject
    lateinit var sessionCommand: SessionCommand
    @Inject
    lateinit var dashboardCommand: DashboardCommand
    @Inject
    lateinit var howToCommand: HowToCommand
    @Inject
    lateinit var deviceViewModel: DeviceViewModel

    private lateinit var binding: FragmentDeviceBinding
    private lateinit var deviceAdapter: DeviceAdapter

    private var isSearching = false
    private var dashBoardViewModel: DashBoardViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentComponent = DaggerFragmentComponent.builder()
            .activityComponent((activity as BaseActivity?)?.activityComponent())
            .fragmentModule(FragmentModule(this))
            .build()
        fragmentComponent.inject(this)
        super.onCreate(savedInstanceState)
        dashBoardViewModel = ViewModelProviders.of(activity!!).get(
            DashBoardViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeviceBinding.inflate(layoutInflater, container, false)
        setView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserver()
        deviceViewModel.processEvent(DeviceViewEvent.Start)
        if (!deviceViewModel.onboardingDisplayed) {
            howToCommand.navigate()
        }
    }

    private fun setView() {
        with(binding) {
            rvDeviceAvailable.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL, false
            )
            deviceAdapter = DeviceAdapter.Builder()
                .setList(ArrayList())
                .setDelegate(DeviceDelegate())
                .setOnNoteListener { data: BleDevice -> onClickDevice(data) }
                .create()
            rvDeviceAvailable.adapter = deviceAdapter
            initializeList(null, null, deviceAdapter)
            operatingAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate)
            operatingAnim?.interpolator = LinearInterpolator()
            tvFirstTime.paintFlags = tvFirstTime.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            btnLocateDevice.setOnClickListener {
                checkPermissions()
                deviceViewModel.processEvent(DeviceViewEvent.LocateDevice)
            }
            ivBack.setOnClickListener {
                if (isSearching) {
                    deviceViewModel.processEvent(DeviceViewEvent.Start)
                } else {
                    dashboardCommand.navigate()
                }
            }
            tvFirstTime.setOnClickListener {
                launchHowToActivity()
            }
        }
    }

    private fun setObserver() {
        deviceViewModel.data.removeObservers(viewLifecycleOwner)
        deviceViewModel.viewEffect.removeObservers(viewLifecycleOwner)
        deviceViewModel.data.observe(viewLifecycleOwner, notesViewStateObserver)
        deviceViewModel.viewEffect.observe(viewLifecycleOwner, notesViewEffectObserver)
        dashBoardViewModel?.data?.observe(requireActivity()) { dashboardViewState: DashboardViewState? ->
            Timber.i("DEVICE_DASHBOARD :: onChanged: received freshObject")
            if (dashboardViewState != null) {
                if (dashboardViewState is Connect) {
                    deviceViewModel.processEvent(DeviceViewEvent.Connected)
                } else if (dashboardViewState is Disconnect) {
                    deviceViewModel.processEvent(DeviceViewEvent.Disconnected)
                }
            }
        }
    }

    private val notesViewStateObserver = Observer { viewState: DeviceViewState? ->
        isSearching = false
//        if (lifecycle.currentState != Lifecycle.State.RESUMED) {
//            return
//        }
        when (viewState) {
            is Success -> {
                binding.llLocateDevice.visibility = View.GONE
                binding.cvDeviceAvailable.visibility = View.VISIBLE
                binding.llContainerDevice.visibility = View.GONE
            }
            is Failure -> {}
            is Loading -> {
                if (viewState.loading) {
                    displayListProgress()
                } else {
                    removeListProgress()
                }
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
    }

    private fun setUserDetail(user: User) {
        // DONE Which name need to display
        if (TextUtils.isEmpty(user.imageThumbnailUrl)) {
            val strArray = user.name!!.split(" ").toTypedArray()
            val builder = StringBuilder()
            if (strArray.isNotEmpty()) {
                builder.append(strArray[0], 0, 1)
            }
            if (strArray.size > 1) {
                builder.append(strArray[1], 0, 1)
            }
        }
    }

    private fun launchHowToActivity() {
        howToCommand.navigate()
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
            deviceViewModel.processEvent(DeviceViewEvent.StartSessionClicked)
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
        sessionCommand.navigate(treatmentLength, protocolFrequency, sonalId)
    }

    private fun connectToDevice(bleDevice: BleDevice) {
        BluetoothManager.getInstance()
            .getDeviceName(bleDevice.mac, bleDevice.name, object : DeviceConnectionCallback {
                override fun onConnected(bleDevice: com.ap.ble.data.BleDevice) {
                    if (requireActivity() is BaseActivity) {
                        (activity as BaseActivity?)?.removeWait()
                    }
                    deviceViewModel.processEvent(DeviceViewEvent.Connected)
                    deviceViewModel.setDeviceId(bleDevice.name)
                    dashBoardViewModel?.processEvent(
                        DashboardViewEvent.Connected(
                            BleDevice(bleDevice)
                        )
                    )
                }

                override fun onCharacterises(value: String) {}
                override fun onDisconnected() {
                    if (requireActivity() is BaseActivity) {
                        (activity as BaseActivity?)?.removeWait()
                    }
                    deviceViewModel.processEvent(DeviceViewEvent.Disconnected)
                    dashBoardViewModel?.processEvent(DashboardViewEvent.Disconnected)
                }
            })
    }

    private var operatingAnim: Animation? = null
    private fun locateDevice() {
        BluetoothManager.getInstance().deviceList(object : BleScanCallback() {
            override fun onScanFinished(scanResultList: List<com.ap.ble.data.BleDevice>) {
                Log.e("DEVICE_LIST", "" + scanResultList.toTypedArray().contentToString())
                if (scanResultList.isNotEmpty()) {
                    val bleDevices: MutableList<BleDevice> = ArrayList()
                    for (i in scanResultList.indices) {
                        bleDevices.add(BleDevice(scanResultList[i]))
                    }
                    loadBleDevices(bleDevices)
                    deviceViewModel.processEvent(DeviceViewEvent.Searched)
                } else {
                    deviceViewModel.processEvent(NoDeviceFound)
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
            }
        })
    }

    private val notesViewEffectObserver: Observer<DeviceViewEffect?> = Observer { viewEffect: DeviceViewEffect? ->
        if (viewEffect is DeviceViewEffect.SessionRedirect) {
            val (treatmentLength, protocolFrequency, sonalId) = viewEffect
            launchSessionScreen(
                treatmentLength, protocolFrequency,
                sonalId
            )
        }
    }

    private fun loadBleDevices(item: List<BleDevice>) {
        addAll(item)
    }

    override fun onClickDevice(data: BleDevice) {
        if (requireActivity() is BaseActivity) {
            (activity as BaseActivity?)!!.displayWait()
        }
        deviceViewModel.processEvent(DeviceViewEvent.DeviceClicked(data))
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
                deviceViewModel.processEvent(LocateDeviceNextClicked)
            }
            Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN -> {
                Timber.e("PERMISSION_GRANTED :: %s", permission)
                deviceViewModel.processEvent(LocateDeviceNextClicked)
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