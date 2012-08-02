package com.as.imgpro.arbrands.Database;

import com.as.imgpro.arbrands.Feature;
import com.saisom.library.DBAdapter.DBAdapter;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;


public class DB_ImageFeature extends DBAdapter{
	
	//-- 
	public static String[][] column = 
		new String[][]{
			{"id","INTEGER"},					
			{"ar_name","VARCHAR"},	
			
			{"ar_key","VARCHAR"},		
			{"ar_feature","VARCHAR"},				
			
						
			{"ar_detail","VARCHAR"},				
			{"ar_youtube","VARCHAR"},			
			{"ar_link","VARCHAR"}
			
			
		};
	
	
	//------- General Code ---------- 
	public DB_ImageFeature(Context ctx) {	
		super(ctx);
		Log.w(super.TAG , "init : DB " + getTableName() );	
	}
	
	public String getTableName() {
		return "DB_ImageFeature";
	}

	public int[] getPrimaryIndex() {
		return new int[] {0,1};
	}
	
	public String[][] getArrColumn() {
		// TODO Auto-generated method stub
		return column;
	}

	
    public long insertFeature(Feature data) 
    {
    	Log.w(TAG ,"Table : " + TableName + "Start Insert data");
    	
    	if(data != null){
    		ContentValues initialValues = new ContentValues();
            
            
    		initialValues.put("ar_key", data.getKP());
            initialValues.put("ar_feature", data.getFeature());
            initialValues.put("ar_name", data.getName());
            initialValues.put("ar_detail", data.getDetail());
            initialValues.put("ar_youtube", data.getYoutube());
            initialValues.put("ar_link", data.getLink());
            
            long id = db.insert(TableName, null, initialValues); 
            
            Log.w(TAG ,"inserted : "+id);
            
            return id;
    	}
    	
    	return 0;
        
    }
}