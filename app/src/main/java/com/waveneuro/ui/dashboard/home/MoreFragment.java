package com.waveneuro.ui.dashboard.home;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    @OnClick(R.id.btnLogOut)
    public void onClickLogout() {
        ((HomeActivity)requireActivity()).dashBoardViewModel.processEvent(new DashboardViewEvent.LogoutClicked());
    }

    @OnClick(R.id.llProfile)
    public void onClickProfile() {
        ((HomeActivity)requireActivity()).dashBoardViewModel.processEvent(new DashboardViewEvent.AccountClicked());
    }

    @OnClick(R.id.ll_device_history)
    public void onClickDeviceHistory() {
        ((HomeActivity)requireActivity()).dashBoardViewModel.processEvent(new DashboardViewEvent.DeviceHistoryClicked());
    }

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

    private MoreFragment() {
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
        try {
            PackageInfo pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            String version = pInfo.versionName;
            tvAppVersion.setText("App version: " + version);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}