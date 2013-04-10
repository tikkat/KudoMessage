package se.kudomessage.kudomessage_jessica;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent intent) {
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdus[0]);
		
		// Might not be the whole message body!
		String message = sms.getMessageBody();
		String sender = sms.getOriginatingAddress();
		
		Log.v("SMSTagTracker", "Got an SMS: " + message + ", from: " + sender);
		
		// Upload the message to Gmail and tell Hustler
	}

}
