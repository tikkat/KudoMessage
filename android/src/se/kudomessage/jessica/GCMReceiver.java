package se.kudomessage.jessica;

import android.content.Context;

import com.google.android.gcm.GCMBroadcastReceiver;

public class GCMReceiver extends GCMBroadcastReceiver {
    @Override
    protected String getGCMIntentServiceClassName(Context context)
    {
        return "se.kudomessage.jessica.models.GCMIntentService";
    }
}