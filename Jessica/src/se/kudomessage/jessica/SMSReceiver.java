package se.kudomessage.jessica;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context ctx, Intent intent) {
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdus[0]);

		KudoMessage message = new KudoMessage();
		
		// Might not be the whole message body!
		message.content = sms.getMessageBody();
		message.origin = sms.getOriginatingAddress();
		message.receiver = Globals.getEmail();
		
		Log.v("SMSTagTracker", "Got an SMS: " + message.content + ", from: " + message.origin);
		
		// Upload the message to Hustler
		PushModel.pushMessage(message);
	}

}
