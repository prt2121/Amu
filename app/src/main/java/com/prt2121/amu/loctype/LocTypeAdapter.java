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

import com.prt2121.amu.AmuApp;
import com.prt2121.amu.R;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

/**
 * Created by pt2121 on 3/13/15.
 */
public class LocTypeAdapter extends RecyclerView.Adapter<LocTypeAdapter.ViewHolder> {

    @Inject
    LocTypeService mLocTypeService;

    private static final String TAG = LocTypeAdapter.class.getSimpleName();

    private LocType[] mTypes;

    public LocTypeAdapter(LocType[] types) {
        mTypes = types;
        AmuApp.getInstance().getGraph().inject(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.type_row_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mTypes[position].name);
        holder.mSwitchCompat.setChecked(mTypes[position].isChecked());
        holder.mSwitchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mLocTypeService.updateLocType(position, isChecked);
        });
    }

    @Override
    public int getItemCount() {
        return mTypes.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final TextView mTextView;

        public final SwitchCompat mSwitchCompat;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.typeTextView);
            mSwitchCompat = (SwitchCompat) v.findViewById(R.id.locSwitch);
        }

    }

}
