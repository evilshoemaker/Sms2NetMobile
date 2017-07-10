package ru.digios.sms2net.core;

import android.content.Context;

public class MessageUploader implements Runnable {

    private Settings settings = null;

    public MessageUploader(Context context) {
        settings = new Settings(context);
    }

    @Override
    public void run() {

    }
}
