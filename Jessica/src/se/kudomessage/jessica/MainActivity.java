package se.kudomessage.jessica;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Globals.setActivity(this);
		
		if(Globals.getApiPath() == null){
			Globals.setApiPath(CONSTANTS.DEFAULT_API_PATH);
		}

		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		
		GCMRegistrar.unregister(this);
		
		String GCMKey = GCMRegistrar.getRegistrationId(this);	
		if(GCMKey.equals("")){
			//Has an callback calling initOAuth
			GCMRegistrar.register(this, CONSTANTS.SENDER_ID );
		}else{
			Globals.setGCM(GCMKey);
			//this.initOAuth();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void init(){
		PushModel.registerServer(); 
		this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, new SMSSentObserver(new Handler(), this));
		
	}

	/**
	 * Triggered when the user clicks the "Sign in with Google" button
	 * 
	 * @param view required for button listeners 
	 */
	public void initOAuth(View view) {
		OAuthModel.requestAccessToken();
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
		}else if(PushModel.testServer() != false){
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
