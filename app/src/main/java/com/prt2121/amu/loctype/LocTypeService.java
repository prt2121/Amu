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

package com.prt2121.amu.loctype;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.prt2121.amu.AmuApp;

import android.content.SharedPreferences;

import java.lang.reflect.Type;
import java.util.Collection;

import javax.inject.Inject;

/**
 * Created by pt2121 on 3/20/15.
 *
 * LocTypeService provides user's selected location types.
 */
public class LocTypeService {

    public static final String LOC_TYPE = "locType";

    @Inject
    SharedPreferences preferences;

    @Inject
    Gson gson;

    private LocType[] types;

    public LocTypeService() {
        AmuApp.getInstance().getGraph().inject(this);
        String s = preferences.getString(LOC_TYPE, null);
        Type type = new TypeToken<Collection<LocType>>() {
        }.getType();
        Collection<LocType> ts = gson.fromJson(s, type);
        if (ts == null) {
            types = new LocType[6];
            types[0] = new LocType(0, "Bin", true);
            types[1] = new LocType(1, "Front-of-Store", true);
            types[2] = new LocType(2, "Drop Off Counter", true);
            types[3] = new LocType(3, "Container Deposit Return, Plastic Bag Return", true);
            types[4] = new LocType(4, "Supermarket/Convenience Store", true);
            types[5] = new LocType(5, "Container Deposit Return", true);
            SharedPreferences.Editor e = preferences.edit();
            e.putString("locType", gson.toJson(types));
            e.apply();
        } else {
            types = new LocType[ts.size()];
            types = ts.toArray(types);
        }
    }

    public LocType[] getLocTypes() {
        return types;
    }

    public void updateLocType(int position, boolean checked) {
        types[position].setChecked(checked);
        SharedPreferences.Editor e = preferences.edit();
        e.putString(LOC_TYPE, gson.toJson(types));
        e.apply();
    }

}
