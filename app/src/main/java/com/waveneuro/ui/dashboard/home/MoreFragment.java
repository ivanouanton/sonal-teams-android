package com.waveneuro.ui.dashboard.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waveneuro.R;
import com.waveneuro.ui.base.BaseFragment;
import com.waveneuro.ui.dashboard.DashboardViewEvent;
import com.waveneuro.ui.dashboard.HomeActivity;
import com.waveneuro.ui.dashboard.more.WebCommand;
import com.waveneuro.ui.session.session.SessionCommand;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoreFragment extends BaseFragment {

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
        ((HomeActivity)requireActivity()).dashBoardViewModel.processEvent(new DashboardViewEvent.HelpClicked());
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null)
            return view;
        view = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

}