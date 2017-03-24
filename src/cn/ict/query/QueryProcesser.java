package cn.ict.query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import cn.ict.cluster.Cluster;
import cn.ict.cluster.KMeans;
import cn.ict.cluster.Recommend;
import cn.ict.dataBean.DocBean;
import cn.ict.dataBean.TermBean;
import cn.ict.database.JavaBean;
import cn.ict.indexAction.ForwardIndexAction;
import cn.ict.indexAction.SearchIndexAction;
import cn.ict.util.Constant;
import cn.ict.ws.WordSegment;

public class QueryProcesser {

	// 合并倒排表
	private static List<DocBean> mergeDocList(List<DocBean> List1,
			List<DocBean> List2) {
		int i, j;
		List<DocBean> newList = new ArrayList<DocBean>();
		for (i = 0, j = 0; i < List1.size() && j < List2.size();) {
			long docId1, docId2;
			docId1 = Long.valueOf(List1.get(i).getDocID());
			docId2 = Long.valueOf(List2.get(j).getDocID());
			if (docId1 < docId2) {
				newList.add(List1.get(i));
				i++;
			} else if (docId1 == docId2) {
				newList.add(List1.get(i));
				i++;
				j++;
			} else {
				newList.add(List2.get(j));
				j++;
			}
		}
		while (i < List1.size()) {
			newList.add(List1.get(i++));
		}
		while (j < List2.size()) {
			newList.add(List2.get(j++));
		}
		return newList;
	}

	/**
	 * get termslist's idf
	 * 
	 * @param queryTerms
	 * @return
	 */
	private static List<Double> getTermsIdfs(List<String> queryTerms) {
		List<Double> list = new ArrayList<Double>();

		for (int i = 0; i < queryTerms.size(); i++) {
			list.add(SearchIndexAction.searchTermDf(queryTerms.get(i)));
		}

		return list;
	}

	/**
	 * 获取推荐结果
	 * 
	 * @param docList
	 * @return
	 */
	private static List<TermBean> getRecommendResult(List<DocBean> docList) {
		HashMap<String, ArrayList<TermBean>> forwardIndexMap = ForwardIndexAction
				.getForwardIndexMap();
		if(docList.size() > Constant.TOPITERMS){
			docList = docList.subList(0, Constant.TOPITERMS - 1);
		}
		return Recommend.getRecommendList(forwardIndexMap,
				docList);
	}
	
	/**
	 * 生成cluster的关键字
	 * @param list
	 * @param queryTerms
	 * @return
	 */
	private static List<JavaBean> genKeywords(List<JavaBean> list,List<String> queryTerms){
		List<DocBean> docIds = new ArrayList<DocBean>();
		for(int i=0;i<list.size();i++){
			docIds.add(new DocBean(list.get(i).getId()));
		}
		if(docIds.size()==0){
			return list;
		}
		HashMap<String,ArrayList<TermBean>> forwardIndexMap = ForwardIndexAction.getForwardIndexMap();
		List<TermBean> terms = Recommend.getRecommendList(forwardIndexMap, docIds);
		String keywords = "";
		for(int i=0;i<terms.size()&&i<4;i++){
			if(i!=0){
				keywords += "、";
			}
			keywords += terms.get(i).getTermID();
		}
		keywords = QueryInfoMaker.highlightString(keywords, queryTerms);
		for(int i=0;i<list.size();i++){
			list.get(i).setKeyword(keywords);
		}
		return list;
	}
//	private static List<JavaBean> genKeywords(List<JavaBean> list,List<String> queryTerms){
//		Map<String,Integer> tag = new HashMap<String,Integer>();
//		List<String> termList = new ArrayList<String>();
//		for(int i=0;i<list.size();i++){
//			List<String> terms = WordSegment.wordSegment(list.get(i).getTitle());
//			for(int j=0;j<terms.size();j++){
//				if(tag.containsKey(terms.get(j))){
//					tag.put(terms.get(j), tag.get(terms.get(j))+1);
//				}else{
//					tag.put(terms.get(j), 1);
//					termList.add(terms.get(j));
//				}
//			}
//		}
//		
//		//按词频给分词的结果排序
//		class sortClass{
//			public List<String> list;
//			public Map<String,Integer> map;
//			
//			public sortClass(List<String> list, Map<String, Integer> map) {
//				super();
//				this.list = list;
//				this.map = map;
//			}
//
//			public List<String> sort(){
//				Collections.sort(list, new Comparator<String>(){
//					
//					public int compare(String o1, String o2) {
//						return map.get(o2)-map.get(o1);
//					}
//				
//				});
//				return list;
//			}
//		};
//		
//		sortClass temp = new sortClass(termList,tag);
//		termList = temp.sort();
//		
//		
//		String keywords = "";
//		for(int i=0;i<termList.size()&&i<3;i++){
//			if(i!=0){
//				keywords+="、";
//			}
//			keywords += termList.get(i);
//		}
//		keywords = QueryInfoMaker.highlightString(keywords, queryTerms);
//		for(int i=0;i<list.size();i++){
//			list.get(i).setKeyword(keywords);
//		}
//		return list;
//	}

	/**
	 * 获取聚类结果 至多5类 每类至多3个文档
	 * 
	 * @param docList
	 * @return
	 */
	private static List<List<JavaBean>> getClusterResult(List<DocBean> docList,
			List<String> queryTerms) {
		HashMap<String, ArrayList<TermBean>> forwardIndexMap = ForwardIndexAction
				.getForwardIndexMap();
		if(docList.size() > Constant.TOPITERMS){
			docList = docList.subList(0, Constant.TOPITERMS);
		}
		List<Cluster> orginList = KMeans.cluster(forwardIndexMap,
				 docList);
		List<List<JavaBean>> retList = new ArrayList<List<JavaBean>>();
		List<JavaBean> tempList = null;
		// System.out.println(orginList.size());
		int count = 0;
		
		for (int i = 0; i < orginList.size() && count < 5; i++) {
			tempList = new ArrayList<JavaBean>();
			for (int j = 0; j < orginList.get(i).getElements().size() && j < 3; j++) {
				JavaBean bean = QueryInfoMaker.genQueryInfo(orginList.get(i)
						.getElements().get(j), queryTerms);
				tempList.add(bean);
			}
			if(tempList.size()!=0){
				retList.add(genKeywords(tempList,queryTerms));
				count++;
			}
		}
		return retList;
	}

	private static List<JavaBean> getSortedQueryResult(List<DocBean> docList,
			List<String> queryTerms, int sortType, int pageNumber, int pageSize) {
		List<JavaBean> queryResult = new ArrayList<JavaBean>();

		if (sortType == Constant.TIME_TYPE) {
			for (int i = 0; i < docList.size(); i++) {
				queryResult.add(QueryInfoMaker.genQueryInfo(docList.get(i)
						.getDocID(), queryTerms));
			}
			// 按时间排序
			Collections.sort(queryResult, new Comparator<JavaBean>() {
				// 按日期先后排序，日期越近，排序越靠前
				public int compare(JavaBean arg0, JavaBean arg1) {
					// TODO 自动生成的方法存根
					if (arg0.getTime().equals("未知时间")) {
						if (arg1.getTime().equals("未知时间")) {
							return 0;
						}
						return 1;
					}
					if (arg1.getTime().equals("未知时间")) {
						return -1;
					}
					int y0, y1, m0, m1, d0, d1, h0, h1, n0, n1;
					int idx;
					Calendar date0, date1;
					date0 = Calendar.getInstance();
					date1 = Calendar.getInstance();

					idx = arg0.getTime().indexOf("年");
					y0 = Integer
							.valueOf(arg0.getTime().substring(idx - 4, idx));
					m0 = Integer.valueOf(arg0.getTime().substring(idx + 1,
							idx + 3));
					d0 = Integer.valueOf(arg0.getTime().substring(idx + 4,
							idx + 6));
					h0 = Integer.valueOf(arg0.getTime().substring(idx + 7,
							idx + 9));
					n0 = Integer.valueOf(arg0.getTime().substring(idx + 10,
							idx + 12));
					date0.set(y0, m0, d0, h0, n0);

					idx = arg1.getTime().indexOf("年");
					y1 = Integer
							.valueOf(arg1.getTime().substring(idx - 4, idx));
					m1 = Integer.valueOf(arg1.getTime().substring(idx + 1,
							idx + 3));
					d1 = Integer.valueOf(arg1.getTime().substring(idx + 4,
							idx + 6));
					h1 = Integer.valueOf(arg1.getTime().substring(idx + 7,
							idx + 9));
					n1 = Integer.valueOf(arg1.getTime().substring(idx + 10,
							idx + 12));
					date1.set(y1, m1, d1, h1, n1);
					return date1.getTime().compareTo(date0.getTime());
				}

			});
			queryResult = queryResult.subList((pageNumber - 1) * pageSize,
					pageNumber * pageSize < queryResult.size() ? pageNumber
							* pageSize : queryResult.size());

		} else if (sortType == Constant.HOT_TYPE) {
			for (int i = 0; i < docList.size(); i++) {
				queryResult.add(QueryInfoMaker.genQueryInfo(docList.get(i)
						.getDocID(), queryTerms));
			}
			Collections.sort(queryResult, new Comparator<JavaBean>() {
				@Override
				public int compare(JavaBean o1, JavaBean o2) {
					return (int) (o2.getComment() - o1.getComment());
				}
			});
			queryResult = queryResult.subList((pageNumber - 1) * pageSize,
					pageNumber * pageSize < queryResult.size() ? pageNumber
							* pageSize : queryResult.size());

		} else {
			List<Double> queryTermsIdfs = QueryProcesser
					.getTermsIdfs(queryTerms);// 存词项的idf
			List<DocWithRank> sortedDocList = new ArrayList<DocWithRank>();

			// sort
			for (int i = 0; i < docList.size(); i++) {
				sortedDocList.add(new DocWithRank(docList.get(i).getDocID(),
						PageRankCalc.calcPageRank(queryTerms, queryTermsIdfs,
								docList.get(i).getDocID())));
			}
			Collections.sort(sortedDocList);

			// geninfo
			for (int i = (pageNumber - 1) * pageSize, len = (pageNumber * pageSize); i < len
					&& i < sortedDocList.size(); i++) {
				queryResult.add(QueryInfoMaker.genQueryInfo(sortedDocList
						.get(i).getDocId(), queryTerms));
			}
		}
		return queryResult;
	}

	/**
	 * 根据URL获取快照
	 * 
	 * @param url
	 * @return
	 */
	public static JavaBean queryFlashCap(String qStr,String url) {
		List<String> terms = WordSegment.wordSegment(qStr);
		return QueryInfoMaker.genQueryFlashCap(url,terms);
	}

	/**
	 * suppose all input is valid
	 * 
	 * @param qStr
	 * @param pageNumber
	 * @param pageSize
	 * @param sortType
	 *     		: relationship = 0 time = 1 hot = 2
	 * @return
	 */
	public static QueryResult query(String qStr, int pageNumber, int pageSize,
			int sortType) {

		List<String> queryTerms = WordSegment.wordSegment(qStr);
		Collections.sort(queryTerms);// 按字典序排好

		List<DocBean> docList = new ArrayList<DocBean>();

		// getdocIds
		for (int i = 0; i < queryTerms.size(); i++) {
			docList = QueryProcesser.mergeDocList(docList,
					SearchIndexAction.searchTermDoc(queryTerms.get(i)));
		}
		List<JavaBean> queryResult = new ArrayList<JavaBean>();
		List<TermBean> recmdList = new ArrayList<TermBean>();
		List<List<JavaBean>> clusterList = new ArrayList<List<JavaBean>>();
		
		if(docList.size()>0){
			queryResult = getSortedQueryResult(docList, queryTerms,
					sortType, pageNumber, pageSize);
			recmdList = getRecommendResult(docList);
			clusterList = getClusterResult(docList, queryTerms);
		}

		// return result
		QueryResult retResult = new QueryResult(queryResult, clusterList,
				recmdList, 1 + (docList.size() - 1) / pageSize, docList.size());

		// 如果没有查到结果
		if (queryResult.size() == 0) {
			retResult.setTotalItemNumber(0);
			retResult.setTotalPageNumber(1);
		}

		return retResult;
	}

	/** debug **/
	public static QueryResult debugQuery(String qStr, int pageNumber, int pageSize,
			int sortType) {

		List<String> queryTerms = WordSegment.wordSegment(qStr);
		Collections.sort(queryTerms);// 按字典序排好

		List<DocBean> docList = new ArrayList<DocBean>();

		// getdocIds
		for (int i = 0; i < queryTerms.size(); i++) {
			docList = QueryProcesser.mergeDocList(docList,
					SearchIndexAction.searchTermDoc(queryTerms.get(i)));
		}
		List<JavaBean> queryResult = new ArrayList<JavaBean>();
		List<TermBean> recmdList = new ArrayList<TermBean>();
		//List<List<JavaBean>> clusterList = new ArrayList<List<JavaBean>>();
		
		if(docList.size()>0){
			queryResult = getSortedQueryResult(docList, queryTerms,
					sortType, pageNumber, pageSize);
			recmdList = getRecommendResult(docList);
			//clusterList = getClusterResult(docList, queryTerms);
		}
		 List<List<JavaBean>> clusterList = new ArrayList<List<JavaBean>>();
		 for(int i=0;i<5;i++){
		 List<JavaBean> list = new ArrayList<JavaBean>();
		 for(int j=0;j<3;j++){
		 list.add(queryResult.get(0));
		 }
		 clusterList.add(list);
		 }
		// List<TermBean> recmdList = null;
		// List<List<JavaBean>> clusterList = null;

		// return result
		QueryResult retResult = new QueryResult(queryResult, clusterList,
				recmdList, 1 + (docList.size() - 1) / pageSize, docList.size());

		// 如果没有查到结果
		if (queryResult.size() == 0) {
			retResult.setTotalItemNumber(0);
			retResult.setTotalPageNumber(1);
		}

		return retResult;
	}
	
	/*
	private static void debugPrintStrings(List<String> list) {
		for (int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i) + " ");
		}
		System.out.println();
	}

	private static void debugPrintDoubles(List<Double> list) {
		for (int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i) + " ");
		}
		System.out.println();
	}

	private static void debugPrintDocs(List<DocBean> list) {
		for (int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i).getDocID() + "||");
		}
		System.out.println();
	}

	private static void debugPrintSortedDocs(List<DocWithRank> list) {
		for (int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i).getDocId() + "<>"
					+ list.get(i).getDocRank() + "||");
		}
		System.out.println();
	}*/
}
