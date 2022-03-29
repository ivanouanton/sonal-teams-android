package com.waveneuro.views;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TextInputEditText extends com.google.android.material.textfield.TextInputEditText {
    public TextInputEditText(@NonNull Context context) {
        super(context);
        initView();
    }

    public TextInputEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TextInputEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {

    }

    @Override
    public void onEditorAction(int actionCode) {
        super.onEditorAction(actionCode);
        if(actionCode== EditorInfo.IME_ACTION_DONE){
            clearFocus();
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }
}
