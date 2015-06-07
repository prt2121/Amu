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

package com.prt2121.amu.util;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.prt2121.amu.R;
import com.prt2121.amu.marker.MarkerCache;
import com.prt2121.amu.model.Loc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;
import android.util.Pair;

import java.util.HashSet;
import java.util.Set;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by prt2121 on 1/19/15.
 */
public class MapUtils {

    private static final String TAG = MapUtils.class.getSimpleName();

    public static Set<Pair<Integer, Integer>> mIconSet = new HashSet<>();

    /**
     * Show the pins on the map.
     *
     * @param context     Android context used for get resources
     * @param pivot       user's location or the center location
     * @param things      bin locations or things
     * @param map         the map to be showed
     * @param maxLocation the number of things
     * @param bounds      map visible area (LatLngBounds)
     * @return Subscription
     */
    public static Subscription showPins(
            Context context,
            Observable<Location> pivot,
            Observable<Loc> things,
            GoogleMap map, int maxLocation,
            LatLngBounds bounds,
            MarkerCache markerCache) {
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
        markerCache.clear();
        return Observable.zip(pivot.first().repeat(),
                things.filter(loc -> loc.getLatitude() != null && loc.getLongitude() != null)
                        .filter(l -> bounds.contains(new LatLng(l.getLatitude(), l.getLongitude()))),
                (location, loc) -> {
                    Location l = new Location(loc.getShortName());
                    l.setLatitude(loc.getLatitude());
                    l.setLongitude(loc.getLongitude());
                    return new Pair<>(location.distanceTo(l) * 0.000621371192, loc);
                }).toSortedList((p1, p2) -> p1.first.compareTo(p2.first))
                .flatMap(Observable::from)
                .take(maxLocation)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(p -> {
                    Loc loc = p.second;
                    loc.setDistance(p.first);
                    if (markerDrawable != null) {
                        //markerDrawable.setColorFilter(0xFF00AD9F, PorterDuff.Mode.MULTIPLY);
                        markerDrawable.setColorFilter(getColor(context, loc.getType()), PorterDuff.Mode.MULTIPLY);
                        markerDrawable.draw(canvas);
                    }
                    String address = beautifyAddress(loc);
                    Marker marker = map.addMarker(new MarkerOptions()
                            .position(new LatLng(loc.getLatitude(), loc.getLongitude()))
                            .title(loc.getShortName())
                            .snippet(address)
                            .icon(BitmapDescriptorFactory.fromBitmap(markerBitmap)));
                    markerCache.put(marker.getId(), loc);
                });
    }

    private static String beautifyAddress(Loc loc) {
        String address = loc.getAddress() != null ?
                loc.getAddress() :
                "";
        if (address.length() > 20) {
            int space = address.substring(0, 20).lastIndexOf(" ");
            if (space > 0) {
                address = address.substring(0, space) + "...";
            }
        }
        return address;
    }

    public static void addIcon(String type) {
        String t = type.toLowerCase();
        if (t.contains("plastic")) {
            mIconSet.add(new Pair<>(R.drawable.ic_plastic, android.R.color.holo_red_dark));
        } else if (t.contains("electric")) {
            mIconSet.add(new Pair<>(R.drawable.ic_electric, android.R.color.white));
        } else if (t.contains("glass")) {
            mIconSet.add(new Pair<>(R.drawable.ic_glass, android.R.color.holo_orange_dark));
        } else if (t.contains("hazard")) {
            mIconSet.add(new Pair<>(R.drawable.ic_hazard, android.R.color.holo_green_dark));
        } else if (t.contains("metal")) {
            mIconSet.add(new Pair<>(R.drawable.ic_metal, android.R.color.darker_gray));
        } else if (t.contains("paper")) {
            mIconSet.add(new Pair<>(R.drawable.ic_paper, android.R.color.holo_purple)); // light gray
        } else {
            mIconSet.add(new Pair<>(R.drawable.ic_other, android.R.color.white));
        }
    }

    public static int getColor(Context context, String type) {
        String t = type.toLowerCase();
        if (t.contains("plastic")) {
            return context.getResources().getColor(android.R.color.holo_red_dark);
        } else if (t.contains("electric")) {
            return context.getResources().getColor(android.R.color.white);
        } else if (t.contains("glass")) {
            return context.getResources().getColor(android.R.color.holo_orange_dark);
        } else if (t.contains("hazard")) {
            return context.getResources().getColor(android.R.color.holo_green_dark);
        } else if (t.contains("metal")) {
            return context.getResources().getColor(android.R.color.darker_gray);
        } else if (t.contains("paper")) {
            return context.getResources().getColor(android.R.color.holo_purple);
        } else {
            return 0xFF00AD9F;
        }
    }

}
