package se.kudomessage.kudomessage_jessica;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;

public class SMSHandler {
	private Activity activity;
	private PendingIntent sendSMSCallback;
	
	public SMSHandler(Activity activity) {
		this.activity = activity;
		
		// Check for SMS sent
		activity.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, new SMSSentObserver(new Handler(), activity));
		
		// Check the status of the SMS sent by sendSMS()
		sendSMSCallback = PendingIntent.getBroadcast(activity, 0, new Intent("SMS_SENT"), 0);
		activity.registerReceiver(new BroadcastReceiver(){
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
	}
	
	public void sendSMS(String message, String receiver) {
		Log.v("SMSTagTracker", "Should now send an SMS");
		
		// Send the SMS
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(receiver, null, message, sendSMSCallback, null);
		
		// Put the SMS in Sent. This should probably be made in another way, this way is not recommended.
		ContentValues values = new ContentValues();
		values.put("address", receiver);
		values.put("body", message);
		activity.getContentResolver().insert(Uri.parse("content://sms/sent"), values);
	}
	
	public class SMSSentObserver extends ContentObserver {
		private Context context;

		public SMSSentObserver(Handler handler, Context context){
			super(handler);
			this.context = context;
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			
			Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms"), null, null, null, null);
			cursor.moveToFirst();

			String protocol = cursor.getString(cursor.getColumnIndex("protocol"));

			if (protocol == null) {
				int type = cursor.getInt(cursor.getColumnIndex("type"));
                
                if(type == 2){	                             
                    String message = cursor.getString(cursor.getColumnIndex("body")).trim();
                    String receiver = cursor.getString(cursor.getColumnIndex("address")).trim();
                    
                    Log.v("SMSTagTracker", "SMS SENT: The user sent the message \"" + message + "\" to " + receiver);
                }
			}
		}
	}
}
