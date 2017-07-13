package ru.digios.sms2net.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Date;

public class SmsMonitor extends BroadcastReceiver {
    private static final Logger logger = Log.getLogger(SmsMonitor.class);
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null && ACTION.compareToIgnoreCase(intent.getAction()) == 0) {
            try {
                Bundle bundle = intent.getExtras();

                if (bundle != null) {
                    String text = "";

                    Object[] pdus = (Object[]) bundle.get("pdus");
                    SmsMessage[] msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        text += msgs[i].getMessageBody().toString();
                    }

                    Message message = new Message();
                    message.setPhoneNumber(msgs[0].getOriginatingAddress());
                    message.setText(text);
                    //message.setDate(new Date(msgs[0].getTimestampMillis()));

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(msgs[0].getTimestampMillis());

                    message.setDate(calendar.getTime());

                    Intent mIntent = new Intent(context, SmsService.class);
                    mIntent.putExtra("message", message);
                    context.startService(mIntent);
                }
            }
            catch (Exception ex) {
                logger.error(ex);
            }
        }
    }
}
