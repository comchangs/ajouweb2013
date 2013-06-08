package ajou.web.mysearch.model;

import java.util.ArrayList;

public class User {
	private String userId;
	private String password;
	private ArrayList<Bookmark> bookmark;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public ArrayList<Bookmark> getBookmark() {
		return bookmark;
	}
	public void setBookmark(ArrayList<Bookmark> bookmark) {
		this.bookmark = bookmark;
	}
}
