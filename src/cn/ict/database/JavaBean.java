package cn.ict.database;

import java.io.Serializable;

public class JavaBean implements Serializable{

	private static final long serialVersionUID = 1L;

	private String id;
	private String url;
	private String title;
	private String content;
	private long comment;
	private String keyword;
	private String time;
	private String snippet;

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getComment() {
		return comment;
	}

	public void setComment(long comment) {
		this.comment = comment;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "JavaBean [id=" + id + ", url=" + url + ", title=" + title
				+ ", content=" + content + ", comment=" + comment
				+ ", keyword=" + keyword + ", time=" + time + "]";
	}

	public JavaBean(String id, String url, String title, String content,
			long comment, String keyword, String time) {
		super();
		this.id = id;
		this.url = url;
		this.title = title;
		this.content = content;
		this.comment = comment;
		this.keyword = keyword;
		this.time = time;
	}

	public JavaBean() {
		super();
	}
}
