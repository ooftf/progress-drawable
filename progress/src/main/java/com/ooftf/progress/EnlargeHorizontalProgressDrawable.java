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

public class EnlargeHorizontalProgressDrawable extends Drawable implements Animatable {
    public static final int DURATION_MILLIS = 500;
    Line mLine;
    Context mContext;
    private Animation mAnimation;
    private View mParent;

    public EnlargeHorizontalProgressDrawable(Context context, View parent) {
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
                mLine.nextColor();
            }
        });
        mAnimation = animation;
    }

    public void setColors(int... colors) {
        mLine.colors = colors;
        invalidateSelf();
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
    public void setBounds(@NonNull Rect bounds) {
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
        int current = 0;
        float progress;
        int[] colors;
        final Paint mPaint = new Paint();

        Line(Resources res) {
            colors = new int[]{res.getColor(R.color.holo_blue_bright), res.getColor(R.color.holo_green_light), res.getColor(R.color.holo_orange_light), res.getColor(R.color.holo_red_light)};
            this.mPaint.setStrokeCap(Paint.Cap.SQUARE);
            this.mPaint.setAntiAlias(true);
            this.mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStyle(Paint.Style.FILL);
        }

        int getBackgroundColor() {
            return colors[(current) % colors.length];
        }

        int getAnimationColor() {
            return colors[(current + 1) % colors.length];
        }

        void nextColor() {
            current = (current + 1) % colors.length;
        }

        void draw(Canvas c, Rect bounds) {

            float radius = Math.min(bounds.width(), bounds.height()) / 2;
            mPaint.setColor(getBackgroundColor());

            //绘画背景线
            c.drawRoundRect(new RectF(bounds.left, bounds.top, bounds.right, bounds.bottom), radius, radius, mPaint);
            //绘制上层线
            mPaint.setColor(getAnimationColor());
            float width = bounds.width() * progress;
            float mid = (bounds.left + bounds.right) / 2;
            c.drawRoundRect(new RectF(mid - width / 2, bounds.top, mid + width / 2, bounds.bottom), radius, radius, mPaint);
        }


        public void setProgress(float interpolatedTime) {
            progress = interpolatedTime;
        }
    }
}
