package se.kudomessage.jessica.controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.json.JSONObject;

import se.kudomessage.jessica.CONSTANTS;
import se.kudomessage.jessica.Globals;
import se.kudomessage.jessica.KudoMessage;

import android.util.Log;

public class PushController {
	private static Socket socket = null;
	private static BufferedReader in = null;
	private static PrintWriter out = null;
	
	// TODO: Variables maybe should be made non-static?
	// If the phone handles many SMS at one time they might null the socket or writers for the other 
	
	private static boolean openConnection() {	
		try {
			socket = new Socket(Globals.getServer(), CONSTANTS.DEFAULT_SERVER_PORT);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (socket != null) {
			in = null;
			out = null;
			
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
				
				OutputStreamWriter outstream = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
				out = new PrintWriter(outstream, true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (in != null && out != null) {
				Log.i(CONSTANTS.TAG, "Connected to server " + Globals.getServer() + ":" + CONSTANTS.DEFAULT_SERVER_PORT);
			
				out.println("GATEWAY");
				out.flush();
				
				return true;
			}
		}
		
		return false;
	}
	
	private static void closeConnection() {
		try {
			out.println("CLOSE");
			out.flush();
			
			in.close();
			out.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		in = null;
		out = null;
		socket = null;
	}
	
	public static void registerDevice() {
		new Thread(new Runnable() {
			public void run() {
				if (openConnection()) {
					try {
						JSONObject output = new JSONObject();
						output.put("action", "register-device");
						output.put("token", Globals.getAccessToken());
						
						output.put("gcm", Globals.getGCM());
						
						out.println(output.toString());
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				closeConnection();
			}
		}).start();
	}
	
	public static void pushMessage(final String content, String origin, String receiver) {
		//TODO: Fix for internationalization
		final String _receiver = receiver.replace(" ", "").replace("+46", "0").replace("-", "");
		final String _origin = origin.replace(" ", "").replace("+46", "0").replace("-", "");
		
		new Thread(new Runnable() {
			public void run() {
				if (openConnection()) {
					try {
						JSONObject output = new JSONObject();
						output.put("action", "push-message");
						output.put("token", Globals.getAccessToken());
						
						JSONObject message = new JSONObject();
						message.put("content", content);
						message.put("origin", _origin);
						message.put("receiver", _receiver);
						
						output.put("message", message);
						
						out.println(output.toString());
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				closeConnection();
			}
		}).start();
	}

	public static void pushMessage(KudoMessage message) {
		pushMessage(message.content, message.origin, message.getFirstReceiver());
	}

	public static boolean testServer() {
		//Not actually working yet
		//TODO: Handle the response and timeouts
		/*new Thread(new Runnable() {
			public void run() {
				if (openConnection()) {
					try {
						JSONObject output = new JSONObject();
						output.put("action", "testServer");
						
						
						out.println(output.toString());
						out.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				closeConnection();
			}
		}).start();*/

		ExecutorService service = Executors.newFixedThreadPool(10);

		FutureTask<String> requestTask = new FutureTask<String>(new TestServerJob());
		service.submit(requestTask);
		
		try {
			String response = requestTask.get(30, TimeUnit.SECONDS);
			Log.d("ServerTest", response);
			return response.equals("TEST_OK");
		} catch (ExecutionException e1) {
			Log.e("ServerTest","Failed in execution: " + e1.getMessage());
			return false;
		} catch (InterruptedException e2) {
			Log.e("ServerTest","InterruptedException");
			return false;
		} catch (TimeoutException e3) {
			Log.e("ServerTest","TimeoutException: " + e3.getMessage());
			return false;
		}
	}
	
	public static class TestServerJob implements Callable<String> {
		@Override
		public String call() throws Exception {
			if (openConnection()) {
				try {
					JSONObject output = new JSONObject();
					output.put("action", "test-server");
					
					out.println(output.toString());
					out.flush();
					
					return in.readLine();
				} catch (Exception e) {
					e.printStackTrace();
					return "TEST_ERROR";
				}	
			}
			
			closeConnection();
			return "TEST_FAILED";
		}
	}
}