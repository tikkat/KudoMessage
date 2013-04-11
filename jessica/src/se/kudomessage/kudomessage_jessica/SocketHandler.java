package se.kudomessage.kudomessage_jessica;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class SocketHandler {
	Socket socket = null;
	PrintWriter out = null;
	BufferedReader in = null;

	public SocketHandler() {
		new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				try {
					socket = new Socket(Constants.IP_ADDRESS, Constants.PORT);

					out = new PrintWriter(socket.getOutputStream(), true);
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

					Log.v("SMSTagTracker", "Connected to socket " + Constants.IP_ADDRESS + ":" + Constants.PORT);
				
					init();
				} catch (UnknownHostException e) {
					Log.v("SMSTagTracker", "Couldn't find the host.");
				} catch (ConnectException e) {
					Log.v("SMSTagTracker", "Failed to connect to socket " + Constants.IP_ADDRESS + ":" + Constants.PORT);
				} catch (Exception e) {
					Log.v("SMSTagTracker", "Exception", e);
				}
			}
			
			private void init() {
				JSONObject output = new JSONObject();
				try {
					output.put("action", "init");
					output.put("token", MainActivity.getToken());
				} catch (JSONException e) {
				}
				
				out.println(output);
				out.flush();
				
				output = new JSONObject();
				try {
					output.put("action", "registerAndroidDevice");
					output.put("gcm", MainActivity.getGCM());
				} catch (JSONException e) {
				}
				
				out.println(output);
				out.flush();
			}
		}).start();
	}
}
