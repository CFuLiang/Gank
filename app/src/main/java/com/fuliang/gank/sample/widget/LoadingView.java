package com.fuliang.gank.sample.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.fuliang.gank.sample.R;

/**
 * loading动画，包含加载动画和各种加载完成后的状态
 */
public class LoadingView extends View {


    /*动画的状态 */
    public static final int STATE_LOADING = 0;
    public static final int STATE_SUCCESS = 1;
    public static final int STATE_FAILED = 2;
    public static final int STATE_EMPTY_RESULT = 3;

    private static final int ANIM_STATE_PINING = 0;            //正在加载
    private static final int ANIM_STATE_WILL_END_PINING = 1;   //即将停止加载动画
    private static final int ANIM_STATE_END_PINING = 2;        //已经停止加载动画
    private static final int ANIM_STATE_ALL_END = 3;

    private static final int PADDING = 5;

    private int mViewState;
    private boolean isReverse;  //当弧度达到最大弧度时，弧度减小，弧度达到最小弧度时，弧度增大
    private int endAngle = 0;  //用来标记结束弧度

    private int animationState; //动画状态

    private float mProgress;                //Loading动画的进度
    private float mEndAnimationProgress;    //状态动画进度
    private float mEndAnimationProgress2;   //failed状态时,第二部分动画的进度

    private RectF mRectF;
    private float mRadius;    //半径
    private int mLineWith;    //圆形线的宽度

    private int mColorNormal;
    private int mColorSuccess;
    private int mColorFailed;
    private int mColorEmptyResult;

    private Paint rimPaint = new Paint();
    //成功状态的paint和path
    private Path mSuccessPath;
    private Paint mSuccessPaint;
    private float mSuccessPathLength;
    private float[] mSuccessPathIntervals;

    //失败状态的paint和path
    private Paint mFailedPaint;
    private Path mFailedPath;
    private Path mFailedPath2;
    private float mFailedPathLength;
    private float[] mFailedPathIntervals;

    //空结果状态的paint和path
    private Paint mEmptyResultPaint;
    private Path mEmptyResultPath;
    private Path mEmptyResultPath2;
    private float mEmptyResultPathLength;

    public LoadingView(Context context) {
        super(context);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context,attrs);
        setupAttr(context,attrs);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,attrs, defStyleAttr);
        setupAttr(context,attrs);
        init();
    }

    private void setupAttr(Context context, AttributeSet attrs){

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LoadingView, 0, 0);
        int size = ta.getInt(R.styleable.LoadingView_view_size,1);
        if(size == 1){
            mRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,24,getResources().getDisplayMetrics());
            mLineWith = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,4,getResources().getDisplayMetrics());
        }else{
            mRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,16,getResources().getDisplayMetrics());
            mLineWith = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,3,getResources().getDisplayMetrics());
        }
        float edge = PADDING + mRadius *2 + mLineWith;
        mRectF = new RectF(PADDING+mLineWith,PADDING+mLineWith,edge,edge);
        mColorNormal = ta.getColor(R.styleable.LoadingView_color_normal,0xff666666);
        mColorSuccess = ta.getColor(R.styleable.LoadingView_color_success, Color.GREEN);
        mColorFailed = ta.getColor(R.styleable.LoadingView_color_failed, Color.RED);
        mColorEmptyResult = ta.getColor(R.styleable.LoadingView_color_empty,0xfffdd835);
        ta.recycle();
    }

    private void init(){

        endAngle = 35;
        isReverse = false;
        animationState = ANIM_STATE_PINING;

        rimPaint.setColor(mColorNormal);
        rimPaint.setAntiAlias(true);
        rimPaint.setStyle(Paint.Style.STROKE);
        rimPaint.setStrokeWidth(mLineWith);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size  = measureDimension((int)(PADDING + mRadius + mLineWith)*2,widthMeasureSpec);
        setMeasuredDimension(size,size);
    }

    private int measureDimension(int defaultSize, int measureSpec) {

        int result;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        }
        else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        }
        else {
            result = defaultSize;
        }
        return result;
    }


    private void createSuccessPath(){

        mSuccessPaint  = new Paint();
        mSuccessPaint.setColor(mColorSuccess);
        mSuccessPaint.setAntiAlias(true);
        mSuccessPaint.setStyle(Paint.Style.STROKE);
        mSuccessPaint.setStrokeWidth(mLineWith);

        float left = PADDING + mRadius /3 + mLineWith;
        float top = PADDING + mRadius /2 + mLineWith;
        float right = PADDING + mLineWith + 2* mRadius - mRadius /2;
        float bottom = PADDING + mLineWith + mRadius + mRadius /2 + mLineWith/2;
        float xPoint = PADDING + mRadius /6*5 + mLineWith;

        mSuccessPath = new Path();
        mSuccessPath.moveTo(left,PADDING + mRadius + mLineWith);
        mSuccessPath.lineTo(xPoint,bottom);
        mSuccessPath.lineTo(right,top);
        PathMeasure measure = new PathMeasure(mSuccessPath, false);
        mSuccessPathLength = measure.getLength();
        mSuccessPathIntervals = new float[]{mSuccessPathLength, mSuccessPathLength};
    }

    private void createFailedPath(){

        mFailedPaint = new Paint();
        mFailedPaint.setColor(mColorFailed);
        mFailedPaint.setAntiAlias(true);
        mFailedPaint.setStyle(Paint.Style.STROKE);
        mFailedPaint.setStrokeWidth(mLineWith+1);

        float left = PADDING+ mRadius +mLineWith;
        float top = PADDING + mLineWith + mRadius /4;
        float bottom = PADDING+mLineWith+ mRadius *2- mRadius /4;

        mFailedPath = new Path();
        mFailedPath.moveTo(left,top);
        mFailedPath.lineTo(left,top+ mRadius);
        PathMeasure measure = new PathMeasure(mFailedPath, false);
        mFailedPathLength = measure.getLength();
        mFailedPathIntervals = new float[]{mFailedPathLength, mFailedPathLength};

        mFailedPath2 = new Path();
        mFailedPath2.moveTo(left,bottom- mRadius /3);
        mFailedPath2.lineTo(left,bottom);
    }

    private void createNoResultPath(){

        mEmptyResultPaint = new Paint();
        mEmptyResultPaint.setColor(mColorEmptyResult);
        mEmptyResultPaint.setAntiAlias(true);
        mEmptyResultPaint.setStyle(Paint.Style.STROKE);
        mEmptyResultPaint.setStrokeWidth(mLineWith+1);

        float left = PADDING+ mRadius /2+mLineWith;
        float top = PADDING + mLineWith + mRadius;
        float right = left+ mRadius;

        mEmptyResultPath = new Path();
        mEmptyResultPath.moveTo(left+ mRadius /2,top);
        mEmptyResultPath.lineTo(left,top);

        mEmptyResultPath2 = new Path();
        mEmptyResultPath2.moveTo(left+ mRadius /2,top);
        mEmptyResultPath2.lineTo(right,top);

        mEmptyResultPathLength = mRadius /2;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawView(canvas);
    }


    private void drawView(Canvas c)
    {
        mProgress += 8;

        if(animationState == ANIM_STATE_END_PINING){
            if(mViewState == STATE_SUCCESS){
                if(mSuccessPath == null){
                    createSuccessPath();
                }
                mEndAnimationProgress += 0.05;
                PathEffect effect = new DashPathEffect(mSuccessPathIntervals, mSuccessPathLength - mSuccessPathLength*mEndAnimationProgress);
                mSuccessPaint.setPathEffect(effect);
                c.drawPath(mSuccessPath,mSuccessPaint);
                if(mEndAnimationProgress >= 1){
                    mEndAnimationProgress = 0.0f;
                    animationState = ANIM_STATE_ALL_END;
                }
            }

            if(mViewState == STATE_FAILED){
                if(mFailedPath == null){
                    createFailedPath();
                }
                mEndAnimationProgress += 0.08;
                PathEffect effect = new DashPathEffect(mFailedPathIntervals, mFailedPathLength - mFailedPathLength*mEndAnimationProgress);
                mFailedPaint.setPathEffect(effect);
                c.drawPath(mFailedPath,mFailedPaint);
                if(mEndAnimationProgress >= 1){
                    mEndAnimationProgress = 1.0f;
                    mEndAnimationProgress2 += 0.1;
                    effect = new DashPathEffect(new float[]{mRadius /3, mRadius /3}, mRadius /3 - mRadius /3*mEndAnimationProgress2);
                    mFailedPaint.setPathEffect(effect);
                    c.drawPath(mFailedPath2,mFailedPaint);
                    if(mEndAnimationProgress2 >= 1){
                        mEndAnimationProgress = 0.0f;
                        mEndAnimationProgress2 = 0.0f;
                        animationState = ANIM_STATE_ALL_END;
                        playShakeAnimation();
                    }
                }
            }

            if(mViewState == STATE_EMPTY_RESULT){
                if(mEmptyResultPaint == null){
                    createNoResultPath();
                }
                mEndAnimationProgress += 0.05;
                PathEffect effect = new DashPathEffect(new float[]{mEmptyResultPathLength, mEmptyResultPathLength}, mEmptyResultPathLength - mEmptyResultPathLength *mEndAnimationProgress);
                mEmptyResultPaint.setPathEffect(effect);
                c.drawPath(mEmptyResultPath, mEmptyResultPaint);
                c.drawPath(mEmptyResultPath2, mEmptyResultPaint);
                if(mEndAnimationProgress >= 1){
                    mEndAnimationProgress = 0.0f;
                    animationState = ANIM_STATE_ALL_END;
                }
            }
        }

        if(animationState == ANIM_STATE_WILL_END_PINING){
            if(endAngle < 360) {
                endAngle += 5;
                if(endAngle == 360){
                    setEndPaintColor();
                }
            }
        }

        if(animationState == ANIM_STATE_PINING){
            if(!isReverse && endAngle <= 325){
                endAngle +=5;
                if(endAngle > 325){
                    isReverse = true;
                }
            }

            if(isReverse && endAngle >= 35){
                endAngle -= 5;
                if(endAngle == 30){
                    isReverse = false;
                }
            }
        }
        c.drawArc(mRectF, mProgress%360, endAngle, false, rimPaint);
        if(animationState != ANIM_STATE_ALL_END) invalidate();
    }

    /**
     * 加载动画结束，显示加载结果
     * @param state
     * STATE_LOADING 加载
     * STATE_SUCCESS 成功
     * STATE_FAILED 失败，显示错误
     * STATE_EMPTY_RESULT 无数据
     */
    public void setViewState(int state){
        mViewState = state;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mViewState != STATE_LOADING){
                    animationState = ANIM_STATE_WILL_END_PINING;
                    return;
                }
                animationState = ANIM_STATE_PINING;
                mProgress = 0;
                endAngle = 35;
                rimPaint.setColor(mColorNormal);
                invalidate();
            }
        },100);
    }

    /**
     * 设置动画从loading状态到其他状态的时的画笔的颜色
     */
    private void setEndPaintColor() {
        if(mViewState == STATE_SUCCESS){
            rimPaint.setColor(mColorSuccess);
        }else if(mViewState == STATE_FAILED){
            rimPaint.setColor(mColorFailed);
        }else{
            rimPaint.setColor(mColorEmptyResult);
        }
        animationState = ANIM_STATE_END_PINING;

    }

    /**
     * 播放failed状态的shake动画
     */
    private void playShakeAnimation(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(this,"rotation",0.0f,15.0f);
        animator.setDuration(100);
        animator.setRepeatCount(3);
        animator.setRepeatMode(ObjectAnimator.REVERSE);
        animator.start();
    }

    public int getViewState(){
        return mViewState;
    }

    public void setNormalColor(int color){
        mColorNormal = color;
        rimPaint.setColor(color);
    }

    public void setSuccessColor(int color){
        mColorSuccess = color;
        mSuccessPaint.setColor(color);
    }

    public void setFailedColor(int color){
        mColorFailed = color;
        mFailedPaint.setColor(color);
    }

    public void setEmptyResultColor(int color){
        mColorEmptyResult = color;
        mEmptyResultPaint.setColor(color);
    }

}
