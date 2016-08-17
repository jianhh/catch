package com.work.businesses.service;

import java.util.List;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.util.StartContent;
import com.framework.util.StringUtils;
import com.work.businesses.bo.TestPhpBO;
import com.work.businesses.schema.PhpSchema;
import com.work.util.JsoupUtil;

/**
 * PHP≤‚ ‘
 * 
 * @author tangbiao
 * 
 */
public class TestPhpService1 extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(TestPhpService1.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {

		if (logger.isDebugEnabled())
			logger.debug("TestPhpService");
//		response.setAttributes("testphp", 1);
//		response.setForwardId("success");
		String jsonStr = JsoupUtil.getJson("2", "…Ã∆∑≤ª∆•≈‰");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}
}
