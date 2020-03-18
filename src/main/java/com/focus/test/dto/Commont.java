package com.focus.test.dto;

public class Commont {
	
	private String time;
	private int level;
	private String attribute;
	private String authorName;
	private String reviewTitle;
	private String reviewUrl;
	private String reviewContent;
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getReviewTitle() {
		return reviewTitle;
	}
	public void setReviewTitle(String reviewTitle) {
		this.reviewTitle = reviewTitle;
	}
	public String getReviewUrl() {
		return reviewUrl;
	}
	public void setReviewUrl(String reviewUrl) {
		this.reviewUrl = reviewUrl;
	}
	public String getReviewContent() {
		return reviewContent;
	}
	public void setReviewContent(String reviewContent) {
		this.reviewContent = reviewContent;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	
	public String toString () {
		return time + " " + authorName  + " " + level + " " + reviewTitle;
	}
	
}
