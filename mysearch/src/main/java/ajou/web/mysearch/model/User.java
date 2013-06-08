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
	
	public ArrayList<Bookmark> getUserBookmark()
	{
		MySqlConnection sql = new MySqlConnection();

		ArrayList<Bookmark> result = sql.getBookmark("SELECT url, name FROM bookmark WHERE user_id='"+ this.userId + "'");
		if(result.isEmpty())
		{
			Bookmark mark = new Bookmark();
			mark.setName("북마크가 없습니다.");
			mark.setUrl("");
			mark.setTF(false);
			result.add(mark);
		}
		else
			for(int i =0; i < result.size(); i++)
				result.get(i).setName(result.get(i).getName() + " :: ");
		
		return result;
	}
	
	public void setUserBookmark(Bookmark addBookmark)
	{
		if(bookmark.get(0).getName().equals("북마크가 없습니다."))
			bookmark.remove(0);
		bookmark.add(addBookmark);
	}
}
