/*
 * Copyright (c) 2015 Prat Tanapaisankit and Intellibins authors
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *  Neither the name of The Intern nor the names of its contributors may
 * be used to endorse or promote products derived from this software
 * without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE LISTED COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.prt2121.tutorialview;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by pt2121 on 4/4/15.
 * A overlay view which contains tutorial text
 */
public final class TutorialView extends RelativeLayout implements View.OnTouchListener, ITutorialView {

    private final AnimationFactory animationFactory;

    private final TextDrawer textDrawer;

    private long fadeInMillis;

    private long fadeOutMillis;

    private boolean isShowing;

    private TutorialViewEventListener mEventListener = TutorialViewEventListener.NONE;

    protected TutorialView(Context context) {
        this(context, null);
    }

    protected TutorialView(Context context, AttributeSet attrs) {
        super(context, attrs);
        animationFactory = new AnimatorAnimationFactory();
        AreaCalculator areaCalculator = new AreaCalculator();
        textDrawer = new TextDrawer(getResources(), areaCalculator, getContext());
        TypedArray a = context.getTheme()
                .obtainStyledAttributes(attrs, R.styleable.TutorialView, 0, 0);
        try {
            int backgroundColor = a.getColor(R.styleable.TutorialView_tv_backgroundColor,
                    Color.argb(128, 0, 173, 159));
            int textColor = a.getColor(R.styleable.TutorialView_tv_textColor,
                    Color.rgb(0, 0, 0));

            fadeInMillis = getResources().getInteger(android.R.integer.config_mediumAnimTime);
            fadeOutMillis = getResources().getInteger(android.R.integer.config_mediumAnimTime);

            setBackgroundColor(backgroundColor);
            setTextColor(textColor);
            setOnTouchListener(this);
        } finally {
            a.recycle();
        }
    }

    private static void insertTutorialView(TutorialView tutorialView, Activity activity) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM
        );

        ((ViewGroup) activity.findViewById(android.R.id.content)).addView(tutorialView, params);
        tutorialView.show();
    }

    public void setTutorialViewEventListener(TutorialViewEventListener listener) {
        if (listener != null) {
            mEventListener = listener;
        } else {
            mEventListener = TutorialViewEventListener.NONE;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = MeasureSpec.getSize(heightMeasureSpec);
        // TODO let user set height
        setMeasuredDimension(widthMeasureSpec, height * 2 / 5);
    }

    @Override
    public void show() {
        isShowing = true;
        mEventListener.onTutorialViewShow(this);
        fadeInShowcase();
    }

    @Override
    public void setText(CharSequence text) {
        textDrawer.setText(text);
    }

    private void setTextColor(int color) {
        textDrawer.setTextColor(color);
    }

    private void fadeInShowcase() {
        animationFactory.fadeInView(this, fadeInMillis,
                new AnimationFactory.AnimationStartListener() {
                    @Override
                    public void onAnimationStart() {
                        setVisibility(View.VISIBLE);
                    }
                }
        );
    }

    @Override
    public void hide() {
        mEventListener.onTutorialViewHide(this);
        fadeOutShowcase();
    }

    private void fadeOutShowcase() {
        animationFactory.fadeOutView(this, fadeOutMillis, new AnimationFactory.AnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                setVisibility(View.GONE);
                isShowing = false;
                mEventListener.onTutorialViewDidHide(TutorialView.this);
            }
        });
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        // dispatchDraw w 800 h 512
        textDrawer.draw(canvas, getWidth(), getHeight());
        super.dispatchDraw(canvas);
    }

    @Override
    public boolean isShowing() {
        return isShowing;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        hide();
        return true;
    }

    /**
     * Builder class for creating {@link TutorialView}s.
     * It is recommended that you use this Builder class.
     */
    public static class Builder {

        private final TutorialView tutorialView;

        private final Activity activity;

        public Builder(Activity activity) {
            this.activity = activity;
            tutorialView = new TutorialView(activity);
        }

        /**
         * Set the text shown on the view.
         */
        public Builder setText(int resId) {
            return setText(activity.getResources().getString(resId));
        }

        /**
         * Set the text shown on the view.
         */
        public Builder setText(CharSequence text) {
            tutorialView.setText(text);
            return this;
        }

        /**
         * Set the color of the text shown on the view.
         */
        public Builder setTextColor(int color) {
            tutorialView.setTextColor(color);
            return this;
        }

        /**
         * Set the background color of this tutorial view.
         */
        public Builder setBackgroundColor(int color) {
            tutorialView.setBackgroundColor(color);
            return this;
        }

        /**
         * Create the {@link com.prt2121.tutorialview.TutorialView} and show it.
         *
         * @return the created TutorialView
         */
        public TutorialView build() {
            insertTutorialView(tutorialView, activity);
            return tutorialView;
        }
    }
}
