package cn.ict.query;


//����λ���࣬�������ɶ�̬ժҪ
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
		// TODO �Զ����ɵķ������
		int ret = this.pos-o.pos;
		if(ret==0){
			ret = this.len-o.len;
		}
		return ret;
	}
}