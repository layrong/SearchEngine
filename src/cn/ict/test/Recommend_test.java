package cn.ict.test;

import java.util.ArrayList;

import cn.ict.dataBean.TermBean;

public class Recommend_test {
	
	public static ArrayList<TermBean> getRecmTermList(){
		
		ArrayList<TermBean> recmTermList = new ArrayList<TermBean>();
		
		TermBean tb1 = new TermBean("����", 0);
		TermBean tb2 = new TermBean("����", 0);
		TermBean tb3 = new TermBean("ƹ����", 0);
		TermBean tb4 = new TermBean("����", 0);
		
		recmTermList.add(tb1);
		recmTermList.add(tb2);
		recmTermList.add(tb3);
		recmTermList.add(tb4);
		
		return recmTermList;
	}
}
