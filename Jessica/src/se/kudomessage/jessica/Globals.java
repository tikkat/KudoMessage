package se.kudomessage.jessica;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Globals{
	
	private static String accessToken;
	private static String email;
	private static Activity activity;
	private static String packageIdentifier;
	private static String apiPath = "http://172.20.10.5:8080/KudoMessage_Web_Application/api/rest/";
	private static String GCM;
	private static SharedPreferences prefs;

	public static String getAccessToken() {
		return accessToken;
	}

	public static void setAccessToken(String accessToken) {
		Globals.accessToken = accessToken;
	}

	public static String getEmail() {
		return email;
	}

	public static void setEmail(String email) {
		Globals.email = email;
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
	
	public static void setApiPath(String apiPath){
		Globals.apiPath = apiPath;
	}

	public static String getApiPath(){
		return apiPath;
	}

	public static String getGCM() {
		return GCM;
	}

	public static void setGCM(String GCM) {
		Globals.GCM = GCM;
	}
}
