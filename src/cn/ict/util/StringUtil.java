package cn.ict.util;

public class StringUtil implements Comparable<StringUtil>{

	public String s;//°ü×°String
	 
	public StringUtil(String s) {
		this.s = s;
	}
	
	public String getString(){
		return this.s;
	} 
	 
	@Override
	public int compareTo(StringUtil o) {
		// TODO Auto-generated method stub
		if(o == null || o.s == null) 
			return 1;
		if(s.length() > o.s.length()) 
			return 1;
		else if(s.length() < o.s.length()) 
			return -1;
		return s.compareTo(o.s);
	}

}
