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

import com.prt2121.amu.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by pt2121 on 3/21/15.
 */
public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    public static String retrieveImage(Context context, String ref) {
        if (!TextUtils.isEmpty(ref)) {
            Bitmap bitmap;
            InputStream iStream = null;
            try {
                URL url = new URL(
                        "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1600&key="
                                + context.getResources().getString(R.string.google_place_key)
                                + "&photoreference="
                                + ref);
                HttpURLConnection urlConnection = (HttpURLConnection) url
                        .openConnection();
                urlConnection.connect();
                iStream = urlConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(iStream);
                return saveBitmap(context, bitmap);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            } finally {
                try {
                    if (iStream != null) {
                        iStream.close();
                    }
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
            }
        }
        return "";
    }

    private static String saveBitmap(Context context, Bitmap bmp) {
        FileOutputStream out = null;
        try {
            Log.d(TAG, context.getCacheDir().getAbsolutePath());
            File file = File.createTempFile("location_photo", ".png", context.getCacheDir());
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            return file.getName();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Throwable ignore) {
            }
        }
        return null;
    }

    public static String getCacheFilePath(Context context, String fileName) {
        if (fileName == null) {
            return null;
        }
        File f = new File(context.getCacheDir(), fileName);
        return f.exists() ? f.getPath() : null;
    }
}
