package com.ahui.lessonwms.application;

import android.app.Application;

import com.blankj.utilcode.util.Utils;

public class NApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(this);
    }
}
