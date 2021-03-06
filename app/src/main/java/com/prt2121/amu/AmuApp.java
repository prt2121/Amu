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

package com.prt2121.amu;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import com.prt2121.amu.materialtype.MaterialTypeServiceModule;
import com.prt2121.amu.userlocation.UserLocationModule;

import android.app.Application;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by pt2121 on 3/7/15.
 */
public class AmuApp extends Application {

    private static AmuApp mInstance;

    private Graph mGraph;

    public static GoogleAnalytics analytics;

    public static Tracker tracker;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initDaggerGraph();
        initCustomFont();
        initAnalyitcs();
    }

    private void initAnalyitcs() {
        // google analytics
        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);
        tracker = analytics.newTracker("UA-52295104-5");
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(getString(R.string.category_splash_screen))
                .setAction(getString(R.string.action_open))
                .setLabel(getString(R.string.label_start_app))
                .build());
    }

    private void initCustomFont() {
        // custom font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Lato-Regular.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }

    private void initDaggerGraph() {
        mGraph = DaggerGraph.builder()
                .userLocationModule(new UserLocationModule(getApplicationContext()))
                .tinyDbModule(new TinyDbModule(getApplicationContext()))
                .materialTypeServiceModule(new MaterialTypeServiceModule())
                .build();
    }

    public static AmuApp getInstance() {
        return mInstance;
    }

    public Graph getGraph() {
        return mGraph;
    }

}
