package se.kudomessage.jessica;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService{

	public GCMIntentService() {
		super("509205524891");
	}

	@Override
	protected void onRegistered(Context arg0, String registrationId) {
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
	}

	@Override
	protected void onMessage(Context arg0, Intent intent) {
		Log.v("SMSTagTracker", "In from GCM: " + intent.toString());
	}

	@Override
	protected void onError(Context arg0, String errorId) {
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		return super.onRecoverableError(context, errorId);
	}
}