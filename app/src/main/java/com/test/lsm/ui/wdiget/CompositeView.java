package com.test.lsm.ui.wdiget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.PowerManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.swm.algorithm.Algorithm;
import com.swm.algorithm.support.IirFilter;
import com.test.lsm.R;


/**
 * Created by yangzhenyu on 2016/9/29.
 */

public class CompositeView extends SurfaceView {
    private static final String TAG = CompositeView.class.getSimpleName();

    private static final int SAMPLE_COUNT = 5*5;
    private static final double IIR_COEFF = 0.992;
    private Bitmap mBackgroundBitmap;
    private Canvas mBackgroundDrawing;

    private Paint mEcgPaint;

    private float mEcgZeroShift = 360;
    private float mEcgHeight;
    private float mEcgTop;
    private float mEcgBottom;

    private static final int GAP = 3;
    private int mMaxWidth;
    private int mHeight;
    private Paint mClearPaint;
    private SurfaceHolder mHolder;

    private boolean mRendering = false;

    private float mDensity;
    private float mPilotSize;
    private static final int PILOT_SIZE = 40;//dp
    private static final int LINE_SIZE = 3;//dp

    int x1=0;
    float y1 = mEcgZeroShift;
    float mScale = 1;
    float mAmp = 1;
    float mLowestValue = Integer.MAX_VALUE;
    float mHighestValue = Integer.MIN_VALUE;
    IirFilter mFilter;
    Integer[] pFilteredOut = new Integer[SAMPLE_COUNT];
    int filteredOutLength = 0;

    private SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.v("drawing", "surfaceCreated");
            mHolder = holder;
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            PowerManager pm = (PowerManager) getContext().getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn = false;
            if (Build.VERSION.SDK_INT < 21)
                isScreenOn = pm.isScreenOn();
            else
                isScreenOn = pm.isInteractive();

            Log.v("drawing", "surfaceChanged: " + isScreenOn);
            if(!isScreenOn)
                stopDrawing();
            else
                startDrawing();

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.v("drawing", "surfaceDestroyed");
            stopDrawing();
        }
    };

    public void addEcgRawData(short[] ecgRawData, int heartRate, int breathRate) {
        if (!mRendering)
            return;

        for(int i = 0; i < ecgRawData.length; i++) {
            pFilteredOut[filteredOutLength+i] = Integer.valueOf(mFilter.filter(Integer.valueOf(ecgRawData[i])));
        }
        filteredOutLength += ecgRawData.length;

        if (filteredOutLength < SAMPLE_COUNT)
            return;

//        EPLog.d(TAG, "pFilteredOut="+ pFilteredOut[0]+" "+pFilteredOut[1]+" "+pFilteredOut[2]+" "+pFilteredOut[3]+" "+pFilteredOut[4]);
        if (x1 >= mMaxWidth) {
            x1 = 0;
        }

        if (mBackgroundBitmap == null) {
            mBackgroundBitmap = Bitmap.createBitmap(mMaxWidth, mHeight, Bitmap.Config.ARGB_8888);
            mBackgroundDrawing = new Canvas();
            mBackgroundDrawing.setBitmap(mBackgroundBitmap);
        }

        for (int value : pFilteredOut) {
            if (x1 % 250 == 0 && x1!=0) {
                scale();
            }

            float y2 = (-value * mScale + mEcgZeroShift);
//EPLog.d(TAG, "x1="+x1+" value="+value+" mScale="+mScale+" y2="+y2);
            if (value < mLowestValue)
                mLowestValue = value;

            if (value > mHighestValue)
                mHighestValue = value;

            int x2 = (x1+1);
            mBackgroundDrawing.drawRect(x1, mEcgTop-GAP-2, x1+mPilotSize, mEcgBottom+GAP+2, mClearPaint);
            mBackgroundDrawing.drawLine(x1, y1, x2, y2, mEcgPaint);
            x1 = x2;
            y1 = y2;
        }

        Canvas canvas = mHolder.lockCanvas();
        if (canvas != null) {
            canvas.drawBitmap(mBackgroundBitmap, 0, 0, null);
            mHolder.unlockCanvasAndPost(canvas);
        }
        filteredOutLength = 0;
    }

    private void scale() {
        if(Math.abs(mHighestValue - mLowestValue) == mAmp)
            return;

        if (Math.abs(mHighestValue - mLowestValue) < 100)
            return;

        mAmp = Math.abs(mHighestValue - mLowestValue);
        mScale = mEcgHeight / mAmp * 0.7f;
//        EPLog.d(TAG, "mEcgHeight="+mEcgHeight+" mAmp="+mAmp+" mHighestValue="+mHighestValue+" mLowestValue="+mLowestValue);
        // Reset high/low value to research highest / lowest value
        mHighestValue = Integer.MIN_VALUE;  // Initial highest value, should be smallest value compare to real signal
        mLowestValue = Integer.MAX_VALUE;   // Initial lowest value, should be greatest value compare to real singal
    }

    public CompositeView(Context context) {
        this(context, null);
    }

    public CompositeView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CompositeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(mSurfaceHolderCallback);
        init();

    }

    private void init() {
        mFilter = Algorithm.newIirFilterInstance();
        mDensity = getResources().getDisplayMetrics().density;
        mPilotSize = PILOT_SIZE * mDensity;

        mEcgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mEcgPaint.setColor(getResources().getColor(R.color.swm_ecg_color));
        mEcgPaint.setStrokeWidth(LINE_SIZE * mDensity);

        mClearPaint = new Paint();
        mClearPaint.setColor(Color.BLACK);
    }

    private synchronized void stopDrawing() {
        if(!mRendering)
            return;

        mRendering = false;

        if (mBackgroundBitmap != null) {
            mBackgroundBitmap.recycle();
            mBackgroundBitmap = null;
        }
    }

    private synchronized void startDrawing() {
        if(mRendering)
            return;

        mRendering = true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mHeight = bottom - top;
        mMaxWidth = right - left;

        mEcgHeight = mHeight;
        mEcgTop = top;
        mEcgBottom = mEcgTop + mEcgHeight;
        mEcgZeroShift = mEcgTop + mEcgHeight / 2;

    }

}
