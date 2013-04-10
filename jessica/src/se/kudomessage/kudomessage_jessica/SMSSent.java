package se.kudomessage.kudomessage_jessica;

import android.app.Activity;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

public class SMSSent {

	public SMSSent(Activity activity) {
		Log.v("SMSTagTracker", "CREATE SMSSent.");
		
		SMSObserver content = new SMSObserver(new Handler(), activity); 
		activity.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, content);
	}

	public class SMSObserver extends ContentObserver {
		private Context context;

		public SMSObserver(Handler handler, Context context){
			super(handler);
			this.context = context;
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			
			Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms"), null, null, null, null);
			cursor.moveToFirst();

			String protocol = cursor.getString(cursor.getColumnIndex("protocol"));

			if (protocol == null) {
				int type = cursor.getInt(cursor.getColumnIndex("type"));
                
                if(type == 2){	                             
                    String message = cursor.getString(cursor.getColumnIndex("body")).trim();
                    String receiver = cursor.getString(cursor.getColumnIndex("address")).trim();
                    
                    Log.v("SMSTagTracker", "SMS SENT: The user sent the message \"" + message + "\" to " + receiver);
                }
			}
		}
	}
}
