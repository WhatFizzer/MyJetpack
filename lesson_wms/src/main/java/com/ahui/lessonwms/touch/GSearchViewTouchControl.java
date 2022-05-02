package com.ahui.lessonwms.touch;

import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ahui.lessonwms.GBaseActivity;
import com.ahui.lessonwms.R;
import com.ahui.lessonwms.anim.GSearchViewControl;

public class GSearchViewTouchControl {

    private static final String TAG = "GSearchViewTouchControl";
    private GBaseActivity mLauncher;

    private GSearchViewControl gSearchViewControl;

    public GSearchViewTouchControl(GBaseActivity activity, GSearchViewControl gSearchViewControl) {
        this.mLauncher = activity;
        this.gSearchViewControl = gSearchViewControl;
    }

    private float downY = -1f;
    public void onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                /*
                * make down poi
                * */
                downY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                /*
                * 1.translateY
                * 2.change width
                *
                * */
                if (downY==-1) {
                    Log.e(TAG, "onTouchEvent: downY is -1, error");
                    return;
                }
                float currentY = event.getY();
                float moveDistance = currentY - downY;
                gSearchViewControl.setProgress(moveDistance);
                /*if (moveDistance>0) {   //向下滑动，+

                } else {    //向上滑动，-




                }*/
                break;
            case MotionEvent.ACTION_UP:
                /*
                 * 1.reset downY
                 * 2.reset trans
                 *
                 * */
                downY=-1;
                if (gSearchViewControl.isDoOpenSearch()) {
                    /*
                    * 1.do open search
                    * 2.reset view*/
                    gSearchViewControl.scaleOvalXY();



                } else {
                    //reset view with anim

                }
                break;

        }
    }

}
