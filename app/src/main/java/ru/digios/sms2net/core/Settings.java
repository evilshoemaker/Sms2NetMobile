package ru.digios.sms2net.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {
    private SharedPreferences settings = null;

    private static final String APP_PREFERENCES = "settings";
    private static final String WHITE_PHONE_LIST = "white_phone_list";
    private static final String HOST_TO_CONNECT = "host_to_connect";
    private static final String AUTOSTART = "autostart";
    private static final String SEND_INTERVAL = "send_interval";

    public Settings(Context context) {
        settings = /*PreferenceManager.getDefaultSharedPreferences(context);*/context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public String getWhitePhoneList() {
        return  settings.getString(WHITE_PHONE_LIST, "");
    }

    public void setWhitePhoneList(String list) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(WHITE_PHONE_LIST, list.trim());
        editor.apply();
    }

    public String getHostToConnect() {
        return settings.getString(HOST_TO_CONNECT, "https://arsenal.army/index.php?option=com_jshopping&controller=modem&task=test");
    }

    public void setHostToConnect(String host) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(HOST_TO_CONNECT, host.trim());
        editor.commit();
    }

    public int getSendInterval() {
        return  settings.getInt(SEND_INTERVAL, 2);
    }

    public void setSendInterval(int interval) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(SEND_INTERVAL, interval);
        editor.commit();
    }
}
