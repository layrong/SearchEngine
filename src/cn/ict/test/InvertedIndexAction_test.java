package cn.ict.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cn.ict.dataBean.DocBean;
import cn.ict.indexAction.InvertedIndexAction;

public class InvertedIndexAction_test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashMap<String, ArrayList<DocBean>> InvertedIndexMap = InvertedIndexAction.getInvertedIndex();
		if(InvertedIndexMap != null){
			for(Iterator<Entry<String, ArrayList<DocBean>>> iter = InvertedIndexMap.entrySet().iterator(); iter.hasNext();){
				
				Map.Entry<String, ArrayList<DocBean>> entry = (Entry<String, ArrayList<DocBean>>)iter.next();
				System.out.println(entry.getKey()+ "-----------------------");
				ArrayList<DocBean> list = entry.getValue();
				for(DocBean bean : list){
					System.out.println(bean.getDocID());
				}
			}
		}
	}
}
