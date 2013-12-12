package com.xiuson.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.*;

import com.xiuson.dao.*;
import com.xiuson.toolsbean.MyTools;
import com.xiuson.valuebean.*;

public class ForwardServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public ForwardServlet() {
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
		
		
		String forward = request.getParameter("forward");
		//System.out.println("test-----------------------ForwardServlet: forward = "+forward);
		if (forward == null) {
			forward = "";		
		}
		if (forward.equals("myArticle")) {

			ArticleDao articleDao = new ArticleDao(); 
			/*********************获取在主页面的内容显示区中的内容****************/
			//从tb_article数据表中获取前3篇文章
			List articleList = articleDao.queryArticleFromTo(1, 10);//不按文章类别，查询前3篇文章
			request.setAttribute("articleList", articleList);
			
			RequestDispatcher rd = request.getRequestDispatcher("/front/article/AllArticles.jsp");
			rd.forward(request, response);
		}
		
		if (forward.equals("myFriend")) {
			List<FriendBean> friendList = new ArrayList<FriendBean>();
			FriendDao friendDao = new FriendDao();
		
			friendList = friendDao.queryFriend(1,10);
			friendDao.getDB().close();//关闭数据库连接
			session = request.getSession();
			session.setAttribute("friendList",friendList);
			
			RequestDispatcher rd = request.getRequestDispatcher("/front/friend/Friend.jsp");
			rd.forward(request, response);
		}
		
		if (forward.equals("myBlog")) {
		//	System.out.println("test-----------------------ForwardServlet: myBlog = "+forward);
			RequestDispatcher rd = request.getRequestDispatcher("/index.jsp");
			rd.forward(request, response);
		}
		
		if (forward.equals("myWord")) {
			session = request.getSession();
			WordDao wordDao = new WordDao();
			int wordSum = wordDao.queryWordSum();
			List wordList = wordDao.queryWord(1,10);//获取前10条留言
			wordDao.getDB().close();//关闭数据库连接
			
			session.setAttribute("wordList", wordList);
			session.setAttribute("wordSum", wordSum);
			RequestDispatcher rd = request.getRequestDispatcher("/front/word/WordSingle.jsp");
			rd.forward(request, response);
		}
		System.out.println("test-----------------------ForwardServlet: forward(end method) = "+forward);
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
