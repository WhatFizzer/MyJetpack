package com.ahui.lessonwms.anim;

import android.view.View;

import java.io.Serializable;

public class ConstraintLayoutWrap implements Serializable {
    private View mTarget;
    public ConstraintLayoutWrap(View baseView) {
        mTarget = baseView;
    }
    public int getWidth() {
        return mTarget.getLayoutParams().width;
    }

    public void setWidth(int width) {
        mTarget.getLayoutParams().width = width;
        mTarget.requestLayout();
    }
}
