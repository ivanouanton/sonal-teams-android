package com.waveneuro.ui.dashboard.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.waveneuro.BuildConfig;
import com.waveneuro.R;
import com.waveneuro.injection.component.DaggerFragmentComponent;
import com.waveneuro.injection.module.FragmentModule;
import com.waveneuro.ui.base.BaseActivity;
import com.waveneuro.ui.base.BaseFragment;
import com.waveneuro.ui.dashboard.DashboardViewEvent;
import com.waveneuro.ui.dashboard.HomeActivity;
import com.waveneuro.ui.dashboard.more.WebCommand;
import com.waveneuro.ui.session.session.SessionCommand;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoreFragment extends BaseFragment {

    @BindView(R.id.tv_app_version)
    TextView tvAppVersion;

//    @OnClick(R.id.btnLogOut)
//    public void onClickLogout() {
//        ((HomeActivity)requireActivity()).dashBoardViewModel.processEvent(DashboardViewEvent.LogoutClicked.INSTANCE);
//    }
//
//    @OnClick(R.id.llProfile)
//    public void onClickProfile() {
//        ((HomeActivity)requireActivity()).dashBoardViewModel.processEvent(DashboardViewEvent.AccountClicked.INSTANCE);
//    }
//
//    @OnClick(R.id.ll_device_history)
//    public void onClickDeviceHistory() {
//        ((HomeActivity)requireActivity()).dashBoardViewModel.processEvent(DashboardViewEvent.DeviceHistoryClicked.INSTANCE);
//    }

    @OnClick(R.id.llHelp)
    public void onClickHelp() {
        webCommand.navigate(WebCommand.PAGE_SUPPORT);
    }

    @Inject
    SessionCommand sessionCommand;
    @Inject
    WebCommand webCommand;

    @Inject
    HomeViewModel homeViewModel;

    public MoreFragment() {
    }

    public static MoreFragment newInstance() {
        return new MoreFragment();
    }

    View view = null;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null)
            return view;
        view = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvAppVersion.setText("App version: " + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")");
    }
}