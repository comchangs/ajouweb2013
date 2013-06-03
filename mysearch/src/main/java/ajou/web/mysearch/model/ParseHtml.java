package ajou.web.mysearch.model;

public class ParseHtml {
	String[] title;
	String[] description;
	String[] keywords;

	public ParseHtml(String[] title, String[] description, String[] keywords) {
		this.title = title;
		this.description = description;
		this.keywords = keywords;
	}

	/**
	 * @return the title
	 */
	public String[] getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String[] title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String[] getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String[] description) {
		this.description = description;
	}

	/**
	 * @return the keywords
	 */
	public String[] getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(String[] keywords) {
		this.keywords = keywords;
	}
}