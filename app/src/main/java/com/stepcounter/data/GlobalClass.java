package com.stepcounter.data;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by surender on 2/2/2016.
 */
public class GlobalClass {

    public static void savePreferences(String key, String value,
                                       final Context context) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(key, value);

        editor.commit();
    }

    public static String callSavedPreferences(String key, Context context) {

        SharedPreferences sharedPreferences = PreferenceManager

                .getDefaultSharedPreferences(context);

        String name = sharedPreferences.getString(key, "0");

        return name;
    }


    public static void savePreferences1(String key, Boolean value,
                                        final Context context) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(key, value);

        editor.commit();
    }

    public static Boolean callSavedPreferences1(String key, Context context) {

        SharedPreferences sharedPreferences = PreferenceManager

                .getDefaultSharedPreferences(context);

        Boolean name = sharedPreferences.getBoolean(key, true);

        return name;
    }

    public static void savePreferences2(String key,int p,
                                        final Context context) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(key, p);

        editor.commit();
    }

    public static int callSavedPreferences2(String key,int p, Context context) {

        SharedPreferences sharedPreferences = PreferenceManager

                .getDefaultSharedPreferences(context);

         p = sharedPreferences.getInt(key, p);

        return p;
    }

    public static void savePreferencesDouble(String key,double p,
                                             final Context context) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putLong(key, Double.doubleToRawLongBits(p));

        editor.commit();
    }

    public static double callSavedPreferencesDouble(String key,double p, Context context) {

        SharedPreferences sharedPreferences = PreferenceManager

                .getDefaultSharedPreferences(context);

        p = Double.longBitsToDouble(sharedPreferences.getLong(key, Double.doubleToLongBits(p)));

        return p;
    }

    public static void savePreferenceslong(String key,long p,
                                        final Context context) {

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putLong(key, p);

        editor.commit();
    }

    public static long callSavedPreferenceslong(String key,long p, Context context) {

        SharedPreferences sharedPreferences = PreferenceManager

                .getDefaultSharedPreferences(context);

        p = sharedPreferences.getLong(key, p);

        return p;
    }


}
