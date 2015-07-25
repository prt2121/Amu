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

import com.prt2121.amu.AmuApp;
import com.prt2121.amu.R;
import com.prt2121.amu.userlocation.IUserLocation;
import com.prt2121.amu.util.FlowUtil;
import com.prt2121.tutorialview.TutorialView;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import javax.inject.Inject;

import rx.Observable;

public class MaterialTypeFilterActivity extends AppCompatActivity {

    private static final double NYC_LAT = 40.7033127;

    private static final double NYC_LONG = -73.979681;

    private static final Location NYC = new Location("NYC");

    static {
        NYC.setLatitude(NYC_LAT);
        NYC.setLongitude(NYC_LONG);
    }

    private static final int WHITE = Color.parseColor("#FFFFFF");

    private static final int BLACK = Color.parseColor("#99000000"); // 99 ~ 60%

    private static final String TAG = MaterialTypeFilterActivity.class.getSimpleName();

    @Inject
    IUserLocation mUserLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AmuApp.getInstance().getGraph().inject(this);
        setContentView(R.layout.activity_filter);

        Observable<Location> locationObservable = mUserLocation.locate();

        findViewById(R.id.applyButton).setOnClickListener(v -> {
            locationObservable
                    .first()
                    .subscribe(location -> {
                        int d = metersToMiles(location.distanceTo(NYC));
                        // we only support 100 miles from NYC
                        if (d < 100) { // TODO: improve this
                            Intent intent = new Intent(MaterialTypeFilterActivity.this, MapActivity.class);
                            MaterialTypeFilterActivity.this.startActivity(intent);
                        } else {
                            Toast.makeText(MaterialTypeFilterActivity.this,
                                    R.string.not_support, Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
        });

        boolean firstTime = FlowUtil.isFirstRun(this, TAG);
        if (firstTime) {
            new TutorialView.Builder(this)
                    .setText("Select the items you'd like to know where to recycle.")
                    .setTextColor(WHITE)
                    .setBackgroundColor(BLACK)
                    .build();
            FlowUtil.setFirstRun(this, TAG);
        }
    }

    private int metersToMiles(float meters) {
        return (int) (meters * 0.621371 / 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUserLocation != null) {
            mUserLocation.stop();
        }
    }
}
