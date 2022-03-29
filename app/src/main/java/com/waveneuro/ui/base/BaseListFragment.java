package com.waveneuro.ui.base;

import android.view.View;

import com.asif.abase.base.NavigationCommand;
import com.asif.abase.data.model.BaseModel;
import com.asif.abase.exception.MethodNotImplementedException;
import com.asif.abase.utils.RecyclerViewAdapter;

import java.util.Arrays;
import java.util.List;

public class BaseListFragment extends BaseFragment implements BaseListView {

    private View mContainerLayout;

    private View mProgressIndicator;

    private RecyclerViewAdapter adapter;

    private NavigationCommand detailCommand;


    protected void initializeList(View containerLayout, View progressIndicator, RecyclerViewAdapter adapter) {
        setContainerLayout(containerLayout);
        setProgressIndicator(progressIndicator);
        setAdapter(adapter);
    }

    public void setDetailCommand(NavigationCommand detailCommand) {
        this.detailCommand = detailCommand;
    }

    @Override
    public void onFailure(Error error) {
        removeWait();

//        if (mContainerLayout != null) {
//            final Snackbar snackbar = Snackbar
//                    .make(mContainerLayout, error.getMessage(), Snackbar.LENGTH_LONG);
//
//            snackbar.setAction("Close", view -> snackbar.dismiss());
//
//            snackbar.show();
//        }
    }

    public void setContainerLayout(View containerLayout) {
        this.mContainerLayout = containerLayout;
    }

    public void setProgressIndicator(View progressIndicator) {
        this.mProgressIndicator = progressIndicator;
    }

    public void setAdapter(RecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void displayListProgress() {
        if (mContainerLayout != null)
            mContainerLayout.setVisibility(View.GONE);
        if (mProgressIndicator != null)
            mProgressIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void removeListProgress() {
        if (mProgressIndicator != null)
            mProgressIndicator.setVisibility(View.GONE);
        if (mContainerLayout != null)
            mContainerLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void addAll(List<? extends BaseModel> items) {
        if (adapter != null) {
            adapter.update(items);

            if (items != null && items.size() > 0) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void appendAll(List<? extends BaseModel> items) {
        if (adapter != null) {
            adapter.addAll(items);

            if (items != null && items.size() > 0) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void add(BaseModel item) {

    }

    @Override
    public void onSuccess(List<? extends BaseModel> items) {
        removeWait();
        appendAll(items);
    }

    @Override
    public void onSuccessUpdate(List<? extends BaseModel> items) {
        removeWait();
        addAll(items);
    }

    @Override
    public void onSuccessDetail(BaseModel item) {
        removeWait();
        try {
            detailCommand.navigate(Arrays.asList(item));
        } catch (MethodNotImplementedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void displayWait() {

    }

    @Override
    public void removeWait() {

    }

    @Override
    public void onSessionExpired() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

