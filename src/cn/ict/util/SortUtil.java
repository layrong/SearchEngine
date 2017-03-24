package cn.ict.util;

import java.util.ArrayList;  
import java.util.Arrays;
import java.util.Collections;  

import cn.ict.dataBean.DocBean;
import cn.ict.dataBean.TermBean;

public class SortUtil{
	
	public static void sortTermList(ArrayList<TermBean> termList){
		
		if(termList.isEmpty()){
			//System.out.println("termList is NULL");
		}else{
	        
	        Collections.sort(termList);  
		}
	}
	
	public static void sortDocList(ArrayList<DocBean> docList){
		
		if(docList.isEmpty()){
			//System.out.println("docList is NULL");
		}else{
	        Collections.sort(docList);  
		}
	}
	
	public static void sortStringList(ArrayList<String> stringList){
		StringUtil mySs[] = new StringUtil[stringList.size()];//创建自定义排序的数组
		
		for (int i = 0; i < stringList.size(); i++) {
		   mySs[i] = new StringUtil(stringList.get(i));
		}
		
		Arrays.sort(mySs);//排序
		  
		for (int i = 0; i < mySs.length; i++) {
			stringList.set(i, mySs[i].getString());
		}
	}
} 
