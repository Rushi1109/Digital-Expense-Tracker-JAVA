package com.example.digitalexpensetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSReceive extends BroadcastReceiver {
    String msg_from;
    String msg_body;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            for (SmsMessage message : smsMessages) {
                // Do whatever you want to do with SMS.

                msg_from = message.getDisplayOriginatingAddress();
                msg_body = message.getMessageBody();

                Toast.makeText(context, "From: " + msg_from + ", Body: " + msg_body, Toast.LENGTH_LONG).show();
            }
        }
    }

    String[] getData(){
        return new String[]{msg_from, msg_body};
    }
}
