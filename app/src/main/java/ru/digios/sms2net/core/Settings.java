package ru.digios.sms2net.core;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {
    private SharedPreferences settings = null;

    private static final String APP_PREFERENCES = "settings";
    private static final String WHITE_PHONE_LIST = "white_phone_list";
    private static final String HOST_TO_CONNECT = "host_to_connect";
    private static final String AUTOSTART = "autostart";

    public Settings(Context context) {
        settings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public String getWhitePhoneList() {
        return  settings.getString(WHITE_PHONE_LIST, "");
    }

    public void setWhitePhoneList(String list) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(WHITE_PHONE_LIST, list);
        editor.apply();
    }

    public String getHostToConnect() {
        return settings.getString(HOST_TO_CONNECT, "");
    }

    public void setHostToConnect(String host) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(HOST_TO_CONNECT, host);
        editor.apply();
    }
}
