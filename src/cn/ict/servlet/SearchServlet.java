package cn.ict.servlet;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.ict.dataBean.TermBean;
import cn.ict.database.JavaBean;
import cn.ict.ioopt.HistoryRecords;
import cn.ict.query.QueryProcesser;
import cn.ict.query.QueryResult;
import cn.ict.util.Constant;
import cn.ict.util.PageUtil;

public class SearchServlet extends HttpServlet implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private PageUtil pageRttr = new PageUtil("", 0, 0, 1, Constant.PAGESIZE);
	
	public void init(ServletConfig config) throws ServletException {

		super.init(config);
	}

	public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		
		response.setContentType("text/html;charset=utf-8");
		HttpSession session = request.getSession();

		String action = request.getParameter("action");
		String pageType = request.getParameter("pageType");
		String sortIndex = request.getParameter("sortIndex");
		
		QueryResult searchResult = null;
		//聚类结果
		List<List<JavaBean>> clusterResultList = null;
		//相关推荐 List
		List<TermBean> recmTermBeanList = null;
		
		int totalPageNumber = pageRttr.getTotalPageNumber();
		int totalRecords = pageRttr.getTotalRecordNumber();
		int pageIndex = pageRttr.getPageIndex();
		String indexKeyWords = pageRttr.getIndexKeyWords();

		if(action.equals("initial") && pageType.equals("first")){
			indexKeyWords = new String(request.getParameter("indexKeyWords").getBytes("ISO-8859-1"),"UTF-8");
			//System.out.println(indexKeyWords);
			pageIndex = 1;
			
			searchResult = QueryProcesser.query(indexKeyWords,pageIndex,Constant.PAGESIZE,Integer.parseInt(sortIndex));
			//searchResult = QueryProcesser.debugQuery(indexKeyWords,pageIndex,Constant.PAGESIZE,Integer.parseInt(sortIndex));
			
			clusterResultList = searchResult.getClusterResultList();
			recmTermBeanList = searchResult.getRecommendResultList();
			//recmTermBeanList = Recommend_test.getRecmTermList();
			
			totalPageNumber = searchResult.getTotalPageNumber();
			totalRecords = searchResult.getTotalItemNumber();
			
			//System.out.println("test:"+pageIndex+":"+searchResult.getTotalPageNumber());
			
		}else if(action.equals("page")){
			if(pageType.equals("first")){
				pageIndex = 1;
				//System.out.println("first+"+pageIndex);
			}else if(pageType.equals("last")){
				pageIndex = pageRttr.getTotalPageNumber();
				//System.out.println("last+"+pageIndex);
			
			}else if(pageType.equals("next")){
				pageIndex = pageIndex + 1;
				//System.out.println("next+"+pageIndex);
			
			}else if(pageType.equals("previous")){
				pageIndex = pageIndex - 1;
				//System.out.println("previous+"+pageIndex);
			
			}else{}
			
			searchResult = QueryProcesser.query(indexKeyWords,pageIndex,Constant.PAGESIZE,Integer.parseInt(sortIndex));
			//searchResult = QueryProcesser.debugQuery(indexKeyWords,pageIndex,Constant.PAGESIZE,Integer.parseInt(sortIndex));
			
			clusterResultList = searchResult.getClusterResultList();
			recmTermBeanList = searchResult.getRecommendResultList();
			//recmTermBeanList = Recommend_test.getRecmTermList();
			
			totalRecords = searchResult.getTotalItemNumber();
		}else{}
		
		//System.out.println(clusterResultList);
		
		pageRttr = new PageUtil(indexKeyWords,
								totalPageNumber,
								totalRecords,
								pageIndex,
								Constant.PAGESIZE);
		
		List<JavaBean> dataList = searchResult.getResultList();

		//System.out.println("test:"+dataList);
		
		session.setAttribute("indexKeyword", indexKeyWords);
		session.setAttribute("sortIndex", sortIndex);
		session.setAttribute("contJavaBean", dataList);
		session.setAttribute("pageJavaBean", pageRttr);
		session.setAttribute("recmTermResult", recmTermBeanList);	
		session.setAttribute("clusterResult", clusterResultList);
		
		if(!indexKeyWords.isEmpty() && HistoryRecords.isNotExisted(indexKeyWords)){
			HistoryRecords.updateHistoryRecords(indexKeyWords);
		}else{
			/* Do nothing */
		}
		
		request.getRequestDispatcher("indexPage.jsp").forward(request, response);
	}
	
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		this.doPost(request, response);
	}
}