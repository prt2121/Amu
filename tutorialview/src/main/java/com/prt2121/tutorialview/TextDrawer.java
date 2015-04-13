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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;

final class TextDrawer {

    private final TextPaint textPaint;

    private final Context context;

    private final AreaCalculator calculator;

    private final float padding;

    private final float actionBarOffset;

    private CharSequence mText;

    private DynamicLayout mDynamicLayout;

    private TextAppearanceSpan mDetailSpan;

    private boolean hasRecalculated = true;

    public TextDrawer(Resources resources, AreaCalculator calculator, Context context) {
        padding = resources.getDimension(R.dimen.text_padding);
        actionBarOffset = resources.getDimension(R.dimen.action_bar_offset);

        this.calculator = calculator;
        this.context = context;

        textPaint = new TextPaint();
        textPaint.setAntiAlias(true);

        int defaultTextSize = resources.getDimensionPixelSize(R.dimen.text_size);
        textPaint.setTextSize(defaultTextSize);
    }

    public void draw(Canvas canvas, int width, int height) {
        if (shouldDrawText()) {
            if (!TextUtils.isEmpty(mText)) {
                canvas.save();
                float textWidth = textPaint.measureText(mText + " ");
                if (hasRecalculated) {
                    mDynamicLayout = new DynamicLayout(mText, textPaint,
                            (int) textWidth,
                            Layout.Alignment.ALIGN_NORMAL,
                            1.0f, 1.0f, true);
                }
                if (mDynamicLayout != null) {
                    canvas.translate((width - textWidth) / 2, height / 2);
                    mDynamicLayout.draw(canvas);
                    canvas.restore();
                }

            }
        }
        hasRecalculated = false;
    }

    public void setText(CharSequence details) {
        if (details != null) {
            SpannableString ssbDetail = new SpannableString(details);
            ssbDetail.setSpan(mDetailSpan, 0, ssbDetail.length(), 0);
            mText = ssbDetail;
        }
    }

    public void setTextStyling(int styleId) {
        mDetailSpan = new TextAppearanceSpan(this.context, styleId);
        setText(mText);
    }

    public void setTextColor(int color) {
        textPaint.setColor(color);
    }

    public CharSequence getContentText() {
        return mText;
    }

    public boolean shouldDrawText() {
        return !TextUtils.isEmpty(mText);
    }
}