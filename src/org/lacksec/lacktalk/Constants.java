/*
 * Copyright (c) 2013.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Don't be a Duck Public License v1.0.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the Don't be a Duck Public License
 * along with this program. If not, just don't be a duck.
 */

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

	public static final String SYSTEM_USER_ID = "systemuser";

}
