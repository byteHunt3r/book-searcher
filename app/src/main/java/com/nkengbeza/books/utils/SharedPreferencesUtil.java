package com.nkengbeza.books.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SharedPreferencesUtil {

    public static final String PREF_NAME = "BooksStore";

    public static final String POSITION = "POSITION";
    public static final String QUERY = "QUERY";

    private SharedPreferencesUtil() {}

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String getString(Context context, String key) {
        return getSharedPreferences(context).getString(key, "");
    }

    public static List<String> getAll(Context context) {
        List<String> queries = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            String query = getString(context, QUERY + i);
            if (!query.isEmpty()) {
                queries.add(query.trim());
            }
        }
        return queries;
    }

    public static int getInt(Context context, String key) {
        return getSharedPreferences(context).getInt(key, 0);
    }

    public static String setString(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.apply();
        return getString(context, key);
    }

    public static int setInt(Context context, String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(key, value);
        editor.apply();
        return getInt(context, key);
    }

    public static void remove(Context context, String key) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.remove(key);
        editor.apply();
    }


}
