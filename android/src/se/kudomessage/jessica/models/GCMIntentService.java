package se.kudomessage.jessica.models;

import org.json.JSONException;
import org.json.JSONObject;

import se.kudomessage.jessica.CONSTANTS;
import se.kudomessage.jessica.Globals;
import se.kudomessage.jessica.KudoMessage;

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
			
			// TODO: Fix multiple receivers
			String receiver = input.getJSONArray("receivers").getString(0);
			String content = input.getString("content");
			
			KudoMessage message = new KudoMessage(content, Globals.getEmail(), receiver);
			SMSModel.sendSMS(message);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onRegistered(Context arg0, String GCMKey) {
		
		Log.i(CONSTANTS.TAG, "GCM DID REG");
		
		//Saves the GCMKey for late usage
		Globals.setGCM(GCMKey);
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {}
}