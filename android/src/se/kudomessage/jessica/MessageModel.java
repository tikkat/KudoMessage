package se.kudomessage.jessica;

import android.content.ContentValues;
import android.net.Uri;
import android.telephony.SmsManager;

public class MessageModel {
	public static void sendMessage(String receiver, String content) {
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(receiver, null, content, null, null);
		
		saveMessageToSent(receiver, content);
	}
	
	private static void saveMessageToSent(String receiver, String content) {
		ContentValues values = new ContentValues();
		values.put("address", receiver);
		values.put("body", content);
		
		Globals.getActivity().getContentResolver().insert(Uri.parse("content://sms/sent"), values);
	}
}