package se.kudomessage;

import se.kudomessage.CONSTANTS;
import se.kudomessage.Globals;

import com.google.android.gcm.GCMRegistrar;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.app.Activity;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Globals.activity = this;
		
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		
		// TODO: Remove in final release.
		GCMRegistrar.unregister(this);
		
		String GCMKey = GCMRegistrar.getRegistrationId(this);	
		
		if (GCMKey.equals("")) {
			GCMRegistrar.register(this, CONSTANTS.SENDER_ID);
		} else {
			Globals.GCMKey = GCMKey;
			OAuthController.requestAccessToken();
		}
	}
	
	public void init() {
		Log.i(CONSTANTS.TAG, "GCMKey: " + Globals.GCMKey);
		Log.i(CONSTANTS.TAG, "TOKEN: " + Globals.accessToken);
	
		PushHandler.registerDevice();
		getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, new SMSSentObserver(new Handler(), this));
	}
}