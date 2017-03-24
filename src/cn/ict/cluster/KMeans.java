package cn.ict.cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import cn.ict.dataBean.DocBean;
import cn.ict.dataBean.TermBean;
import cn.ict.indexAction.SearchIndexAction;
import cn.ict.util.Constant;

/*去除频率过低或者过高的词项*/

public class KMeans {

	/* 用於查找IDF */

	/* 计算中心向量和某个文档向量之间的距离 */
	public static double computeDistance(ArrayList<TermBean> vector1, ArrayList<TermBean> vector2) {
		double distance = 0;
		int i = 0, j = 0;
		/* 向量的长度应该大于0 */
		if (vector1.size() == 0 || vector2.size() == 0)
			return 0;
		double temp1 = 0,temp2 = 0;
		TermBean termBean1 = vector1.get(i++), termBean2 = vector2.get(j++);
		/* 遍历两个向量链表，累计相等的词项的乘积 */
		while (i < vector1.size() && j < vector2.size()) {
			if (termBean1.getTermID().compareTo(termBean2.getTermID()) < 0)
			{				
				temp1+= Math.pow(termBean1.getTf() * SearchIndexAction.searchTermDf(termBean1.getTermID()),2);
				termBean1 = vector1.get(i++);
			}
				
			else if (termBean1.getTermID().compareTo(termBean2.getTermID()) > 0)
			{				
				temp2+= Math.pow(termBean2.getTf() * SearchIndexAction.searchTermDf(termBean2.getTermID()),2);
				termBean2 = vector2.get(j++);
			}				
			else {
				distance += (termBean1.getTf() * SearchIndexAction.searchTermDf(termBean1.getTermID())) * (termBean2.getTf() * SearchIndexAction.searchTermDf(termBean2.getTermID()));
				temp1+= Math.pow(termBean1.getTf() * SearchIndexAction.searchTermDf(termBean1.getTermID()),2);
				temp2+= Math.pow(termBean2.getTf() * SearchIndexAction.searchTermDf(termBean2.getTermID()),2);
				termBean1 = vector1.get(i++);
				termBean2 = vector2.get(j++);
			}
		}
		distance /= (Math.sqrt(temp1) * Math.sqrt(temp2));
		return distance;
	}

	/* 合并两个向量，用于重新计算此类的中心向量时使用 */
	public static ArrayList<TermBean> mergeVector(ArrayList<TermBean> vector1, ArrayList<TermBean> vector2) {

		ArrayList<TermBean> result = new ArrayList<TermBean>();
		int i = 0, j = 0;
		TermBean temp = null, termBean1 = null, termBean2 = null;

		if (vector1.size() > 0)
			termBean1 = vector1.get(i++);
		if (vector2.size() > 0)
			termBean2 = vector2.get(j++);

		while (i < vector1.size() && j < vector2.size()) {
			if (termBean1.getTermID().compareTo(termBean2.getTermID()) < 0) {
				// System.out.print("<"+termBean1.getTermID()+" "+termBean2.getTermID());
				temp = new TermBean(termBean1.getTermID(), termBean1.getTf());
				result.add(temp);
				termBean1 = vector1.get(i++);
			} else if (termBean1.getTermID().compareTo(termBean2.getTermID()) > 0) {
				// System.out.print(">"+termBean1.getTermID()+" "+termBean2.getTermID());
				temp = new TermBean(termBean2.getTermID(), termBean2.getTf());
				result.add(temp);
				termBean2 = vector2.get(j++);
			} else {
				// System.out.print("="+termBean1.getTermID()+" "+termBean2.getTermID());
				temp = new TermBean(termBean1.getTermID(), termBean1.getTf() + termBean2.getTf());
				result.add(temp);
				termBean1 = vector1.get(i++);
				termBean2 = vector2.get(j++);
			}
			// System.out.println();
		}

		while (j < vector2.size()) {
			temp = new TermBean(vector2.get(j).getTermID(), vector2.get(j).getTf());
			result.add(temp);
			j++;
		}

		while (i < vector1.size()) {
			temp = new TermBean(vector1.get(i).getTermID(), vector1.get(i).getTf());
			result.add(temp);
			i++;
		}

		return result;
	}

	/* 更新此类的中心，取平均值 */
	public static void freshCenter(HashMap<String, ArrayList<TermBean>> forwardIndexMap, Cluster cluster) {

		/* 设置前一个类的中心，用于最终结束条件的判断 */
		cluster.setPreCenterVector(cluster.getCenterVector());
		if (cluster.getElements().size() == 0) {
			cluster.setCenterVector(new ArrayList<TermBean>());
			return;
		}
		/* 不断的合并更新 */
		int size = cluster.getElements().size();
		int i = 1;
		/**/
		ArrayList<TermBean> tempVector = forwardIndexMap.get(cluster.getElements().get(0));
		while (i < size) {
			tempVector = mergeVector(tempVector, forwardIndexMap.get(cluster.getElements().get(i++)));
		}
		/**/
		cluster.setCenterVector(tempVector);

		for (Iterator<TermBean> iterator = cluster.getCenterVector().iterator(); iterator.hasNext();) {
			TermBean termBean = (TermBean) iterator.next();
			termBean.setTf(termBean.getTf() / size); /* 取平均 */
		}
	}

	/* 判断两个向量是否相等 */
	public static boolean isEqual(ArrayList<TermBean> vector1, ArrayList<TermBean> vector2) {
		if (vector1.size() != vector2.size())
			return false;

		Iterator<TermBean> iterator1 = vector1.iterator();
		Iterator<TermBean> iterator2 = vector2.iterator();
		TermBean termBean1 = null, termBean2 = null;

		while (iterator1.hasNext() && iterator2.hasNext()) {
			termBean1 = (TermBean) iterator1.next();
			termBean2 = (TermBean) iterator2.next();
			if (!(termBean1.getTermID().equals(termBean2.getTermID()) && termBean1.getTf() == termBean2.getTf()))
				break;
		}
		if (iterator1.hasNext() || iterator2.hasNext())
			return false;
		return true;
	}

	/*void print(ArrayList<Cluster> clusterResult) {
		for (Iterator iterator = clusterResult.iterator(); iterator.hasNext();) {
			Cluster cluster = (Cluster) iterator.next();
			ArrayList<TermBean> center = cluster.getCenterVector();
			for (Iterator iterator2 = center.iterator(); iterator2.hasNext();) {
				TermBean termBean = (TermBean) iterator2.next();
				System.out.print(termBean + " ");
			}
			System.out.println();

		}
	}*/

	/* KMean聚类实现，对搜索结果进行了聚类 */
	public static ArrayList<Cluster> cluster(HashMap<String, ArrayList<TermBean>> forwardIndexMap, List<DocBean> resultList) {

		ArrayList<Cluster> clusterResult = new ArrayList<Cluster>(Constant.KMEANS);
		HashSet<Integer> history = new HashSet<Integer>();
		int temp = 0;
		Cluster cluster = null;
		/* 随机选择种子 */
		for (int i = 0; i < Constant.KMEANS; i++) {
			temp = (int) (Math.random() * resultList.size());
			if (history.contains(temp)) {
				continue;
			}
			history.add(temp);
			cluster = new Cluster();
			ArrayList<TermBean> vector = forwardIndexMap.get(resultList.get(temp).getDocID());

			if (vector == null) {
				//System.out.println("vector is null");
				i--;
				continue;
			}
			cluster.setCenterVector(forwardIndexMap.get(resultList.get(temp).getDocID()));
			clusterResult.add(cluster);
		}
		int record = 0, i = 0;
		boolean flag = false;
		double maxDistance = 0, curDistance = 0;

		while (true) {
			for (Iterator<DocBean> iterator = resultList.iterator(); iterator.hasNext();) {
				String id = (String) iterator.next().getDocID();
				ArrayList<TermBean> vector = forwardIndexMap.get(id);
				if (vector == null)
					continue;
				i = 0;
				maxDistance = -1;
				curDistance = -1;
				record = -1;
				for (Iterator<Cluster> iterator2 = clusterResult.iterator(); iterator2.hasNext();) {
					cluster = (Cluster) iterator2.next();
					curDistance = computeDistance(vector, cluster.getCenterVector());
					if (maxDistance < curDistance) {
						maxDistance = curDistance;
						record = i;
					}
					i++;
				}
				clusterResult.get(record).insertElement(id);/* 插入到距离最小的类中 */
			}

			/* 更新每一个类的中心 */
			for (Iterator<Cluster> iterator = clusterResult.iterator(); iterator.hasNext();) {
				cluster = (Cluster) iterator.next();
				//System.out.println("cluster size is"+cluster.getElements().size());
				freshCenter(forwardIndexMap, cluster);
				/* 将之前类中的元素清空一下 */
				/* 判断是否达到结束条件 */
				if (isEqual(cluster.getCenterVector(), cluster.getPreCenterVector()))
					flag = true;
				else
					flag = false;
			}
			System.out.println();
			if (flag)
				break;
			for (Iterator<Cluster> iterator = clusterResult.iterator(); iterator.hasNext();) {
				cluster = (Cluster) iterator.next();
				cluster.getElements().clear();
			}
		}
		return clusterResult;
	}
}
