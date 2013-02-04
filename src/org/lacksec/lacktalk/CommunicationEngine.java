package org.lacksec.lacktalk;


import java.util.Collection;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;


import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.lacksec.lacktalk.Constants;


public class CommunicationEngine extends Service implements MessageListener {
	public static final String LOG_TAG = CommunicationEngine.class.getName();
	public static ConnectionConfiguration gtalkConnectionConfiguration = new ConnectionConfiguration("talk.google.com",5222, "gmail.com");

	XMPPConnection connection;
	Thread messageHandler;
	
	 private final IBinder mBinder = new getBinder();
	    public class getBinder extends Binder {
	        CommunicationEngine getService() {
	            return CommunicationEngine.this;
	        }
	    }
	

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onCreate() {
		LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(connectRequestReceiver,
			      new IntentFilter("android.intent.action.login"));

		Log.v(LOG_TAG,"Created");
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
				LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(sendRequestReceiver,
					      new IntentFilter("android.intent.action.SendMsg"));
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
	
    public void processMessage(Chat chat, Message message)
    {
    	if(message.getType() == Message.Type.chat)
    	{
    		Log.v(LOG_TAG,chat.getParticipant() + " says: " + message.getBody());
    		ChatMessageObject CMO = parseMessage(message);
    	}
    }
	
	/**
	 * Translates Library Message Object to Internal Message Representation.
	 * @param msg
	 * @return
	 */
	private ChatMessageObject parseMessage(Message msg){
		ChatMessageObject CMO = new ChatMessageObject();
		CMO.receiver = msg.getTo();
		CMO.sender	= msg.getFrom();
		CMO.timestamp = System.currentTimeMillis();      // TODO: Epochtimestamp Will this suffice, is there a better option?
		CMO.message = msg.getBody();
		
		return CMO;
	}

	
	private boolean broadcastMessage(ChatMessageObject messageObject){
		
		Intent newChatMessageIntent = new Intent(Constants.INTENT_CHATMSG_SEND);
		newChatMessageIntent.putExtra("ChatMessageObject", messageObject);
		LocalBroadcastManager.getInstance(getApplicationContext());
		return false;
	}
	
	
	/**
	 * Uglyness to Get Network Off The UI Thread.
	 */
	class ConnectGTalkTask extends AsyncTask<String, Void, Boolean> {

	    protected Boolean doInBackground(String... params) {
	    	Log.v(LOG_TAG,">"+params[0] +" % " +params[1]+"<");
	    	return connectToXMPPService(gtalkConnectionConfiguration, params[0], params[1]) ;
	        
	    }

	    protected void onPostExecute(Boolean b) {
	    	Intent responseIntent;
	    	if(b){
  	    		responseIntent = new Intent(Constants.INTENT_CONNECT_SUCCEEDED);
  	    		messageHandler = startHandlerLoop();								// TODO: Find better place to start HandlerLoop / Get Rid Of handler Loop cuze its stupid
  	    	}else{
  	    		responseIntent = new Intent(Constants.INTENT_CONNECT_FAILED);
  	    	}
  	    	
		
  	    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(responseIntent);
	    }
	 }

	
	/**
	 * Establishes Connection to XMPP transport Service.
	 * TODO: Secure Way to pass passwords?
	 * @param config 
	 * @param username
	 * @param password
	 * @return
	 */
	
	 //new RetreiveFeedTask().execute(urlToRssFeed);
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
	
	 public void sendMessage(String message, String to) throws XMPPException
	    {
		 Log.v(LOG_TAG,"Message out (To: " + to + " Message: " + message + ")");
	    Chat chat = connection.getChatManager().createChat(to, this);
	    chat.sendMessage(message);
	    }
	
	private BroadcastReceiver sendRequestReceiver = new BroadcastReceiver() {
	  	  @Override
	  	  public void onReceive(Context context, Intent intent) {
	  	    // Get extra data included in the Intent
	  		  Log.d(LOG_TAG,"enter Recieve");
	  	    try {
				sendMessage(intent.getExtras().getString("message"),intent.getExtras().getString("recipient"));
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				Log.e(LOG_TAG,e.getMessage());
			}
	  	  }
	  	};
	  	
		private BroadcastReceiver connectRequestReceiver = new BroadcastReceiver() {
		  	  @Override
		  	  public void onReceive(Context context, Intent intent) {
		  		  new ConnectGTalkTask().execute(intent.getStringExtra("username"),intent.getStringExtra("password"));
		  	  }
		  	};
	  	
	   
	 
	    public String displayBuddyList()
	    {
	    Roster roster = connection.getRoster();
	    Collection<RosterEntry> entries = roster.getEntries();
//	 
//	    System.out.println("\n\n" + entries.size() + " buddy(ies):");
//	    for(RosterEntry r:entries)
//	    {
//	    System.out.println(r.getUser());
//	    }
	    return "ROSTER";
	    }
	
	

}
