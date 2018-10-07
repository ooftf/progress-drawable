package com.ooftf.progress;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;


/**
 * @author ooftf
 * @email 994749769@qq.com
 * @date 2018/9/29 0029
 */

public class ShuntHorizontalProgressDrawable extends Drawable implements Animatable {
    private static final int DURATION_MILLIS = 500;
    private Line mLine;
    private Context mContext;
    private Animation mAnimation;
    private View mParent;

    public ShuntHorizontalProgressDrawable(Context context, View parent) {
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
        mAnimation = animation;
    }

    @Override
    public void start() {
        this.mAnimation.reset();
        this.mParent.startAnimation(this.mAnimation);

    }


    public void setColors(int... colors) {
        mLine.colors = colors;
        invalidateSelf();
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
    public boolean isRunning() {
        if (mAnimation.hasStarted() && !mAnimation.hasEnded()) {
            return true;
        }
        return false;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        mLine.draw(canvas, this.getBounds());
    }

    @Override
    public void setAlpha(int alpha) {
        mLine.mPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mLine.mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public static class Line {

        float progress;
        int[] colors;
        final Paint mPaint = new Paint();

        Line(Resources res) {
            colors = new int[]{res.getColor(R.color.holo_blue_bright), res.getColor(R.color.holo_green_light), res.getColor(R.color.holo_orange_light), res.getColor(R.color.holo_red_light)};
            this.mPaint.setStrokeCap(Paint.Cap.SQUARE);
            this.mPaint.setAntiAlias(true);
            this.mPaint.setStyle(Paint.Style.STROKE);
        }

        void draw(Canvas c, Rect bounds) {
            mPaint.setStyle(Paint.Style.FILL);

            float perWidth = bounds.width() / 2f / (colors.length - 1f);
            float totalOffset = perWidth * colors.length;
            float radius = Math.min(perWidth, bounds.height()) / 2;
            float mid = (bounds.left + bounds.right) / 2f;
            for (int i = 0; i < colors.length; i++) {
                mPaint.setColor(colors[i]);
                float currentProgress = (progress + (float) i / colors.length) % 1;
                float offset = totalOffset * currentProgress;
                float leftEnd = mid - offset;
                float rightEnd = mid + offset;
                float leftStart = leftEnd + perWidth;
                float rightStart = rightEnd - perWidth;
                leftStart = Math.min(leftStart, mid);
                rightStart = Math.max(rightStart, mid);
                c.drawRoundRect(new RectF(leftEnd, bounds.top, leftStart, bounds.bottom), radius, radius, mPaint);
                c.drawRoundRect(new RectF(rightStart, bounds.top, rightEnd, bounds.bottom), radius, radius, mPaint);
            }
        }


        public void setProgress(float interpolatedTime) {
            progress = interpolatedTime;
        }
    }
}
