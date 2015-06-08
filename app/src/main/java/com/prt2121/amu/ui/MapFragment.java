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


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.prt2121.amu.AmuApp;
import com.prt2121.amu.R;
import com.prt2121.amu.location.FindLoc;
import com.prt2121.amu.marker.MarkerCache;
import com.prt2121.amu.materialtype.MaterialType;
import com.prt2121.amu.materialtype.MaterialTypeService;
import com.prt2121.amu.model.Loc;
import com.prt2121.amu.userlocation.IUserLocation;
import com.prt2121.amu.util.MapUtils;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;


/**
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {

    private static final String TAG = MapFragment.class.getSimpleName();

    @Inject
    IUserLocation mUserLocation;

    @Inject
    MaterialTypeService mMaterialTypeService;

    Set<String> mTypeSet = new HashSet<>();

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private Loc mLoc;

    private OnFragmentInteractionListener mListener;

    private static final float ZOOM = 17f;

    private static final int MAX_LOCATION = Integer.MAX_VALUE;

    private Subscription mMarkerSubscription; //mUserLocationSubscription,

    private Observable<Location> mUser;

    private Observable<Loc> mLocations;

    private FloatingActionButton mRefreshButton;

    @Inject
    MarkerCache mMarkerCache;

    //Test Location : New York City Department of Health and Mental Hygiene
    private final Loc mUserLoc = new Loc.Build("Your location", 40.715522, -74.002452)
            .address("")
            .type("User")
            .build();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameter.
     *
     * @return A new instance of fragment MapFragment.
     */
    public static MapFragment newInstance() {
        return new MapFragment();
    }

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AmuApp.getInstance().getGraph().inject(this);
        MapUtils.clearIcon();
        for (MaterialType type : mMaterialTypeService.getMaterialTypes()) {
            if (type.isChecked()) {
                mTypeSet.add(type.name);
                MapUtils.addIcon(type.name);
            }
        }
        // TODO remove this hardcoded user loc
//        mLoc = mUserLoc;
        mUser = findUserLocation();
        mLocations = findLocation(getActivity());
    }

    private Observable<Loc> findLocation(Context context) {
        return new FindLoc(context)
                .getLocs()
                .filter(loc -> {
                    for (String t : mTypeSet) {
                        String[] ms = loc.getMaterialType();
                        for (String m : ms) {
                            if (m.equalsIgnoreCase(t)) {
                                return true;
                            }
                        }
                    }
                    return false;
                });
    }

    private Observable<Location> findUserLocation() {
        return mUserLocation.locate().filter(location -> location != null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMarkerSubscription != null &&
                !mMarkerSubscription.isUnsubscribed()) {
            mMarkerSubscription.unsubscribe();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        setUpMapIfNeeded();
        mRefreshButton = (FloatingActionButton) view.findViewById(R.id.refreshButton);
        mRefreshButton.setEnabled(false);
        mRefreshButton.setOnClickListener(v -> {
            if (mMap != null) {
                mMap.clear();
                updateMarkers(mUser, mMap.getProjection().getVisibleRegion().latLngBounds);
                onUserLocationAvailable(
                        latLng -> mMap.addMarker(new MarkerOptions().position(latLng).title(mUserLoc.getShortName())));
            }
        });

        ViewGroup layout = (ViewGroup) view.findViewById(R.id.layout_icon);
        for (Pair<Integer, Integer> i : MapUtils.mIconSet) {
            FloatingActionButton button = new FloatingActionButton(getActivity());
            button.setSize(FloatingActionButton.SIZE_MINI);
            button.setIcon(i.first);
            button.setColorNormalResId(i.second);
            layout.addView(button);
        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void onUserLocationAvailable(Action1<LatLng> onNext) {
        mUser.map(location -> new LatLng(location.getLatitude(), location.getLongitude()))
                .first()
                .subscribe(onNext, throwable -> Log.e(TAG, throwable.getLocalizedMessage()));
    }

    /**
     * Init map
     */
    private void setUpMap() {
        onUserLocationAvailable(latLng -> {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM));
            mMap.addMarker(new MarkerOptions().position(latLng).title(mUserLoc.getShortName()));
        });
        mMap.setOnMapLoadedCallback(() -> {
            LatLngBounds latLngBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
            mMarkerSubscription = updateMarkers(mUser, latLngBounds);
            mRefreshButton.setEnabled(true);
        });

        mMap.setOnInfoWindowClickListener(new InfoWindowClickListener(getActivity()));
    }

    private Subscription updateMarkers(LatLng center, LatLngBounds latLngBounds) {
        Observable<Location> mockObservable = mockUserLocation(center);
        return MapUtils.showPins(getActivity(),
                mockObservable, //mUser,
                mLocations, mMap, MAX_LOCATION, latLngBounds, mMarkerCache
        );
    }

    private Subscription updateMarkers(Observable<Location> center, LatLngBounds latLngBounds) {
        return MapUtils.showPins(getActivity(),
                center, //mUser,
                mLocations, mMap, MAX_LOCATION, latLngBounds, mMarkerCache
        );
    }

    private Observable<Location> mockUserLocation(LatLng center) {
        Location location = new Location("Center");
        location.setLatitude(center.latitude);
        location.setLongitude(center.longitude);
        return Observable.just(location);
    }

}
