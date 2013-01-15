package com.contactlist;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends Activity {
	
	ListView listView;
    List<RowItem> rowItems;
    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getContactsDetails();
	}

	//Method to retrieve Contact details from the phone database and display in the app in a list view
	private void getContactsDetails() {
		
		//A Progress dialog with a spinning wheel, to instruct the user about the app's current state
		final ProgressDialog dialog = ProgressDialog.show(MainActivity.this, "Please Wait", "Retrieving Contacts...", true);
		
		//A new worker thread is created to retrieve and display the contacts.
		new Thread(new Runnable() {
			public void run() {
				
				rowItems = new ArrayList<RowItem>();
		
				ArrayList<String> contactNames = new ArrayList<String>();
				ArrayList<String> contactNumbers = new ArrayList<String>();
				ArrayList<Bitmap> contactImages = new ArrayList<Bitmap>();
		
				//The 'noImage' is used wherever an image of a contact is not present in the database.
				Bitmap noImage = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		
				Cursor phones = getContentResolver().query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
						null, Phone.DISPLAY_NAME + " COLLATE NOCASE ASC");
        
				phones.moveToFirst();
				do {
					String name = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
					
					String number = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					
					String strId = phones
							.getString(phones
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
            
					Uri contact_Uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, strId);
					InputStream photo_stream = ContactsContract.Contacts.openContactPhotoInputStream
							(getContentResolver(), contact_Uri);
					
					if (photo_stream != null) { //Retrieve and add image if present in database
						BufferedInputStream buf = new BufferedInputStream(photo_stream,8192);
						Bitmap image = BitmapFactory.decodeStream(buf);
						contactImages.add(image);
					}
					else {	//otherwise add 'noImage'
						contactImages.add(noImage);
					}
            
					contactNames.add(name);
					contactNumbers.add(number);
                
				} while (phones.moveToNext());
        
				String[] names = new String[contactNames.size()];
				names = contactNames.toArray(names);
        
				String[] numbers = new String[contactNumbers.size()];
				numbers = contactNumbers.toArray(numbers);
        
				Bitmap[] images = new Bitmap[contactImages.size()];
				images = contactImages.toArray(images);
        
				for (int i = 0; i < names.length; i++) {
					RowItem item = new RowItem(images[i], names[i], numbers[i]);
					rowItems.add(item);
				}
        
				listView = (ListView) findViewById(R.id.contactslist);
				listView.post(new Runnable() {

					@Override
					public void run() {
						CustomListViewAdapter adapter = new CustomListViewAdapter(MainActivity.this,
								R.layout.list_row, rowItems);
						listView.setAdapter(adapter);
		        
						listView.setClickable(true);
		        
						//Assign a click listener to the rows of the list view which runs a new activity with the contact page to make calls and send messages.
						listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
		            
							@Override
							public void onItemClick(AdapterView<?> parent, View view, int position,
							long id) {
								RowItem ri = new RowItem();
								ri = (RowItem) listView.getItemAtPosition(position);
						
								Intent intent = new Intent(view.getContext(), DisplayContactActivity.class);
						
								String cName = ri.getName();
								String cNumber = ri.getNumber();
								Bitmap cImage = ri.getImage();
						
								intent.putExtra("com.contactlist.NAME", cName);
								intent.putExtra("com.contactlist.NUMBER", cNumber);
								intent.putExtra("com.contactlist.IMAGE", cImage);
								startActivity(intent);
							}
						});
					}
        		});
				dialog.dismiss();
        	}
		}).start();
	}
}