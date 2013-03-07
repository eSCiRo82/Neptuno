package com.lucassoft.neptunoproject;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class NeptunoMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_neptuno_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_neptuno_main, menu);
		return true;
	}

}
