package cn.ict.query;

public class SnippetParagraph implements Comparable<SnippetParagraph>{
	
	private int paraStart;
	private int paraEnd;
	private String paraContext;
	private double paraRank;
	
	public SnippetParagraph() {
		super();
	}

	public SnippetParagraph(int paraStart, int paraEnd, String paraContext,
			double paraRank) {
		super();
		this.paraStart = paraStart;
		this.paraEnd = paraEnd;
		this.paraContext = paraContext;
		this.paraRank = paraRank;
	}

	public int getParaStart() {
		return paraStart;
	}

	public void setParaStart(int paraStart) {
		this.paraStart = paraStart;
	}

	public int getParaEnd() {
		return paraEnd;
	}

	public void setParaEnd(int paraEnd) {
		this.paraEnd = paraEnd;
	}

	public String getParaContext() {
		return paraContext;
	}

	public void setParaContext(String paraContext) {
		this.paraContext = paraContext;
	}

	public double getParaRank() {
		return paraRank;
	}

	public void setParaRank(double paraRank) {
		this.paraRank = paraRank;
	}

	@Override
	public int compareTo(SnippetParagraph arg0) {
		// TODO 自动生成的方法存根
		double temp = paraRank-arg0.paraRank;
		int ret = 0;
		if(temp>0){
			ret = -1;
		}else if(temp==0){
			ret = 0;
		}else{
			ret = 1;
		}
		return ret;
	}
	
	

}
