package org.lacksec.lacktalk.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.widget.Toast;
import org.lacksec.lacktalk.util.NfcUtils;

/**
 * Creation info:
 * User: darndt
 * Date: 2013/02/23
 * Time: 23:31
 */
public class AddUserActivity extends Activity {
	NfcAdapter mNfcAdapter;
	PendingIntent mNfcPendingIntent;
	IntentFilter[] mNdefExchangeFilters;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		mNfcPendingIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		try {
			ndefDetected.addDataType("text/plain");
		} catch (IntentFilter.MalformedMimeTypeException e) {
			e.printStackTrace();
		}
		mNdefExchangeFilters = new IntentFilter[]{ndefDetected};
	}

	@Override
	protected void onResume() {
		super.onResume();

		mNfcAdapter.enableForegroundNdefPush(AddUserActivity.this, NfcUtils.getNdef());
		mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mNdefExchangeFilters, null);
	}

	@Override
	protected void onPause() {
		super.onPause();

		mNfcAdapter.disableForegroundNdefPush(this);
		mNfcAdapter.disableForegroundDispatch(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			NdefMessage[] msgs = NfcUtils.getNdefMessages(intent);
			NdefRecord ndefRecord = msgs[0].getRecords()[0];
			String msg = new String(ndefRecord.getPayload());
			Toast.makeText(this, msg.toString(), Toast.LENGTH_LONG).show();
		}
	}
}
