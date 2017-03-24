package cn.ict.indexAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import cn.ict.dataBean.TermBean;
import cn.ict.database.DatabaseUtil;
import cn.ict.database.JavaBean;
import cn.ict.ioopt.IndexFile;
import cn.ict.util.SortUtil;
import cn.ict.ws.WordSegment;

public class ForwardIndexAction {

	private static HashMap<String, ArrayList<TermBean>> forwardIndexMap = null;

	private ForwardIndexAction() {
		forwardIndexMap = new HashMap<String, ArrayList<TermBean>>();
	}

	private static HashMap<String, ArrayList<TermBean>> createForwardIndex() {
		System.out.println("CreateForwardIndex...");

		int indexID = 1;
		HashMap<String, ArrayList<TermBean>> forwardMap = new HashMap<String, ArrayList<TermBean>>();
		HashMap<String, String> termMap = new HashMap<String, String>();
		while (true) {
			JavaBean jb = DatabaseUtil.getItemById(String.valueOf(indexID));
			if (jb == null) {
				break;
			}

			/* logical coding */
			ArrayList<TermBean> termBeanList = new ArrayList<TermBean>();
			String docID = jb.getId();
			String cnt = jb.getContent();
			String temp = null;
			if (cnt.length() > 0) {
				ArrayList<String> wordList = WordSegment.wordSegment(cnt);
				if (wordList == null || wordList.size() == 0) {
					indexID++;
					continue;
				}
				for (Iterator<String> tmpItor = wordList.iterator(); tmpItor.hasNext();) {
					TermBean tb = new TermBean();
					temp = tmpItor.next();
					if (termMap.containsKey(temp))
						tb.setTermID(termMap.get(temp));
					else						
					{
						termMap.put(temp, temp);
						tb.setTermID(temp);
					}
						
					tb.setTf(1);
					int count = 1;
					/* 判断termID是否已经存在 */
					for (TermBean termBean : termBeanList) {
						if ((termBean.getTermID()).equals(tb.getTermID())) {
							termBean.setTf(termBean.getTf() + 1);
							break;
						}
						count++;
					}

					/* 如果termID不存在，添加 */
					if (count > termBeanList.size()) {
						termBeanList.add(tb);
					}
				}
				/*
				 * for(TermBean termBean : termBeanList) {
				 * termBean.setTf(termBean.getTf()/termBeanList.size()); }
				 */
				SortUtil.sortTermList(termBeanList);
				forwardMap.put(docID, termBeanList);
			}

			indexID++;
		}
		IndexFile.saveTermMap(termMap);

		System.out.println("CreateForwardIndex done...");

		return forwardMap;
	}

	public static HashMap<String, ArrayList<TermBean>> getForwardIndexMap() {
		if (forwardIndexMap == null) {
			System.out.println("ImportForwardIndex file...");
			forwardIndexMap = IndexFile.importForwardIndex();
			System.out.println("ImportForwardIndex file done...");
			if (forwardIndexMap == null) {
				forwardIndexMap = createForwardIndex();
				System.out.println("SaveForwardIndex file...");
				IndexFile.saveForwardIndex(forwardIndexMap);
				System.out.println("SaveForwardIndex file done...");
			}
		}
		return forwardIndexMap;
	}
}