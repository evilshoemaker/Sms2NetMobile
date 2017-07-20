package ru.digios.sms2net.core;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SmsStorage {
    private static final int SMS_DATE = 4;
    private static final int DATE_SENT = 5;
    private static final int SMS_PHONE_NUMBER = 2;
    private static final int SMS_BODY = 12;


    private Context context;

    public SmsStorage(Context context) {
        this.context = context;
    }

    public List<Message> getAllSms() {
        List<Message> messageList = new ArrayList<>();

        if (context == null)
            return messageList;

        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);

        while (cursor.moveToNext()) {
            Message message = new Message();
            message.setDate(new Date(cursor.getLong(DATE_SENT)));
            message.setPhoneNumber(cursor.getString(SMS_PHONE_NUMBER));
            message.setText(cursor.getString(SMS_BODY));
            messageList.add(message);
        }
        cursor.close();

        return messageList;
    }
}
