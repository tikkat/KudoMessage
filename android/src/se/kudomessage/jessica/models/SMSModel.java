package se.kudomessage.jessica.models;

import se.kudomessage.jessica.CONSTANTS;
import se.kudomessage.jessica.Globals;
import se.kudomessage.jessica.KudoMessage;
import android.content.ContentValues;
import android.net.Uri;
import android.telephony.SmsManager;
import android.util.Log;

public class SMSModel {
	
	private SMSModel(){}
	
	public static void sendSMS(KudoMessage m) {
		Log.v(CONSTANTS.TAG, "Supposed to send \"" + m.content + "\" to " + m.getFirstReceiver());
		
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
