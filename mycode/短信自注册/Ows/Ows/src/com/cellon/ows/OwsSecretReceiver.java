package com.cellon.ows;

import static android.provider.Telephony.Intents.SECRET_CODE_ACTION;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OwsSecretReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("Ows", "OwsSecretReceiver action=" + intent.getAction());
		if (intent.getAction().equals(SECRET_CODE_ACTION)) {
			Intent in = new Intent(context, OwsSettingPreference.class);
	        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		    context.startActivity(in);
		}

	}

}
