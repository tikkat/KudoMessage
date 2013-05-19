package se.kudomessage.jessica;

import android.content.ContentValues;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;

public class SMSModel {
	
	private SMSModel(){}
	
	public static void sendSMS(KudoMessage m) {
		Log.v("SMSTagTracker", "Skall skicka meddelande " + m.content + " till " + m.getFirstReceiver());
		
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(m.getFirstReceiver(), null, m.content, null, null);
		
		saveSMSToSent(m);
	}
	
	private static void saveSMSToSent(KudoMessage m) {
		ContentValues values = new ContentValues();
		values.put("address", m.getFirstReceiver());
		values.put("body", m.content);
		
		Globals.getActivity().getContentResolver().insert(Uri.parse("content://sms/sent"), values);
	}

}
