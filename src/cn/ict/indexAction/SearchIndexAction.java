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
	 * param termID: 给定词项ID
	 * param docID: 给定文档ID
	 * return double, 返回该词项在文档中的词项频率tf
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
	 * param termId: 给定词项ID
	 * return ArrayList<DocBean>： 返回该词项所对应的所有文档ID集合docArrayList
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
	 * param docID: 给定文档ID
	 * return HashList<TermBean> docTermTfList： 返回该文档中所有词项ID集合+对应的词项频率docTermTfList
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
	 * param termID: 给定词项ID
	 * return: double, 返回该词项的文档频率idf
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