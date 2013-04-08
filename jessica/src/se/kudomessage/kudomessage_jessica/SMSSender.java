package se.kudomessage.kudomessage_jessica;

import android.telephony.SmsManager;

public class SMSSender {
	public void sendSMS(String message, String destination) {
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(destination, null, message, null, null);
	}
}
