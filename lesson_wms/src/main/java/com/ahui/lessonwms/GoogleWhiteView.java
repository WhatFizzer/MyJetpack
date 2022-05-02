package com.ahui.lessonwms;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class GoogleWhiteView  extends ConstraintLayout {
    private static final String TAG = "GoogleWhiteView";
    View baseView;
    Context mContext;
    private int startMoveProgress;
    private int finishMoveProgress;
    private int finishStretchProgress;
    private int halfHeight;
    private ConstraintLayout googleIcon;

    public ConstraintLayout getGoogleIcon() {
        return googleIcon;
    }

    public GoogleWhiteView(@NonNull Context context) {
        super(context);
        this.mContext=context;
        init();
    }

    public GoogleWhiteView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        init();
    }

    private void init() {
        baseView= LayoutInflater.from(mContext).inflate(R.layout.google_white_layout,this, true);
        googleIcon=baseView.findViewById(R.id.google_icon);
        startMoveProgress= (int) mContext.getResources().getDimension(R.dimen.dimen_start);
        finishMoveProgress = (int) mContext.getResources().getDimension(R.dimen.dimen_finish);
        finishStretchProgress = (int) mContext.getResources().getDimension(R.dimen.dimen_finish_2);
        halfHeight = (int) mContext.getResources().getDimension(R.dimen.dimen_300) / 2;
    }


    public void progress(float progress)  {
        Log.d(TAG, "progress: progress=" + progress + ",halfHeight="+ halfHeight + ",finishMoveProgress=" + finishMoveProgress + ",finishStretchProgress=" + finishStretchProgress);
        if (progress>=startMoveProgress&&progress<finishMoveProgress) {

            //移动g图标
            ConstraintLayout.LayoutParams layoutParams = (LayoutParams) googleIcon.getLayoutParams();
            layoutParams.topMargin = layoutParams.topMargin+10;
            googleIcon.setLayoutParams(layoutParams);

        } else if (progress>=finishMoveProgress&&progress<finishStretchProgress) {

            //拉升view的size
            ConstraintLayout.LayoutParams layoutParams = (LayoutParams) googleIcon.getLayoutParams();
            layoutParams.width = layoutParams.width + 10;
            googleIcon.setLayoutParams(layoutParams);


        } else if (progress ==halfHeight) {
            //start acitivity
            Log.d(TAG, "progress: do start page");


            /*Intent activity = new Intent();
//            activity.setClassName("com.aiwinn.smartgatetemp", "com.aiwinn.smartgatetemp.ui.view.WelcomeActivity");
            activity.setClassName("com.aiwinn.faceattendance", "com.aiwinn.faceattendance.ui.m.MainActivity");
            activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(activity);*/


            /*if(notInit){
                return;
            }
            ((MainActivity) mContext).overridePendingTransition(0,0);
            ScaleAnimation scaleAnim = createScaleAnim();
            googleIcon.startAnimation(scaleAnim);*/

        }



    }

    boolean notInit = false;
    private ScaleAnimation createScaleAnim() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 0.2f, 0, Animation.RELATIVE_TO_PARENT);
        scaleAnimation.setDuration(2000);
        scaleAnimation.setFillAfter(true);//设置为true，动画转化结束后被应用
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent activity = new Intent();
                activity.setClassName("com.aiwinn.faceattendance", "com.aiwinn.faceattendance.ui.m.MainActivity");
                activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(activity);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        notInit=true;
        return scaleAnimation;
    }


}
