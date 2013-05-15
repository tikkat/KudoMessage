package se.kudomessage.jessica;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
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
			this.initOAuth();
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

	public void initOAuth() {
		OAuthModel.requestAccessToken();
	}

}
