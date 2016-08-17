package com.framework.config;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * ��¼��֤������ LoginFilter
 * @author weigou
 */
public class LoginFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;
		HttpSession session = servletRequest.getSession();
		String path = servletRequest.getRequestURI();

		// ��ȡ��¼�û�ID
		String empId = (String) session.getAttribute("empId");
		String requestid = servletRequest.getParameter("requestid");

		if (path.indexOf("addadmin.shtml")  > -1
				||(requestid != null && (
						 requestid.equals("trader")
						|| requestid.equals("supplier")
						|| requestid.equals("orderList")))) {			
			if (empId == null || "".equals(empId)) {
				// ��ת����½ҳ��
				servletResponse.sendRedirect("/weigou/defaultSite/template/login.shtml");
			}else{
				// �Ѿ���½,�����˴�����
				chain.doFilter(request, response);
			}				
		}else{
			// �Ѿ���½,�����˴�����
			chain.doFilter(request, response);
		}
	}
	
	public void destroy() {

	}

	public void init(FilterConfig fConfig) throws ServletException {

	}
}
