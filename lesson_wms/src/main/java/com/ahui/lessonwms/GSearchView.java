package com.ahui.lessonwms;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class GSearchView extends FrameLayout {
    public static final String SEARCH_VIEW_TAG = "GSearchTag";
    private Context mCtx;
    private ConstraintLayout googleLayoutView;
    private FrameLayout scrimFl;
    private View ovalView;
    private View greyView;

    public GSearchView(@NonNull Context context) {
        super(context);
        this.mCtx = context;
        init();
    }

    public GSearchView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mCtx=context;
        init();
    }

    private void init() {
        View baseView = LayoutInflater.from(mCtx).inflate(R.layout.google_search_layout, this, true);
        googleLayoutView=baseView.findViewById(R.id.google_icon_layout);
        ovalView=baseView.findViewById(R.id.ovel_view);
        scrimFl=baseView.findViewById(R.id.scrim_fl);
        greyView=baseView.findViewById(R.id.grey_view);

        baseView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);

                //修改Oval的大小
                FrameLayout.LayoutParams layoutParams = (LayoutParams) ovalView.getLayoutParams();
                layoutParams.width = (int) getResources().getDimension(R.dimen.dimen_412);
                layoutParams.height = (int) getResources().getDimension(R.dimen.dimen_231);
                ovalView.setLayoutParams(layoutParams);

                return true;
            }
        });


    }

    public ConstraintLayout getGoogleLayoutView() {
        return googleLayoutView;
    }

    public View getOvalView() {
        return ovalView;
    }

    public FrameLayout getScrimFl() {
        return scrimFl;
    }

    public View getGreyView() {
        return greyView;
    }
}
