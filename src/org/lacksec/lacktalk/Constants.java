package org.lacksec.lacktalk;


public interface Constants {

	public static final String INTENT_CHATMSG_RECEIVED = "intent.chatmsg.received";
	public static final String INTENT_CHATMSG_SEND = "intent.chatmsg.send";
	public static final String INTENT_CONNECT_REQUEST = "intent.chatmsg.connect";
	public static final String INTENT_CONNECT_SUCCEEDED = "intent.connection.established";
	public static final String INTENT_CONNECT_FAILED = "intent.connection.failed";
	
	public static final String INTENT_ROSTER_REQUEST = "intent.roster.request";
	public static final String INTENT_ROSTER_UPDATE = "intent.roster.update";
	
	public static final String INTENT_NOT_CONNECTED = "intent.update.not_connected";

}
