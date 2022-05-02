package com.ahui.lessonwms.anim;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.PathInterpolator;

public class AnimManager {

    private static final String TAG = "AnimManager";

    /*google图标灰色底部width差之旗*/
    private static PathInterpolator GOOGLE_GREY_WIDTH_INTER = new PathInterpolator(0.33f,0f,0.67f,1f);

    /*椭圆白底动画*/
    private static PathInterpolator OVAL_WHITE_TRANS_Y = new PathInterpolator(0f,0f,0.1f,1f);

    /*google灰色底部View的动画*/
    private static PathInterpolator GOOGLE_GREY_TRANS_Y = new PathInterpolator(0.12f, 0f, 0f, 1f);

    private static float screenHeight;


    /*隐藏View*/
    public static ObjectAnimator hideView(View view,int height) {
        screenHeight = height;
        float startY = view.getY();
        float endY = startY + height;
        Log.d(TAG, "getHideTransY: startY=" + startY +",endY=" + endY);
        float[] trans = new float[]{startY,endY};
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, trans);
        objectAnimator.setDuration(1);
        return objectAnimator;
    }

    public static ObjectAnimator ovalWhiteTransY(View view, int maxHeight) {
        float startY = view.getY();
        float endY = startY - maxHeight;
        float[] trans = new float[]{startY,endY};
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, trans);
        objectAnimator.setDuration(800);
        objectAnimator.setInterpolator(OVAL_WHITE_TRANS_Y);
        return objectAnimator;
    }

    public static ObjectAnimator googleGeryTransY(View view, int maxHeight) {
        float startY = view.getY();
        float endY = startY - maxHeight;
        float[] trans = new float[]{startY,endY};
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, trans);
        objectAnimator.setDuration(717);
        objectAnimator.setInterpolator(GOOGLE_GREY_TRANS_Y);
        return objectAnimator;
    }

    public static ObjectAnimator googleGreyWidth(ConstraintLayoutWrap view, int maxWidth) {
        int startWidth = view.getWidth();
        int endWidth = view.getWidth() + maxWidth;
        int[] trans = new int[]{startWidth,endWidth};
//        Log.d(TAG, "googleGreyWidth: start startWidth=" + startWidth + ",endWidth=" + endWidth);
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(view, "width", trans);
        objectAnimator.setDuration(414);
        objectAnimator.setInterpolator(GOOGLE_GREY_WIDTH_INTER);
        return objectAnimator;
    }

    public static ObjectAnimator scaleOvalViewX(View view) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 1f, 3f);
        scaleX.setDuration(300);
        return scaleX;
    }

    public static ObjectAnimator scaleOvalViewY(View view) {
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 1f, 6f);
        scaleY.setDuration(500);
        return scaleY;
    }

    public static ObjectAnimator getHideTransY(View view) {
        float startY = view.getY();
        float endY = startY + view.getHeight();
        Log.d(TAG, "getHideTransY: startY=" + startY +",endY=" + endY);
        float[] trans = new float[]{view.getY(),endY};
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, trans);
        objectAnimator.setDuration(1000);
        return objectAnimator;
    }



}
