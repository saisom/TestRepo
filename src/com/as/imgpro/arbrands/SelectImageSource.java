package com.as.imgpro.arbrands;

import java.io.FileOutputStream;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class SelectImageSource extends Activity{

	private final int ACTIVITY_SELECT_IMAGE = 1;
	private String imagePath;
	
	@Override
	public void onCreate(Bundle saved){
		super.onCreate(saved);
		setContentView(R.layout.select_image_source);
		
		findViewById(R.id.btnCamera).setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), ShootAndCropActivity.class));
				
			}
			
		});
		
		findViewById(R.id.btnGallery).setOnClickListener(new OnClickListener(){
			
			public void onClick(View v) {
//				startActivity(new Intent(getApplicationContext(), ShootAndCropActivity.class));
				
				Intent galleryIntent = new Intent(Intent.ACTION_PICK, Images.Media.INTERNAL_CONTENT_URI);
				startActivityForResult(galleryIntent, ACTIVITY_SELECT_IMAGE);
				
			}
			
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ACTIVITY_SELECT_IMAGE && resultCode == RESULT_OK) {
			try {
				Uri currImageURI = data.getData();
				String[] proj = { Images.Media.DATA, Images.Media.ORIENTATION };
				Cursor cursor = managedQuery(currImageURI, proj, null, null,
						null);
				int columnIndex = cursor.getColumnIndex(proj[0]);
				cursor.moveToFirst();
				imagePath = cursor.getString(columnIndex);
				//Toast.makeText(getApplicationContext(), "Path : " + mCurrentImagePath, Toast.LENGTH_SHORT).show();
				
				Intent intent = new Intent(getApplicationContext(), ShootAndCropActivity.class);
				intent.putExtra("ImagePath", imagePath);
				startActivity(intent);
				
				
			} catch (Exception e) {
			}
		}
	}
}
