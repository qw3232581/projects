<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String callback = request.getParameter("callback");
	if(StringUtils.isEmpty(callback)){
	    //不需要jsonp的支持
	    out.print("{\"abc\":123}");
	}else{
	    out.print(callback + "({\"abc\":123})");
	}
%>