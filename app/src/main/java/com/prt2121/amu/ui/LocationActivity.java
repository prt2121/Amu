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
import com.prt2121.amu.place.GooglePlaceService;
import com.prt2121.amu.place.PlaceUtil;
import com.prt2121.amu.place.model.Photo;
import com.prt2121.amu.place.model.Result;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import javax.inject.Inject;

import retrofit.RestAdapter;
import rx.schedulers.Schedulers;

public class LocationActivity extends ActionBarActivity {

    @Inject
    RestAdapter mRestAdapter;

    public static final String LOCATION_EXTRA = "location_extra";

    public static final String TITLE_EXTRA = "title_extra";

    private static final String TAG = LocationActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AmuApp.getInstance().getGraph().inject(this);
        setContentView(R.layout.activity_location);
        Intent intent = getIntent();
        String location = intent.getStringExtra(LOCATION_EXTRA);
        String title = intent.getStringExtra(TITLE_EXTRA);
        String apiKey = getResources().getString(R.string.google_place_key);
        mRestAdapter.create(GooglePlaceService.class)
                .getPlaces(location, title, apiKey)
                .subscribeOn(Schedulers.newThread())
                        //.observeOn(AndroidSchedulers.mainThread())
                .subscribe(place -> {
                    List<Result> results = place.getResults();
                    if (results != null && !results.isEmpty()) {
                        Result result = results.get(0);
                        Log.d(TAG, "Name " + result.getName());
                        Log.d(TAG, "PlaceId " + result.getPlaceId());
                        Log.d(TAG, "Vicinity " + result.getVicinity());
                        List<Photo> photos = result.getPhotos();
                        if (photos != null && !photos.isEmpty()) {
                            PlaceUtil.retrieveImage(LocationActivity.this,
                                    result.getPhotos().get(0).getPhotoReference());
                        }
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
