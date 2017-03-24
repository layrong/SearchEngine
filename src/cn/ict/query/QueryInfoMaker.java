package cn.ict.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.ict.database.DatabaseUtil;
import cn.ict.database.JavaBean;

public class QueryInfoMaker {

	private static List<String> sentenceEnding = null;
	private static final int SNIPPETSIZE = 160;
	private static final int SNIPPETMAXPARA = 2;

	static {
		sentenceEnding = new ArrayList<String>();
		sentenceEnding.add("。");
		sentenceEnding.add("？");
		sentenceEnding.add("；");
		sentenceEnding.add("”");
		sentenceEnding.add("“");
		sentenceEnding.add("！");

		// System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
	}

	public static JavaBean genQueryInfo(String docId, List<String> queryTerms) {

		JavaBean result = DatabaseUtil.getItemById(docId);
		
		result.setTitle(highlightString(result.getTitle(), queryTerms));
		// 设置摘要
		result.setSnippet(genSnippet(result.getContent(), queryTerms));
		// 正文高亮
		result.setContent(genSubContent(result.getContent(), queryTerms));
		// 如果时间为空，处理一下
		result.setTime(genTime(result.getTime()));
		return result;
	}

	public static JavaBean genQueryFlashCap(String url,List<String> terms) {
		JavaBean result = DatabaseUtil.getItemByUrl(url);
		result.setTime(genTime(result.getTime()));
		result.setTitle(highlightString(result.getTitle(), terms));
		result.setContent(highlightString(result.getContent(),terms));
		return result;
	}

	
	/**
	 * 生成统一格式的时间 xxxx年xx月xx日 HH:MM
	 * 
	 * @param time
	 * @return
	 */
	private static String genTime(String time) {
		if (time == null || time.equals("")) {
			return "未知时间";
		}
		String y, m, d, h, n;
		int idx = time.indexOf("-");
		idx = idx == -1 ? time.indexOf("年") : idx;
		y = time.substring(idx - 4, idx);
		m = time.substring(idx + 1, idx + 3);
		d = time.substring(idx + 4, idx + 6);
		idx = time.indexOf(":");
		if (idx != -1) {
			h = time.substring(idx - 2, idx);
			n = time.substring(idx + 1, idx + 3);
		} else {
			h = "00";
			n = "00";
		}
		return y + "年" + m + "月" + d + "日" + h + "时" + n + "分";
	}

	/**
	 * 生成部分正文 用于预览
	 * 
	 * @param content
	 * @param queryTerms
	 * @return
	 */
	private static String genSubContent(String content, List<String> queryTerms) {
		int endBound = 30;
		int initSize = SNIPPETSIZE * 4 - endBound;
		int initParaEnd = initSize > content.length() ? content.length()
				: initSize;
		int paraEnd = getParaEnd(initParaEnd, content, sentenceEnding, endBound);
		return highlightString(content.substring(0, paraEnd), queryTerms);
	}

	public static String highlightString(String str, List<String> queryTerms) {

		for (int i = 0; i < queryTerms.size(); i++) {
			int left = str.indexOf(queryTerms.get(i));
			int right = left + queryTerms.get(i).length();
			while (left != -1) {
				str = str.substring(0, left) + "<span style='color:red'>"
						+ queryTerms.get(i) + "</span>"
						+ str.substring(right, str.length());
				left = str.indexOf(queryTerms.get(i),
						left + queryTerms.get(i).length()
								+ "<span style='color:red'>".length()
								+ "</span>".length());
				right = left + queryTerms.get(i).length();
			}
		}
		return str;
	}

	private static String genSnippet(String content, List<String> queryTerms) {
		String snippet = null;
		snippet = genDynamicSnippet(content, queryTerms);
		snippet = highlightString(snippet, queryTerms);
		return snippet;
	}

	private static String genDynamicSnippet(String content,
			List<String> queryTerms) {
		// 找出每个词第一次出现的位置
		List<Integer> posList = new ArrayList<Integer>();
		for (int i = 0; i < queryTerms.size(); i++) {
			int pos = content.indexOf(queryTerms.get(i));
			if (pos != -1) {
				posList.add(pos);
			}
		}
		Collections.sort(posList);

		// 根据词的位置信息，扩展出若干个段落
		// 至少会出现一个段落
		int lastParaEnd = 0;
		double maxParaRank = -1;
		int paraStart, paraEnd;
		int initParaLen = SNIPPETSIZE / 2;
		int paraNum = 0;
		int startBound = 50;
		int endBound = 30;
		double seed1 = 1, seed2 = 0.01;
		List<SnippetParagraph> paraList = new ArrayList<SnippetParagraph>();

		for (int i = 0; i < posList.size(); i++) {

			if (posList.get(i) >= lastParaEnd) {// 如果该词还没有处于任何一段中
				paraStart = getParaStart(posList.get(i), lastParaEnd, content,
						sentenceEnding, startBound);
				paraEnd = paraStart + initParaLen > content.length() ? content
						.length() : paraStart + initParaLen;
				paraEnd = getParaEnd(paraEnd, content, sentenceEnding, endBound);
				String paraContext = content.substring(paraStart, paraEnd);
				double paraRank = calcParaRank(paraContext, queryTerms, seed1,
						seed2);

				if (maxParaRank < paraRank) {
					maxParaRank = paraRank;
					paraList.add(new SnippetParagraph(paraStart, paraEnd,
							paraContext, paraRank));
				} else if (paraNum < SNIPPETMAXPARA) {
					paraList.add(new SnippetParagraph(paraStart, paraEnd,
							paraContext, paraRank));
				}
				lastParaEnd = paraEnd;
			}
		}

		Collections.sort(paraList);

		String snippet = "";
		// 如果有两个以上的段落，则合并生成摘要
		if (paraList.size() >= SNIPPETMAXPARA) {
			snippet = paraList.get(0).getParaContext() + "……"
					+ paraList.get(1).getParaContext();
		} else {
			// 前后扩展15个以上的字符，以便显示更多的摘要信息
			int extend = 15;
			int pos = paraList.get(0).getParaStart() - extend >= 0 ? paraList
					.get(0).getParaStart() - extend : 0;
			paraStart = getParaStart(pos, 0, content, sentenceEnding, extend);
			pos = paraList.get(0).getParaEnd() + extend > content.length() ? content
					.length() : paraList.get(0).getParaEnd() + extend;
			paraEnd = getParaEnd(pos, content, sentenceEnding, extend);
			snippet = content.substring(paraStart, paraEnd);
		}

		return snippet;
	}

	// 计算段落评分
	private static double calcParaRank(String paraContext,
			List<String> queryTerms, double seed1, double seed2) {
		double rank = 0;
		for (int i = 0; i < queryTerms.size(); i++) {
			int j = paraContext.indexOf(queryTerms.get(i));
			int k = 0;
			while (j != -1) {
				if (k == 0) {
					rank += seed1;
					k = 1;
				} else {
					rank += seed2;
				}
				j = paraContext.indexOf(queryTerms.get(i), j
						+ queryTerms.get(i).length());
			}
		}
		return rank;
	}

	private static int getParaStart(int pos, int lastParaEnd, String content,
			List<String> sentenceEnding, int startBound) {
		int left = pos - 1;
		while (left >= lastParaEnd && startBound > 0) {
			// System.out.println(content.substring(left,left+1));
			if (sentenceEnding.indexOf(content.substring(left, left + 1)) != -1) {
				break;
			}
			left--;
			startBound--;
		}
		return left + 1;
	}

	private static int getParaEnd(int initParaEnd, String content,
			List<String> sentenceEnding, int endBound) {
		int right = initParaEnd;
		while (right < content.length() && endBound > 0) {
			if (sentenceEnding.indexOf(content.substring(right - 1, right)) != -1) {
				break;
			}
			right++;
			endBound--;
		}
		return right;
	}
}
