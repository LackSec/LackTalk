package org.lacksec.lacktalk;


import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.lacksec.lacktalk.Constants;


public class CommunicationEngine extends Service {
	public static final String LOG_TAG = CommunicationEngine.class.getName();
	public static ConnectionConfiguration gtalkConnectionConfiguration = new ConnectionConfiguration("talk.google.com",5222, "gmail.com");


	XMPPConnection connection;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onCreate() {


	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//TODO do something useful

		Log.v(LOG_TAG,"On Start");
		return Service.START_NOT_STICKY;
	}

	/**
	 * Launches Separate Thread to Poll for Messages
	 * TODO: Replace this mechanism with a passive system is possible. NOT Clearly supported by this verison of aSmack
	 * @return
	 */

	private Thread startHandlerLoop(){
		Thread t = new Thread(new Runnable() {
			public void run() {
//				LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(sendRequestReceiver,
//						new IntentFilter("android.intent.action.SendMsg"));
				messageHandlerLoop();
			}
		});
		t.start();
		return t;
	}

	/**
	 * Infite Loop to Check For messages. TBR
	 * Fetch, Parse, Broadcast 
	 * 
	 */
	private void messageHandlerLoop()
	{
		PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
		
		// Collect these messages
		PacketCollector collector = connection.createPacketCollector(filter);
		while(true) {
			Packet packet = collector.nextResult();
			if (packet instanceof Message) {
				Message msg = (Message) packet;
				Log.v(LOG_TAG, "RAWMSG:" +msg.getFrom() + "::" + msg.getBody());
				ChatMessageObject CMO = parseMessage(msg);
				broadcastMessage(CMO);
				
			}
		}
	}
	
	/**
	 * Translates Library Message Object to Internal Message Representation.
	 * @param msg
	 * @return
	 */
	private ChatMessageObject parseMessage(Message msg){
		ChatMessageObject CMO = new ChatMessageObject();
		// ...
		// ...
		
		return CMO;
	}

	
	private boolean broadcastMessage(ChatMessageObject messageObject){
		
		Intent newChatMessageIntent = new Intent(Constants.INTENT_CHATMSG_SEND);
		newChatMessageIntent.
		LocalBroadcastManager.getInstance(getApplicationContext());
		return false;
	}
	
	/**
	 * Establishes Connection to XMPP transport Service.
	 * TODO: Secure Way to pass passwords?
	 * @param config 
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean connectToXMPPService(ConnectionConfiguration config, String username, String password) 
	{

		try{

			connection = new XMPPConnection(config);
			connection.connect();
			connection.login(username, password);
			Log.e(LOG_TAG, "END Login");
			return true;
		}
		catch(Exception e)
		{
			Log.e(LOG_TAG,"ERROR");
			Log.e(LOG_TAG,"ERROR:" + e.getCause() + " " + e);
			return false;

		}
	}

}
