package com.waveneuro.ui.base;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ActionMode;

import com.asif.abase.base.FormView;
import com.google.android.material.textfield.TextInputLayout;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.adapter.ViewDataAdapter;
import com.mobsandgeeks.saripaar.exception.ConversionException;

import java.util.List;

public abstract class BaseFormActivity extends BaseActivity implements FormView, Validator.ValidationListener {

    protected Validator mValidator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mValidator = new Validator(this);
        this.mValidator.setValidationListener(this);
        this.mValidator.registerAdapter(TextInputLayout.class, new TextInputLayoutAdapter());
        this.mValidator.setViewValidatedAction(view -> {
            if (view instanceof TextInputLayout) {
                ((TextInputLayout) view).setError("");
                ((TextInputLayout) view).setErrorEnabled(false);
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        submit();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(view.getContext());

            if (view instanceof EditText) {
                EditText et = (EditText) view;
                et.setError(message);
            }else if (view instanceof TextInputLayout) {
                ((TextInputLayout) view).setError(message);
            }
        }
    }

    public class TextInputLayoutAdapter implements ViewDataAdapter<TextInputLayout, String> {
        @Override
        public String getData(TextInputLayout view) throws ConversionException {
            return view.getEditText().getText().toString();
        }
    }
}

