package com.ahui.lessonwms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

//    Version 1

    private Drawable shapeDrawable;

    private GoogleWhiteView googleWhiteView;
    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button=findViewById(R.id.button);

        googleWhiteView = findViewById(R.id.google_white_view);

        googleWhiteView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                googleWhiteView.getViewTreeObserver().removeOnPreDrawListener(this);

//                googleWhiteView.setAlpha(0);

                /*FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) googleWhiteView.getLayoutParams();
//                layoutParams.topMargin = Resources.getSystem().getDisplayMetrics().heightPixels;
                layoutParams.topMargin = Resources.getSystem().getDisplayMetrics().heightPixels/2;
                googleWhiteView.setLayoutParams(layoutParams);*/

                mainHandler.sendEmptyMessageDelayed(-1,2);

                return true;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startValueAnimator();


                /*TranslateAnimation translateAnimation = new TranslateAnimation(0,300,0,300);
                translateAnimation.setDuration(2000);
                translateAnimation.setFillAfter(true);//设置为true，动画转化结束后被应用
                button.startAnimation(translateAnimation);*/
            }
        });


        shapeDrawable=getResources().getDrawable(R.drawable.shape_default);
        shapeDrawable.setAlpha(0);


    }

    private class FourTwoFourIn implements Interpolator {

        @Override
        public float getInterpolation(float input) {



            return 0;
        }
    }


    private void startValueAnimator() {
        int height = (int) getResources().getDimension(R.dimen.dimen_300);
        ValueAnimator valueAnimator = initValueAnimator(height);
        valueAnimator.start();
        TranslateAnimation translateAnimation = initTranslateAnimator(height);
        googleWhiteView.startAnimation(translateAnimation);

    }

    private ValueAnimator initValueAnimator(int height) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0,height/2);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animValue = (int) animation.getAnimatedValue();
                googleWhiteView.progress(animValue);
            }
        });
        return valueAnimator;
    }

    private TranslateAnimation initTranslateAnimator(int height) {
        int startX = (int) (googleWhiteView.getX() + (googleWhiteView.getWidth() / 2));
        int startY = (int) (googleWhiteView.getY() + (googleWhiteView.getHeight() / 2));

        Log.d(TAG, "initTranslateAnimator: height=" + height + ",height/2=" + height/2);


        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,height,height/2);
        translateAnimation.setDuration(2000);
        translateAnimation.setFillAfter(true);//设置为true，动画转化结束后被应用
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (notInit) {
                    return;
                }
                googleWhiteView.getGoogleIcon().setVisibility(View.GONE);
                Animation scaleAnim = createScaleAnim(height);
                googleWhiteView.startAnimation(scaleAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return translateAnimation;
    }

    private Handler mainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            //do move


            TranslateAnimation translateAnimation = new TranslateAnimation(0,0,0,googleWhiteView.getHeight());
            translateAnimation.setDuration(2);
            translateAnimation.setFillAfter(true);//设置为true，动画转化结束后被应用
            translateAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    Log.d(TAG, "onAnimationEnd: ");


                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            googleWhiteView.startAnimation(translateAnimation);

            /*FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) googleWhiteView.getLayoutParams();
//                layoutParams.topMargin = Resources.getSystem().getDisplayMetrics().heightPixels;
                layoutParams.topMargin = Resources.getSystem().getDisplayMetrics().heightPixels/2;
                googleWhiteView.setLayoutParams(layoutParams);
            googleWhiteView.setAlpha(1);*/

        }
    };


    boolean notInit = false;
    private Animation createScaleAnim(int height) {
        Animation scaleAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_scale_set);

//        ScaleAnimation scaleAnimation = new ScaleAnimation(height, 0.5f, height/2, 2f);
        scaleAnimation.setDuration(5000);
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
                startActivity(activity);
                overridePendingTransition(0, 0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        notInit=true;
        return scaleAnimation;
    }


    /*private AlphaAnimation initAlphaAnimation() {

    }*/

    /*private void startAnim() {

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,10);
        valueAnimator.setDuration(2000);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {


                ViewGroup.LayoutParams layoutParams = baseCl.getLayoutParams();
                float animValue = (float)animation.getAnimatedValue();
                layoutParams.width = (int) (baseCl.getWidth() + animValue);
                shapeDrawable.setAlpha((int) (animValue/25f*255));
                baseCl.setBackground(shapeDrawable);
                baseCl.setLayoutParams(layoutParams);
            }
        });
    }*/




    /*private ConstraintLayout backCl;
    private FrameLayout frameLayout;
    private Drawable shapeDrawable;
    private ConstraintLayout baseCl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backCl=findViewById(R.id.back_cl);
        frameLayout=findViewById(R.id.frame_layout);

        baseCl=findViewById(R.id.base_cl);
        shapeDrawable=getResources().getDrawable(R.drawable.shape_default);
        shapeDrawable.setAlpha(0);
        baseCl.setBackground(shapeDrawable);

    }

    private float lastY = -1;
    private float downY = -1;

    private static final float MAX_DISTANCE = 400f;

//    private float lastPercent=-1;

    @Override
    protected void onResume() {
        super.onResume();


        backCl.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                backCl.getViewTreeObserver().removeOnPreDrawListener(this);
//                backCl.layout(backCl.getLeft(),backCl.getTop()+backCl.getHeight(),backCl.getRight(),backCl.getBottom()+backCl.getHeight());
                return true;
            }
        });


        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        downY = event.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        float currentY = event.getY();
                        float distanceY = lastY-currentY;
                        lastY=currentY;
                        float allDistanceY = downY-currentY;
                        if (allDistanceY>backCl.getHeight()) {
                            return false;
                        }


//                        float percent = (distanceY%MAX_DISTANCE)/MAX_DISTANCE;

                        Log.d(TAG, "onTouch: currentY=" + currentY+",distanceY=" + distanceY + ",allDistanceY=" + allDistanceY + ",height=" + backCl.getHeight()
                                + ",top=" + backCl.getTop() + ",bottom=" + backCl.getBottom());

//                        if (lastPercent>0.95f) {
//                            return false;
//                        }
//                        lastPercent=percent;

                        backCl.layout(backCl.getLeft(), (int) (backCl.getTop()- distanceY),backCl.getRight(), (int) (backCl.getBottom()-distanceY));



                   *//*     ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) baseCl.getLayoutParams();
                        float animValue = allDistanceY/backCl.getHeight();
                        Log.d(TAG, "onTouch: animValue=" + animValue);
                        layoutParams.width = (int) (baseCl.getWidth() + animValue);
                        shapeDrawable.setAlpha((int) (animValue/25f*255));
                        baseCl.setBackground(shapeDrawable);
                        baseCl.setLayoutParams(layoutParams);*//*


                        break;
                }

                return true;
            }
        });

    }*/
}