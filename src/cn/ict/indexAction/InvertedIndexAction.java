package cn.ict.indexAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cn.ict.dataBean.DocBean;
import cn.ict.dataBean.TermBean;
import cn.ict.indexAction.ForwardIndexAction;
import cn.ict.ioopt.IndexFile;
import cn.ict.util.SortUtil;

public class InvertedIndexAction {
	
	private static HashMap<String, ArrayList<TermBean>> forwardIndexMap = null;
	private static HashMap<String, ArrayList<DocBean>> invertedIndexMap = null;
	
	private InvertedIndexAction()
	{
		forwardIndexMap = new HashMap<String, ArrayList<TermBean>>();
		invertedIndexMap = new HashMap<String, ArrayList<DocBean>>();
	}
	
	private static HashMap<String, ArrayList<DocBean>> createInvertedIndex() {
		
		forwardIndexMap = ForwardIndexAction.getForwardIndexMap();
		
		System.out.println("CreateInvertedIndex...");
		
		HashMap<String, ArrayList<DocBean>> docBeanMap = new HashMap<String, ArrayList<DocBean>>();
		
		if(forwardIndexMap == null){
			System.out.println("No records obtained");
		}else{
			for(Iterator<Entry<String, ArrayList<TermBean>>> iter = forwardIndexMap.entrySet().iterator(); iter.hasNext();){
		
				Map.Entry<String, ArrayList<TermBean>> entry = (Entry<String, ArrayList<TermBean>>)iter.next();
				String docID = (String)entry.getKey();
				ArrayList<TermBean> termBeanList = (ArrayList<TermBean>)entry.getValue();
				DocBean docBean = new DocBean();
				docBean.setDocID(docID);
				
				for(TermBean termBean : termBeanList){
					
					String termID = termBean.getTermID();
					if(docBeanMap.containsKey(termID)){
						docBeanMap.get(termID).add(docBean);
					}else{
						ArrayList<DocBean> docBeanList = new ArrayList<DocBean>();
						docBeanList.add(docBean);
						docBeanMap.put(termID, docBeanList);
					}
				}
			}
		}
		
		for(Iterator<Entry<String, ArrayList<DocBean>>> iter = docBeanMap.entrySet().iterator(); iter.hasNext();){
	
			Map.Entry<String, ArrayList<DocBean>> entry = (Entry<String, ArrayList<DocBean>>)iter.next();
			//System.out.println(entry.getKey()+":"+entry.getValue().size());
			SortUtil.sortDocList(entry.getValue());
		}
		
		System.out.println("CreateInvertedIndex done...");
		
		return docBeanMap;
	}

	public static HashMap<String, ArrayList<DocBean>> getInvertedIndex()
	{
		if(invertedIndexMap == null){
			System.out.println("ImportInvertedIndex file...");
			invertedIndexMap = IndexFile.importInvertedIndex();
			System.out.println("ImportInvertedIndex file done...");
			if(invertedIndexMap == null){
				invertedIndexMap = createInvertedIndex();
				System.out.println("SaveInvertedIndex file...");
				IndexFile.saveInvertedIndex(invertedIndexMap);
				System.out.println("SaveInvertedIndex file done...");
			}
		}
		
		return invertedIndexMap;
	}
}