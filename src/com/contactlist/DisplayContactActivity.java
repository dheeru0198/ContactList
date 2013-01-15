package com.contactlist;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

//An activity that displays a single contact details with a functionality to make calls and send messages.
public class DisplayContactActivity extends Activity {
	
	TextView nameView;
	TextView numberView;
	ImageView imageView;
	
	String cName;
	String cNumber;
	Bitmap cImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_contact);
		showContactDetails();
	}
	
	private void showContactDetails(){
		Intent intent = getIntent();
		
		nameView = (TextView) findViewById(R.id.cName);
		numberView = (TextView) findViewById(R.id.cNumber);
		imageView = (ImageView) findViewById(R.id.cImage);
		
		cName = intent.getStringExtra("com.contactlist.NAME");
		cNumber = intent.getStringExtra("com.contactlist.NUMBER");
		cImage = intent.getParcelableExtra("com.contactlist.IMAGE");
		
		nameView.setText(cName);
		numberView.setText(cNumber);
		imageView.setImageBitmap(cImage);
	}
	
	public void makeCall(View view){
		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse("tel:" + cNumber));
		startActivity(callIntent);
	}
	
	public void sendMessage(View view){
		Intent smsIntent = new Intent(Intent.ACTION_VIEW);
		smsIntent.setData(Uri.parse("sms:" + cNumber));
		startActivity(smsIntent);
	}
}