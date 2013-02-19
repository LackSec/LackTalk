package org.lacksec.lacktalk;

import android.test.ActivityInstrumentationTestCase2;

/**
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class org.lacksec.lacktalk.DebugActivityTest \
 * org.lacksec.lacktalk.tests/android.test.InstrumentationTestRunner
 */
public class DebugActivityTest extends ActivityInstrumentationTestCase2<DebugActivity> {

	public DebugActivityTest() {
		super("org.lacksec.lacktalk", DebugActivity.class);
	}

}
