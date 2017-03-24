package cn.ict.test;

import java.util.ArrayList;

import cn.ict.ioopt.HistoryRecords;
import cn.ict.util.SortUtil;

public class StringSort_test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<String> list = new ArrayList<String>();
		list.add("ÖÐ¹ú");
		ArrayList<String> history = HistoryRecords.getHistoryRecords(list);
		SortUtil.sortStringList(history);
	}

}
