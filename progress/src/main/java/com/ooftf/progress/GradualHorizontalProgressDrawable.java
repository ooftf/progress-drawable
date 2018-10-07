package com.ooftf.progress;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * 渐变色进度条
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2018/9/29 0029
 */

public class GradualHorizontalProgressDrawable extends Drawable implements Animatable {
    public static final int DURATION_MILLIS = 666;
    Line mLine;
    Context mContext;
    private Animation mAnimation;
    private View mParent;

    public GradualHorizontalProgressDrawable(Context context, View parent) {
        mContext = context;
        mParent = parent;
        intrinsicHeight = mContext.getResources().getDimensionPixelSize(R.dimen.HorizontalProgressDrawable_height_default);
        intrinsicWidth = mContext.getResources().getDimensionPixelSize(R.dimen.HorizontalProgressDrawable_height_default);
        mLine = new Line(mContext.getResources());
        setupAnimator();
    }


    private void setupAnimator() {
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                mLine.setProgress(interpolatedTime);
                invalidateSelf();
            }


        };
        animation.setDuration(DURATION_MILLIS);
        animation.setRepeatCount(ValueAnimator.INFINITE);
        animation.setRepeatMode(ValueAnimator.RESTART);
        animation.setInterpolator(new LinearInterpolator());
        animation.setAnimationListener(new EmptyAnimationListener() {

            @Override
            public void onAnimationRepeat(Animation animation) {
                mLine.next();
            }
        });
        mAnimation = animation;
    }

    public void setColors(int... colors) {
        mLine.setColors(colors);
        invalidateSelf();
    }

    public void setDuration(long durationMillis) {
        mAnimation.setDuration(durationMillis);
    }

    @Override
    public void start() {
        this.mAnimation.reset();
        this.mParent.startAnimation(this.mAnimation);

    }

    @Override
    public void stop() {
        this.mParent.clearAnimation();
    }

    @Override
    public int getIntrinsicHeight() {
        return intrinsicHeight;
    }

    int intrinsicWidth;
    int intrinsicHeight;

    @Override
    public int getIntrinsicWidth() {
        return intrinsicWidth;
    }

    public void setIntrinsicHeight(int intrinsicHeight) {
        this.intrinsicHeight = intrinsicHeight;
    }

    public void setIntrinsicWidth(int intrinsicWidth) {
        this.intrinsicWidth = intrinsicWidth;
    }

    @Override
    public void setBounds(Rect bounds) {
        super.setBounds(bounds);
    }

    @Override
    public boolean isRunning() {
        if (mAnimation.hasStarted() && !mAnimation.hasEnded()) {
            return true;
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        mLine.draw(canvas, this.getBounds());
    }

    @Override
    public void setAlpha(int alpha) {
        mLine.mPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mLine.mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public static class Line {
        int current = 0;
        float progress;
        int[] colors;
        float[] positions;
        final Paint mPaint = new Paint();

        Line(Resources res) {
            colors = new int[]{res.getColor(R.color.holo_blue_bright), res.getColor(R.color.holo_green_light), res.getColor(R.color.holo_orange_light), res.getColor(R.color.holo_red_light)};
            positions = new float[colors.length];
            for (int i = 0; i < colors.length; i++) {
                positions[i] = (float) i / (positions.length - 1);
            }
            this.mPaint.setStrokeCap(Paint.Cap.SQUARE);
            this.mPaint.setAntiAlias(true);
            this.mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStyle(Paint.Style.FILL);
        }

        void next() {
            current = (current + 1) % 2;
        }

        void draw(Canvas c, Rect bounds) {
            float left = bounds.left + bounds.width() * (current + progress);
            LinearGradient linearGradient = new LinearGradient(left, bounds.top, left + bounds.width(), bounds.bottom, colors, positions, Shader.TileMode.MIRROR);
            mPaint.setShader(linearGradient);
            c.drawRect(bounds, mPaint);
        }

        public void setColors(int[] colors) {
            this.colors = colors;
            positions = new float[colors.length];
            for (int i = 0; i < colors.length; i++) {
                positions[i] = (float) i / (positions.length - 1);
            }
        }

        public void setProgress(float interpolatedTime) {
            progress = interpolatedTime;
        }
    }
}
