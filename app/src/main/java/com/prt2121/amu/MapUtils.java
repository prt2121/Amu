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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.prt2121.amu.model.Loc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;
import android.util.Pair;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by prt2121 on 1/19/15.
 */
public class MapUtils {

    private static final String TAG = MapUtils.class.getSimpleName();

    /**
     * Show the pins on the map.
     *
     * @param context     Android context used for get resources
     * @param pivot       user's location or the center location.
     * @param things      bin locations or things
     * @param map         the map to be showed
     * @param maxLocation the number of things
     * @return Subscription
     */
    public static Subscription showPins(
            Context context,
            Observable<Location> pivot,
            Observable<Loc> things,
            GoogleMap map, int maxLocation) {
        if (map == null) {
            Log.e(TAG, "map is NULL");
            return Subscriptions.empty();
        }

        final int px = context.getResources().getDimensionPixelSize(R.dimen.map_circle_marker_size);
        final Bitmap markerBitmap = Bitmap.createBitmap(px, px, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(markerBitmap);
        final Drawable markerDrawable = context.getResources()
                .getDrawable(R.drawable.circle_marker);
        if (markerDrawable != null) {
            markerDrawable.setBounds(0, 0, markerBitmap.getWidth(), markerBitmap.getHeight());
        }

        return Observable.zip(pivot.first().repeat(),
                things.filter(loc -> loc.getLatitude() != null && loc.getLongitude() != null),
                (location, loc) -> {
                    Location l = new Location(loc.getShortName());
                    l.setLatitude(loc.getLatitude());
                    l.setLongitude(loc.getLongitude());
                    return new Pair<>(location.distanceTo(l), loc);
                }).toSortedList((p1, p2) -> p1.first.compareTo(p2.first))
                .flatMap(Observable::from)
                .map(p -> p.second)
                .take(maxLocation)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Loc>() {

                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.toString());
                    }

                    @Override
                    public void onNext(Loc loc) {
                        if (markerDrawable != null) {
                            markerDrawable.setColorFilter(getColor(loc.getType()), PorterDuff.Mode.MULTIPLY);
                            markerDrawable.draw(canvas);
                        }
                        map.addMarker(new MarkerOptions()
                                .position(new LatLng(loc.getLatitude(), loc.getLongitude()))
                                .title(loc.getShortName())
                                .icon(BitmapDescriptorFactory.fromBitmap(markerBitmap)));
                    }
                });
    }

    private static int getColor(String type) {
        if (type.equalsIgnoreCase("Bin")) {
            return 0xFF00AD9F;
        } else if (type.equalsIgnoreCase("Front-of-Store")) {
            return 0xFF00AD9F;
        } else if (type.equalsIgnoreCase("Drop Off Counter")) {
            return 0xFFFFFF00;
        } else if (type.equalsIgnoreCase("Container Deposit Return, Plastic Bag Return")) {
            return 0xFF00AD9F;
        } else if (type.equalsIgnoreCase("Supermarket/Convenience Store")) {
            return 0xFF00FF00;
        } else if (type.equalsIgnoreCase("Container Deposit Return")) {
            return 0xFF00AD9F;
        } else {
            return 0xFFFFFFFF;
        }
    }

}
