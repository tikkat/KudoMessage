package se.kudomessage.kudomessage_jessica;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.util.Log;

public class SocketHandler {
	Socket socket = null;
	PrintWriter out = null;
	BufferedReader in = null;

	public SocketHandler() {
		new Thread(new Runnable() {
			public void run() {
				try {
					socket = new Socket(Constants.IP_ADDRESS, Constants.PORT);

					out = new PrintWriter(socket.getOutputStream(), true);
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

					Log.v("SMSTagTracker", "Connected to socket " + Constants.IP_ADDRESS + ":" + Constants.PORT);
				} catch (UnknownHostException e) {
					Log.v("SMSTagTracker", "Couldn't find the host.");
				} catch (ConnectException e) {
					Log.v("SMSTagTracker", "Failed to connect to socket " + Constants.IP_ADDRESS + ":" + Constants.PORT);
				} catch (Exception e) {
					Log.v("SMSTagTracker", "Exception", e);
				}
			}
		}).start();
	}
}
