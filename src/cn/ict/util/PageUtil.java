package cn.ict.util;

import java.io.Serializable;

public class PageUtil implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String indexKeyWords;
	private int totalPageNumber;
	private int totalRecordNumber;
	private int pageIndex;
	private int pageSize;
	
	public PageUtil(){
		
	} 
	
	public PageUtil(String indexKeyWords, int totalPageNumber, int totalRecordNumber,
			int pageIndex, int pageSize) {
		super();
		this.indexKeyWords = indexKeyWords;
		this.totalPageNumber = totalPageNumber;
		this.totalRecordNumber = totalRecordNumber;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
	}
	
	public String getIndexKeyWords() {
		return indexKeyWords;
	}

	public void setIndexKeyWords(String indexKeyWords) {
		this.indexKeyWords = indexKeyWords;
	}

	public int getTotalPageNumber() {
		return totalPageNumber;
	}
	
	public void setTotalPageNumber(int totalPageNumber) {
		this.totalPageNumber = totalPageNumber;
	}
	
	public int getTotalRecordNumber() {
		return totalRecordNumber;
	}
	
	public void setTotalRecordNumber(int totalRecordNumber) {
		this.totalRecordNumber = totalRecordNumber;
	}
	
	public int getPageIndex() {
		return pageIndex;
	}
	
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
