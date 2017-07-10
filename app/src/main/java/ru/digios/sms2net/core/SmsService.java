package ru.digios.sms2net.core;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.List;

public class SmsService extends Service {
    private static final Logger logger = Log.getLogger(SmsService.class);


    private SmsStorage smsStorage = null;
    private MessageDatabaseHelper messageDatabase = null;
    private Settings settings = null;

    public SmsService () {
        smsStorage = new SmsStorage(SmsService.this);
        messageDatabase = new MessageDatabaseHelper(SmsService.this);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        settings = new Settings(SmsService.this);
        loadSmsAsync();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return Service.START_STICKY;
        }

        Serializable obj = intent.getSerializableExtra("message");
        if (obj == null) {
            return Service.START_STICKY;
        }

        Message message = (Message)obj;
        if (!messageDatabase.isMessageExist(message)) {
            messageDatabase.addMessage(message);
            //Log.i("INFO.SmsService", "Add message. " + message.toString());
        }
        //Log.i("INFO.SmsService", "Message. " + message.toString());

        return Service.START_STICKY;//super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void loadSmsAsync () {
        new Thread(new Runnable() {
            public void run () {
                loadSms();
            }
        }).start();
    }

    private void loadSms () {
        List<Message> messageList = smsStorage.getAllSms();
        for (Message message : messageList) {
            if (!messageDatabase.isMessageExist(message)) {
                messageDatabase.addMessage(message);
                //Log.i("INFO.SmsService", "Add message. " + message.toString());
            }
        }
    }
}
