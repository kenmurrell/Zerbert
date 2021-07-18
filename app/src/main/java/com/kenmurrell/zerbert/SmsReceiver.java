package com.kenmurrell.zerbert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.kenmurrell.zerbert.ui.SmsListener;

public class SmsReceiver extends BroadcastReceiver
{
    private static SmsListener mListener;

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Intent recieved: " + intent.getAction());

        if (intent.getAction().equals(SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
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
                        String number = context.getSharedPreferences("userpreferences",0).getString("number", "0");
                        if(number.equals(address))
                        {
                            String text = String.format("Message received: {%s}", messageBody);
                            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
                            mListener.messageReceived(messageBody);
                        }
                    }
                }
            }
        }
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}