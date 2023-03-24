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
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.waveneuro.R
import com.waveneuro.databinding.FragmentHomeBinding
import com.waveneuro.ui.base.fragment.BaseViewModelFragment
import com.waveneuro.ui.dashboard.device.DeviceActivity
import com.waveneuro.ui.dashboard.home.HomeClientsViewState.*
import com.waveneuro.ui.dashboard.home.adapter.ClientListAdapter
import com.waveneuro.ui.dashboard.home.bottom_sheet.filters.FiltersBottomSheet
import com.waveneuro.ui.dashboard.home.bottom_sheet.view_client.ViewClientBottomSheet
import com.waveneuro.ui.dashboard.home.viewmodel.HomeViewModel
import com.waveneuro.ui.dashboard.home.viewmodel.HomeViewModelImpl
import com.waveneuro.ui.model.client.ClientUi
import com.waveneuro.utils.ext.getAppComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : BaseViewModelFragment<FragmentHomeBinding, HomeViewModel>() {

    override val viewModel: HomeViewModelImpl by viewModels {
        getAppComponent().homeViewModelFactory()
    }

    private lateinit var adapter: ClientListAdapter
    private var filtersBottomSheet: FiltersBottomSheet? = null
    private val currentList = mutableListOf<ClientUi>()


    override fun initBinding(container: ViewGroup?): FragmentHomeBinding =
        FragmentHomeBinding.inflate(layoutInflater)

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        setView()
        setAdapter()

        viewModel.processEvent(HomeViewEvent.Start)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserver()
        binding?.spinKit?.visibility = View.INVISIBLE
    }

    private fun setView() {
        binding?.let { binding ->
            with(binding) {
                srlClients.setOnRefreshListener {
                    viewModel.processEvent(HomeViewEvent.NewQuery(etSearch.text.toString()))
                    srlClients.isRefreshing = false
                }
                etSearch.onTextChanged {
                    viewModel.processEvent(HomeViewEvent.NewQuery(it))
                }
                tilSearch.setEndIconOnClickListener {
                    filtersBottomSheet?.show(childFragmentManager, "")
                }
            }
        }
    }

    private fun setAdapter() {
        adapter = ClientListAdapter(requireContext(), ::onItemClick, ::moreRequest)
        binding?.rvClients?.adapter = adapter
        adapter.submitList(currentList)
    }

    private fun setObserver() {
        viewModel.clientsData.observe(this.viewLifecycleOwner, Observer { viewState ->
            when (viewState) {
                is ClientsSuccess -> {
                    updateList(viewState.clientList)

                    if (viewState.clientList.isEmpty()) {
                        binding?.tvEmptyResult?.isVisible = true
                        binding?.rvClients?.isVisible = false
                    } else {
                        binding?.tvEmptyResult?.isVisible = false
                        binding?.rvClients?.isVisible = true
                    }
                }
                is OrganizationsSuccess -> {
                    filtersBottomSheet = viewModel.filters.value?.let { filters ->
                        FiltersBottomSheet
                            .newInstance(viewState.organizationList, filters, ::onFilterChanged)
                    }
                }
                is ClientProtocolSuccess -> {
                    val viewClientBottomSheet = ViewClientBottomSheet.newInstance(
                        ::onStartSession, ::onClientUpdated,
                        viewState.client, viewState.isTreatmentDataPresent
                    )
                    viewClientBottomSheet.show(childFragmentManager, "ViewClient BottomSheet");
                }
            }
        })
        viewModel.viewEffect.observe(this.viewLifecycleOwner, Observer { viewEffect ->
            when (viewEffect) {
                is HomeViewEffect.DeviceRedirect -> launchDeviceScreen()
            }
        })
    }

    private fun launchDeviceScreen() {
        startActivity(DeviceActivity.newIntent(requireContext()))
    }

    private fun onItemClick(client: ClientUi) {
        viewModel.processEvent(HomeViewEvent.OnClientClick(client.id))
    }

    private fun moreRequest() {
        viewModel.getMoreClients(binding?.etSearch?.text.toString())
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateList(list: List<ClientUi>) {
        currentList.clear()
        currentList.addAll(list)
        adapter.notifyDataSetChanged()
    }

    private fun onFilterChanged(ids: List<Int>) {
        viewModel.filters.value = ids
        viewModel.processEvent(HomeViewEvent.NewQuery(binding?.etSearch?.text.toString()))
    }

    private fun onStartSession() {
        viewModel.processEvent(HomeViewEvent.OnStartSessionClick)
    }

    private fun onClientUpdated(fullName: String?) {
        showSuccessUserChangedDialog(fullName)
        viewModel.processEvent(HomeViewEvent.NewQuery(binding?.etSearch?.text.toString()))
    }

    private fun showSuccessUserChangedDialog(fullName: String?) {
        MaterialAlertDialogBuilder(requireContext(), R.style.MaterialDialogGeneral)
            .setTitle(R.string.info_updated)
            .setMessage(getString(R.string.info_updated_description, fullName))
            .setPositiveButton(R.string.ok) { dialog: DialogInterface?, _: Int ->
                dialog?.dismiss()
            }
            .setCancelable(true)
            .show()
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

    private fun onPermissionGranted(permission: String) {
        when (permission) {
            Manifest.permission.ACCESS_FINE_LOCATION -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkGPSIsOpen()) {
                AlertDialog.Builder(requireActivity())
                    .setTitle("Location Services not enabled")
                    .setMessage("Android requires that Location Services to enabled to scan for Bluetooth Low Energy devices.\nPlease enable Location Services in Settings to continue. ")
                    .setNegativeButton(
                        R.string.cancel
                    ) { _: DialogInterface?, _: Int -> }
                    .setPositiveButton(
                        "Setting"
                    ) { _: DialogInterface?, _: Int ->
                        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        startActivityForResult(intent, REQUEST_CODE_OPEN_GPS)
                    }
                    .setCancelable(false)
                    .show()
            } else {
                viewModel.processEvent(HomeViewEvent.OnStartSessionClick)
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
        }
    }

    companion object {
        private const val REQUEST_CODE_OPEN_GPS = 1
        private const val REQUEST_CODE_PERMISSION_LOCATION = 2

        private const val DELAY = 1000L

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

}