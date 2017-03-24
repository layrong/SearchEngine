package cn.ict.query;


//词项位置类，用于生成动态摘要
public class TermPosition implements Comparable<TermPosition> {
	private int pos;
	private int len;

	
	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getLen() {
		return len;
	}

	public void setLen(int len) {
		this.len = len;
	}

	public TermPosition(int pos, int len) {
		super();
		this.pos = pos;
		this.len = len;
	}

	public int compareTo(TermPosition o) {
		// TODO 自动生成的方法存根
		int ret = this.pos-o.pos;
		if(ret==0){
			ret = this.len-o.len;
		}
		return ret;
	}
}