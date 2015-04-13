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

import android.graphics.Rect;
import android.util.Log;

final class AreaCalculator {

    private final Rect mRect = new Rect();

    /**
     * Creates a {@link Rect}.
     * Used to calculate where best to place the text
     *
     * @return true if voidedArea has changed, false otherwise.
     */
    public boolean calculateShowcaseRect(float x, float y) {

        int cx = (int) x, cy = (int) y;

        if (mRect.left == cx / 2 && mRect.top == cy / 2) {
            return false;
        }

        Log.d("ShowcaseView", "Recalculated");

        mRect.left = cx / 2;
        mRect.top = cy / 2;
        mRect.right = cx / 2;
        mRect.bottom = cy / 2;

        return true;

    }

    public Rect getRect() {
        return mRect;
    }

}