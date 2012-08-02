package com.as.imgpro.arbrands;

import java.io.File;

import com.as.imgpro.arbrands.Database.DBRegister;
import com.as.imgpro.arbrands.Database.DB_ImageFeature;
import com.truelife.mobile.android.components.camera.CameraCapture;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShootAndCropActivity extends Activity implements OnClickListener {
	
	//keep track of camera capture intent
	final int CAMERA_CAPTURE = 1;
	//keep track of cropping intent
	final int PIC_CROP = 2;

	private String filePath;
	
	private OpenCV opencv = new OpenCV();
	private TextView txtName;
	private TextView txtDetail;
	private TextView txtYoutube;
	private TextView txtLink;
	private ImageButton captureBtn;
	private ImageView picView;
	private DB_ImageFeature db;
	private Feature imgFeature;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
		setContentView(R.layout.main);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		initControls();
		
		new DBRegister(getApplicationContext());
		
		db = new DB_ImageFeature(getApplicationContext()); 
		
		
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			
			filePath = extras.getString("ImagePath");
			
			File file = new File(filePath);
				Uri uri = Uri.fromFile(file);
				
  			performCrop(uri);
		}else{
			takePhoto();
		}
		
	}
	
	private void initControls() {
		txtName = (TextView) findViewById(R.id.txtName);
		txtDetail = (TextView) findViewById(R.id.txtDetail);
		txtYoutube = (TextView) findViewById(R.id.txtYoutube);
		txtLink = (TextView) findViewById(R.id.txtLink);
		
		picView = (ImageView)findViewById(R.id.picture);
		
		findViewById(R.id.btnCapture).setOnClickListener(this);
		findViewById(R.id.btnSave).setOnClickListener(this);
			
	}

	/**
	   * Handle user returning from both capturing and cropping the image
	   */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
	  		//user is returning from capturing an image using the camera
			if(requestCode == CameraCapture.REQUEST_CAMERA){
				
				Bundle extras = data.getExtras();
	  			
	  			if(extras != null){
	  				filePath = extras.getString(CameraCapture.FILE_FULLPATH);
		  			
	  				File file = new File(filePath);
	  				Uri uri = Uri.fromFile(file);
	  				
		  			performCrop(uri);
		  			
	  			}
	  			
	  		}else if(requestCode == PIC_CROP){
	  			//get the returned data
	  			Bundle extras = data.getExtras();
	  			//get the cropped bitmap
	  			Bitmap thePic = extras.getParcelable("data");
	  			
	  			initSURF(thePic);
	  		}
	  	}
	  }
	  
	  private void initSURF(Bitmap img){
		  
		  int width = img.getWidth();
		  int height = img.getHeight();
		  int[] pixels = new int[width * height];
		  img.getPixels(pixels, 0, width, 0, 0, width, height);
		  
		  opencv.setSURFParams(200);
		  
		  opencv.setSourceImage(pixels, width, height);
		  long start = System.currentTimeMillis();
		  opencv.extractSURFFeature();
			
		  long end = System.currentTimeMillis();
		  byte[] imageData = opencv.getSourceImage();
		  long elapse = end - start;
		  
		  Toast.makeText(this, "" + elapse + " ms is used to extract features.", Toast.LENGTH_LONG).show();
		  img = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
		  picView.setImageBitmap(img);
		  
	  }
	  
	  /**
	   * Helper method to carry out crop operation
	   */
	  private void performCrop(Uri picUri){
	  	//take care of exceptions
	  	try {
	  		//call the standard crop action intent (the user device may not support it)
	    	Intent cropIntent = new Intent("com.android.camera.action.CROP"); 
	    	
	    	//indicate image type and Uri
	    	cropIntent.setDataAndType(picUri, "image/*");
	    	
	    	cropIntent.putExtra("crop", "true");
	    	
	    	cropIntent.putExtra("aspectX", 1);
	    	cropIntent.putExtra("aspectY", 1);
	    	
	    	cropIntent.putExtra("outputX", 256);
	    	cropIntent.putExtra("outputY", 256);
	    	
	    	//retrieve data on return
	    	cropIntent.putExtra("return-data", true);
	    	
	    	//start the activity - we handle returning in onActivityResult
	        startActivityForResult(cropIntent, PIC_CROP);  
	  	}catch(ActivityNotFoundException anfe){
	  		//display an error message
	  		String errorMessage = "Whoops - your device doesn't support the crop action!";
	  		Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
	  		toast.show();
	  	}
	  	
	  }

	  private void takePhoto(){
		  try {
	            Intent i = new Intent(getApplicationContext(), CameraCapture.class);
	        	startActivityForResult(i, CameraCapture.REQUEST_CAMERA );
	      	}catch(ActivityNotFoundException anfe){
	      		//display an error message
	      		String errorMessage = "Whoops - your device doesn't support capturing images!";
	      		Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
	      		toast.show();
	      	}
	  }
	  
	/**
	 * Click method to handle user pressing button to launch camera
	*/
	public void onClick(View v) {
		
		switch(v.getId()){
			case R.id.btnCapture:
				takePhoto();
				break;
				
			case R.id.btnSave:
				
 
				if(this.txtName.getText().toString().equals("")){
					Toast.makeText(getApplicationContext(), "Please Insert Data", Toast.LENGTH_SHORT).show();
					return;
				}
				
				imgFeature = new Feature ();
				imgFeature.setFeature(
								"",		//-- Key
								"", 	//-- Feature
								this.txtName.getText().toString(), 
								this.txtDetail.getText().toString(), 
								this.txtYoutube.getText().toString(), 
								this.txtLink.getText().toString()
							);
				
				db.open();
				long id = db.insertFeature(imgFeature);
				db.close();
				
				if(id != 0){
					
					picView.setImageResource(android.R.drawable.ic_menu_report_image);
					
					this.txtName.setText(""); 
					this.txtDetail.setText(""); 
					this.txtYoutube.setText(""); 
					this.txtLink.setText("");
					
					imgFeature = null;
					
					Toast.makeText(getApplicationContext(), "Saved!!", Toast.LENGTH_SHORT).show();
					
				}else{
					Toast.makeText(getApplicationContext(), "Insert Error!!", Toast.LENGTH_SHORT).show();
				}
				
				
				Intent intent = new Intent(this, Home.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				
//				
//				moveTaskToBack(true);
//				startActivity(new Intent(getApplicationContext(), Home.class));
//				this.finish();
				break;
				
		}
	}
}