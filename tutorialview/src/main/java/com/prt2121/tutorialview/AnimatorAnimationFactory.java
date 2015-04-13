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

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

final class AnimatorAnimationFactory implements AnimationFactory {

    private static final String ALPHA = "alpha";

    private static final float INVISIBLE = 0f;

    private static final float VISIBLE = 1f;

    private final AccelerateDecelerateInterpolator interpolator;

    public AnimatorAnimationFactory() {
        interpolator = new AccelerateDecelerateInterpolator();
    }

    @Override
    public void fadeInView(View target, long duration, final AnimationStartListener listener) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(target, ALPHA, INVISIBLE, VISIBLE);
        oa.setDuration(duration).addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                listener.onAnimationStart();
            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        oa.start();
    }

    @Override
    public void fadeOutView(View target, long duration, final AnimationEndListener listener) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(target, ALPHA, INVISIBLE);
        oa.setDuration(duration).addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                listener.onAnimationEnd();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        oa.start();
    }

    @Override
    public void animateTargetToPoint(TutorialView tutorialView, Point point) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator xAnimator = ObjectAnimator.ofInt(tutorialView, "tutorialX", point.x);
        ObjectAnimator yAnimator = ObjectAnimator.ofInt(tutorialView, "tutorialY", point.y);
        set.playTogether(xAnimator, yAnimator);
        set.setInterpolator(interpolator);
        set.start();
    }

}