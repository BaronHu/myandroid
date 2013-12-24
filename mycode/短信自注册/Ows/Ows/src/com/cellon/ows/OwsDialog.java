package com.cellon.ows;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.util.Log;

public class OwsDialog extends AlertDialog {

	private OwsTool tool;
	private Context mContext;
	private AlertDialog alertDialog;
	private boolean isDealed = false;
	
	public OwsDialog(Context context) {
		super(context);
		tool = new OwsTool(context);
		mContext = context;
		showDialog();
	}
	
	public void showDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(R.string.prompt)
		.setMessage(R.string.ows_text)
		.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String s = tool.composeMsg("1");
				tool.sendSms(s, 1);
				isDealed = true;
			}
		})
		.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String s = tool.composeMsg("0");
				tool.sendSms(s, 1);
				isDealed = true;
			}
		});
		alertDialog = builder.create();
		alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				if(!isDealed){
					String s = tool.composeMsg("1");
					tool.sendSms(s,1);
					Log.d("Ows", "onDismiss--->" + isDealed);
				}else{
					Log.d("Ows", "onDismiss--->" + isDealed);
				}
			}
		});
		alertDialog.show();
	}
}
