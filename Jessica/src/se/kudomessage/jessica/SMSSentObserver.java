package se.kudomessage.jessica;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

public class SMSSentObserver extends ContentObserver {
	private Context context;

	public SMSSentObserver(Handler handler, Context context){
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
                String content = cursor.getString(cursor.getColumnIndex("body")).trim();
                String receiver = cursor.getString(cursor.getColumnIndex("address")).trim();

        		KudoMessage message = new KudoMessage();
        		
        		// Might not be the whole message body!
        		message.content = content;
        		message.origin = Globals.getEmail();
        		message.addReceiver(receiver);
        		
                Log.v("SMSTagTracker", "SMS SENT: The user sent the message \"" + content + "\" to " + receiver);
                
                MessageModel.sentMessage(message);
                
            }
		}
	}
}

