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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.prt2121.amu.R;
import com.prt2121.amu.util.FlowUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new SplashFragment())
                    .commit();
        }

    }

    public static class SplashFragment extends Fragment {

        private Class<?> clazz;

        public SplashFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            Activity activity = getActivity();
            boolean firstTime = FlowUtil.isFirstRun(activity, TAG);
            int splashScreenTimeout = firstTime ? 2500 : 1500;
            clazz = firstTime ? OnboardingActivity.class : MaterialTypeFilterActivity.class;
            if (firstTime) {
                FlowUtil.setFirstRun(activity, TAG);
            } else {
                boolean accepted = FlowUtil.isPrivacyPolicyAccepted(activity, PrivacyPolicyActivity.TAG);
                clazz = accepted ? MaterialTypeFilterActivity.class : PrivacyPolicyActivity.class;
            }
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(activity, clazz);
                activity.startActivity(intent);
                activity.finish();
            }, splashScreenTimeout);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_splash, container, false);
            SpannableStringBuilder cs = new SpannableStringBuilder("IntellibinsTM");
            cs.setSpan(new SuperscriptSpan(), 11, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            cs.setSpan(new RelativeSizeSpan(0.40f), 11, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ((TextView) v.findViewById(R.id.brandTextView)).setText(cs);

            // AdMob
            AdView mAdView = (AdView) v.findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
            return v;
        }
    }

}
