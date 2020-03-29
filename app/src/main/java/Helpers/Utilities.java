package Helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import database.entities.CarUser;
import database.entities.Parking;

import static android.content.Context.MODE_PRIVATE;

public class Utilities {
    private static String MY_PREFS_NAME = "my_prefs";
    private static String PREF_USER_KEY = "key_user";
    public static String PREF_USER_TYPE_KEY = "key_user_type";

    public static String objectToString(Object object) {
        Gson gson = new Gson();
        if (object instanceof CarUser) {
            CarUser user = (CarUser) object;
            return gson.toJson(user);

        } else {
            Parking owner = (Parking) object;
            return gson.toJson(owner);
        }
    }

    public static void saveToSharedPrefs(String data, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(PREF_USER_KEY, data);
        editor.apply();
    }


    public static void saveBooleanToSharedPrefs(Boolean isOwner, String key, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(key, isOwner);
        editor.apply();
    }

    public static Boolean getBooleanFromSharedPrefs(String key, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        return prefs.getBoolean(key, true);
    }

    public static void saveUserToSharedPref(Object object, Context context) {
        saveToSharedPrefs(objectToString(object), context);
    }


    public static String getFromSharedPrefs(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(key, null);

    }

    public static Object getCurrentUserFromSharedPrefs(Boolean isOwner, Context context) {
//        Boolean isOwner = getBooleanFromSharedPrefs(PREF_USER_TYPE_KEY, context);
        String userString = getFromSharedPrefs(context, PREF_USER_KEY);

        Gson gson = new Gson();
        if (isOwner) {
            return gson.fromJson(userString, Parking.class);
        } else {
            return gson.fromJson(userString, CarUser.class);
        }
    }
}
