package com.work.admin.service;




import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.work.admin.bo.AdminBO;
import com.work.admin.content.AdminContent;

/**
 * 给没有供应商添加商务人员
 * 
 * @author zhangwentao
 * 
 */
public class AddShopAdminService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(AddShopAdminService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
	    AdminBO bo=new AdminBO();
	    String jsonStr=AdminContent.S_ZORE;
	    try {
	    	String adminId=request.getParameter("adminId");
	    	String shopId=request.getParameter("shopId");
	    	boolean info=bo.addShopAdmin(adminId,shopId);
	    	if(info){
	    		jsonStr=AdminContent.S_THIRD;
	    	}else{
	    		jsonStr=AdminContent.S_FIRST;
	    	}
		} catch (Exception e) {
			logger.error("给店铺添加商务人员错误:", e);
		}
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
		response.setForwardId("success");
	    
	} 
}

