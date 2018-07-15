package com.portfolio.jgsilveira.customersportfolio.settings;

import android.content.Context;
import android.content.SharedPreferences;

public final class AppSettings {

    private static SharedPreferences SETTINGS;

    public static final String STATE_KEY = "state";

    private static final String SETTINGS_NAME = "CustomersPortfolio";

    private AppSettings() {

    }

    public static SharedPreferences getPreferences(Context context) {
        if (SETTINGS == null) {
            initPreferences(context);
        }
        return SETTINGS;
    }

    private static void initPreferences(Context context) {
        SETTINGS = context.getSharedPreferences(SETTINGS_NAME, Context.MODE_PRIVATE);
    }

    public static void putState(String value) {
        SharedPreferences.Editor editor = SETTINGS.edit();
        editor.putString(STATE_KEY, value);
        editor.apply();
        editor.commit();
    }

    public static String getState(String defaultValue) {
        return SETTINGS.getString(STATE_KEY, defaultValue);
    }

}
