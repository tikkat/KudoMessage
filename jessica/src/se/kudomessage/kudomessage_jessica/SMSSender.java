package se.kudomessage.kudomessage_jessica;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;

public class SMSSender extends Activity {
	public void sendSMS(String message, String receiver) {
		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
		
		registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                    	Log.v("SMSTagTracker", "SEND SMS: SMS sent.");
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    	Log.v("SMSTagTracker", "SEND SMS: Generic failure.");
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                    	Log.v("SMSTagTracker", "SEND SMS: No service.");
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                    	Log.v("SMSTagTracker", "SEND SMS: Null PDU.");
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                    	Log.v("SMSTagTracker", "SEND SMS: Radio off.");
                        break;
                }
            }
        }, new IntentFilter("SMS_SENT"));
		
		// Send the SMS
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(receiver, null, message, sentPI, null);
		
		// Put the SMS in Sent. This should probably be made in another way, this way is not recommended.
		ContentValues values = new ContentValues();
		values.put("address", receiver);
		values.put("body", message);
		getContentResolver().insert(Uri.parse("content://sms/sent"), values);
	}
}
