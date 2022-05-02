package com.ahui.lessonwms.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.ahui.lessonwms.GBaseActivity;
import com.ahui.lessonwms.GSearchView;
import com.ahui.lessonwms.R;
import com.blankj.utilcode.util.ScreenUtils;

import java.util.Arrays;

public class GSearchViewControl  {
    private static final String TAG = "GSearchViewControl";
    private GSearchView searchView;
    private View baseView;
    private Button startAnimBtn;
    private ConstraintLayoutWrap wrap;

    private GBaseActivity mLauncher;

    private static float oval_view_max_height;
    private static float show_grey_view_height;
    private static float[] google_grey_view_transy_distance;
    private static float[] google_grey_view_width_distance;

    /*一元二次函数的k,b系数*/
    private static float heightConvertWidhtKCoefficient = 0f;
    private static float heightConvertWidhtBCoefficient = 0f;

    //
    private float mShifeRange;

    public GSearchViewControl(GBaseActivity activity) {
        this.mLauncher = activity;
        baseView = mLauncher.getWindow().getDecorView();
        oval_view_max_height = this.mLauncher.getResources().getDimension(R.dimen.dimen_140);
        show_grey_view_height = this.mLauncher.getResources().getDimension(R.dimen.dimen_22);
        google_grey_view_transy_distance = new float[]{
                this.mLauncher.getResources().getDimension(R.dimen.dimen_13),   //滑动到这个位置执行google view的translate
                this.mLauncher.getResources().getDimension(R.dimen.dimen_118),
        };
        google_grey_view_width_distance = new float[]{
                this.mLauncher.getResources().getDimension(R.dimen.dimen_72),   //滑动到这个距离开始执行width
                this.mLauncher.getResources().getDimension(R.dimen.dimen_118),
        };
        heightConvertWidhtKCoefficient =mLauncher.getResources().getDimension(R.dimen.dimen_188)/(google_grey_view_width_distance[1]-google_grey_view_width_distance[0]);
        heightConvertWidhtBCoefficient = mLauncher.getResources().getDimension(R.dimen.dimen_50) - (heightConvertWidhtKCoefficient *google_grey_view_width_distance[0]);
        mShifeRange = ScreenUtils.getScreenHeight();
        Log.d(TAG, "GSearchViewControl: heightConvertWidhtKCoefficient=" + heightConvertWidhtKCoefficient + ",heightConvertWidhtBCoefficient=" + heightConvertWidhtBCoefficient
                + ", oval_view_max_height=" + oval_view_max_height + ",show_grey_view_height=" + show_grey_view_height
                + ", google_grey_view_transy_distance=" + Arrays.toString(google_grey_view_transy_distance) + ",google_grey_view_width_distance=" + Arrays.toString(google_grey_view_width_distance));
        initView();
        bindEvent();

    }


    private void bindEvent() {
        startAnimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setVisibility(View.VISIBLE);
                makeAnim();
            }
        });
    }

    private void makeAnim() {
        searchView.getScrimFl().setVisibility(View.VISIBLE);
        int ovalWhiteMaxHeight = (int) mLauncher.getResources().getDimension(R.dimen.dimen_140);
        int googleGreyMaxHeight = (int) mLauncher.getResources().getDimension(R.dimen.dimen_118);
        int googleGreyMaxWidth = (int) mLauncher.getResources().getDimension(R.dimen.dimen_238);
        ObjectAnimator ovalWhiteTransY = AnimManager.ovalWhiteTransY(searchView.getOvalView(), ovalWhiteMaxHeight);
        ObjectAnimator googleGeryTransY = AnimManager.googleGeryTransY(searchView.getGoogleLayoutView(), googleGreyMaxHeight);
        ObjectAnimator googleGreyWidth = AnimManager.googleGreyWidth(wrap, googleGreyMaxWidth);
        googleGreyWidth.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                searchView.getGreyView().setVisibility(View.VISIBLE);
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(ovalWhiteTransY);
        animatorSet.play(googleGeryTransY);
        animatorSet.play(googleGreyWidth).after(300);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                scaleOvalXY();
            }
        });
        animatorSet.start();
    }

    public void scaleOvalXY() {
        //start white anim && page start
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                searchView.getGoogleLayoutView().setVisibility(View.GONE);
                ObjectAnimator scaleOvalViewX = AnimManager.scaleOvalViewX(searchView.getOvalView());
                ObjectAnimator scaleOvalViewY = AnimManager.scaleOvalViewY(searchView.getOvalView());
                AnimatorSet scaleSet = new AnimatorSet();
                scaleSet.play(scaleOvalViewX);
                scaleSet.play(scaleOvalViewY);
                scaleSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startGSearchPage();
                        uiHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                resetGSearchView();
                            }
                        },400);
                    }
                });
                scaleSet.start();
            }
        }, 600);
    }

    private void startGSearchPage() {
        //page start
        Intent activity = new Intent();
        activity.setClassName("com.aiwinn.faceattendance", "com.aiwinn.faceattendance.ui.m.MainActivity");
        activity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mLauncher.startActivity(activity);
        mLauncher.overridePendingTransition(0, 0);
    }

    private void resetGSearchView() {
        /*
         * 1.reset GSearch View,i.width.ii.transY
         * 2.reset Oval White View.i.scale,ii.transY
         * */
        searchView.getOvalView().setScaleX(1.0f);
        searchView.getOvalView().setScaleY(1.0f);
        searchView.getOvalView().setTranslationY(ScreenUtils.getScreenHeight());
        searchView.getScrimFl().setVisibility(View.GONE);
        searchView.getGreyView().setVisibility(View.INVISIBLE);
        searchView.getGoogleLayoutView().setVisibility(View.VISIBLE);
        wrap.setWidth((int) mLauncher.getResources().getDimension(R.dimen.dimen_50));
        searchView.getGoogleLayoutView().setTranslationY(ScreenUtils.getScreenHeight());
    }

    private void initView() {
        startAnimBtn=mLauncher.findViewById(R.id.start_anim_btn);

        addPullUpView();
        wrap = new ConstraintLayoutWrap(searchView.getGoogleLayoutView());

        if (searchView!=null) {
            searchView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    searchView.getViewTreeObserver().removeOnPreDrawListener(this);



                    /*ObjectAnimator hideTransY = AnimManager.getHideTransY(searchView.getOvalView());
                    hideTransY.start();*/

                    searchView.getScrimFl().setVisibility(View.GONE);
//                    searchView.setVisibility(View.INVISIBLE);

                    /*AnimManager.hideView(searchView.getOvalView(), ScreenUtils.getScreenHeight()).start();
                    AnimManager.hideView(searchView.getGoogleLayoutView(), ScreenUtils.getScreenHeight()).start();*/

                    setGSearchViewBottom();

                    return true;
                }
            });

        }

    }

    private void setGSearchViewBottom() {
        /*FrameLayout.LayoutParams ovalViewLp = (FrameLayout.LayoutParams) searchView.getOvalView().getLayoutParams();
        ovalViewLp.topMargin = mShifeRange;
        searchView.getOvalView().setLayoutParams(ovalViewLp);
        FrameLayout.LayoutParams gLayoutLp = (FrameLayout.LayoutParams) searchView.getGoogleLayoutView().getLayoutParams();
        gLayoutLp.topMargin = mShifeRange;
        searchView.getGoogleLayoutView().setLayoutParams(gLayoutLp);*/
        searchView.getOvalView().setTranslationY(ScreenUtils.getScreenHeight());
        searchView.getGoogleLayoutView().setTranslationY(ScreenUtils.getScreenHeight());
    }

    private void addPullUpView() {
        if (baseView!=null) {
            ViewGroup baseView = (ViewGroup) this.baseView;
            int childCount = baseView.getChildCount();
            if (childCount>0) {
                for (int i = 0; i < childCount; i++) {
                    View view = baseView.getChildAt(i);
                    if (GSearchView.SEARCH_VIEW_TAG.equals(view.getTag())) {
                        //already add this view
                        Log.d(TAG, "addPullUpView: already add this view");
                        searchView = (GSearchView) view;
                        return;
                    }
                }
            }
            searchView = new GSearchView(mLauncher);
            searchView.setTag(GSearchView.SEARCH_VIEW_TAG);
            baseView.addView(searchView);

        }
    }


    //hide view, Type 1
    //wait 400ms
    private Handler uiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };


    /*-------- 通过触摸事件控制View的移动 -----------*/
    private boolean doOpenSearch = false;

    public void setProgress(float moveDistance) {
        Log.d(TAG, "setProgress: moveDistance=" + moveDistance + ",translateY=" + searchView.getOvalView().getTranslationY()+",mShifeRange=" + mShifeRange);
        float absProgress = Math.abs(moveDistance);
        if (absProgress > oval_view_max_height) {
            //移动距离超过view最大的height的时候，oval的状态应该是显示或是隐藏的状态
            //存在1-2像素的偏差，故直接判断大于小于即可
            if (searchView.getOvalView().getTranslationY()>mShifeRange
                    ||searchView.getOvalView().getTranslationY()<=(mShifeRange-oval_view_max_height+5)) { //5像素容错
                doOpenSearch=true;
            } else {
                Log.e(TAG, "setProgress: oval view,wrong status,reset oval view,oval view transY=" + searchView.getOvalView().getTranslationY() + ",oval_view_max_height=" + oval_view_max_height);
                doOpenSearch=false;
            }
        } else {
            float v = absProgress / oval_view_max_height;   //[0-1]

            int ovalTransY = (int) (mShifeRange + moveDistance);
            searchView.getOvalView().setTranslationY(ovalTransY);
            if (absProgress >= google_grey_view_transy_distance[0]
                    && absProgress <= google_grey_view_transy_distance[1]) {
                //google_grey_view y轴的移动
                int gGreyViewTransY = (int) (mShifeRange + google_grey_view_transy_distance[0] + moveDistance);
                searchView.getGoogleLayoutView().setTranslationY(gGreyViewTransY);
            }
            if (absProgress >= google_grey_view_width_distance[0]
                    && absProgress <= google_grey_view_width_distance[1]) {
                //Y轴移动的这个高度转换成width
                float viewWidth = (heightConvertWidhtKCoefficient * absProgress) + heightConvertWidhtBCoefficient;
                Log.d(TAG, "setProgress: viewWidth=" + viewWidth);
                wrap.setWidth((int) viewWidth);
            }
            if (moveDistance<0&&absProgress >= google_grey_view_width_distance[0]) {
                if (searchView.getGreyView().getVisibility()!=View.VISIBLE) {
                    searchView.getGreyView().setVisibility(View.VISIBLE);
                }
            } else {
                searchView.getGreyView().setVisibility(View.INVISIBLE);
            }
//            if (moveDistance>0) {   //向下滑动，+
//
//            } else {    //向上滑动，-
//
//
//            }

        }

    }


    public boolean isDoOpenSearch() {
        return doOpenSearch;
    }
}
