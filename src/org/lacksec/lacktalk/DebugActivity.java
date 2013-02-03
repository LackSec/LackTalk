package org.lacksec.lacktalk;

import com.lack.lackim.XMPPEngine;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class DebugActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_debug, menu);
		
        Intent service = new Intent(this, CommunicationEngine.class);
        this.startService(service);
		
		
		
		return true;
	}

}
