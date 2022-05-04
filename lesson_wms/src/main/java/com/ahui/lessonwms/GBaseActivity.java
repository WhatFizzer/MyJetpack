package com.ahui.lessonwms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;

import com.ahui.lessonwms.anim.AnimManager;
import com.ahui.lessonwms.anim.ConstraintLayoutWrap;
import com.ahui.lessonwms.anim.GSearchViewControl;
import com.ahui.lessonwms.popupwindow.PopupWindowManager;
import com.ahui.lessonwms.touch.GSearchViewController;
import com.ahui.lessonwms.touch.GSearchViewTouchControl;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.ScreenUtils;

public class GBaseActivity extends AppCompatActivity {

    private static final String TAG = "GBaseActivity";

    private GSearchViewControl searchViewControl;
    private TouchView baseView;
    private GSearchViewTouchControl gSearchViewTouchControl;
    private Button showPopupWindowBtn;
    private PopupWindowManager popupWindowManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_google_view);
        baseView=findViewById(R.id.base_view);
        showPopupWindowBtn=findViewById(R.id.button2);

        searchViewControl = new GSearchViewControl(this);
        gSearchViewTouchControl = new GSearchViewTouchControl(this, searchViewControl);

        baseView.setController(new GSearchViewController(this, searchViewControl));
        popupWindowManager = new PopupWindowManager(GBaseActivity.this);

        showPopupWindowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    popupWindowManager.showPopupWindow();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


//        uiHandler.sendEmptyMessageDelayed(MSG_HIDE_VIEW, 1000);
        /*baseView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gSearchViewTouchControl.onTouchEvent(event);
                return true;
            }
        });*/
    }

    private static final int MSG_HIDE_VIEW = 1001;
    private Handler uiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MSG_HIDE_VIEW:

                    /*searchView.getScrimFl().setVisibility(View.GONE);

                    AnimManager.hideView(searchView.getOvalView(), ScreenUtils.getScreenHeight()).start();
                    AnimManager.hideView(searchView.getGoogleLayoutView(), ScreenUtils.getScreenHeight()).start();*/

                    break;
            }
        }
    };
}