package cn.ict.dataBean;

import java.io.Serializable;

public class DocBean implements Comparable<DocBean>, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String docID;
	//others

	public DocBean()
	{
		
	}
	public DocBean(String docID)
	{
		this.docID = docID;
	}
	public String getDocID() {
		return docID;
	}

	public void setDocID(String docID) {
		this.docID = docID;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return docID;
	}
	
	@Override
	public int compareTo(DocBean o) {
		// TODO 自动生成的方法存根
		return this.docID.compareTo(o.getDocID());
	}
}
