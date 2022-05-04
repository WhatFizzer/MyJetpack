package com.ahui.lessonwms.popupwindow;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.ahui.lessonwms.GBaseActivity;
import com.ahui.lessonwms.R;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;

public class PopupWindowManager {


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
                ToastUtils.showShort("pop miss");
            }
        });
        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissPopupWindow();
            }
        });
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
