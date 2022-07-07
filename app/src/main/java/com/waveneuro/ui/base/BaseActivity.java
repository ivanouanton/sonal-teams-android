package com.waveneuro.ui.base;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.asif.abase.base.APIStatusView;
import com.asif.abase.base.ProgressWaitView;
import com.asif.abase.data.model.APIError;
import com.asif.abase.data.model.BaseError;
import com.asif.abase.data.model.BaseModel;
import com.messagebar.messagebar.Flashbar;
import com.waveneuro.R;
import com.waveneuro.WaveNeuroApplication;
import com.waveneuro.injection.component.ActivityComponent;
import com.waveneuro.injection.component.DaggerPersistComponent;
import com.waveneuro.injection.component.PersistComponent;
import com.waveneuro.injection.module.ActivityModule;
import com.waveneuro.views.WaveProgressDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import timber.log.Timber;

public class BaseActivity extends AppCompatActivity implements ProgressWaitView, APIStatusView {

    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);

    private static final Map<Long, PersistComponent> sComponentsMap = new HashMap();
    private ActivityComponent mActivityComponent;
    private WaveProgressDialog waveProgressDialog;
    private long mActivityId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivityId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();



        mActivityComponent = getActivityComponent();
        waveProgressDialog = new WaveProgressDialog(this);

    }

    private ActivityComponent getActivityComponent() {
        PersistComponent persistComponent = null;
        if (!sComponentsMap.containsKey(mActivityId)) {
            Timber.i("Creating new ConfigPersistentComponent id=%d", mActivityId);
            persistComponent = DaggerPersistComponent.builder()
                    .applicationComponent(((WaveNeuroApplication) getApplication()).appComponent)
                    .build();
            sComponentsMap.put(mActivityId, persistComponent);
        } else {
            Timber.i("Reusing ConfigPersistentComponent id=%d", mActivityId);
            persistComponent = sComponentsMap.get(mActivityId);
        }
       return persistComponent.activityComponent(new ActivityModule(this));
    }

    public ActivityComponent activityComponent() {
        if(this.mActivityComponent == null)
            this.mActivityComponent = getActivityComponent();
        return this.mActivityComponent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, mActivityId);
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", mActivityId);
            sComponentsMap.remove(mActivityId);
        }
        super.onDestroy();
    }

    @Override
    public void onSuccess(BaseModel model) {
        removeWait();
    }

    @Override
    public void onSuccess(List<? extends BaseModel> model) {
        removeWait();
    }

    @Override
    public void onFailure(BaseError error) {
        APIError apiError = (APIError) error;
        String message = "Please contact admin at support@waveneurohelp.zendesk.com";
        if (!TextUtils.isEmpty(apiError.getMessage())) {
            message = apiError.getMessage();
        } else if (!TextUtils.isEmpty(apiError.getMsg())) {
            message = apiError.getMsg();
        } else if (!TextUtils.isEmpty(apiError.getError())) {
            message = apiError.getError();
        }

        removeWait();
        new Flashbar.Builder(BaseActivity.this)
                .gravity(Flashbar.Gravity.BOTTOM)
                .message(message)
                .messageColorRes(R.color.white)
                .backgroundColorRes(R.color.black)
                .primaryActionText("DISMISS")
                .primaryActionTapListener(Flashbar::dismiss)
                .showOverlay()
                .dismissOnTapOutside()
                .build().show();
    }

    @Override
    public void displayWait() {
        if (waveProgressDialog != null && !isFinishing()) {
            waveProgressDialog.startProgressDialog();
        }
    }

    public void displayWait(String title) {
        displayWait(title, null);
    }

    public void displayWait(String title, String message) {
        if (waveProgressDialog != null && !isFinishing()) {
            waveProgressDialog.startProgressDialog();
        }
    }

    @Override
    public void removeWait() {
        if (waveProgressDialog != null && waveProgressDialog.isShowing()) {
            waveProgressDialog.dismiss();
        }
    }

}