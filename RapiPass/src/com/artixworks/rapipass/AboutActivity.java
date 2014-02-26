package com.artixworks.rapipass;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;
import com.artix.rapipass.commons.Utils;

public class AboutActivity extends Activity {

	private Typeface font;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		font = Utils.loadFont(getApplicationContext());
		TextView textName = (TextView) findViewById(R.id.textViewSoftName);
		TextView textVersion = (TextView) findViewById(R.id.textViewSoftVersion);
		TextView textDerechos = (TextView) findViewById(R.id.textViewDerechos);
		textName.setTypeface(font);
		textName.setTextSize(26);
		textVersion.setTypeface(font);
		textVersion.setTextSize(14);
		textDerechos.setTypeface(font);
		textDerechos.setTextSize(15);
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.about, menu);
//		return true;
//	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
