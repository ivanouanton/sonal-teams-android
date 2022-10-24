package com.waveneuro.ui.dashboard.view_client;

import static com.waveneuro.utils.DateUtil.PATTERN_RFC1123;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.waveneuro.R;
import com.waveneuro.injection.component.DaggerFragmentComponent;
import com.waveneuro.injection.component.FragmentComponent;
import com.waveneuro.injection.module.FragmentModule;
import com.waveneuro.ui.base.BaseActivity;
import com.waveneuro.ui.dashboard.HomeActivity;
import com.waveneuro.ui.dashboard.device.DeviceFragment;
import com.waveneuro.ui.dashboard.edit_client.EditClientBottomSheet;
import com.waveneuro.ui.dashboard.edit_client.EditClientViewModel;
import com.waveneuro.ui.dashboard.home.HomeFragment;
import com.waveneuro.ui.dashboard.home.HomeViewModel;
import com.waveneuro.ui.device.MyDeviceCommand;
import com.waveneuro.ui.session.history.Session;
import com.waveneuro.ui.session.history.SessionHistoryCommand;
import com.waveneuro.ui.session.session.SessionCommand;
import com.waveneuro.utils.DateUtil;

import javax.inject.Inject;

public class ViewClientBottomSheet extends BottomSheetDialogFragment {

    protected FragmentComponent fragmentComponent;


    @Inject
    SessionHistoryCommand sessionHistoryCommand;

    @Inject
    SessionCommand sessionCommand;

    private int id;
    private String firstName;
    private String lastName;
    private String dob;
    private boolean isMale;
    private String email;
    private String username;
    private String organization;
    private boolean tos;

    TextView tvEdit;
    TextView tvName;
    TextView tvDob;
    TextView tvSex;
    TextView tvEmail;
    TextView tvUsername;
    TextView tvOrganization;
    TextView tvTos;
    TextView tvViewHistory;
    MaterialButton btnStartSession;

    EditClientViewModel.OnClientUpdated listener;

    public static ViewClientBottomSheet newInstance(EditClientViewModel.OnClientUpdated listener, int id, String name, String lastName, String dob, boolean sex, String email, String username, String organization, boolean tos) {
        ViewClientBottomSheet viewClientBottomSheet = new ViewClientBottomSheet();
        viewClientBottomSheet.id = id;
        viewClientBottomSheet.firstName = name;
        viewClientBottomSheet.lastName = lastName;
        viewClientBottomSheet.dob = dob;
        viewClientBottomSheet.isMale = sex;
        viewClientBottomSheet.email = email;
        viewClientBottomSheet.username = username;
        viewClientBottomSheet.organization = organization;
        viewClientBottomSheet.tos = tos;
        viewClientBottomSheet.listener = listener;
        return viewClientBottomSheet;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_view_client, container,
                false);

        tvEdit = view.findViewById(R.id.tv_edit_client);
        tvName = view.findViewById(R.id.tv_clients_name);
        tvDob = view.findViewById(R.id.tv_dob_value);
        tvSex = view.findViewById(R.id.tv_sab_value);
        tvEmail = view.findViewById(R.id.tv_email_value);
        tvUsername = view.findViewById(R.id.tv_username_value);
        tvOrganization = view.findViewById(R.id.tv_organization_value);
        tvTos = view.findViewById(R.id.tv_tos_status_value);
        tvViewHistory = view.findViewById(R.id.tv_view_history);
        btnStartSession = view.findViewById(R.id.btn_start_session);

        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        fragmentComponent = DaggerFragmentComponent.builder()
                .activityComponent(((BaseActivity) getActivity()).activityComponent())
                .fragmentModule(new FragmentModule(this))
                .build();

        fragmentComponent.inject(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvName.setText(firstName + " " + lastName);
        tvDob.setText(DateUtil.parseDate(dob, PATTERN_RFC1123, "MM/dd/YYYY"));
        tvSex.setText(isMale?"Male":"Female");
        tvEmail.setText(email);
        tvUsername.setText(username);
        tvOrganization.setText(organization);
        tvTos.setText(tos?"Signed":"Not Signed");
        tvEdit.setOnClickListener(v -> editClient());
        tvViewHistory.setOnClickListener(v -> sessionHistoryCommand.navigate(String.valueOf(id), firstName + " " + lastName));
        btnStartSession.setOnClickListener(v -> {
            dismiss();
            ((HomeActivity)requireActivity()).addFragment(R.id.fr_home, DeviceFragment.newInstance());
        });
    }

    private void editClient(){
        EditClientBottomSheet editClientBottomSheet = EditClientBottomSheet.newInstance(listener, id,
                firstName, lastName, dob, isMale, email, username, organization, tos
        );
        editClientBottomSheet.show(getParentFragmentManager(), "");
        dismiss();
    }

    @Override
    public int getTheme() {
        return R.style.CustomBottomSheetDialog;
    }
}
