package com.xiuson.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.*;

import com.xiuson.dao.*;
import com.xiuson.toolsbean.MyTools;
import com.xiuson.valuebean.*;

public class WordServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public WordServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		request.setCharacterEncoding("UTF8");
		System.out.println("test--------------wordServlet.action="+action);
		if (action == null) {
			action = "";
			
		}
	
		if (action.equals("readWord"))// 閱讀文章
			this.readWord(request, response);
		
		if(action.equals("addWord"))// 發表文章回覆
			this.addWord(request, response);
	}

	
	public void readWord(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		IPDao ipDao = new IPDao();	
		IPBean ipBean = new IPBean();
		
		ipBean.setMethod(request.getMethod());
		ipBean.setProtocol(request.getProtocol());
		ipBean.setRealPath(request.getContextPath());//可能需要修改 
		ipBean.setReferer(request.getHeader("Referer"));
		if (request.getHeader("x-forwarded-for") == null) {   
			  ipBean.setRemoteAddr(request.getRemoteAddr());
			} 
			else {
				ipBean.setRemoteAddr(request.getHeader("x-forwarded-for")   );
			}
		ipBean.setRemoteHost(request.getRemoteHost());
		ipBean.setRequestUrl(request.getRequestURI());
		ipBean.setServerName(request.getServerName());
		ipBean.setServerPath(request.getServletPath());
		ipBean.setServerPort(""+request.getServerPort());
		
		HttpSession session = request.getSession();
		ipBean.setCharacterEncoding(request.getCharacterEncoding());
		ipBean.setQueryString(request.getQueryString());
		ipBean.setPathInfo(request.getPathInfo());
		ipBean.setRemoteUser(request.getRemoteUser());
		ipBean.setAcceptLanguage(request.getHeader("Accept-Language"));
		ipBean.setAcceptEncoding(request.getHeader("Accept-Encoding"));
		ipBean.setUserAgent(request.getHeader("User-Agent"));
		ipBean.setLastAccessed(session.getLastAccessedTime()+"");
		
		String sdTime = MyTools.ChangeTime(new Date());
		ipBean.setTime(sdTime);
		ipDao.addIP(ipBean);
		
		WordDao wordDao = new WordDao();
		//HttpSession session = request.getSession();
		
		int begin ;
		int count;
		String beginString = request.getParameter("wordBegin");//分页显示时的第几页
		String countString = request.getParameter("wordCount");//每页的留言数
		if(beginString == null)
			begin = 1;
		else 
			begin= Integer.parseInt(beginString);
		if(countString == null)
			count = 10;
		else
			count = Integer.parseInt(countString);
		
		List wordList = wordDao.queryWord(begin,count);//获取所有留言
		int wordSum = wordDao.queryWordSum();
		wordDao.getDB().close();//关闭数据库连接
		session.setAttribute("wordSum", wordSum);
		session.setAttribute("wordList", wordList);
		
		RequestDispatcher rd = request.getRequestDispatcher("/front/word/Words.jsp");
		rd.forward(request, response);

	}
	
	
	
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void addWord(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		IPDao ipDao = new IPDao();	
		IPBean ipBean = new IPBean();
		
		ipBean.setMethod(request.getMethod());
		ipBean.setProtocol(request.getProtocol());
		ipBean.setRealPath(request.getContextPath());//可能需要修改 
		ipBean.setReferer(request.getHeader("Referer"));
		if (request.getHeader("x-forwarded-for") == null) {   
			  ipBean.setRemoteAddr(request.getRemoteAddr());
			} 
		else {
				ipBean.setRemoteAddr(request.getHeader("x-forwarded-for")   );
			}
		ipBean.setRemoteHost(request.getRemoteHost());
		ipBean.setRequestUrl(request.getRequestURI());
		ipBean.setServerName(request.getServerName());
		ipBean.setServerPath(request.getServletPath());
		ipBean.setServerPort(""+request.getServerPort());
		
		HttpSession session = request.getSession();
		ipBean.setCharacterEncoding(request.getCharacterEncoding());
		ipBean.setQueryString(request.getQueryString());
		ipBean.setPathInfo(request.getPathInfo());
		ipBean.setRemoteUser(request.getRemoteUser());
		ipBean.setAcceptLanguage(request.getHeader("Accept-Language"));
		ipBean.setAcceptEncoding(request.getHeader("Accept-Encoding"));
		ipBean.setUserAgent(request.getHeader("User-Agent"));
		ipBean.setLastAccessed(session.getLastAccessedTime()+"");
		
		String sdTime = MyTools.ChangeTime(new Date());
		ipBean.setTime(sdTime);
		ipDao.addIP(ipBean);
		
		WordDao wordDao = new WordDao();
		WordBean wordBean = new WordBean();
		String content = request.getParameter("content");
		String author = request.getParameter("author");
		
		if(content != null)
		content = new String(content.getBytes("ISO-8859-1"),"UTF8");
		if(author != null)
		author = new String(author.getBytes("ISO-8859-1"),"UTF8");
		
		wordBean.setAuthor(author);
		wordBean.setContent(content);
		wordBean.setSdTime(sdTime);
	
		wordDao.addWord(wordBean);
		
		//HttpSession session = request.getSession();
		List wordList = wordDao.queryWord(1,10);//获取前10条留言
		session.setAttribute("wordList", wordList);
		
		
		RequestDispatcher rd = request
				.getRequestDispatcher("/front/word/Words.jsp");
		rd.forward(request, response);
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
