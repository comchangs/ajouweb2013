package ajou.web.mysearch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "keyword")
public class Keywords {

	@Id
	private String id;
	private String search_keyword;
	private String relation_keyword;
	private String timestamp;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Keywords [id=" + id + ", search_keyword=" + search_keyword
				+ ", relation_keyword=" + relation_keyword + "]";
	}

	public Keywords() {
	}
	
	public Keywords(String search_keyword, String relation_keyword, String timestamp) {
		this.search_keyword = search_keyword;
		this.relation_keyword = relation_keyword;
		this.timestamp = timestamp;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the search_keyword
	 */
	public String getSearch_keyword() {
		return search_keyword;
	}

	/**
	 * @param search_keyword
	 *            the search_keyword to set
	 */
	public void setSearch_keyword(String search_keyword) {
		this.search_keyword = search_keyword;
	}

	/**
	 * @return the relation_keyword
	 */
	public String getRelation_keyword() {
		return relation_keyword;
	}

	/**
	 * @param relation_keyword
	 *            the relation_keyword to set
	 */
	public void setRelation_keyword(String relation_keyword) {
		this.relation_keyword = relation_keyword;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
