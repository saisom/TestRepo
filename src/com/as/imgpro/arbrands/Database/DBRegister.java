package com.as.imgpro.arbrands.Database;

import android.content.Context;
import android.util.Log;

import com.saisom.library.DBAdapter.DBBaseRegister;

public class DBRegister extends DBBaseRegister{

	private static int Database_Version = 1;
	
	
	Context context ;
	public DBRegister(Context ctx){
		super(ctx, Database_Version);
		this.context = ctx;
	}

	@Override
	public void initAddDAO() {
		Log.e("DB_LOG", "init DAO");
		
		addDAO(new DB_ImageFeature(context));
		
	}
	
	
	
}
