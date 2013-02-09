package org.lacksec.lacktalk;

import android.app.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RosterActivity extends Activity{
	public static String LOG_TAG = RosterActivity.class.getName();
	
	String[] values = {"gosd","sdasdo,asd:","qwe,w"};
	MySimpleArrayAdapter adapter; 
	
	Context context;
	public void onCreate(Bundle savedInstanceState)
	{
		Log.v(LOG_TAG,"Create Enter");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_roster);
		Log.v(LOG_TAG,"Create End");
		
		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(rosterUpdateReceiver,
				new IntentFilter(Constants.INTENT_ROSTER_UPDATE));

		
		adapter =  new MySimpleArrayAdapter(this, values);
		
		
	       ListView list1 = (ListView) findViewById(R.id.rosterList);
	            // Use your own layout
	        
	        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.rowlayout,values);
	        list1.setAdapter(adapter);
	        
	        
	        Intent rosterRequest = new Intent(Constants.INTENT_ROSTER_REQUEST);
	        LocalBroadcastManager.getInstance(this).sendBroadcast(rosterRequest);
	        
	        
	}
	
	private BroadcastReceiver rosterUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.v(LOG_TAG,"Roster Recieved");
			String[] contacts = intent.getStringArrayExtra("roster");
			adapter.updateValues(contacts);
			adapter.notifyDataSetChanged();
		}
	};

}
