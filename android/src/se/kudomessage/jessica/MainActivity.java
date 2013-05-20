package se.kudomessage.jessica;

import se.kudomessage.jessica.CONSTANTS;
import se.kudomessage.jessica.Globals;

import com.google.android.gcm.GCMRegistrar;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.app.Activity;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Globals.setActivity(this);
		
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		
		// TODO: Remove in final release.
		GCMRegistrar.unregister(this);
		
		String GCMKey = GCMRegistrar.getRegistrationId(this);	
		
		if (GCMKey.equals("")) {
			GCMRegistrar.register(this, CONSTANTS.SENDER_ID);
		} else {
			Globals.setGCM(GCMKey);
			OAuthController.requestAccessToken();
		}
	}
	
	public void init() {
		Log.i(CONSTANTS.TAG, "GCMKey: " + Globals.getGCM());
		Log.i(CONSTANTS.TAG, "TOKEN: " + Globals.getAccessToken());
	
		PushController.registerDevice();
		getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, new SMSSentObserver(new Handler(), this));
	}
	
	/** Button listener **/

	/**
	 * Triggered when the user clicks the "Sign in with Google" button
	 * 
	 * @param view required for button listeners 
	 */
	public void requestOAuth(View view) {
		OAuthController.requestAccessToken();
	}
	
	
	/**
	 * Triggered when the users clicks the "Continue" button
	 * 
	 * @param view required for button listeners 
	 */
	public void registerUser(View view){
		// To be added some error control, check so that the server 
		// address is a valid server and that the user have connected with google.
		if(Globals.getAccessToken() != null ){
			//Error stuff for accesstoken
			Log.e("registerUser", "Accesstoken not granted");
		}else if(PushController.testServer() != false){
			//If the server isn't a valid KudoMessage server.
			Log.e("registerUser", "The message server wasn't valid");
		}else if(Globals.getGCM() != null){
			//Please wait for GCM something message
			Log.e("registerUser", "GCM wasn't fetched yet");
		}else{
			//Success
			Log.d("registerUser", "Successfully");
			init();
		}
	}
}