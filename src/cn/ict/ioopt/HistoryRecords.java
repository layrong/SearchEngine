package cn.ict.ioopt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import cn.ict.util.Constant;
import cn.ict.util.SortUtil;

public class HistoryRecords {
	
	private static int recordsLen = 0;
	
	public static int getRecordsLen(){
		return recordsLen;
	}
	
	public static Boolean isNotExisted(String indexKeyWords){
		
		File file = new File(Constant.HISTORYRPATH);
        BufferedReader reader = null;
        
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
            	if(tempString.equals(indexKeyWords)){
            		return false;
            	}
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        
		return true;
	}
	
	public static void updateHistoryRecords(String indexKeyWords){
		
		if(!indexKeyWords.isEmpty()){
			try {
				//打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
		        FileWriter writer = new FileWriter(Constant.HISTORYRPATH, true);
		        writer.write(indexKeyWords+"\n");
		        recordsLen++;
		        writer.close();	
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
	}
	
	/**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     */
    public static ArrayList<String> getHistoryRecords(ArrayList<String> indexWordStringList) {
        
    	ArrayList<String> recordsList = null;
    	recordsList = new ArrayList<String>();
    	
    	File file = new File(Constant.HISTORYRPATH);
        BufferedReader reader = null;
        
        try {
            //System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
            	//System.out.println("test:"+indexWordString+":"+tempString);
            	for(String index : indexWordStringList){
                	if(tempString.startsWith(index)){
                		recordsList.add(tempString);
                	}	
            	}
            	recordsLen++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        
        if(recordsList.size() > Constant.RETURNRECORDS){
        	return (ArrayList<String>) recordsList.subList(0, Constant.RETURNRECORDS - 1); 
        }
        
        SortUtil.sortStringList(recordsList);
        
        return recordsList;
    }
}
