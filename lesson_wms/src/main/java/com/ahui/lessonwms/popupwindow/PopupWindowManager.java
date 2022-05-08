package com.ahui.lessonwms.popupwindow;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionListenerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.ahui.lessonwms.GBaseActivity;
import com.ahui.lessonwms.R;
import com.blankj.utilcode.util.ToastUtils;
import com.google.common.reflect.TypeToken;

public class PopupWindowManager {


    private static final String TAG = "PopupWindowManager";
    private GBaseActivity mLauncher;
    public PopupWindowManager(GBaseActivity context) {
        this.mLauncher = context;
        initPopupWindow();
    }

    private PopupWindow popupWindow;
    private View baseView;
    private Button clickButton;

    private void initPopupWindow() {

        baseView = LayoutInflater.from(mLauncher).inflate(R.layout.tips_pop_window_layout, null, false);
        clickButton=baseView.findViewById(R.id.confirm_button);
        popupWindow = new PopupWindow(baseView, ConstraintLayout.LayoutParams.MATCH_PARENT, mLauncher.getResources().getDimensionPixelOffset(R.dimen.dimen_412));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.popwin_anim);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mLauncher.setNavigationBarColor(Color.TRANSPARENT);
//                ToastUtils.showShort("pop miss");
                mLauncher.getgSearchViewController().getgSearchViewControl().makeAnim(600);
            }
        });

        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopupWindow();
            }
        });
       /* baseView.setOnTouchListener(new View.OnTouchListener() {
            int orgY;
            int offsetY;
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        orgY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        offsetY = (int) event.getRawY() - orgY;
                        popupWindow.update(0, -offsetY, -1, -1, true);
                        break;
                }

                return true;
            }
        });*/
    }

    public void showPopupWindow() {
        if (popupWindow!=null) {
            popupWindow.showAtLocation(mLauncher.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
//            popupWindow.showAsDropDown(baseView, 0, mLauncher.getResources().getDimensionPixelOffset(R.dimen.dimen_412));
        }
    }

    public void dismissPopupWindow() {
        if (popupWindow!=null&&popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

}
