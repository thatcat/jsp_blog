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

public class FriendServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public FriendServlet() {
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
		
		List<FriendBean> friendList = new ArrayList<FriendBean>();
		FriendDao friendDao = new FriendDao();
		
		int begin = Integer.valueOf(request.getParameter("begin"));
		int count = Integer.valueOf(request.getParameter("count"));
		
		friendList = friendDao.queryFriend(begin,count);
		//HttpSession session = request.getSession();
		session.setAttribute("friendList",friendList);
		RequestDispatcher rd = request.getRequestDispatcher("/front/friend/Friend.jsp");
		rd.forward(request, response);
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
		doGet(request,response);

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
