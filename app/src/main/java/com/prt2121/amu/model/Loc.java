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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Loc {

    public Loc(String shortName, String borough, String parkSiteName, String address, Double latitude,
            Double longitude, String type, String materialType) {
        ShortName = shortName;
        Borough = borough;
        ParkSiteName = parkSiteName;
        Address = address;
        Latitude = latitude;
        Longitude = longitude;
        Type = type;
        MaterialType = materialType;
    }

    @SerializedName("Short Name")
    @Expose
    private String ShortName;

    @Expose
    private String Borough;

    @SerializedName("Park/Site Name")
    @Expose
    private String ParkSiteName;

    @Expose
    private String Address;

    @Expose
    private Double Latitude;

    @Expose
    private Double Longitude;

    @Expose
    private String Type;

    @SerializedName("Material Type")
    @Expose
    private String MaterialType;

    /**
     * @return The ShortName
     */
    public String getShortName() {
        return ShortName;
    }

    /**
     * @param ShortName The Short Name
     */
    public void setShortName(String ShortName) {
        this.ShortName = ShortName;
    }

    /**
     * @return The Borough
     */
    public String getBorough() {
        return Borough;
    }

    /**
     * @param Borough The Borough
     */
    public void setBorough(String Borough) {
        this.Borough = Borough;
    }

    /**
     * @return The ParkSiteName
     */
    public String getParkSiteName() {
        return ParkSiteName;
    }

    /**
     * @param ParkSiteName The Park/Site Name
     */
    public void setParkSiteName(String ParkSiteName) {
        this.ParkSiteName = ParkSiteName;
    }

    /**
     * @return The Address
     */
    public String getAddress() {
        return Address;
    }

    /**
     * @param Address The Address
     */
    public void setAddress(String Address) {
        this.Address = Address;
    }

    /**
     * @return The Latitude
     */
    public Double getLatitude() {
        return Latitude;
    }

    /**
     * @param Latitude The Latitude
     */
    public void setLatitude(Double Latitude) {
        this.Latitude = Latitude;
    }

    /**
     * @return The Longitude
     */
    public Double getLongitude() {
        return Longitude;
    }

    /**
     * @param Longitude The Longitude
     */
    public void setLongitude(Double Longitude) {
        this.Longitude = Longitude;
    }

    /**
     * @return The Type
     */
    public String getType() {
        return Type;
    }

    /**
     * @param Type The Type
     */
    public void setType(String Type) {
        this.Type = Type;
    }

    /**
     * @return The MaterialType
     */
    public String getMaterialType() {
        return MaterialType;
    }

    /**
     * @param MaterialType The Material Type
     */
    public void setMaterialType(String MaterialType) {
        this.MaterialType = MaterialType;
    }

    public static class Build {

        private String shortName;

        private String borough;

        private String parkSiteName;

        private String address;

        private Double latitude;

        private Double longitude;

        private String type;

        private String materialType;

        public Build(String shortName, Double latitude, Double longitude) {
            this.shortName = shortName;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public Loc build() {
            return new Loc(shortName, borough, parkSiteName, address, latitude,
                    longitude, type, materialType);
        }

        public Build borough(String borough) {
            this.borough = borough;
            return this;
        }

        public Build parkSiteName(String parkSiteName) {
            this.parkSiteName = parkSiteName;
            return this;
        }

        public Build address(String address) {
            this.address = address;
            return this;
        }

        public Build latitude(Double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Build longitude(Double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Build type(String type) {
            this.type = type;
            return this;
        }

        public Build materialType(String materialType) {
            this.materialType = materialType;
            return this;
        }

    }

}
