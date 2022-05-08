package com.ahui.lessonwms.touch;

import static com.ahui.lessonwms.anim.Interpolators.LINEAR;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.text.method.Touch;
import android.util.FloatProperty;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ahui.lessonwms.GBaseActivity;
import com.ahui.lessonwms.GSearchView;
import com.ahui.lessonwms.R;
import com.ahui.lessonwms.anim.AnimationSuccessListener;
import com.ahui.lessonwms.anim.AnimatorPlaybackController;
import com.ahui.lessonwms.anim.GSearchViewControl;
import com.ahui.lessonwms.anim.PendingAnimation;
import com.ahui.lessonwms.anim.StateAnimationConfig;
import com.ahui.lessonwms.utils.Utilities;
import com.blankj.utilcode.util.ScreenUtils;

public class GSearchViewController implements SingleAxisSwipeDetector.Listener {

    private static final String TAG = "GSearchViewController";

    public static final FloatProperty<GSearchViewController> PULL_UP_PROGRESS =
            new FloatProperty<GSearchViewController>("allAppsProgress") {

                @Override
                public Float get(GSearchViewController controller) {
                    return controller.mCurrentProgress;
                }

                @Override
                public void setValue(GSearchViewController controller, float progress) {
                        //得到progress
                    Log.d(TAG, "setValue: progress=" + progress);
                    controller.setProgress(progress);
                }
            };

    protected final SingleAxisSwipeDetector mDetector;
    protected final SingleAxisSwipeDetector.Direction mSwipeDirection;
    private GBaseActivity mLauncher;
    private GSearchViewControl gSearchViewControl;

    public GSearchViewControl getgSearchViewControl() {
        return gSearchViewControl;
    }

    // Ratio of transition process [0, 1] to drag displacement (px)
    private float mProgressMultiplier;

    /**/
    private float mStartMoveGViewShift; //开始移动GView的系数
    private float mEndMoveGViewShift;   //结束移动GView的系数
    private float mStartReWidthShift;   //开始修改GView的Width的系数
    private float mEndReWidthShift;     //结束修改GView的Width的系数


    private static float[] google_grey_view_transy_distance;
    private static float[] google_grey_view_width_distance;

    /*一元二次函数的k,b系数*/
    private static float heightConvertWidhtKCoefficient = 0f;
    private static float heightConvertWidhtBCoefficient = 0f;

    /*动画处理控制器*/
    private AnimatorPlaybackController mCurrentAnimation;

    private float mDisplacementShift;
    private float mStartProgress;   //若是动画进行到一半，那么执行的动画开始的progress不一定是从0开始，但该值默认是从0开始

    private float mCurrentProgress; //当前progress的进度；

    private FlingBlockCheck mFlingBlockCheck = new FlingBlockCheck();
    private boolean mCanBlockFling = false;

    private float mShiftRange;


    public GSearchViewController(GBaseActivity mLauncher, GSearchViewControl searchViewControl) {
        this.mLauncher = mLauncher;
        this.gSearchViewControl = searchViewControl;
        mDetector = new SingleAxisSwipeDetector(this.mLauncher, this, SingleAxisSwipeDetector.VERTICAL);
        mSwipeDirection = SingleAxisSwipeDetector.VERTICAL;

        mShiftRange = this.mLauncher.getResources().getDimension(R.dimen.dimen_140);
        //开始移动GView的区间系数
        mStartMoveGViewShift = this.mLauncher.getResources().getDimension(R.dimen.dimen_13) / mShiftRange;
        mEndMoveGViewShift = this.mLauncher.getResources().getDimension(R.dimen.dimen_118) / mShiftRange;
        //
        mStartReWidthShift = this.mLauncher.getResources().getDimension(R.dimen.dimen_72) / mShiftRange;
        mEndReWidthShift = this.mLauncher.getResources().getDimension(R.dimen.dimen_118) / mShiftRange;

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
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Now figure out which direction scroll events the controller will start
                // calling the callbacks.
                final int directionsToDetectScroll = SingleAxisSwipeDetector.DIRECTION_BOTH;
                ;
                boolean ignoreSlopWhenSettling = true;

                mDetector.setDetectableScrollConditions(
                        directionsToDetectScroll, ignoreSlopWhenSettling);
                break;
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        return mDetector.onTouchEvent(ev);
    }

    public void getState() {
        /*
        * 1.PullUpTouch正在处理这个触摸事件
        * 2.触摸向上走的时候，获取GSerachView的translate
        *   i.若是超过了设定的dimensHeight.heightPiex-140dp,则无效，且不处理向上的手势，只处理向下的手势
        *   ii.若是translate的值高于heihgtPiex则说明View未显示，不处理向下的手势，只处理向上的手势
        *   iii.其它状态都执行向上或是向下的手势滑动
        * */


    }

    public void setProgress(float progress) {
        this.mCurrentProgress = progress;
        int moveDistance = (int) (mShiftRange * progress);
        Log.d(TAG, "setProgress: move Distance, moveDistance=" + moveDistance);
        gSearchViewControl.setProgress(moveDistance);
    }

    private float defaultDuration = 1000;   //默认的动画区间是1000ms

    /*初始化动画*/
    private void initCurrentAnim(int viewTransY, boolean isDragTowardPositive) {

        mStartProgress = 0; //开始的进度初始化为0；


        /*final StateAnimationConfig config = totalShift == 0 ? new StateAnimationConfig()
                : getConfigForStates(mFromState, mToState);
        config.animFlags = updateAnimComponentsOnReinit(animFlags);
        config.duration = maxAccuracy;*/

        mCurrentAnimation = createAnimationToNewWorkspaceInternal().createPlaybackController();

    }

    private PendingAnimation createAnimationToNewWorkspaceInternal(/*final STATE_TYPE state*/) {
        /*if (TestProtocol.sDebugTracing) {
            Log.d(TestProtocol.OVERIEW_NOT_ALLAPPS, "createAnimationToNewWorkspaceInternal: "
                    + state);
        }*/
        //初始化AnimatorSet
        PendingAnimation builder = new PendingAnimation((long) defaultDuration);
        /*if (mConfig.getAnimComponents() != 0) {
            for (StateHandler handler : getStateHandlers()) {
                //触发 @AllAppsTransitionController的setStateWithAnimation（）回调方法
                handler.setStateWithAnimation(state, mConfig, builder); //传入builder填充
            }
        }*/
        setStateWithAnimation(builder);
        builder.addListener(new AnimationSuccessListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                // Change the internal state only when the transition actually starts
//                onStateTransitionStart(state);
                Log.d(TAG, "onAnimationStart: ");
            }

            @Override
            public void onAnimationSuccess(Animator animator) {
//                if (TestProtocol.sDebugTracing) {
//                    Log.d(TestProtocol.OVERIEW_NOT_ALLAPPS, "onAnimationSuccess: " + state);
//                }
//                onStateTransitionEnd(state);
                Log.d(TAG, "onAnimationSuccess: ");
            }
        });
//        mConfig.setAnimation(builder.buildAnim(), state);
        return builder;
    }

    public void setStateWithAnimation(/*LauncherState toState,
                                      StateAnimationConfig config, */PendingAnimation builder) {
//        float targetProgress = toState.getVerticalProgress(mLauncher);
        float targetProgress = getTargetProgress(true); //1或0


        if (Float.compare(mCurrentProgress, targetProgress) == 0) {
            /*if (!config.onlyPlayAtomicComponent()) {
                setAlphas(toState, config, builder);
            }
            // Fail fast
            onProgressAnimationEnd();*/
            //目标状态与当前状态一致，不初始化
            return;
        }

        /*if (config.onlyPlayAtomicComponent()) {
            // There is no atomic component for the all apps transition, so just return early.
            return;
        }*/

//        Interpolator interpolator = config.userControlled ? LINEAR : toState == OVERVIEW
//                ? config.getInterpolator(ANIM_OVERVIEW_SCALE, FAST_OUT_SLOW_IN)
//                : FAST_OUT_SLOW_IN;

        Animator anim = createSpringAnimation(mCurrentProgress, targetProgress);
        anim.setInterpolator(LINEAR);
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d(TAG, "setStateWithAnimation onAnimationEnd: ");
                super.onAnimationEnd(animation);
            }
        });
        builder.add(anim);

//        setAlphas(toState, config, builder);
    }

    public Animator createSpringAnimation(float... progressValues) {
        return ObjectAnimator.ofFloat(this, PULL_UP_PROGRESS, progressValues);
    }

    private float getTargetProgress(boolean isDragTowardPositive) {
        return isDragTowardPositive?1f:0f;
    }

    @Override
    public void onDragStart(boolean start, float startDisplacement) {
        Log.d(TAG, "onDragStart: start=" + start + ",startDisplacement=" + startDisplacement);
//        mStartState = mLauncher.getStateManager().getState();
//        mIsLogContainerSet = false;

//        if (mCurrentAnimation == null) {
//            mFromState = mStartState;
//            mToState = null;
//            cancelAnimationControllers();
//            reinitCurrentAnimation(false, mDetector.wasInitialTouchPositive());
            mDisplacementShift = 0;
//        } else {
//            mCurrentAnimation.pause();
//            mStartProgress = mCurrentAnimation.getProgressFraction();
//
//            mAtomicAnimAutoPlayInfo = null;
//            if (mAtomicComponentsController != null) {
//                mAtomicComponentsController.pause();
//            }
//        }
//        mCanBlockFling = mFromState == NORMAL;  //TODO

        initCurrentAnim(-1, false);

        mFlingBlockCheck.unblockFling();
        mProgressMultiplier = 1/ (float)mLauncher.getResources().getDimension(R.dimen.dimen_140);
        // Must be called after all the animation controllers have been paused
        /*if (mToState == ALL_APPS || mToState == NORMAL) {
            mLauncher.getAllAppsController().onDragStart(mToState == ALL_APPS);
        }*/
    }

    @Override
    public boolean onDrag(float displacement) {
//        Log.d(TAG, "onDrag: displacement=" + displacement);
        float deltaProgress = mProgressMultiplier * (displacement - mDisplacementShift);
        float progress = deltaProgress + mStartProgress;
        updateProgress(progress);
        boolean isDragTowardPositive = mSwipeDirection.isPositive(
                displacement - mDisplacementShift);
        Log.d(TAG, "onDrag: deltaProgress=" + deltaProgress + ",progress=" + progress +",isDragTowardPositive=" + isDragTowardPositive + ",mProgressMultiplier=" + mProgressMultiplier + ",displacement=" + displacement);
        if (progress <= 0) {
            Log.d(TAG, "onDrag: progress smaller 0");
            /*if (reinitCurrentAnimation(false, isDragTowardPositive)) {
                mDisplacementShift = displacement;
                if (mCanBlockFling) {
                    mFlingBlockCheck.blockFling();
                }
            }*/
        } else if (progress >= 1) {
            Log.d(TAG, "onDrag: progress bigger 1");
            /*if (reinitCurrentAnimation(true, isDragTowardPositive)) {
                mDisplacementShift = displacement;
                if (mCanBlockFling) {
                    mFlingBlockCheck.blockFling();
                }
            }*/
        } else {
            mFlingBlockCheck.onEvent();
        }

        return true;
    }

    protected void updateProgress(float fraction) {
        if (mCurrentAnimation == null) {
            Log.d(TAG, "updateProgress: mCurrentAnimation is null");
            return;
        }
        mCurrentAnimation.setPlayFraction(fraction);
        /*if (mAtomicComponentsController != null) {
            // Make sure we don't divide by 0, and have at least a small runway.
            float start = Math.min(mAtomicComponentsStartProgress, 0.9f);
            mAtomicComponentsController.setPlayFraction((fraction - start) / (1 - start));
        }
        maybeUpdateAtomicAnim(mFromState, mToState, fraction);*/
    }


    @Override
    public void onDragEnd(float velocity) {
        boolean fling = mDetector.isFling(velocity);    //是否是快速滑动
        Log.d(TAG, "onDragEnd: velocity=" + velocity + ",fling=" + fling);
//        final int logAction = fling ? Touch.FLING : Touch.SWIPE;

        boolean blockedFling = fling && mFlingBlockCheck.isBlocked();
        if (blockedFling) {
            fling = false;
        }

        final float progress = mCurrentAnimation.getProgressFraction();
        Log.d(TAG, "onDragEnd: progress =" + progress + ",open GV=" + gSearchViewControl.isDoOpenSearch());
        if (progress >= 1 && gSearchViewControl.isDoOpenSearch()) {
            gSearchViewControl.scaleOvalXY();
            gSearchViewControl.resetOpenSearch();
            if (mCurrentAnimation!=null) {
                mCurrentProgress=0;
                mCurrentAnimation.pause();
            }
        } else if (progress>0&&progress<1) {
            //区间内需要自动补偿动画
            //todo
        }

        /*final LauncherState targetState;
        final float progress = mCurrentAnimation.getProgressFraction();
        final float progressVelocity = velocity * mProgressMultiplier;
        final float interpolatedProgress = mCurrentAnimation.getInterpolatedProgress();
        if (fling) {
            targetState =
                    Float.compare(Math.signum(velocity), Math.signum(mProgressMultiplier)) == 0
                            ? mToState : mFromState;
            // snap to top or bottom using the release velocity
        } else {
            float successProgress = mToState == ALL_APPS
                    ? MIN_PROGRESS_TO_ALL_APPS : SUCCESS_TRANSITION_PROGRESS;
            targetState = (interpolatedProgress > successProgress) ? mToState : mFromState;
        }

        final float endProgress;
        final float startProgress;
        final long duration;
        // Increase the duration if we prevented the fling, as we are going against a high velocity.
        final int durationMultiplier = blockedFling && targetState == mFromState
                ? LauncherAnimUtils.blockedFlingDurationFactor(velocity) : 1;

        if (targetState == mToState) {
            endProgress = 1;
            if (progress >= 1) {
                duration = 0;
                startProgress = 1;
            } else {
                startProgress = Utilities.boundToRange(progress
                        + progressVelocity * getSingleFrameMs(mLauncher), 0f, 1f);
                duration = BaseSwipeDetector.calculateDuration(velocity,
                        endProgress - Math.max(progress, 0)) * durationMultiplier;
            }
        } else {
            // Let the state manager know that the animation didn't go to the target state,
            // but don't cancel ourselves (we already clean up when the animation completes).
            mCurrentAnimation.dispatchOnCancelWithoutCancelRunnable();

            endProgress = 0;
            if (progress <= 0) {
                duration = 0;
                startProgress = 0;
            } else {
                startProgress = Utilities.boundToRange(progress
                        + progressVelocity * getSingleFrameMs(mLauncher), 0f, 1f);
                duration = BaseSwipeDetector.calculateDuration(velocity,
                        Math.min(progress, 1) - endProgress) * durationMultiplier;
            }
        }

        mCurrentAnimation.setEndAction(() -> onSwipeInteractionCompleted(targetState, logAction));
        ValueAnimator anim = mCurrentAnimation.getAnimationPlayer();
        anim.setFloatValues(startProgress, endProgress);
        maybeUpdateAtomicAnim(mFromState, targetState, targetState == mToState ? 1f : 0f);
        updateSwipeCompleteAnimation(anim, Math.max(duration, getRemainingAtomicDuration()),
                targetState, velocity, fling);
        mCurrentAnimation.dispatchOnStart();
        if (fling && targetState == LauncherState.ALL_APPS && !UNSTABLE_SPRINGS.get()) {
            mLauncher.getAppsView().addSpringFromFlingUpdateListener(anim, velocity);
        }
        anim.start();
        mAtomicAnimAutoPlayInfo = new AutoPlayAtomicAnimationInfo(endProgress, anim.getDuration());
        maybeAutoPlayAtomicComponentsAnim();*/
    }

}
