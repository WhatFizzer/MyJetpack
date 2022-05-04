package com.ahui.lessonwms;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.ahui.lessonwms.touch.GSearchViewController;

public class TouchView  extends View {

    private static final String TAG = "TouchView";
    private GSearchViewController controller;

    public TouchView(Context context) {
        super(context);
    }

    public TouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
//        Log.d(TAG, "dispatchTouchEvent: ");
        controller.dispatchTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.d(TAG, "onTouchEvent: ");
        return controller.onTouchEvent(event);
    }

    public void setController(GSearchViewController controller) {
        this.controller = controller;
    }
}
