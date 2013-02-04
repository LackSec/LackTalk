package org.lacksec.lacktalk;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Internal Representation of ChatMessage
 * @author jazz
 *
 */

public class ChatMessageObject implements Parcelable  {

	String sender;
	String receiver;
	String message;
	long timestamp;
	
	
	public ChatMessageObject(){
		
	}
	
	public ChatMessageObject(Parcel in){
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(receiver);
		dest.writeString(sender);
		dest.writeLong(timestamp);
		dest.writeString(message);
	}

	
	public void readFromParcel(Parcel in) {
		// TODO Auto-generated method stub
		receiver = in.readString();
		sender = in.readString();
		timestamp = in.readLong();
		message = in.readString();
	}
	
	
	public static final Parcelable.Creator<ChatMessageObject> CREATOR = new Parcelable.Creator<ChatMessageObject>() {  
	    
        public ChatMessageObject createFromParcel(Parcel in) {  
            return new ChatMessageObject(in);  
        }  
   
        public ChatMessageObject[] newArray(int size) {  
            return new ChatMessageObject[size];  
        }  
          
    };
}
