package ru.digios.sms2net.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.Date;

public class SmsMonitor extends BroadcastReceiver {
    private static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction() != null && ACTION.compareToIgnoreCase(intent.getAction()) == 0) {
            Bundle bundle = intent.getExtras();

            if(bundle != null)
            {
                String text = "";

                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] msgs = new SmsMessage[pdus.length];
                for(int i = 0; i < msgs.length; i++){
                    msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                    text += msgs[i].getMessageBody().toString();
                }

                Message message = new Message();
                message.setPhoneNumber(msgs[0].getOriginatingAddress());
                message.setText(text);
                message.setDate(new Date(msgs[0].getTimestampMillis()));

                Intent mIntent = new Intent(context, SmsService.class);
                mIntent.putExtra("message", message);
                context.startService(mIntent);
            }
        }
    }
}
