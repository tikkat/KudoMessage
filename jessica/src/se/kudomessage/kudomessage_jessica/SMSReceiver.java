package se.kudomessage.kudomessage_jessica;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent intent) {
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdus[0]);
		
		// Might not be the whole message body!
		String message = sms.getMessageBody();
		String sender = sms.getOriginatingAddress();
		
		// Get the message to Hustler
	}

}
