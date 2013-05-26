package se.kudomessage.jessica;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Fetches and saves preferences using SharedPreferences when it makes sense to save them between sessions.
 */

public class Globals{
	
	private static Activity activity;
	private static String packageIdentifier;
	private static SharedPreferences prefs;

	public static String getAccessToken() {
		return prefs.getString(prefKey("accessToken"), null);
	}

	public static void setAccessToken(String accessToken) {
		prefs.edit().putString(prefKey("accessToken"), accessToken).commit();
	}

	public static String getEmail() {
		return prefs.getString(prefKey("email"), null);
	}

	public static void setEmail(String email) {
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
		prefs.edit().putString(prefKey("server"), server).commit();
	}

	public static String getServer(){
		String server = prefs.getString(prefKey("server"), null);
		return ( server == null ) ? CONSTANTS.DEFAULT_SERVER_ADDRESS : server;
	}
	
	public static void setPort(int port){
		prefs.edit().putInt(prefKey("port"), port).commit();
	}

	public static int getPort(){
		int port = prefs.getInt(prefKey("port"), 0);
		return ( port == 0 ) ? CONSTANTS.DEFAULT_SERVER_PORT : port;
	}

	public static String getGCM() {
		return prefs.getString(prefKey("GCM"), null);
	}

	public static void setGCM(String GCM) {
		prefs.edit().putString(prefKey("GCM"), GCM).commit();
	}
	
	private static String prefKey(String pref){
		return Globals.packageIdentifier+"."+pref;
	}

	public static boolean getRegStatus() {
		return prefs.getBoolean(prefKey("regStatus"), false);
	}

	public static void setRegStatus(Boolean reg) {
		prefs.edit().putBoolean(prefKey("regStatus"), reg).commit();
	}
}
