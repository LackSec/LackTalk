package org.lacksec.lacktalk;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Internal Representation of ChatMessage
 * @author jazz
 *
 */

public class ChatMessageObject implements Parcelable {

	String sender;
	String receiver;
	String message;
	long timestamp;
	
	
	public ChatMessageObject(){
		
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}
}
