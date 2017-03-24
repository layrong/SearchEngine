package cn.ict.ioopt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import cn.ict.dataBean.DocBean;
import cn.ict.dataBean.TermBean;
import cn.ict.util.Constant;

/*将索引存储到本地文件中，将索引从本地文件中读出*/

public class IndexFile {

	public static void saveInvertedIndex(HashMap<String, ArrayList<DocBean>> invertedIndexMap) {
		File invertedFile = new File(Constant.INVERTEDPATH);
		FileOutputStream fos = null;
		try {
			if (!invertedFile.exists())
				invertedFile.createNewFile();
			fos = new FileOutputStream(invertedFile);

			if (invertedIndexMap != null) {
				for (Iterator<Entry<String, ArrayList<DocBean>>> iterator = invertedIndexMap.entrySet().iterator(); iterator.hasNext();) {

					Map.Entry<String, ArrayList<DocBean>> entry = (Entry<String, ArrayList<DocBean>>) iterator.next();

					fos.write((entry.getKey() + " ").getBytes());
					ArrayList<DocBean> list = entry.getValue();
					for (Iterator<DocBean> iterator1 = list.iterator(); iterator1.hasNext();) {
						DocBean bean = (DocBean) iterator1.next();
						fos.write((bean.toString() + " ").getBytes());
					}
					fos.write("\n".getBytes());
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void saveTermMap(HashMap<String, String> termMap) {
		//System.out.println("saveTermMap");
		File termFile = new File(Constant.TERMMAPPATH);
		//System.out.println("termMap size is "+termMap.size());
		 
		FileOutputStream fos = null;
		int i = 1, lineSize = 1000;
		try {
			if (!termFile.exists())
				termFile.createNewFile();
			fos = new FileOutputStream(termFile);
			for (Iterator<Entry<String, String>> iter = termMap.entrySet().iterator(); iter.hasNext();) {

				Map.Entry<String, String> entry = (Entry<String, String>) iter.next();				 
				String string = entry.getValue();
				fos.write((string+" ").getBytes());
				if((i++) % lineSize == 0)
					fos.write("\n".getBytes());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
				System.out.println("saveTermMap done");
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}

		}
	}

	public static void saveForwardIndex(HashMap<String, ArrayList<TermBean>> forwardIndexMap) {
		File forwardFile = new File(Constant.FORWARDPATH);
		FileOutputStream fos = null;
		try {
			if (!forwardFile.exists())
				forwardFile.createNewFile();
			fos = new FileOutputStream(forwardFile);
			if (forwardIndexMap != null) {
				for (Iterator<Entry<String, ArrayList<TermBean>>> iterator = forwardIndexMap.entrySet().iterator(); iterator.hasNext();) {

					Map.Entry<String, ArrayList<TermBean>> entry = (Entry<String, ArrayList<TermBean>>) iterator.next();

					fos.write((entry.getKey() + " ").getBytes());
					ArrayList<TermBean> list = entry.getValue();

					for (Iterator<TermBean> iterator1 = list.iterator(); iterator1.hasNext();) {
						TermBean bean = (TermBean) iterator1.next();
						fos.write((bean.getTermID() + " " + bean.getTf() + " ").getBytes());
					}

					fos.write("\n".getBytes());
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void saveIndex(HashMap<String, ArrayList<TermBean>> forwardIndexMap, HashMap<String, ArrayList<DocBean>> invertedIndexMap) {

		saveForwardIndex(forwardIndexMap);
		saveInvertedIndex(invertedIndexMap);
	}

	private static HashMap<String,String> importTermMap() {

		File termFile = new File(Constant.TERMMAPPATH);
		if (!termFile.exists())
			return null;
		HashMap<String,String> termMap = new HashMap<String,String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(termFile));
			String temp[] = null, line = null;
			while ((line = br.readLine()) != null) {
				temp = line.split(" +");
				for (int i = 0; i < temp.length; i++) {
					if (!termMap.containsKey(temp[i]))
						termMap.put(temp[i],temp[i]);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (Exception e2) {
				// TODO: handle exception
				e2.printStackTrace();
			}

		}
		return termMap;
	}

	public static HashMap<String, ArrayList<TermBean>> importForwardIndex() {
		HashMap<String,String> termMap = importTermMap();
		File forwardFile = new File(Constant.FORWARDPATH);
		if (!forwardFile.exists())
			return null;

		HashMap<String, ArrayList<TermBean>> forwardIndexMap = new HashMap<String, ArrayList<TermBean>>();
		String line = null;
		BufferedReader bf = null;
		String temp[] = null;
		 
		try {
			bf = new BufferedReader(new FileReader(forwardFile));
			while ((line = bf.readLine()) != null) {

				 temp = line.split(" +");
				ArrayList<TermBean> termList = new ArrayList<TermBean>();

				for (int i = 1; i < temp.length;) {
					 termList.add(new TermBean(termMap.get(temp[i++]), Double.parseDouble(temp[i++])));
				}
				forwardIndexMap.put(temp[0], termList);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (bf != null)
					bf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return forwardIndexMap;
	}

	public static HashMap<String, ArrayList<DocBean>> importInvertedIndex() {
		File invertedFile = new File(Constant.INVERTEDPATH);
		if (!invertedFile.exists())
			return null;
		HashMap<String, ArrayList<DocBean>> invertedIndexMap = new HashMap<String, ArrayList<DocBean>>();
		String line = null;
		BufferedReader bf = null;
		String temp[] = null;
		try {
			bf = new BufferedReader(new FileReader(invertedFile));
			while ((line = bf.readLine()) != null) {

				 temp = line.split(" +");
				ArrayList<DocBean> docList = new ArrayList<DocBean>();
				for (int i = 1; i < temp.length;) {
					docList.add(new DocBean(temp[i++]));
				}
				invertedIndexMap.put(temp[0], docList);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				if (bf != null)
					bf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return invertedIndexMap;
	}

}
