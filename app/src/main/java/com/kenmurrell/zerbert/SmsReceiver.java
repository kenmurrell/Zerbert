package com.kenmurrell.zerbert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.kenmurrell.zerbert.ui.SmsListener;

import java.time.LocalDateTime;

public class SmsReceiver extends BroadcastReceiver
{
    private static SmsListener mListener;
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSReceiver";
    private LocalDateTime createdDate;

    public SmsReceiver()
    {
        super();
        this.createdDate = LocalDateTime.now();
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.i(TAG, "Intent received: " + intent.getAction());
        if (!intent.getAction().equals(SMS_RECEIVED))
        {
            return;
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        boolean show = sp.getBoolean("show_messages", true);
        int interval = Integer.parseInt(sp.getString("messaging_interval", "5"));
        if(!show || createdDate.plusSeconds(interval).isAfter(LocalDateTime.now()))
        {
            Log.i(TAG, String.format("Messaging interval (%d) has not yet passed, returning.", interval));
            return;
        }
        Bundle bundle = intent.getExtras();
        if (bundle != null)
        {
            Object[] pdus = (Object[])bundle.get("pdus");
            final SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++)
            {
                messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
            }
            if(messages.length > 0)
            {
                for(SmsMessage message : messages)
                {
                    String address = message.getOriginatingAddress();
                    String messageBody = message.getMessageBody();
                    String number = sp.getString("partner_number", "0");
                    if(number.equals(address))
                    {
                        Log.i(TAG, String.format("Message received: {%s}.", messageBody));
                        createdDate = LocalDateTime.now(); // OnReceive often gets called twice from 1 txt message...this stops it
                        mListener.messageReceived(messageBody);
                        return;
                    }
                }
            }
        }
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}