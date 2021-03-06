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
import com.prt2121.amu.marker.MarkerCache;
import com.prt2121.amu.model.Loc;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.inject.Inject;

import retrofit.RestAdapter;

public class LocationFragment extends Fragment {

    private static final String TAG = LocationFragment.class.getSimpleName();

    private static final String ARG_ID = "id";

    private static final String ARG_LOCATION = "location";

    @Inject
    MarkerCache mMarkerCache;

    @Inject
    RestAdapter mRestAdapter;

    private String mMarkerId;

    private String mLocation;

    private TextView mAddressTextView;

    private ImageView mLocationImageView;

    private TextView mDistanceTextView;

    private TextView mAcceptTextView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id       marker's id.
     * @param location lat,long string.
     * @return A new instance of fragment LocationActivityFragment.
     */
    public static LocationFragment newInstance(String id, String location) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, id);
        args.putString(ARG_LOCATION, location);
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
            mMarkerId = getArguments().getString(ARG_ID);
            mLocation = getArguments().getString(ARG_LOCATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        AmuApp.getInstance().getGraph().inject(this);
        Loc loc = mMarkerCache.get(mMarkerId);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        mAddressTextView = (TextView) view.findViewById(R.id.addressTextView);
        mLocationImageView = (ImageView) view.findViewById(R.id.locationImageView);
        mDistanceTextView = (TextView) view.findViewById(R.id.distanceTextView);
        mAcceptTextView = (TextView) view.findViewById(R.id.acceptTextView);
        mAcceptTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        view.findViewById(R.id.openMapButton).setOnClickListener(v -> {
//            Uri uri = Uri.parse("http://maps.google.com/maps?saddr=40.715522,-74.002452&daddr=" +
//                    loc.getLatitude() + "," + loc.getLongitude());
            Uri uri = Uri.parse("http://maps.google.com/maps?daddr=" +
                    loc.getLatitude() + "," + loc.getLongitude());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(mapIntent);
        });

        String apiKey = getResources().getString(R.string.google_maps_key);

        ((LocationActivity) getActivity()).setActionBarTitle(loc.getShortName());
        mAddressTextView.setText(loc.getAddress());
        NumberFormat formatter = new DecimalFormat("#0.00 mi");
        mDistanceTextView.setText(formatter.format(loc.getDistance()));
        mAcceptTextView.setText(makeString(loc.getMaterialType()));

        String imageViewUrl = "https://maps.googleapis.com/maps/api/streetview?size=640x384&location="
                + mLocation + "&key=" + apiKey;

        Picasso.with(getActivity())
                .load(imageViewUrl)
                        //.placeholder(R.drawable.user_placeholder)
                .into(mLocationImageView);

        return view;
    }

    public String makeString(String[] strings) {
        if (strings == null || strings.length == 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        for (String string : strings) {
            result.append(string);
            result.append(",");
        }
        return result.length() > 0 ? result.substring(0, result.length() - 1) : "";
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

