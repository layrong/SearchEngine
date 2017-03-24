<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<style type="text/css">
			a:hover{
				color:red;
			}
		</style>
    	<meta http-equiv="description" content="Yshy Search">
    	<script language="javascript" src="js/jquery.js" charset="utf-8"></script>
    	<script language="javascript" src="js/jqueryAutoComplete.js" charset="utf-8"></script>
		<script type="text/javascript">
			$(document).bind("scroll",function(){  
    			//alert("scroll");
    			//$(".title").css{
    				//position: static;
    				//backgroudColor:red;
    			//};
    			//alert("scroll");
			}); 
		</script>
	</head>
	<body>
     <div style="position: fixed; 
     			 z-index:1;
     			 width:100%; 
     			 height:125px;
     			 background-color: white; 
     			 top:0; 
     			 left:0; 
     			 left:0;  ">
     	<div id="search" style="backgroud-color:white; float:none; clear: both; width:100%; height:75px;">
    		<div style="float:left; margin-left:20px; margin-top:-15px;">
				<img alt="Yshy 搜索" src="./img/logo.png" width="140px" height="80px">
			</div>
	    	<div align="left" style="margin-top: 30px; float:left;">
    			<input type="text" name="indexKeyWords" id="indexKeyWords" size="68" 
    					style="height: 35; 
    			   		width:550px; 
    			   		font-size: 18px;
    			   		background-color: white;" value="${indexKeyword}"/>
    			<input type="button" id="submit" value="Let's Go" style="height: 35; width: 84"/>
	    	</div>
	    	<div id="auto"></div>
	    </div>
	    <div id="sort" style="backgroud-color:white; float:none; width:100%;  margin-left: 158px">
	    	<span><font>总共有<c:out value="${pageJavaBean.getTotalRecordNumber()}"></c:out>相关记录</font></span>
	    	<input class="radio" type="radio" name="sortIndex" value = "0">按相关性排序
			<input class="radio" type="radio" name="sortIndex" value = "1">按时间排序
       		<input class="radio" type="radio" name="sortIndex" value = "2" checked>按热度排序
       		<script type="text/javascript">
				$(function(){
					//alert(sortIndex);
					$("input[type=radio][name='sortIndex'][value=${sortIndex}]").attr("checked","checked"); 
			  	});
			</script>
	    </div>
	    <hr style="color: border: 1 solid #aaaaaa;">
	    <div>
	    	&nbsp;
	    </div>
	</div>	
	<div id="indexResult" style="float:left; margin-top:115px; width: 60%">
		<div style="float:left; z-index:0; position:fixed; top:130px;">
			<div style="margin-left:40px;">
				<font style="font-weight: bold">相关推荐</font>
			</div>
			<c:forEach var="recmTermResult" items="${recmTermResult}">
				<ul  class="recm" style="list-style: none;">
					<li style="width:80px">
						<a href="<%=path %>/SearchServlet?action=initial&pageType=first&indexKeyWords=${recmTermResult.getTermID()}&sortIndex=0">
							${recmTermResult.getTermID()}</a>
					</li>
				</ul>
			</c:forEach>
		</div>
		<!-- 数据显示列表 -->
		<c:forEach var="contJavaBean" items="${contJavaBean}" varStatus="status">
			<ul style="margin-left: 110px; list-style: none">
				<li style="position: relative; ">
					<span style="position: absolute; top: -10px;">
						<a class="title" id="${status.index}" href="${contJavaBean.getUrl()}">${contJavaBean.getTitle()}</a>
					</span>
					<span id="S${status.index}" style="display: none; 
													   border: 1px solid;
													   withd:200px;
													   background-color:#dddddd;
													   position: absolute;
													   z-index:1;
													   left:200px;
													   top:10px">
						<!-- 修改此处的预览信息  -->
						"${contJavaBean.getContent()}"
					</span>
					<script type="text/javascript">
						$("#${status.index}").bind("mouseover",function(){
		 	 				//alert("in");
		 	 				var node = $("#S${status.index}");
		 	 				node.attr("display", "block");
		 	 				node.show();
		 	 			});
		 	 			$("#${status.index}").bind("mouseout",function(){
		 	 				//alert("out");
		 	 				$("#S${status.index}").hide();
		 	 			});
		 	 			
					</script>
				</li>
				<li>&nbsp;</li>
				<li>
					${contJavaBean.getSnippet()}
				</li>
				<li style="margin-top:5">
					<a  class="snapshotlink" href="<%=path %>/SnapshotServlet?indexKeyWords=${indexKeyword}&url=${contJavaBean.getUrl()}">
						网页快照
					</a>
					<span style="float:right">${contJavaBean.getTime()}</span>
				</li>
				<li>&nbsp;</li>
			</ul>
		</c:forEach>
		<!-- 分页表单 -->			
		<table cellSpacing=0 cellPadding=0 border="0" width="70%" style="margin-left:115px;">	
			<tr><td  height="25"></td></tr>		
			<tr>
				<td align="right">
					第<c:out value="${pageJavaBean.getPageIndex()}"></c:out>页/共<c:out value="${pageJavaBean.getTotalPageNumber()}"></c:out>页
					&nbsp;&nbsp;
					每页<c:out value="${pageJavaBean.getPageSize()}"></c:out>行/共<c:out value="${pageJavaBean.getTotalRecordNumber()}"></c:out>行
					&nbsp;&nbsp;
					<c:choose>
					<c:when test="${pageJavaBean.getPageIndex()==1}">				
					首页&nbsp;上页&nbsp;
					</c:when>		
					<c:otherwise>
					<a href="<%=path %>/SearchServlet?action=page&pageType=first&indexKeyword=${indexKeyword}&sortIndex=${sortIndex}">
						首页
					</a>&nbsp;<a href="<%=path %>/SearchServlet?action=page&pageType=previous&indexKeyword=${indexKeyword}&sortIndex=${sortIndex}">上页</a>&nbsp;
					</c:otherwise>	
					</c:choose>		
					<c:choose>				
					<c:when test="${pageJavaBean.getPageIndex()==pageJavaBean.getTotalPageNumber()}">
					下页&nbsp;尾页&nbsp;
					</c:when>	
					<c:otherwise>
					<a href="<%=path %>/SearchServlet?action=page&pageType=next&indexKeyword=${indexKeyword}&sortIndex=${sortIndex}">
						下页
					</a>&nbsp;<a href="<%=path %>/SearchServlet?action=page&pageType=last&indexKeyword=${indexKeyword}&sortIndex=${sortIndex}">尾页</a>&nbsp;
					</c:otherwise>	
					</c:choose>							
					&nbsp;&nbsp;&nbsp;&nbsp;	
				</td>
			</tr>
		</table>
		<div>&nbsp;</div>
	</div>
	
	<div style="float: right; z-index: 0; margin-top: 125px; width: 36%; height:100%">
		<div><font style="font-weight: bold;">结果分类</font></div>
		<!-- 数据显示列表 -->
		<c:forEach var="result" items="${clusterResult}" varStatus="status">
			<div>&nbsp;</div>
			<div class="type">${status.index + 1}. ${result[0].getKeyword()}</div>
			<c:forEach var="javaBean" items="${result}">
				<ul style="width: 400px;">
					<li>
						<a href="${javaBean.getUrl()}">${javaBean.getTitle()}</a>
					</li>
				</ul>
			</c:forEach>
		</c:forEach>	
	</div>
	</body>
</html>
