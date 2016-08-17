package com.work.admin.service;

import net.sf.json.JSONArray;

import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.work.admin.bo.AdminBO;
import com.work.admin.content.AdminContent;
import com.work.admin.schema.User;

/**
 * 用户登录
 * @author weigou
 *
 */
public class AdminUserService extends BaseListener{

	private static JLogger logger = LoggerFactory
			.getLogger(AdminUserService.class);
	
	@Override
	public void doPerform(Req request, Resp response) throws Exception {
		
		AdminBO bo = new AdminBO();
		JSONArray jsonarray = null;
		String jsonStr = AdminContent.S_FIRST;
		try {
			String name = request.getParameter("name");
			String password = request.getParameter("password");
			User user = bo.getUserByNameAndPwd(name, password);
			if (user != null) {
				jsonarray = JSONArray.fromObject(user);
				request.getSession().setAttribute("empId", user.getU_id());
				request.getSession().setAttribute("loginUser", user);
				 jsonStr = AdminContent.S_ZORE;
			} 
		} catch (Exception e) {
			logger.error("用户登录:", e);
		}
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
		response.setForwardId("success");
	}

}
