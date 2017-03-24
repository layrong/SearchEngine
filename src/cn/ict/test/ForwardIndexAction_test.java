package cn.ict.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cn.ict.dataBean.TermBean;
import cn.ict.indexAction.ForwardIndexAction;

public class ForwardIndexAction_test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap<String, ArrayList<TermBean>> forwardIndexMap = ForwardIndexAction.getForwardIndexMap();
		if(forwardIndexMap != null){
			for(Iterator<Entry<String, ArrayList<TermBean>>> iter = forwardIndexMap.entrySet().iterator(); iter.hasNext();){
				
				Map.Entry<String, ArrayList<TermBean>> entry = (Entry<String, ArrayList<TermBean>>)iter.next();
				System.out.println(entry.getKey()+ "-----------------------");
				ArrayList<TermBean> list = entry.getValue();
				for(TermBean bean : list){
					System.out.println(bean.getTermID() + " " + bean.getTf());
				}
			}
		}
	}
}
