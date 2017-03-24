package cn.ict.query;

import java.util.HashMap;
import java.util.List;

import cn.ict.dataBean.TermBean;
import cn.ict.indexAction.SearchIndexAction;

public class PageRankCalc {

	public static Double calcPageRank(List<String> queryTerms,
			List<Double> queryTermsIdfs, String docId) {
		List<TermBean> docTerms = SearchIndexAction.searchDocTermTf(docId);
		double rank = 0;
		double mod1, mod2;
		mod1 = mod2 = 0;

		HashMap<String, Double> queryTermsTfMap = new HashMap<String, Double>();
		for (int i = 0; i < queryTerms.size(); i++) {
			if (queryTermsTfMap.containsKey(queryTerms.get(i))) {
				queryTermsTfMap.put(queryTerms.get(i),
						queryTermsTfMap.get(queryTerms.get(i)) + 1);
			} else {
				queryTermsTfMap.put(queryTerms.get(i), 1.0);
			}
		}

		for (int i = 0, j = 0; i < queryTerms.size() && j < docTerms.size();) {
			
			if (queryTerms.get(i).compareTo(docTerms.get(j).getTermID()) < 0) {
				mod2 += Math.pow(
						queryTermsIdfs.get(i)
								* queryTermsTfMap.get(queryTerms.get(i)), 2);
				i++;
			} else if (queryTerms.get(i).compareTo(docTerms.get(j).getTermID()) > 0) {
				mod1 += Math.pow(docTerms.get(j).getTf(), 2);
				j++;
			} else {
				// System.out.println("tf:" + docTerms.get(j).getTf());
				rank += docTerms.get(j).getTf() * queryTermsIdfs.get(i)
						* queryTermsTfMap.get(queryTerms.get(i));
				mod1 += Math.pow(docTerms.get(j).getTf(), 2);
				mod2 += Math.pow(
						queryTermsIdfs.get(i)
								* queryTermsTfMap.get(queryTerms.get(i)), 2);
				i++;
				j++;
			}
		}

		if (mod1 * mod2 != 0) {
			rank = rank / (Math.sqrt(mod1) * Math.sqrt(mod2));
		}
		return rank;
	}

}
