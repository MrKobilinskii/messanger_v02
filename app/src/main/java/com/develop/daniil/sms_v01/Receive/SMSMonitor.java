package com.develop.daniil.sms_v01.Receive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SMSMonitor extends BroadcastReceiver {

    SmsMessage[] messages;
    String fromAddress = "";
    String bodyMsg = "";
    private static final String TAG = "Принято новое сообщение!";

    @Override
    public void onReceive(Context context, Intent intent) { // вызывается системой каждый раз при получении сообщения

        Bundle bundle = intent.getExtras();

        if(bundle != null){
            Object[] pdus = (Object[]) bundle.get("pdus");
            messages = new SmsMessage[pdus.length];

            for (int i = 0; i < messages.length; i++) { //собираем СМС из кусочков
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                fromAddress = messages[i].getOriginatingAddress();
                bodyMsg = messages[i].getMessageBody();
            }

            Toast.makeText(context, TAG, Toast.LENGTH_SHORT).show();

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("SMS_RECEIVED_ACTION");
            broadcastIntent.putExtra("bodyMsg", bodyMsg);
            broadcastIntent.putExtra("fromAddress", fromAddress);
            context.sendBroadcast(broadcastIntent);
        }
    }

}
