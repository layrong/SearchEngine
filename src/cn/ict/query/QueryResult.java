package cn.ict.query;

import java.util.List;

import cn.ict.dataBean.TermBean;
import cn.ict.database.JavaBean;

public class QueryResult {
	
	private List<JavaBean> resultList;
	private List<List<JavaBean>> clusterResultList;
	private List<TermBean> recommendResultList;
	private int totalPageNumber;
	private int totalItemNumber;
	
	public QueryResult() {
		super();
	}
	
	public QueryResult(List<JavaBean> resultList,
						List<List<JavaBean>> clusterResultList,
						List<TermBean> recommendResultList, int totalPageNumber,
						int totalItemNumber) 
	{
		super();
		this.resultList = resultList;
		this.clusterResultList = clusterResultList;
		this.recommendResultList = recommendResultList;
		this.totalPageNumber = totalPageNumber;
		this.totalItemNumber = totalItemNumber;
	}

	public QueryResult(List<JavaBean> resultList,
			List<List<JavaBean>> clusterResultList, int totalPageNumber,
			int totalItemNumber) {
		super();
		this.resultList = resultList;
		this.clusterResultList = clusterResultList;
		this.totalPageNumber = totalPageNumber;
		this.totalItemNumber = totalItemNumber;
	}

	public QueryResult(List<JavaBean> resultList, int totalPageNumber,
			int totalItemNumber) {
		super();
		this.resultList = resultList;
		this.totalPageNumber = totalPageNumber;
		this.totalItemNumber = totalItemNumber;
	}
	
	public List<TermBean> getRecommendResultList() {
		return recommendResultList;
	}

	public void setRecommendResultList(List<TermBean> recommendResultList) {
		this.recommendResultList = recommendResultList;
	}

	public List<List<JavaBean>> getClusterResultList() {
		return clusterResultList;
	}
	public void setClusterResultList(List<List<JavaBean>> clusterResultList) {
		this.clusterResultList = clusterResultList;
	}
	public List<JavaBean> getResultList() {
		return resultList;
	}
	public void setResultList(List<JavaBean> resultList) {
		this.resultList = resultList;
	}
	public int getTotalPageNumber() {
		return totalPageNumber;
	}
	public void setTotalPageNumber(int totalPageNumber) {
		this.totalPageNumber = totalPageNumber;
	}
	public int getTotalItemNumber() {
		return totalItemNumber;
	}
	public void setTotalItemNumber(int totalItemNumber) {
		this.totalItemNumber = totalItemNumber;
	}
	
	

}
