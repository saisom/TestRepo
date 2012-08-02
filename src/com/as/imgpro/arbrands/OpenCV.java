package com.as.imgpro.arbrands;

public class OpenCV {
	static{
		System.loadLibrary("arbrands");
	}
	public native boolean setSourceImage(int[] pixels, int width, int height);
	
	public native void setSURFParams(int pixels);
	
	public native byte[] getSourceImage();
	public native void extractSURFFeature();
}