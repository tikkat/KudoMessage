package se.kudomessage.jessica;

import org.json.JSONException;
import org.json.JSONObject;

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
		Globals.setGCM(registrationId);
		
		Log.v("GCMKey", "GOT GCM:" + registrationId);
		
		//((MainActivity) Globals.getActivity()).initOAuth();
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {	
	}

	@Override
	protected void onMessage(Context arg0, Intent intent) {
		Log.v("SMSTagTracker", "In from GCM: " + intent.toString());
		
		try {
			JSONObject json = new JSONObject(intent.getExtras().getString("message"));
			KudoMessage message = new KudoMessage();
			
			message.content = json.getString("content");
			message.receiver = json.getString("receiver");
			
			MessageModel.sendMessage(message);
		} catch (JSONException e) {
			Log.v("SMSTagTracker", "Något fel i json från Hustler.");
		}
	}

	@Override
	protected void onError(Context arg0, String errorId) {
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		return super.onRecoverableError(context, errorId);
	}
}