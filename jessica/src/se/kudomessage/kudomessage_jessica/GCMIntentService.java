package se.kudomessage.kudomessage_jessica;

import android.content.Context;
import android.content.Intent;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService{

	public GCMIntentService() {
		super(Constants.SENDER_ID);
	}

	@Override
	protected void onRegistered(Context arg0, String registrationId) {
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
	}

	@Override
	protected void onMessage(Context arg0, Intent intent) {
		String message = intent.getStringExtra("message");
	}

	@Override
	protected void onError(Context arg0, String errorId) {
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		return super.onRecoverableError(context, errorId);
	}
}
