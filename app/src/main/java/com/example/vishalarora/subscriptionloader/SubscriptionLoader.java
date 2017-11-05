package com.example.vishalarora.subscriptionloader;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by vishalarora on 04/11/17.
 */

public class SubscriptionLoader extends View {

    private static final String TAG = SubscriptionLoader.class.getSimpleName();
    Paint arcPaint;
    Drawable srcDrawable;
    float startArcAngle = 0;
    float endArcAngle = 360;

    int arcMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
    int arcStrokeWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
    int bitmapMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());

    int drawableMargin = arcMargin + arcStrokeWidth + bitmapMargin;

    int width;
    int height;

    public SubscriptionLoader(Context context) {
        super(context);
    }

    public SubscriptionLoader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        arcPaint = new Paint();
        arcPaint.setColor(Color.parseColor("#38414d"));
        arcPaint.setStrokeWidth(arcStrokeWidth);
        arcPaint.setAntiAlias(true);
        arcPaint.setStyle(Paint.Style.STROKE);
        srcDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.subscription_logo_loader, context.getTheme());
    }

    public SubscriptionLoader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // start the animation here
        startAnimation();
    }

    ValueAnimator animator = ValueAnimator.ofFloat(-180, 180);

    float rotateAngle = 0;

    private void startAnimation() {
        animator.setDuration(800);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                rotateAngle = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    private void stopAnimation() {
        animator.cancel();
        animator.removeAllListeners();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // stop the animation here
        stopAnimation();
    }

    int drawableWidth;
    int drawableHeight;
    int drawableLeft;
    int drawableTop;
    int drawableCenterX;
    int drawableCenterY;
    RectF rect = new RectF();


    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        // draw the outer arc
        canvas.drawArc(rect, startArcAngle, endArcAngle, false, arcPaint);

        // set the bounds to canvas bounds
        srcDrawable.setBounds(0, 0, drawableWidth, drawableHeight);

        canvas.scale(rotateAngle / 180, 1, drawableCenterX, drawableCenterY);

        // translate the canvas to draw the drawable on canvas
        canvas.translate(drawableLeft, drawableTop);

        // draw the drawable on the canvas
        srcDrawable.draw(canvas);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = View.MeasureSpec.getSize(widthMeasureSpec);
        height = View.MeasureSpec.getSize(heightMeasureSpec);
        drawableWidth = width - 2 * (drawableMargin);
        drawableHeight = height - 2 * (drawableMargin);
        drawableLeft = (width - drawableWidth) / 2;
        drawableTop = (height - drawableHeight) / 2;
        drawableCenterX = drawableLeft + drawableWidth / 2;
        drawableCenterY = drawableTop + drawableHeight / 2;
        rect.top = arcMargin;
        rect.left = arcMargin;
        rect.right = width - arcMargin;
        rect.bottom = height - arcMargin;
    }

}
