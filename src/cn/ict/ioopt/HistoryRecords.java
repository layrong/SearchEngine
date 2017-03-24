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
            // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
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
				//��һ��д�ļ��������캯���еĵڶ�������true��ʾ��׷����ʽд�ļ�
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
     * ����Ϊ��λ��ȡ�ļ��������ڶ������еĸ�ʽ���ļ�
     */
    public static ArrayList<String> getHistoryRecords(ArrayList<String> indexWordStringList) {
        
    	ArrayList<String> recordsList = null;
    	recordsList = new ArrayList<String>();
    	
    	File file = new File(Constant.HISTORYRPATH);
        BufferedReader reader = null;
        
        try {
            //System.out.println("����Ϊ��λ��ȡ�ļ����ݣ�һ�ζ�һ���У�");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // һ�ζ���һ�У�ֱ������nullΪ�ļ�����
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
