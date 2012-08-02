package com.as.imgpro.arbrands;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class Home extends Activity{

	@Override
	public void onCreate(Bundle saved){
		super.onCreate(saved);
		setContentView(R.layout.home);
		
		findViewById(R.id.btnAdd).setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), SelectImageSource.class));
			}
			
		});
	}
}
