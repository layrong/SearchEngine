package cn.ict.test;

import java.util.ArrayList;

import cn.ict.database.JavaBean;

public class Cluster_test {
	
	public ArrayList<ArrayList<JavaBean>> clusterListFunc(){
		
		ArrayList<ArrayList<JavaBean>> clusterListTest = new ArrayList<ArrayList<JavaBean>>();
		/*
		List<List<JavaBean>> clusterList = getClusterResult(docList, queryTerms);
		List<List<JavaBean>> clusterList = new ArrayList<List<JavaBean>>();
		
		for(int i=0;i<5;i++){
			List<JavaBean> list = new ArrayList<JavaBean>();
			for(int j=0;j<3;j++){
				list.add(queryResult.get(0));
			}
			clusterList.add(list);
		}
		*/
		return clusterListTest;
	}
}
