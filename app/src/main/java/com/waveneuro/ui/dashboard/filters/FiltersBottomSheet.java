package com.waveneuro.ui.dashboard.filters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.waveneuro.R;
import com.waveneuro.data.model.response.patient.PatientListResponse;
import com.waveneuro.injection.component.DaggerFragmentComponent;
import com.waveneuro.injection.component.FragmentComponent;
import com.waveneuro.injection.module.FragmentModule;
import com.waveneuro.ui.base.BaseActivity;
import com.waveneuro.ui.dashboard.home.OnFiltersChangedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

public class FiltersBottomSheet extends BottomSheetDialogFragment implements OrgsListAdapter.OnItemClickListener {

    @BindView(R.id.rvOrganizations)
    RecyclerView rvOrganizations;

    protected FragmentComponent fragmentComponent;

    List<PatientListResponse.Patient.Organization> orgs = new ArrayList<>();

    List<Integer> selectedIds = new ArrayList<>();

    protected OrgsListAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;

    private OnFiltersChangedListener listener;

    public void setListener(OnFiltersChangedListener listener){
        this.listener = listener;
    }


    public static FiltersBottomSheet newInstance(List<PatientListResponse.Patient.Organization> organizations, Integer[] selected) {
        FiltersBottomSheet viewClientBottomSheet = new FiltersBottomSheet(organizations, selected);
        return viewClientBottomSheet;
    }

    public FiltersBottomSheet() {
    }

    public FiltersBottomSheet(List<PatientListResponse.Patient.Organization> orgs, Integer[] selected) {
        this.orgs = orgs;
        selectedIds.clear();
        if (selected != null && selected.length > 0) {
            selectedIds.addAll(Arrays.asList(selected));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_filters, container,
                false);

        rvOrganizations = view.findViewById(R.id.rvOrganizations);

        mAdapter = new OrgsListAdapter(orgs, selectedIds);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvOrganizations.setLayoutManager(mLayoutManager);
        rvOrganizations.setAdapter(mAdapter);
        mAdapter.setListener(this);

        view.findViewById(R.id.tv_clear).setOnClickListener(v -> {
            selectedIds.clear();
            mAdapter.notifyDataSetChanged();
        });

        view.findViewById(R.id.btn_apply).setOnClickListener(v -> {
            Integer[] a = new Integer[selectedIds.size()];
            listener.onFiltersChanged(selectedIds.toArray(a));
            dismiss();
        });

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
    public int getTheme() {
        return R.style.CustomBottomSheetDialog;
    }

    @Override
    public void onSelected(int id) {
        selectedIds.add(id);
    }

    @Override
    public void onDeselected(int id) {
        selectedIds.remove((Integer) id);
    }
}
