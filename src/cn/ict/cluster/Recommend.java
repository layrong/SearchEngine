package cn.ict.cluster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
//import java.util.Iterator;
import java.util.List;

import cn.ict.dataBean.DocBean;
import cn.ict.dataBean.TermBean;
import cn.ict.indexAction.SearchIndexAction;

public class Recommend {

	public static int RECOMSIZE = 10;

	public static List<TermBean> getRecommendList(HashMap<String, ArrayList<TermBean>> forwardIndexMap, List<DocBean> resultList) {

		int i = 1;
		
		List<TermBean> recomList = forwardIndexMap.get(resultList.get(0).getDocID());

		while (i < resultList.size()) {
			if (forwardIndexMap.get(resultList.get(i).getDocID()) == null) {
				i++;
				continue;
			}
			recomList = KMeans.mergeVector((ArrayList<TermBean>) recomList, forwardIndexMap.get(resultList.get(i++).getDocID()));

		}
		/*
		for (Iterator<TermBean> iterator = recomList.iterator(); iterator.hasNext();) {
			TermBean termBean = (TermBean) iterator.next();
			System.out.print(termBean);

		}
		System.out.println();
		*/
		Collections.sort(recomList, new Comparator<TermBean>() {

			@Override
			public int compare(TermBean bean1, TermBean bean2) {
				// TODO Auto-generated method stub
				Double d1 = new Double(bean1.getTf() * SearchIndexAction.searchTermDf(bean1.getTermID()));
				Double d2 = new Double(bean2.getTf() * SearchIndexAction.searchTermDf(bean2.getTermID()));
				return d2.compareTo(d1);
				 
			}
		});		 

		return recomList.subList(0, RECOMSIZE);
	}
}
