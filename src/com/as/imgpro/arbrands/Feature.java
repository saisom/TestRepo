package com.as.imgpro.arbrands;

public class Feature {
	
	private String KP;
	private String feature;
	
	private String name;
	private String detail;
	private String youtube;
	private String link;
	

	
	public void setFeature(String kp, String feature, String name, String detail, String youtube, String link){
		this.KP = kp;
		this.feature = feature;
		
		this.name = name;
		this.detail = detail;
		this.youtube = youtube;
		this.link = link;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	public String getYoutube() {
		return youtube;
	}
	public void setYoutube(String youtube) {
		this.youtube = youtube;
	}
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	public String getFeature() {
		return feature;
	}
	public void setFeature(String feature) {
		this.feature = feature;
	}
	
	public String getKP() {
		return KP;
	}
	public void setKP(String kP) {
		KP = kP;
	}
	
	

	
}
