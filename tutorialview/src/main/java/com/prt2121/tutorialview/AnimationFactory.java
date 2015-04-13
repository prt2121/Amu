/*
 * Copyright (c) 2015 Prat Tanapaisankit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.prt2121.tutorialview;

import android.graphics.Point;
import android.view.View;

/**
 * Created by pt2121 on 4/4/15.
 */
interface AnimationFactory {

    void fadeInView(View target, long duration, AnimationStartListener listener);

    void fadeOutView(View target, long duration, AnimationEndListener listener);

    void animateTargetToPoint(TutorialView tutorialView, Point point);

    interface AnimationStartListener {

        void onAnimationStart();
    }

    interface AnimationEndListener {

        void onAnimationEnd();
    }
}
