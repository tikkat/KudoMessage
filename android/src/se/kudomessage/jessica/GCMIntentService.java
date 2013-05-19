package se.kudomessage.jessica;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
	
	public GCMIntentService() {
		super(CONSTANTS.SENDER_ID);
	}

	@Override
	protected void onError(Context arg0, String arg1) {}

	@Override
	protected void onMessage(Context arg0, Intent intent) {
		Log.i(CONSTANTS.TAG, "IN FROM GCM: " + intent.getExtras().getString("json"));
		
		try {
			JSONObject input = new JSONObject(intent.getExtras().getString("json"));
			
			String receiver = input.getString("receiver");
			String content = input.getString("content");
			
			MessageModel.sendMessage(receiver, content);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onRegistered(Context arg0, String GCMKey) {
		Log.i(CONSTANTS.TAG, "GCM DID REG");
		
		Globals.setGCM(GCMKey);
		OAuthController.requestAccessToken();
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {}
}