package org.lacksec.lacktalk;


import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;




public class DebugActivity extends Activity {

	public static final String LOG_TAG = DebugActivity.class.getName();
	
	// Until Accounts are tied in use Hardcoded values for testing
	static final String LOGIN_USERNAME = "user@provider.com";
	static final String LOGIN_PASSWORD = "secret";
	

	private CommunicationEngine commEngine = null;
	private ServiceConnection commEngineConnection = new ServiceConnection(){
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.v(LOG_TAG, "Service: " + name + " connected");
			commEngine = ((CommunicationEngine.getBinder)service).getService();
		}

		public void onServiceDisconnected(ComponentName name) {
			Log.v(LOG_TAG, "Service: " + name + " disconnected");
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(LOG_TAG,"Create");
		setContentView(R.layout.activity_debug);
		
		Intent service = new Intent(this, CommunicationEngine.class);
        this.startService(service);
		getApplicationContext().bindService(service, commEngineConnection, 0);
		
		
		final Button connectButton = (Button) findViewById(R.id.button1);
        connectButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Log.v(LOG_TAG,"CLICK");
                Intent connectRequest = new Intent("android.intent.action.login");
                connectRequest.putExtra("username", LOGIN_USERNAME);
                connectRequest.putExtra("password", LOGIN_PASSWORD);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(connectRequest);
            }
        });
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_debug, menu);
		
		
		return true;
	}

	
	

	
}
