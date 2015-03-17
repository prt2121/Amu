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

package com.prt2121.amu.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by prt2121 on 9/27/14.
 */
public class Loc implements Parcelable {

    public final String name;

    public final String address;

    public final double latitude;

    public final double longitude;

    /**
     * @see com.prt2121.amu.loctype.LocType
     */
    public final int type;

    public String image;

    public Loc(String name, String address, double latitude, double longitude, int type) {
        this(name, address, latitude, longitude, type, null);
    }

    public Loc(String name, String address, double latitude, double longitude, int type,
            String image) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.type = type;
        this.image = image;
    }

    private Loc(Parcel in) {
        name = in.readString();
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        type = in.readInt();
        image = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeInt(type);
        dest.writeString(image);
    }

    public static final Creator CREATOR = new Creator() {
        public Loc createFromParcel(Parcel in) {
            return new Loc(in);
        }

        public Loc[] newArray(int size) {
            return new Loc[size];
        }
    };

    public static class Builder {

        private String name;

        private String address;

        private double latitude;

        private double longitude;

        private String image;

        /**
         * LocType id.
         * eg. {@link com.prt2121.amu.loctype.LocType#USER} for user's location.
         */
        private int type;

        public Builder(String name) {
            this.name = name;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder latitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder longitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder image(String image) {
            this.image = image;
            return this;
        }

        /**
         * @param type LocType
         * @return the builder with this {@code LocType}
         * @see com.prt2121.amu.loctype.LocType
         */
        public Builder type(int type) {
            this.type = type;
            return this;
        }

        public Loc build() {
            Loc l = new Loc(this.name,
                    this.address,
                    this.latitude,
                    this.longitude,
                    this.type);
            l.image = this.image;
            return l;
        }
    }
}

// NYC
// Borough	Site type	Park/Site Name	Address	Latitude	Longitude
// [ 1, "09BA982F-E777-4D8F-BCAB-3A04D56FB719", 1, 1341003073, "392904", 1341003073, "392904", "{\n}", "Bronx", "Subproperty", "227th St. Plgd", "E 227 St/Bronx River Pkway", "40.890848989", "-73.864223918" ]
