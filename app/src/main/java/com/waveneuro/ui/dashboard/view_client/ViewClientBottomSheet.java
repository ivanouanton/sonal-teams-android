package com.waveneuro.ui.dashboard.view_client;

import static com.waveneuro.utils.DateUtil.PATTERN_RFC1123;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
    private int tosStatus;

    private boolean treatmentDataPresent;

    TextView tvEdit;
    TextView tvName;
    TextView tvDob;
    TextView tvSex;
    TextView tvEmail;
    TextView tvUsername;
    TextView tvOrganization;

    TextView tvTosSignedLabel;
    ImageView tvTosSignedIcon;

    TextView tvTosNotSignedLabel;
    ImageView tvTosNotSignedIcon;

    TextView tvTosWaitingLabel;
    ImageView tvTosWaitingIcon;





    TextView tvViewHistory;
    MaterialButton btnStartSession;

    EditClientViewModel.OnClientUpdated listener;

    public static ViewClientBottomSheet newInstance(EditClientViewModel.OnClientUpdated listener, int id, String name, String lastName, String dob, boolean sex, String email, String username, String organization, int tosStatus, boolean treatmentDataPresent) {
        ViewClientBottomSheet viewClientBottomSheet = new ViewClientBottomSheet();
        viewClientBottomSheet.id = id;
        viewClientBottomSheet.firstName = name;
        viewClientBottomSheet.lastName = lastName;
        viewClientBottomSheet.dob = dob;
        viewClientBottomSheet.isMale = sex;
        viewClientBottomSheet.email = email;
        viewClientBottomSheet.username = username;
        viewClientBottomSheet.organization = organization;
        viewClientBottomSheet.tosStatus = tosStatus;
        viewClientBottomSheet.listener = listener;
        viewClientBottomSheet.treatmentDataPresent = treatmentDataPresent;
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

        tvTosSignedLabel = view.findViewById(R.id.tv_tos_status_signed_label);
        tvTosSignedIcon = view.findViewById(R.id.tv_tos_status_signed_icon);

        tvTosNotSignedLabel = view.findViewById(R.id.tv_tos_status_not_signed_label);
        tvTosNotSignedIcon = view.findViewById(R.id.tv_tos_status_not_signed_icon);

        tvTosWaitingLabel = view.findViewById(R.id.tv_tos_status_waiting_label);
        tvTosWaitingIcon = view.findViewById(R.id.tv_tos_status_waiting_icon);

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

        switch (tosStatus) {
            case 0:
            case 3:
                tvTosSignedLabel.setVisibility(View.VISIBLE);
                tvTosSignedIcon.setVisibility(View.VISIBLE);
                break;
            case 2:
                tvTosWaitingLabel.setVisibility(View.VISIBLE);
                tvTosWaitingIcon.setVisibility(View.VISIBLE);
                break;
            case 1:
            default:
                tvTosNotSignedLabel.setVisibility(View.VISIBLE);
                tvTosNotSignedIcon.setVisibility(View.VISIBLE);
        }

        tvEdit.setOnClickListener(v -> editClient());
        tvViewHistory.setOnClickListener(v -> sessionHistoryCommand.navigate(requireActivity(), String.valueOf(id), firstName + " " + lastName, treatmentDataPresent));
        btnStartSession.setOnClickListener(v -> {
            dismiss();
            ((HomeActivity)requireActivity()).addFragment(R.id.fr_home, DeviceFragment.newInstance());
        });
        btnStartSession.setEnabled(treatmentDataPresent);
    }

    private void editClient(){
        EditClientBottomSheet editClientBottomSheet = EditClientBottomSheet.newInstance(listener, id,
                firstName, lastName, dob, isMale, email
        );
        editClientBottomSheet.show(getParentFragmentManager(), "");
        dismiss();
    }

    @Override
    public int getTheme() {
        return R.style.CustomBottomSheetDialog;
    }
}
