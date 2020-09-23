package com.steve.surfacetv.datasource.util;

import android.content.SharedPreferences;

public class SharedPreferenceUtil {
    private static SharedPreferenceUtil instance;

    public static final String PRE_XML_NAME = "surface_tv";
    public static final String PRE_KEY_SEARCH_INPUT = "PRE_KEY_SEARCH_INPUT";

    private SharedPreferences pref;

    public static SharedPreferenceUtil getInstance() {
        if (instance == null)
            instance = new SharedPreferenceUtil();
        return instance;
    }

    private SharedPreferenceUtil() {
    }

    public void init(SharedPreferences pref) {
        this.pref = pref;
    }

    public void setSearchInput(String searchInput) {
        pref.edit().putString(PRE_KEY_SEARCH_INPUT, searchInput).apply();
    }

    public String getSearchInput() {
        return pref.getString(PRE_KEY_SEARCH_INPUT, "");
    }
}
