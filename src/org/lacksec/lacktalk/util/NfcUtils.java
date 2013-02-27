package org.lacksec.lacktalk.util;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.util.Log;

/**
 * Creation info:
 * User: darndt
 * Date: 2013/02/23
 * Time: 23:44
 */
public class NfcUtils {
	public static final String LOG_TAG = NfcUtils.class.getSimpleName();

	public static NdefMessage getNdefMessage() {
		Log.v(LOG_TAG, "getNdefMessage - begin");
		NdefRecord[] ndefRecords = {createNdefRecord("Hello World")};
		return new NdefMessage(ndefRecords);
	}

	public static NdefRecord createNdefRecord(String message) {
		Log.v(LOG_TAG, "createNdefRecord - begin");
		byte[] textBytes = message.getBytes();
		return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], textBytes);
	}

	public static NdefMessage[] getNdefMessages(Intent intent) {
		Log.v(LOG_TAG, "getNdefMessages - begin");
		// Parse the intent
		NdefMessage[] msgs = null;
		String action = intent.getAction();
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage) rawMsgs[i];
				}
			} else {
				// Unknown tag type
				byte[] empty = new byte[]{};
				NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
				NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
				msgs = new NdefMessage[]{msg};
			}
		} else {
			Log.d(LOG_TAG, "Unknown intent.");
		}
		return msgs;
	}
}
