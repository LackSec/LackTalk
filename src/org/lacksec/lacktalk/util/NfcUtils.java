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
	public static final String TAG = NfcUtils.class.getSimpleName();

	public static NdefMessage getNdef() {
		NdefRecord[] ndefRecords = {createNdefRecord("Hello World")};
		NdefMessage ndefMessage = new NdefMessage(ndefRecords);
		return ndefMessage;
	}

	public static NdefRecord createNdefRecord(String message) {
		byte[] textBytes = message.getBytes();
		NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], textBytes);
		return recordNFC;
	}

	public static NdefMessage[] getNdefMessages(Intent intent) {
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
			Log.d(TAG, "Unknown intent.");
		}
		return msgs;
	}
}
