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
 * PHP����
 * 
 * @author tangbiao
 * 
 */
public class TestPhpService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(TestPhpService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {

		if (logger.isDebugEnabled())
			logger.debug("TestPhpService");
		String type = request.getParameter("type");
		logger.debug("domainUrl:" + StartContent.getInstance().getDomainUrl());
		logger.debug("domainIp:" + StartContent.getInstance().getDomainIp());
		logger.debug("weigouIp:" + StartContent.getInstance().getWeigouIp());

		try {
			TestPhpBO bo = new TestPhpBO();
			if (StringUtils.isEmpty(type)) {// ��ѯ����
				List<PhpSchema> testList = bo.getTestMysql();
				response.setAttributes("testList", testList);
			} else if (type.equals("1")) {// ����id��ѯ
				String id = request.getParameter("id");
				PhpSchema testVO = bo.getTestMysqlById(id);
				response.setAttributes("testVO", testVO);
			} else if (type.equals("2")) {// �������Ʋ�ѯ����
				String name = request.getParameter("name");
				List<PhpSchema> testStateList = bo.getTestMysqlByName(name);
				response.setAttributes("testStateList", testStateList);
			} else if (type.equals("3")) {// ����
				String name = request.getParameter("name");
				String age = request.getParameter("age");
				String sex = request.getParameter("sex");
				boolean b = bo.addTestMysql(name, age, sex);
				response.setAttributes("b", b);
			} else if (type.equals("4")) {// ɾ��
				String id = request.getParameter("id");
				boolean b = bo.delTestMysqlById(id);
				response.setAttributes("b", b);
			} else if (type.equals("5")) {// �޸�
				String id = request.getParameter("id");
				String name = request.getParameter("name");
				boolean b = bo.updateTestMysql(id, name);
				response.setAttributes("b", b);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
//		response.setAttributes("type", type);
//		response.setForwardId("success");
		String jsonStr = JsoupUtil.getJson("1", "��Ʒ��ƥ��");
		response.setContentType("text/json");
		response.setCaseType(response.CASETYPE_FLUSH);
		response.setFlushContent(jsonStr);
	}
}
