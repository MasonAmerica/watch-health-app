package com.mason.sample.masonhealth;
/**
 * Copyright(C) 2021 Mason America. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    //SharedPreferences file name
    private static String SHARED_PREFS_FILE_NAME = "health_app_shared_prefs";

    //here you can centralize all your shared prefs keys
    public static String KEY_SHARED_HEART_RATE = "heartRateResult";
    public static String KEY_SHARED_HEART_RATE_TIME = "heartRateMeasuredTime";
    public static String KEY_SHARED_BREATHING_RATE = "breathingRateResult";
    public static String KEY_SHARED_BLOOD_OXYGEN = "bloodOxygenResult";
    public static String KEY_SHARED_BREATHING_RATE_TIME = "breathingRateMeasuredTime";
    public static String KEY_SHARED_BLOOD_OXYGEN_TIME = "bloodOxygenMeasuredTime";


    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    //Strings
    public static void save(Context context, String key, String value) {
        getPrefs(context).edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key) {
        return getPrefs(context).getString(key, "");
    }

    public static String getString(Context context, String key, String defaultValue) {
        return getPrefs(context).getString(key, defaultValue);
    }


    //Longs
    public static void save(Context context, String key, long value) {
        getPrefs(context).edit().putLong(key, value).commit();
    }

    public static long getLong(Context context, String key) {
        return getPrefs(context).getLong(key, 0L);
    }

    public static long getLong(Context context, String key, long defaultValue) {
        return getPrefs(context).getLong(key, defaultValue);
    }
}
