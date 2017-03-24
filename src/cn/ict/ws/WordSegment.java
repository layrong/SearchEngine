package cn.ict.ws;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import cn.ict.util.Constant;

public class WordSegment {

	private static HashSet<String> dictionary = null;
	private static HashSet<String> stopDic = null;
	private static boolean isInit = false;

	private static void dicReader(HashSet<String> dic, String path) {
		String word = null;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(path));
			while ((word = reader.readLine()) != null) {
				dic.add(word);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void initDictionary() {

		isInit = true;
		dictionary = new HashSet<String>();
		stopDic = new HashSet<String>();
		dicReader(dictionary, Constant.DICTIONARYPATH);
		dicReader(stopDic, Constant.STOPDICPATH);
	}

	/* 采用最大逆向匹配法进行分词并去停用词 */
	public static ArrayList<String> wordSegment(String text) {
		if(!isInit) 
		initDictionary();
		int curPos = 0;
		ArrayList<String> wordList = new ArrayList<String>();
		String word = null;

		while (text.length() > 0) {
			if (Constant.MAXWORDLEN < text.length())
				curPos = Constant.MAXWORDLEN;
			else
				curPos = text.length();
			do {
				curPos--;
				word = text
						.substring(text.length() - curPos - 1, text.length());
			} while (word != null &&(!dictionary.contains(word)) && (curPos > 0));
			if (word.length() != 1) {
				if (!stopDic.contains(word)) {
					wordList.add(word);
				}
			}
			text = text.substring(0, text.length() - word.length());
		}
		return wordList;
	}
}
