package com.waveneuro.ui.dashboard.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.waveneuro.R
import com.waveneuro.databinding.FragmentHomeBinding
import com.waveneuro.injection.component.DaggerFragmentComponent
import com.waveneuro.injection.module.FragmentModule
import com.waveneuro.ui.base.BaseActivity
import com.waveneuro.ui.base.BaseFragment
import com.waveneuro.ui.dashboard.home.HomeClientsViewState.Success
import com.waveneuro.ui.dashboard.home.adapter.ClientListAdapter
import com.waveneuro.ui.dashboard.home.adapter.model.PatientItem
import com.waveneuro.ui.dashboard.web.WebCommand
import com.waveneuro.ui.session.session.SessionCommand
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragment : BaseFragment() { // OnClientUpdated, OnFiltersChangedListener

    @Inject
    lateinit var sessionCommand: SessionCommand
    @Inject
    lateinit var webCommand: WebCommand
    @Inject
    lateinit var viewModel: HomeViewModel

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: ClientListAdapter

    private val currentList = mutableListOf<PatientItem>()

//    private var dashBoardViewModel: DashBoardViewModel? = null
//
//    var filtersBottomSheet: FiltersBottomSheet? = null
//
//    override fun onClientUpdated() {
//        homeViewModel.processEvent(HomeViewEvent.Start(1, "", null))
//    }
//
//    override fun onFiltersChanged(ids: Array<Int>) {
//        homeViewModel.filters.addAll(ids)
//        homeViewModel.getNewClients(binding.etSearch.text.toString())
//        homeViewModel.processEvent(
//            HomeViewEvent.Start(
//                homeViewModel.page.value!!, binding.etSearch.text.toString(), homeViewModel.filters
//            )
//        )
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentComponent = DaggerFragmentComponent.builder()
            .activityComponent((activity as BaseActivity?)?.activityComponent())
            .fragmentModule(FragmentModule(this))
            .build()
        fragmentComponent.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        setView()
        setAdapter()

        viewModel.processEvent(HomeViewEvent.Start)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserver()
        binding.spinKit.visibility = View.INVISIBLE
//        binding.tilSearch.setEndIconOnClickListener { v: View? ->
//            if (filtersBottomSheet != null) {
//                filtersBottomSheet!!.show(childFragmentManager, "")
//            }
//        }
    }

    private fun setView() {
        with(binding) {
            srlClients.setOnRefreshListener {
                viewModel.processEvent(HomeViewEvent.NewQuery(etSearch.text.toString()))
                srlClients.isRefreshing = false
            }
            etSearch.onTextChanged {
                viewModel.processEvent(HomeViewEvent.NewQuery(it))
            }
        }
    }

    private fun setAdapter() {
        adapter = ClientListAdapter(
            requireContext(),
            ::onItemClick,
            ::onStartSessionClick,
            ::moreRequest
        )
        binding.rvClients.adapter = adapter
        adapter.submitList(currentList)
    }

    private fun setObserver() {
//        viewModel.userData.observe(this.viewLifecycleOwner, homeUserViewStateObserver)
        viewModel.clientsData.observe(this.viewLifecycleOwner, homeClientsViewStateObserver)
        viewModel.viewEffect.observe(this.viewLifecycleOwner, homeViewEffectObserver)
        viewModel.protocolData.observe(this.viewLifecycleOwner, homeProtocolDataObserver)
//        dashBoardViewModel!!.data.observe(requireActivity()) { dashboardViewState: DashboardViewState? ->
//            Timber.i("DEVICE_DASHBOARD :: onChanged: received freshObject")
//            if (dashboardViewState != null) {
//                if (dashboardViewState is Connect) {3
//                    homeViewModel.processEvent(HomeViewEvent.DeviceConnected)
//                } else if (dashboardViewState is Disconnect) {
//                    homeViewModel.processEvent(HomeViewEvent.DeviceDisconnected)
//                }
//            }
//        }
//        viewModel.page.observe(this.viewLifecycleOwner, pageObserver)
    }

//    var homeUserViewStateObserver = Observer { viewState: HomeUserViewState? ->
//        if (viewState is HomeUserViewState.Success) {
//            val success = viewState
//        }
//    }
    private val homeClientsViewStateObserver = Observer { viewState: HomeClientsViewState? ->
        if (viewState is Success) {
            updateList(viewState.patientList)

            if (viewState.patientList.isEmpty()) {
                binding.tvEmptyResult.isVisible = true
                binding.rvClients.isVisible = false
            } else {
                binding.tvEmptyResult.isVisible = false
                binding.rvClients.isVisible = true
            }
        } else {
//            when (viewState) {
//                is PatientSuccess -> {
//                    val patient = viewState.item
//                    val viewClientBottomSheet =
//                        ViewClientBottomSheet.newInstance(this, patient, viewState.treatmentDataPresent)
//                    viewClientBottomSheet.show(childFragmentManager, "")
//                }
//                is OrganizationSuccess -> {
//                    filtersBottomSheet = FiltersBottomSheet.newInstance(viewState.item, homeViewModel.filters.toTypedArray())
//                    filtersBottomSheet?.setListener(this)
//                }
//                is PatientSessionSuccess -> {
//                    //TODO uncomment
//                    //            ((HomeActivity) requireActivity()).addFragment(R.id.fr_home, DeviceFragment.newInstance());
//                }
//                else -> {}
//            }
        }
    }

    private val homeViewEffectObserver = Observer { viewEffect: HomeViewEffect? ->
        if (viewEffect is HomeViewEffect.BackRedirect) {
        } else if (viewEffect is HomeViewEffect.SessionRedirect) {
            val (treatmentLength, protocolFrequency, sonalId) = viewEffect
            launchSessionScreen(
                treatmentLength, protocolFrequency,
                sonalId
            )
        }
    }
    private val homeProtocolDataObserver = Observer { protocol: HomeProtocolViewState? ->
        if (protocol is HomeProtocolViewState.Loading) {
            if (protocol.loading) {
                binding.pbProgress.visibility = View.VISIBLE
            } else {
                binding.pbProgress.visibility = View.INVISIBLE
            }
        }
    }
//    private val pageObserver: Observer<Int> = Observer { newPage ->
//        if (newPage != null && newPage != 1) {
//            viewModel.processEvent(
//                HomeViewEvent.Start(
//                    newPage,
//                    binding.etSearch.text.toString(),
//                    viewModel.filters
//                )
//            )
//        }
//    }

    private fun onItemClick(patient: PatientItem) {
        viewModel.getClientWithId(patient.id)
    }

    private fun onStartSessionClick(patient: PatientItem) {
        viewModel.startSessionForClientWithId(patient.id)
    }

    private fun launchSessionScreen(
        treatmentLength: String,
        protocolFrequency: String,
        sonalId: String
    ) {
        if (TextUtils.isEmpty(treatmentLength) || TextUtils.isEmpty(protocolFrequency)) {
            Toast.makeText(requireActivity(), "Treatment data not available.", Toast.LENGTH_SHORT)
                .show()
            return
        }
//        sessionCommand.navigate(treatmentLength, protocolFrequency, sonalId)
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
                viewModel.processEvent(HomeViewEvent.StartSessionClicked)
            }
        }
    }

    private fun checkGPSIsOpen(): Boolean {
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
                ?: return false
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_PERMISSION_LOCATION -> if (grantResults.size > 0) {
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

    private fun moreRequest() {
        viewModel.getMoreClients(binding.etSearch.text.toString())
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateList(list: List<PatientItem>) {
        currentList.clear()
        currentList.addAll(list)
        adapter.notifyDataSetChanged()
    }

    private fun TextInputEditText.onTextChanged(onTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewLifecycleOwner.lifecycleScope.launch {
                    onTextChanged.invoke(s.toString())
                    delay(DELAY)
                }
            }

            override fun afterTextChanged(editable: Editable?) {}
        })
    }

    companion object {
        private const val REQUEST_CODE_OPEN_GPS = 1
        private const val REQUEST_CODE_PERMISSION_LOCATION = 2

        private const val DELAY = 1000L

        const val BUBBLE_NO_INTERNET = 0
        const val BUBBLE_DEVICE_NOT_CONNECTED = 1
        const val BUBBLE_LOW_BATTERY = 2
        const val BUBBLE_LOW_BATTERY_PLUG = 3
        const val BUBBLE_LOW_BATTERY_KEEP_PLUG = 4

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}