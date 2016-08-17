package com.work.admin.service;

import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;

/**
 * �Ƿ�������Ӧ
 * @author weigou
 *
 */
public class ResponseSuccessService  extends BaseListener{
	
	private static JLogger logger = LoggerFactory.getLogger(ResponseSuccessService.class);
	
	@Override
	public void doPerform(Req request, Resp response) throws Exception {
		logger.error("�Ƿ�������Ӧ");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent("success");
		response.setForwardId("success");
	}

}
