package com.waveneuro.ui.dashboard.edit_client;

import static com.waveneuro.utils.DateUtil.PATTERN_RFC1123;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.waveneuro.R;
import com.waveneuro.domain.usecase.patient.GetPatientUseCase;
import com.waveneuro.domain.usecase.patient.UpdatePatientUseCase;
import com.waveneuro.injection.component.DaggerFragmentComponent;
import com.waveneuro.injection.component.FragmentComponent;
import com.waveneuro.injection.module.FragmentModule;
import com.waveneuro.ui.base.BaseActivity;
import com.waveneuro.ui.dashboard.home.HomeClientsViewState;
import com.waveneuro.ui.dashboard.home.HomeFragment;
import com.waveneuro.ui.dashboard.home.HomeUserViewState;
import com.waveneuro.utils.DateUtil;

import java.util.Date;

import javax.inject.Inject;

import butterknife.OnClick;

public class EditClientBottomSheet extends BottomSheetDialogFragment {

    protected FragmentComponent fragmentComponent;

    private int id;
    private String firstName;
    private String lastName;
    private String dob;
    private boolean isMale;
    private String email;
    private String username;
    private String organization;
    private boolean tos;

    TextInputEditText etFirstName;
    TextInputEditText etLastName;
    TextInputEditText etBirthdate;
    TextInputEditText etEmail;

    RadioButton rbMale;
    RadioButton rbFemale;

    @Inject
    EditClientViewModel editClientViewModel;

    EditClientViewModel.OnClientUpdated listener;


    public static EditClientBottomSheet newInstance(EditClientViewModel.OnClientUpdated listener, int id, String name, String lastName, String dob, boolean sex, String email, String username, String organization, boolean tos) {
        EditClientBottomSheet editClientBottomSheet = new EditClientBottomSheet();
        editClientBottomSheet.id = id;
        editClientBottomSheet.firstName = name;
        editClientBottomSheet.lastName = lastName;
        editClientBottomSheet.dob = dob;
        editClientBottomSheet.isMale = sex;
        editClientBottomSheet.email = email;
        editClientBottomSheet.username = username;
        editClientBottomSheet.organization = organization;
        editClientBottomSheet.tos = tos;
        editClientBottomSheet.listener = listener;
        return editClientBottomSheet;
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
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_edit_client, container,
                false);
        etFirstName = view.findViewById(R.id.tip_firstName);
        etLastName = view.findViewById(R.id.tip_lastName);
        etBirthdate = view.findViewById(R.id.tip_birthday);
        etEmail = view.findViewById(R.id.tip_email);
        rbMale = view.findViewById(R.id.rb_Male);
        rbFemale = view.findViewById(R.id.rb_Female);

        view.findViewById(R.id.btn_save_changes).setOnClickListener(v -> onClickSave());
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etFirstName.setText(firstName);
        etLastName.setText(lastName);
        etBirthdate.setText(DateUtil.parseDate(dob, PATTERN_RFC1123, "MM/dd/YYYY"));
        etBirthdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    openDatePicker();
                }
            }
        });
        etBirthdate.setOnClickListener(v -> openDatePicker());
        etEmail.setText(email);
        if (isMale) {
            rbMale.setChecked(true);
        } else {
            rbFemale.setChecked(true);
        }

        editClientViewModel.getDataPatientsLive().observe(this.getViewLifecycleOwner(), viewStateObserver);

    }

    Observer<HomeClientsViewState> viewStateObserver = viewState -> {
        if (viewState instanceof HomeClientsViewState.PatientSuccess) {
            dismiss();
            listener.onClientUpdated();
        }
    };

    private void openDatePicker(){
        Date date = DateUtil.parseDate(dob, DateUtil.PATTERN_RFC1123);
        MaterialDatePicker datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(date != null ? date.getTime() : MaterialDatePicker.todayInUtcMilliseconds())
                .setTitleText("Select Birth date").build();

        datePicker.show(getChildFragmentManager(), datePicker.toString());
        datePicker.addOnPositiveButtonClickListener(selection -> {
            String dateString = DateUtil.formatDate((new Date((Long) selection)), "MM/dd/YYYY");
            etBirthdate.setText(dateString);
        });
    }


    @Override
    public int getTheme() {
        return R.style.CustomBottomSheetDialog;
    }

    public void onClickSave() {
        editClientViewModel.updateClient(id,
                etFirstName.getText().toString(),
                etLastName.getText().toString(),
                DateUtil.parseDate(etBirthdate.getText().toString(), "MM/dd/yyyy", PATTERN_RFC1123),
                rbMale.isChecked(),
                etEmail.getText().toString()
                );
    }
}
