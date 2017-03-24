package cn.ict.dataBean;

import java.io.Serializable;

public class TermBean implements Comparable<TermBean>, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	//词项ID
	private String termID;
	//词频
	private double tf;
	
	public TermBean(){}
	
	public TermBean(String termID, double tf){
		this.termID = termID;
		this.tf = tf;
	}
	
	public String getTermID() {
		return termID;
	}
	
	public void setTermID(String termID) {
		this.termID = termID;
	}
	
	public double getTf() {
		return tf;
	}
	
	public void setTf(double tf) {
		this.tf = tf;
	}

	@Override
	public String toString() {
		return termID + "," + tf;
	}
	
	@Override
	public int compareTo(TermBean arg0) {
		// TODO 自动生成的方法存根
		return this.termID.compareTo(arg0.getTermID());
	}
}
