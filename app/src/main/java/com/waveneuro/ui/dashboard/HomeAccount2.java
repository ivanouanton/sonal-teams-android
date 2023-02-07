//package com.waveneuro.ui.dashboard;
//
//import static com.waveneuro.ui.session.history.SessionHistoryActivity.START_SESSION;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.FrameLayout;
//
//import androidx.annotation.IntDef;
//import androidx.appcompat.app.ActionBarDrawerToggle;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.core.view.GravityCompat;
//import androidx.drawerlayout.widget.DrawerLayout;
//import androidx.fragment.app.Fragment;
//import androidx.fragment.app.FragmentManager;
//import androidx.fragment.app.FragmentTransaction;
//import androidx.lifecycle.Observer;
//import androidx.lifecycle.ViewModelProviders;
//
//import com.ap.ble.BluetoothManager;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.google.android.material.navigation.NavigationView;
//import com.waveneuro.R;
//import com.waveneuro.ui.base.BaseActivity;
//import com.waveneuro.ui.dashboard.account.AccountCommand;
//import com.waveneuro.ui.dashboard.device.DeviceFragment;
//import com.waveneuro.ui.dashboard.help.HelpCommand;
//import com.waveneuro.ui.dashboard.history.HistoryCommand;
//import com.waveneuro.ui.dashboard.home.HomeFragment;
//import com.waveneuro.ui.dashboard.home.MoreFragment;
//import com.waveneuro.ui.dashboard.more.WebCommand;
//import com.waveneuro.ui.device.MyDeviceCommand;
//import com.waveneuro.ui.user.login.LoginCommand;
//
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//
//import javax.inject.Inject;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//public class HomeActivity2 extends BaseActivity {
//
//    @BindView(R.id.fr_home)
//    FrameLayout frHome;
//    @BindView(R.id.fr_device)
//    FrameLayout frDevice;
//    @BindView(R.id.fr_history)
//    FrameLayout frHistory;
//    @BindView(R.id.fr_more)
//    FrameLayout frMore;
//
//    @BindView(R.id.bottom_navigation)
//    BottomNavigationView bottomNavigationView;
//
//    @BindView(R.id.right_navigation_drawer)
//    NavigationView rightNavigationDrawer;
//
//    @BindView(R.id.drawer_layout)
//    DrawerLayout drawerLayout;
//
//    @BindView(R.id.main_content)
//    ConstraintLayout mainContent;
//
//    private FragmentManager fragmentManager;
//
//    @Retention(RetentionPolicy.SOURCE)
//    @IntDef({TAB_HOME, TAB_DEVICE, TAB_HISTORY, TAB_MORE})
//    public @interface BottomTab {
//    }
//
//    public static final int TAB_HOME = 0;
//    public static final int TAB_DEVICE = 1;
//    public static final int TAB_HISTORY = 2;
//    public static final int TAB_MORE = 3;
//
//    HomeFragment homeFragment;
//    MoreFragment moreFragment;
//
//    BluetoothManager bluetoothManager;
//
//    @Inject
//    LoginCommand loginCommand;
//    @Inject
//    AccountCommand accountCommand;
//    @Inject
//    MyDeviceCommand myDeviceCommand;
//    @Inject
//    WebCommand webCommand;
//    @Inject
//    HelpCommand helpCommand;
//    @Inject
//    HistoryCommand deviceHistoryCommand;
//
//    @Inject
//    public DashBoardViewModel dashBoardViewModel;
//
//    DashBoardViewModel dashBoardViewModelConnection;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        activityComponent().inject(this);
//        setContentView(R.layout.activity_home);
//        ButterKnife.bind(this);
//
//        dashBoardViewModelConnection = ViewModelProviders.of(this).get(DashBoardViewModel.class);
//
//        setView();
//        setObserver();
//        bottomNavigationView.setSelectedItemId(R.id.bottom_navigation_home);
//        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.bottom_navigation_home:
//                    setContainerVisible(TAB_HOME);
//                    addFragment(TAB_HOME);
//                    return true;
//                case R.id.bottom_navigation_more:
//                    setContainerVisible(TAB_MORE);
//                    addFragment(TAB_MORE);
//                    return true;
//                default:
//                    return false;
//            }
//        });
//
//        bottomNavigationView.setOnNavigationItemReselectedListener(item -> {
//
//        });
//        setContainerVisible(TAB_HOME);
//        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.home, R.string.home) {
//
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//                super.onDrawerSlide(drawerView, slideOffset);
//                float slideX = drawerView.getWidth() * slideOffset;
//                mainContent.setTranslationX(-slideX);
//            }
//        };
//
//        drawerLayout.setDrawerElevation(0f);
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);
//
//    }
//
//    private void setView() {
//        fragmentManager = getSupportFragmentManager();
//        if (homeFragment == null)
//            homeFragment = HomeFragment.newInstance();
//        if (moreFragment == null)
//            moreFragment = MoreFragment.newInstance();
//        addFragment(frHome, homeFragment);
//        addFragment(frMore, moreFragment);
//    }
//
//    private void setObserver() {
//        this.dashBoardViewModel.getData().observe(this, dashboardViewStateObserver);
//        this.dashBoardViewModel.getViewEffect().observe(this, dashboardViewEffectObserver);
//        this.dashBoardViewModelConnection.getViewEffect().observe(this, dashboardConnectionViewEffectObserver);
//    }
//
//    Observer<DashboardViewState> dashboardViewStateObserver = viewState -> {
//
//    };
//
//    Observer<DashboardViewEffect> dashboardViewEffectObserver = viewEffect -> {
//        if (viewEffect instanceof DashboardViewEffect.Login) {
//            launchLoginScreen();
//        } else if (viewEffect instanceof DashboardViewEffect.Account) {
//            launchAccountScreen();
//        } else if (viewEffect instanceof DashboardViewEffect.Help) {
//            launchHelpScreen();
//        } else if (viewEffect instanceof DashboardViewEffect.DeviceHistory) {
//            launchDeviceHistoryScreen();
//        }else if (viewEffect instanceof DashboardViewEffect.Device) {
//            DashboardViewEffect.Device effect = (DashboardViewEffect.Device) viewEffect;
//            launchMyDeviceScreen(effect.getDeviceName());
//        }
//    };
//
//    Observer<DashboardViewEffect> dashboardConnectionViewEffectObserver = viewEffect -> {
//        if (viewEffect instanceof DashboardViewEffect.Device) {
//            DashboardViewEffect.Device effect = (DashboardViewEffect.Device) viewEffect;
//            launchMyDeviceScreen(effect.getDeviceName());
//        }
//    };
//
//    private void launchLoginScreen() {
//        this.loginCommand.navigate();
//    }
//
//    private void launchAccountScreen() {
//        this.accountCommand.navigate();
//    }
//
//    private void launchHelpScreen() {
//        this.helpCommand.navigate();
//    }
//
//    private void launchDeviceHistoryScreen() {
//        this.deviceHistoryCommand.navigate();
//    }
//
//    private void launchMyDeviceScreen(String deviceName) {
//        this.myDeviceCommand.navigate(deviceName);
//    }
//
//    private void setContainerVisible(@BottomTab int tab) {
//        frHome.setVisibility(TAB_HOME == tab ? View.VISIBLE : View.GONE);
//        frDevice.setVisibility(TAB_DEVICE == tab ? View.VISIBLE : View.GONE);
//        frHistory.setVisibility(TAB_HISTORY == tab ? View.VISIBLE : View.GONE);
//        frMore.setVisibility(TAB_MORE == tab ? View.VISIBLE : View.GONE);
//    }
//
//    private void addFragment(@BottomTab int tab) {
//        switch (tab) {
//            case TAB_HOME:
//                if (homeFragment == null) {
//                    homeFragment = HomeFragment.newInstance();
//                    addFragment(frHome, homeFragment);
//                }
//                break;
//            case TAB_MORE:
//                if (moreFragment == null) {
//                    moreFragment = MoreFragment.newInstance();
//                    addFragment(frMore, moreFragment);
//                }
//                break;
//        }
//    }
//
//    private void addFragment(View view, Fragment fragment) {
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(view.getId(), fragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//    }
//
//    public void addFragment(int viewId, Fragment fragment) {
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(viewId, fragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//    }
//
//    public void switchToHome() {
//        bottomNavigationView.setSelectedItemId(R.id.bottom_navigation_home);
//    }
//
//    private void resetTabs() {
//        frHome.setVisibility(View.GONE);
//        frDevice.setVisibility(View.GONE);
//        frHistory.setVisibility(View.GONE);
//    }
//
//    @OnClick(R.id.ll_container_account)
//    public void onClickAccountContainer() {
//        drawerLayout.closeDrawer(GravityCompat.END);
//        this.dashBoardViewModel.processEvent(DashboardViewEvent.AccountClicked.INSTANCE);
//    }
//
//    @OnClick(R.id.ll_container_device)
//    public void onClickDeviceContainer() {
////        bottomNavigationView.setSelectedItemId(R.id.bottom_navigation_device);
//        this.dashBoardViewModelConnection.processEvent(DashboardViewEvent.DeviceClicked.INSTANCE);
//        drawerLayout.closeDrawer(GravityCompat.END);
//    }
//
//    @OnClick(R.id.tv_faq)
//    public void onClickFaq() {
//        drawerLayout.closeDrawer(GravityCompat.END);
//        webCommand.navigate(WebCommand.PAGE_FAQ);
//    }
//
//    @OnClick(R.id.tv_support)
//    public void onClickSupport() {
//        drawerLayout.closeDrawer(GravityCompat.END);
//        webCommand.navigate(WebCommand.PAGE_SUPPORT);
//    }
//
//    @OnClick(R.id.tv_terms_of_use)
//    public void onClickTermsOfUse() {
//        drawerLayout.closeDrawer(GravityCompat.END);
//        webCommand.navigate(WebCommand.PAGE_TERMS_CONDITIONS);
//    }
//
//    @OnClick(R.id.tv_privacy_policy)
//    public void onClickPrivacyPolicy() {
//        drawerLayout.closeDrawer(GravityCompat.END);
//        webCommand.navigate(WebCommand.PAGE_POLICY);
//    }
//
//    @OnClick(R.id.tv_contact_us)
//    public void onClickContactUs() {
//        drawerLayout.closeDrawer(GravityCompat.END);
//        webCommand.navigate(WebCommand.PAGE_CONTACT);
//    }
//
//    @OnClick(R.id.tv_logout)
//    public void onClickLogout() {
//        drawerLayout.closeDrawer(GravityCompat.END);
//        this.dashBoardViewModel.processEvent(DashboardViewEvent.LogoutClicked.INSTANCE);
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        this.finish();
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (data != null && data.hasExtra(START_SESSION)) {
//            if (data.getBooleanExtra(START_SESSION, false)) {
//                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                addFragment(R.id.fr_home, DeviceFragment.newInstance());
//            }
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        //DONE Destroy all connections to bluetooth
//        //TODO Destroy connections from Application, not from activity
////        if (bluetoothManager == null)
////            bluetoothManager = new BluetoothManager(this);
////        bluetoothManager.disconnectDevice();
//    }
//}