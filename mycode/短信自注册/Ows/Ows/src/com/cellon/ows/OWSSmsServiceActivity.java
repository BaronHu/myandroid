package com.cellon.ows;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class OWSSmsServiceActivity extends Activity implements OnClickListener {
	
	private Button send;
	private Button send1;
	
	private OwsTool tool;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		tool = new OwsTool(this);
		
		send = (Button) findViewById(R.id.send);
		send1 = (Button) findViewById(R.id.send1);
		send.setOnClickListener(this);
		send1.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.send :
			String s = "qmobile ch ";
			tool.sendServiceSms(s);
			break;
		case R.id.send1 :
			String s1 = "qmobile ktbh ";
			tool.sendServiceSms(s1);
			break;
		default : 
			break;
		}
		
	}

}
