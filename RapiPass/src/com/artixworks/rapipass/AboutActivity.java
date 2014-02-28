package com.artixworks.rapipass;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.artix.rapipass.commons.Utils;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_about)
public class AboutActivity extends Activity {

	private Typeface font;
	@ViewById TextView textViewSoftName;
	@ViewById TextView textViewSoftVersion;
	@ViewById TextView textViewDerechos;
	@ViewById ImageView icon_facebook;
	@ViewById ImageView icon_twitter;

	@AfterViews
	void initializeView() {
		//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		font = Utils.loadFont(getApplicationContext());
		textViewSoftName.setTypeface(font);
		textViewSoftName.setTextSize(26);
		textViewSoftVersion.setTypeface(font);
		textViewSoftVersion.setTextSize(14);
		textViewDerechos.setTypeface(font);
		textViewDerechos.setTextSize(15);
	}
	
	public void openFacebook(View v) {
		Uri uri = Uri.parse("http://www.facebook.com");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
	
	public void openTwitter(View v) {
		Uri uri = Uri.parse("http://www.twitter.com");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
	
	public void openArtix(View v) {
		Uri uri = Uri.parse("http://artixworks.com/");
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
}
