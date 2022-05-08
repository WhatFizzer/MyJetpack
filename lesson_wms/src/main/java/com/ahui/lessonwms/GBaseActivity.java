package com.ahui.lessonwms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ahui.lessonwms.anim.GSearchViewControl;
import com.ahui.lessonwms.popupwindow.PopupWindowManager;
import com.ahui.lessonwms.touch.GSearchViewController;
import com.ahui.lessonwms.touch.GSearchViewTouchControl;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class GBaseActivity extends AppCompatActivity {

    private static final String TAG = "GBaseActivity";

    private GSearchViewControl searchViewControl;
    private TouchView baseView;
    private GSearchViewTouchControl gSearchViewTouchControl;
    private Button showPopupWindowBtn;
    private PopupWindowManager popupWindowManager;
    private Button showBottomSheetDialog;
    public GSearchViewController gSearchViewController;
    public GSearchViewController getgSearchViewController() {
        return gSearchViewController;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_google_view);
        baseView=findViewById(R.id.base_view);
        showPopupWindowBtn=findViewById(R.id.button2);

        searchViewControl = new GSearchViewControl(this);
        gSearchViewTouchControl = new GSearchViewTouchControl(this, searchViewControl);

        gSearchViewController=new GSearchViewController(this, searchViewControl);
        baseView.setController(gSearchViewController);
        popupWindowManager = new PopupWindowManager(GBaseActivity.this);

        showPopupWindowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    popupWindowManager.showPopupWindow();
                    setNavigationBarColor(Color.WHITE);
            }
        });

        showBottomSheetDialog = findViewById(R.id.button3);
        showBottomSheetDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Log.d(TAG, "onClick: state=" + bottomSheetBehavior.getState());
//                bottomSheetBehavior.setState(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED?BottomSheetBehavior.STATE_EXPANDED:BottomSheetBehavior.STATE_COLLAPSED);

                sheetDialog.show();
            }
        });
        initBottomSheetDialog();
//        initBottomSheetView();
    }

    BottomSheetBehavior bottomSheetBehavior;
    private void initBottomSheetView() {
        View bottomSheetView = LayoutInflater.from(GBaseActivity.this).inflate(R.layout.behavior_bottom_sheet_window_layout, null, false);
//        ViewGroup decorView = (ViewGroup) this.getWindow().getDecorView();
//        decorView.addView(bottomSheetView);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView.findViewById(R.id.base_tips_view));
    }

    private BottomSheetDialog sheetDialog;

    private void initBottomSheetDialog() {
        sheetDialog = new BottomSheetDialog(GBaseActivity.this);
        sheetDialog.setContentView(R.layout.behavior_bottom_sheet_window_layout);
        sheetDialog.getWindow().setDimAmount(0);
        sheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (gSearchViewController!=null) {
                    gSearchViewController.getgSearchViewControl().makeAnim();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        setNavigationBarColor(Color.TRANSPARENT);

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

    public void setNavigationBarColor(int color) {
        getWindow().setNavigationBarColor(color);
    }
}