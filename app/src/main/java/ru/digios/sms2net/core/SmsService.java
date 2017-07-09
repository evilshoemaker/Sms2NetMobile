package ru.digios.sms2net.core;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

public class SmsService extends Service {

    private SmsStorage smsStorage = null;
    private MessageDatabaseHelper messageDatabase = null;
    private Settings settings = null;

    public SmsService () {
        smsStorage = new SmsStorage(this);
        messageDatabase = new MessageDatabaseHelper(this);
        settings = new Settings(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

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
            Log.i("INFO.SmsService", "Add message. " + message.toString());
        }
        Log.i("INFO.SmsService", "Message. " + message.toString());

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
                Log.i("INFO.SmsService", "Add message. " + message.toString());
            }
        }
    }
}
