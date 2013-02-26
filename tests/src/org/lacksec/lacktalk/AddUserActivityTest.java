package org.lacksec.lacktalk;

import android.test.ActivityInstrumentationTestCase2;
import org.lacksec.lacktalk.activity.AddUserActivity;

/**
 * Creation info:
 * User: darndt
 * Date: 2013/02/26
 * Time: 11:51
 */
public class AddUserActivityTest extends ActivityInstrumentationTestCase2<AddUserActivity> {

	AddUserActivity mActivity;

	public AddUserActivityTest() {
		super("org.lacksec.lacktalk.activity", AddUserActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		setActivityInitialTouchMode(false);
		mActivity = getActivity();

		// Things we want to execute before every test
	}

	public void testPreConditions() {
		// Test things that are initialized when the activity is started.
	}
}
