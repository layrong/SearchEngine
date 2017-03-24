package cn.ict.query;

public class DocWithRank implements Comparable<DocWithRank>{
	
	private String docId;
	private double docRank;

	public DocWithRank() {
		super();
	}
	public DocWithRank(String docId, double docRank) {
		super();
		this.docId = docId;
		this.docRank = docRank;
	}
	public String getDocId() {
		return docId;
	}
	public void setDocId(String docId) {
		this.docId = docId;
	}
	public double getDocRank() {
		return docRank;
	}
	public void setDocRank(double docRank) {
		this.docRank = docRank;
	}
	@Override
	//��û��д
	public int compareTo(DocWithRank arg0) {
		// TODO �Զ����ɵķ������
		int ret;
		if(this.docRank<arg0.getDocRank()){
			ret = 1;
		}else if(this.docRank==arg0.getDocRank()){
			ret = 0;
		}else{
			ret = -1;
		}
		return ret;
	}
	@Override
	public String toString() {
		return "DocWithRank [docId=" + docId + ", docRank=" + docRank + "]";
	}
	

}
