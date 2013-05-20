package se.kudomessage.jessica;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent intent) {
		Log.i(CONSTANTS.TAG, "onReceive");
		
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdus[0]);
		
		// Might not be the whole message body! (multipart sms)
		String content = sms.getMessageBody();
		String origin = sms.getOriginatingAddress();
		
		KudoMessage message = new KudoMessage(content, origin, Globals.getEmail());
		PushController.pushMessage(message);
	}
}