package com.waveneuro.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MonospaceSpan extends ReplacementSpan {
    private boolean ignoreFullText;

    public void setIgnoreFullText(boolean ignoreFullText) {
        this.ignoreFullText = ignoreFullText;
    }

    private int getMaxCharWidth(@NonNull Paint paint, @NonNull CharSequence text, int start, int end, float[] widths) {
        if (widths == null) {
            widths = new float[end - start];
        }

        paint.getTextWidths(text, start, end, widths);

        float max = 0;

        for (float w : widths) {
            if (max < w) {
                max = w;
            }
        }

        return Math.round(max);
    }

    @Override
    public int getSize(@NonNull Paint paint, @NonNull CharSequence text, int start, int end, @Nullable Paint.FontMetricsInt fm) {
        if (fm != null) {
            paint.getFontMetricsInt(fm);
        }

        int count = end - start;

        if (text.charAt(start) == '\n') {
            count -= 1;
        }

        if (text.charAt(end - 1) == '\n') {
            count -= 1;
        }

        if (count < 0) {
            count = 0;
        }

        if (ignoreFullText) {
            return getMaxCharWidth(paint, text, start, end, null) * count;
        } else {
            return getMaxCharWidth(paint, text, 0, text.length(), null) * count;
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas, @NonNull CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        float[] widths = new float[end - start];
        int max = getMaxCharWidth(paint, text, start, end, widths);

        if (!ignoreFullText) {
            max = getMaxCharWidth(paint, text, 0, text.length(), null);
        }

        for (int i = 0, n = end - start; i < n; ++i) {
            float p = (max - widths[i]) / 2;
            canvas.drawText(text, start + i, start + i + 1, x + max * i + p, y, paint);
        }
    }
}
