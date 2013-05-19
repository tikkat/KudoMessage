package se.kudomessage.jessica;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author hultner
 *
 * Fetches and saves preferences using SharedPreferences when it makes sense to save them between sessions.
 * 
 */

public class Globals{
	
	private static String accessToken;
	private static String email;
	private static Activity activity;
	private static String packageIdentifier;
	private static String server;
	private static String GCM;
	private static SharedPreferences prefs;

	public static String getAccessToken() {
		return prefs.getString(prefKey("accessToken"), null);
	}

	public static void setAccessToken(String accessToken) {
		//Globals.accessToken = accessToken;
		prefs.edit().putString(prefKey("accessToken"), accessToken).commit();
	}

	public static String getEmail() {
		return prefs.getString(prefKey("email"), null);
	}

	public static void setEmail(String email) {
		//Globals.email = email;
		prefs.edit().putString(prefKey("email"), email).commit();
	}
	
	public static Activity getActivity() {
		return activity;
	}

	public static void setActivity(Activity activity) {
		Globals.activity = activity;
		Globals.packageIdentifier = activity.getPackageName();
		Globals.prefs = activity.getSharedPreferences(
				Globals.packageIdentifier, Context.MODE_PRIVATE);
	}
	
	public static void setServer(String server){
		//Globals.apiPath = apiPath;
		prefs.edit().putString(prefKey("server"), server).commit();
	}

	public static String getServer(){
		return prefs.getString(prefKey("server"), null);
	}

	public static String getGCM() {
		return prefs.getString(prefKey("GCM"), null);
	}

	public static void setGCM(String GCM) {
		//Globals.GCM = GCM;
		prefs.edit().putString(prefKey("GCM"), GCM).commit();
	}
	
	private static String prefKey(String pref){
		return Globals.packageIdentifier+"."+pref;
	}
}
