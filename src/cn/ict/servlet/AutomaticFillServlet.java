package cn.ict.servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.ict.ioopt.HistoryRecords;
import cn.ict.ws.WordSegment;

import net.sf.json.JSONArray;

/**
 * 自动补全
 * 向客服端返XML数据的servlet
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class AutomaticFillServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//获得页面传过来的字符串
		response.setContentType("text/html;charset=utf-8");
		String indexKeyWords = new String(request.getParameter("word").getBytes("ISO-8859-1"),"UTF-8");
		
		ArrayList<String> indexKeywordsList = null;
		ArrayList<String> returnHistoryList = null;
		
		indexKeywordsList = WordSegment.wordSegment(indexKeyWords);
		returnHistoryList = HistoryRecords.getHistoryRecords(indexKeywordsList);
		//System.out.println("test"+returnHistoryList);
		
		JSONArray json = JSONArray.fromObject(returnHistoryList);
		response.getWriter().println(json.toString());
	}

	public void init() throws ServletException
	{
		
	}
	
	public void destroy()
	{
		super.destroy(); 
	}
}
