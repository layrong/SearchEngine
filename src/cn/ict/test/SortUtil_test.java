package cn.ict.test;

import java.util.ArrayList;

import cn.ict.dataBean.TermBean;
import cn.ict.util.SortUtil;

public class SortUtil_test {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TermBean tb1 = new TermBean("Äã", 10);
		TermBean tb2 = new TermBean("ÎÒ", 8);
		TermBean tb3 = new TermBean("Äá", 12);
		
		ArrayList<TermBean> list = new ArrayList<TermBean>();
		list.add(tb1);
		list.add(tb2);
		list.add(tb3);
		
		SortUtil.sortTermList(list);
		
		for (int i =0;i<list.size();i++) {  
            System.out.println(list.get(i).getTermID()+""+list.get(i).getTf());    //Êä³ö
		}
	}
}
