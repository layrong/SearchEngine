package cn.ict.indexAction;
/*
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import cn.ict.dataBean.DocBean;
import cn.ict.dataBean.TermBean;
import cn.ict.util.Constant;
*/
public class ExternalSort {
	/*
	private static int recordsNumEachSort = Constant.MAXRECORDS;
	private static ArrayList<DocBean> docBeanList1, docBeanList2, docBeanList;
	private static ArrayList<TermBean> termBeanList1, termBeanList2, termBeanList; 
	
	private static void initSort(){
	
		recordsNumEachSort = Constant.MAXRECORDS;
		docBeanList1 = null;
		docBeanList2 = null;
		termBeanList1 = null;
		termBeanList2 = null;
		docBeanList = null;
		termBeanList = null;
	}
	
	public static void externSort(String fName1, String fName2){
		
		if(fName1.isEmpty() || fName2.isEmpty()){
			
			File file1 = new File(fName1);
			File file2 = new File(fName2);
	        
			BufferedReader reader1 = null;
	        BufferedReader reader2 = null;
	        
	        try {
	            //System.out.println("以行为单位读取文件内容，一次读一整行：");
	            reader1 = new BufferedReader(new FileReader(file1));
	            reader2 = new BufferedReader(new FileReader(file2));
	            
	            String tempString1 = null;
	            String tempString2 = null;
	            
	            // 一次读入一行，直到读入null为文件结束
	            while ((tempString1 = reader1.readLine()) != null && (tempString2 = reader2.readLine()) != null ) {
	            	//recordsList.add(tempString);
	            	//recordsLen++;
	            	if(tempString1.compareTo(tempString2) > 0){
	            		//docBeanList.add();
	            	}
	            }
	            reader1.close();
	            reader2.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (reader1 != null || reader2 != null) {
	                try {
	                    reader1.close();
	                    reader2.close();
	                } catch (IOException e1) {
	                }
	            }
	        }
		}
	}
	*/
}
