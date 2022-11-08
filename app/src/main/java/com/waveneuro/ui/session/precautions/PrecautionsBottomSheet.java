package com.waveneuro.ui.session.precautions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.waveneuro.R;
import com.waveneuro.injection.component.DaggerFragmentComponent;
import com.waveneuro.injection.component.FragmentComponent;
import com.waveneuro.injection.module.FragmentModule;
import com.waveneuro.ui.base.BaseActivity;

public class PrecautionsBottomSheet extends BottomSheetDialogFragment {

    protected FragmentComponent fragmentComponent;

    public static PrecautionsBottomSheet newInstance() {
        PrecautionsBottomSheet editClientBottomSheet = new PrecautionsBottomSheet();
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

        View view = inflater.inflate(R.layout.dialog_precautions, container,
                false);

        return view;

    }


    @Override
    public int getTheme() {
        return R.style.CustomBottomSheetDialog;
    }

}
