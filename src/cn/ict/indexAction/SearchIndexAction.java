package cn.ict.indexAction;

import java.util.ArrayList;
import java.util.HashMap;

import cn.ict.dataBean.DocBean;
import cn.ict.dataBean.TermBean;
import cn.ict.database.DatabaseUtil;
import cn.ict.util.Constant;
import cn.ict.util.MathUtil;

public class SearchIndexAction {
	
	/*
	 * param termID: ��������ID
	 * param docID: �����ĵ�ID
	 * return double, ���ظô������ĵ��еĴ���Ƶ��tf
	 * */
	public static double searchTermDocTf(String termID, String docID){
		double tf = 0;
		
		HashMap<String, ArrayList<TermBean>> forwardMap = ForwardIndexAction.getForwardIndexMap();
		if(forwardMap == null){
			System.out.println("Warning: no forwad map");
		}else{
			ArrayList<TermBean> termList = forwardMap.get(docID);
			for(TermBean termBean : termList){
				if(termBean.getTermID() == termID){
					tf = termBean.getTf();
					break;
				}
			}
		}
		
		return tf;
	}
	
	/*
	 * param termId: ��������ID
	 * return ArrayList<DocBean>�� ���ظô�������Ӧ�������ĵ�ID����docArrayList
	 * */
	public static ArrayList<DocBean> searchTermDoc(String termID){
		
		ArrayList<DocBean> docArrayList = new ArrayList<DocBean>();
		HashMap<String, ArrayList<DocBean>> forwardMap = InvertedIndexAction.getInvertedIndex();
		
		if(forwardMap == null){
			System.out.println("Warning: no forwad map");
		}else if(forwardMap.containsKey(termID)){
			docArrayList = forwardMap.get(termID);
		}else{
			/* Do nothing */
		}
		
		return docArrayList;
	}

	/*
	 * param docID: �����ĵ�ID
	 * return HashList<TermBean> docTermTfList�� ���ظ��ĵ������д���ID����+��Ӧ�Ĵ���Ƶ��docTermTfList
	 * */
	public static ArrayList<TermBean> searchDocTermTf(String docID){
		
		ArrayList<TermBean> docTermTfList = new ArrayList<TermBean>();
		HashMap<String, ArrayList<TermBean>> forwardMap = ForwardIndexAction.getForwardIndexMap();
		
		if(forwardMap == null){
			System.out.println("Warning: no forwad map");
		}else if(forwardMap.containsKey(docID)){
			docTermTfList = forwardMap.get(docID);
		}else{
			/* Do nothing */
		}
		
		return docTermTfList;
	}
	
	/*
	 * param termID: ��������ID
	 * return: double, ���ظô�����ĵ�Ƶ��idf
	 * */
	public static double searchTermDf(String termID){
		double idf = 0;
		HashMap<String, ArrayList<DocBean>> forwardMap = InvertedIndexAction.getInvertedIndex();
		if(forwardMap == null){
			System.out.println("Warning: no forwad map");
		}else{
			ArrayList<DocBean> docList = forwardMap.get(termID);
			if(docList != null && !docList.isEmpty()){
				/* Calculate the idf of termID */ 
				int totalSize = DatabaseUtil.getTotalSize();
				//System.out.println(totalSize);
				if(totalSize != 0){
					idf = MathUtil.log((totalSize/docList.size()), Constant.BASE_10);
				}else{
					System.out.println("No records in database");
				}
			}
		}
		
		return idf;
	}
}