package se.kudomessage.jessica;

import android.content.ContentValues;
import android.net.Uri;
import android.telephony.SmsManager;

public class SMSModel {
	
	private SMSModel(){}
	
	public static void sendSMS(KudoMessage m) {
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(m.receiver, null, m.content, null, null);
		
		saveSMSToSent(m);
	}
	
	private static void saveSMSToSent(KudoMessage m) {
		ContentValues values = new ContentValues();
		values.put("address", m.receiver);
		values.put("body", m.content);
		
		Globals.getActivity().getContentResolver().insert(Uri.parse("content://sms/sent"), values);
	}

}
