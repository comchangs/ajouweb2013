package ajou.web.mysearch.model;


public class Bookmark{
	private String url;
	private String name;
	private boolean TF = true;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isTF() {
		return TF;
	}
	public void setTF(boolean tF) {
		TF = tF;
	}
}