package com.cyclone.land;

public class Announce {

	private int seqID;
	private String contentTitle;
	private String content;
	private String editTime;
	
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
