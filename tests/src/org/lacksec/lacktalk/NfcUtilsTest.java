package org.lacksec.lacktalk;

import android.nfc.NdefRecord;
import android.test.AndroidTestCase;
import junit.framework.Assert;
import org.lacksec.lacktalk.util.NfcUtils;

import java.util.Arrays;

/**
 * Creation info:
 * User: darndt
 * Date: 2013/02/25
 * Time: 15:14
 */
public class NfcUtilsTest extends AndroidTestCase {

	public void testCreateNdefRecord() {
		final String message = "testCreateNdefRecord";
		NdefRecord ndefRecord = NfcUtils.createNdefRecord(message);
		byte[] payload = ndefRecord.getPayload();

		// Compare
		Assert.assertTrue(Arrays.equals(message.getBytes(), payload));
	}

}
