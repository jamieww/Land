package com.cyclone.land;

public class Announce {

	private int seqID;
	private String contentTitle;
	private String content;
	private String editTime;
	
	public final static String HOSTADDR = "http://192.168.1.129:8080";
	//public final static String HOSTADDR = "http://197.168.1.109:8080";
	//public final static String HOSTADDR = "http://172.27.0.1:8080";
	
	public Announce() {
		// TODO Auto-generated constructor stub
	}
	
	public Announce(String title, String content) {
		// TODO Auto-generated constructor stub
		this.contentTitle = title;
		this.content = content;
	}

	public int getSeqID() {
		return seqID;
	}

	public void setSeqID(int seqID) {
		this.seqID = seqID;
	}

	public String getContentTitle() {
		return contentTitle;
	}

	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEditTime() {
		return editTime;
	}

	public void setEditTime(String editTime) {
		this.editTime = editTime;
	}

}
