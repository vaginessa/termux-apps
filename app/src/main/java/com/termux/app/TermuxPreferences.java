package com.termux.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.TypedValue;

public class TermuxPreferences {

    private static final String PREF_KEEP_SCREEN_ON = "screen_on";
    private static final String PREF_CURRENT_SESSION = "current_session";
    private static final String PREF_FONT_SIZE = "font_size";
    private static final String PREF_SHOW_TOOLBAR = "show_toolbar";

    private int minFontSize;
    private int maxFontSize;
    private int defaultFontSize;


    private final SharedPreferences prefs;

    TermuxPreferences(TermuxActivity activity) {
        prefs = activity.getPreferences(Context.MODE_PRIVATE);
        setupFontSizeDefaults(activity);
    }

    public void setKeepScreenOn(boolean newValue) {
        prefs.edit().putBoolean(PREF_KEEP_SCREEN_ON, newValue).apply();
    }

    public boolean isKeepScreenOn() {
        return prefs.getBoolean(PREF_KEEP_SCREEN_ON, false);
    }

    public void setCurrentSession(String currentSession) {
        prefs.edit().putString(PREF_CURRENT_SESSION, currentSession).apply();
    }

    public String getCurrentSession() {
        return prefs.getString(PREF_CURRENT_SESSION, null);
    }

    public void setShowTerminalToolbar(boolean newValue) {
        prefs.edit().putBoolean(PREF_SHOW_TOOLBAR, newValue).apply();
    }

    public boolean isShowTerminalToolbar() {
        return prefs.getBoolean(PREF_SHOW_TOOLBAR, true);
    }

    public boolean toggleShowTerminalToolbar() {
        boolean newValue = !isShowTerminalToolbar();
        prefs.edit().putBoolean(PREF_SHOW_TOOLBAR, newValue).apply();
        return newValue;
    }

    public int getFontSize() {
        return prefs.getInt(PREF_FONT_SIZE, defaultFontSize);
    }

    public int changeFontSize(boolean increase) {
        int fontSize = getFontSize();

        fontSize += (increase ? 1 : -1) * 2;
        fontSize = Math.max(minFontSize, Math.min(fontSize, maxFontSize));

        prefs.edit().putInt(PREF_FONT_SIZE, fontSize).apply();
        return fontSize;
    }

    private void setupFontSizeDefaults(Context context) {
        float dipInPixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, context.getResources().getDisplayMetrics());

        // This is a bit arbitrary and sub-optimal. We want to give a sensible default for minimum font size
        // to prevent invisible text due to zoom be mistake:
        minFontSize = (int) (4f * dipInPixels);
        maxFontSize = 256;

        // http://www.google.com/design/spec/style/typography.html#typography-line-height
        defaultFontSize = Math.round(12 * dipInPixels);
        // Make it divisible by 2 since that is the minimal adjustment step:
        if (defaultFontSize % 2 == 1) defaultFontSize--;
    }

}
