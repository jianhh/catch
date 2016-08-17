package com.work.admin.service;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.work.admin.bo.AdminBO;
import com.work.admin.content.AdminContent;
import com.work.admin.schema.AdminSchema;

/**
 * 添加商务人员
 * 
 * @author zhangwentao
 * 
 */
public class AddAdminService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(AddAdminService.class);

	public void doPerform(Req request, Resp response) throws BOProvideException {
		AdminBO bo = new AdminBO();
		String jsonStr = AdminContent.S_ZORE;
		try {
			String name = request.getParameter("name");
		    name=name.replaceAll(" ", "");
			//name.replaceAll("  ", "");
			AdminSchema info = bo.getAdminByName(name);
			if (info == null) {
				if (name != null && !name.isEmpty()) {
					boolean es=bo.addAdmin(name);
					if(es){
						jsonStr = AdminContent.S_THIRD;	
					}else{
						jsonStr=AdminContent.S_ZORE;
					}
				} else {
					jsonStr = AdminContent.S_SECOND;
				}
			} else {
				jsonStr = AdminContent.S_FIRST;
			}
		} catch (Exception e) {
			logger.error("添加商务人员错误:", e);
		}
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
		response.setForwardId("success");

	}
	
}
