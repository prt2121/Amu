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

package com.prt2121.amu.ui;

import com.prt2121.amu.R;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.test.ActivityInstrumentationTestCase2;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by pt2121 on 3/8/15.
 */
public class SplashActivityTest extends ActivityInstrumentationTestCase2<SplashActivity> {

    private SplashActivity mSplashActivity;

    private Resources instrumentResources;

    private Configuration originalConfig;

    private Configuration changeableConfig;

    public SplashActivityTest() {
        super(SplashActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mSplashActivity = getActivity();
        instrumentResources = getInstrumentation().getTargetContext().getResources();
        originalConfig = instrumentResources.getConfiguration();
        changeableConfig = new Configuration(originalConfig);
    }

    @Override
    protected void tearDown() throws Exception {
        // Restore configuration of phone if it was changed during testing
        instrumentResources.updateConfiguration(originalConfig, instrumentResources.getDisplayMetrics());
    }

    public void testBrandDisplayed() {
        onView(withId(R.id.brandTextView))
                .check(matches(isDisplayed()));
        onView(withId(R.id.brandTextView))
                .check(matches(withText("Intellibins")));
    }

    public void testSloganDisplayed() {
        onView(withId(R.id.sloganTextView))
                .check(matches(isDisplayed()));
        onView(withId(R.id.sloganTextView))
                .check(matches(withText("Recycling made easy.")));
    }

//    public void testActivityShouldOnlyBeInPortraitOrientation() {
//        changeableConfig.orientation = Configuration.ORIENTATION_LANDSCAPE;
//        // switch to landscape
//        instrumentResources.updateConfiguration(changeableConfig, instrumentResources.getDisplayMetrics());
//
//        assertEquals(mSplashActivity.getResources().getConfiguration().orientation, Configuration.ORIENTATION_PORTRAIT);
//    }

}
