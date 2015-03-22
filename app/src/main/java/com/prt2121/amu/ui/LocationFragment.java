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
import com.prt2121.amu.gapi.GoogleMapService;
import com.prt2121.amu.gapi.model.Place;
import com.prt2121.amu.gapi.model.Result;
import com.prt2121.amu.userlocation.IUserLocation;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import retrofit.RestAdapter;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * A placeholder fragment containing a simple view.
 */
public class LocationFragment extends Fragment {

    private static final String TAG = LocationFragment.class.getSimpleName();

    private static final String ARG_LOCATION = "location";

    private static final String ARG_TITLE = "title";

    private static final String ARG_ADDRESS = "address";

    @Inject
    RestAdapter mRestAdapter;

    @Inject
    IUserLocation mUserLocation;

    private Observable<Location> mUser;

    private String mLocation;

    private String mTitle;

    private String mAddress;

    private Subscription mSubscription;

    private TextView mAddressTextView;

    private ImageView mLocationImageView;

    private TextView mDistanceTextView;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param location lat,long string.
     * @param title    Location title.
     * @param address  Address from our database.
     * @return A new instance of fragment LocationActivityFragment.
     */
    public static LocationFragment newInstance(String location, String title, String address) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOCATION, location);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_ADDRESS, address);
        fragment.setArguments(args);
        return fragment;
    }

    public LocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLocation = getArguments().getString(ARG_LOCATION);
            mTitle = getArguments().getString(ARG_TITLE);
            mAddress = getArguments().getString(ARG_ADDRESS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ((LocationActivity) getActivity()).setActionBarTitle(mTitle);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        mAddressTextView = (TextView) view.findViewById(R.id.addressTextView);
        mLocationImageView = (ImageView) view.findViewById(R.id.locationImageView);
        mDistanceTextView = (TextView) view.findViewById(R.id.distanceTextView);
        AmuApp.getInstance().getGraph().inject(this);
        String apiKey = getResources().getString(R.string.google_maps_key);

        String imageViewUrl = "https://maps.googleapis.com/maps/api/streetview?size=640x480&location="
                + mLocation + "&key=" + apiKey;

        Picasso.with(getActivity())
                .load(imageViewUrl)
                //.placeholder(R.drawable.user_placeholder)
                .into(mLocationImageView);

        mUser = findUserLocation();

        mSubscription = mRestAdapter.create(GoogleMapService.class)
                .getPlaces(mLocation, mTitle, apiKey)
                .map(Place::getResults)
                .flatMap(Observable::from)
                .first()
                .zipWith(mUser, new Func2<Result, Location, LocationViewModel>() {
                    @Override
                    public LocationViewModel call(Result result, Location location) {
                        return createLocationViewModel(result, location);
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(locationViewModel -> {
                    mAddressTextView.setText(locationViewModel.address);
                    mDistanceTextView.setText(locationViewModel.distance);
                }, e -> {
                    Log.e(TAG, e.toString());
                });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private LocationViewModel createLocationViewModel(Result result, Location location) {
        String[] latLngStr = mLocation.split(",");
        Location l = new Location("");
        l.setLatitude(Double.parseDouble(latLngStr[0]));
        l.setLongitude(Double.parseDouble(latLngStr[1]));
        location.distanceTo(l);
        return new LocationViewModel(mTitle,
                mAddress,
                String.valueOf(location.distanceTo(l)));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mSubscription != null &&
                !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    private Observable<Location> findUserLocation() {
        return mUserLocation.locate().filter(location -> location != null);
    }

    class LocationViewModel {

        final String name;

        final String address;

        final String distance;

        LocationViewModel(String name, String address, String distance) {
            this.name = name;
            this.address = address;
            this.distance = distance;
        }
    }

}

