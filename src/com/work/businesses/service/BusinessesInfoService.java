package com.work.businesses.service;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.util.StringUtils;
import com.work.businesses.util.BusinessesInfoUtil;
import com.work.util.JsoupUtil;

/**
 * ץȡ�̼���Ϣ
 * 
 * @author tangbiao
 * 
 */
public class BusinessesInfoService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(BusinessesInfoService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		if (logger.isDebugEnabled())
			logger.debug("BusinessesInfoService......................");

		try {
			String city = request.getParameter("city");
			String keyword = request.getParameter("keyword");
			String page = request.getParameter("page");
			int pageNum = 1;
			if (StringUtils.isNotEmpty(page)) {
				pageNum = Integer.parseInt(page);
			}
			if (StringUtils.isEmpty(city)) {
				city = "��ɽ";
			}
			if (StringUtils.isNotEmpty(keyword)) {
				if (keyword.equals("�����̼�")) {
					String url = "http://nanshan.1688.com/qiye.htm?keywords=";// Ĭ����ɽ
					if (city.equals("�޺�")) {
						url = "http://luohu.1688.com/qiye.htm?keywords=";
					} else if (city.equals("����")) {
						url = "http://futian.1688.com/qiye.htm?keywords=";
					} else if (city.equals("����")) {
						url = "http://baoan.1688.com/qiye.htm?keywords=";
					} else if (city.equals("����")) {
						url = "http://longgang.1688.com/qiye.htm?keywords=";
					} else if (city.equals("Խ��")) {
						url = "http://yuexiu.1688.com/qiye.htm?keywords=";
					} else if (city.equals("����")) {
						url = "http://haizhu.1688.com/qiye.htm?keywords=";
					} else if (city.equals("����")) {
						url = "http://liwan.1688.com/qiye.htm?keywords=";
					} else if (city.equals("���")) {
						url = "http://tianhe.1688.com/qiye.htm?keywords=";
					} else if (city.equals("����")) {
						url = "http://baiyun.1688.com/qiye.htm?keywords=";
					} else if (city.equals("����")) {
						url = "http://huadu.1688.com/qiye.htm?keywords=";
					} else if (city.equals("��خ")) {
						url = "http://panyu.1688.com/qiye.htm?keywords=";
					} else if (city.equals("����")) {
						url = "http://humen.1688.com/qiye.htm?keywords=";
					} else if (city.equals("��ݸ")) {
						url = "http://dongguan.1688.com/qiye.htm?keywords=";
					}
					BusinessesInfoUtil
							.infoQiyeUrl(url, city + ";�����̼�", pageNum);
					logger.debug(city + ";�����̼���ȡ�������");
				} else {
					String url = "http://nanshan.1688.com/qiye.htm";// Ĭ����ɽ
					if (city.equals("�޺�")) {
						url = "http://luohu.1688.com/qiye.htm";
					} else if (city.equals("����")) {
						url = "http://futian.1688.com/qiye.htm";
					} else if (city.equals("����")) {
						url = "http://baoan.1688.com/qiye.htm";
					} else if (city.equals("����")) {
						url = "http://longgang.1688.com/qiye.htm";
					} else if (city.equals("Խ��")) {
						url = "http://yuexiu.1688.com/qiye.htm";
					} else if (city.equals("����")) {
						url = "http://haizhu.1688.com/qiye.htm";
					} else if (city.equals("����")) {
						url = "http://liwan.1688.com/qiye.htm";
					} else if (city.equals("���")) {
						url = "http://tianhe.1688.com/qiye.htm";
					} else if (city.equals("����")) {
						url = "http://baiyun.1688.com/qiye.htm";
					} else if (city.equals("����")) {
						url = "http://huadu.1688.com/qiye.htm";
					} else if (city.equals("��خ")) {
						url = "http://panyu.1688.com/qiye.htm";
					} else if (city.equals("����")) {
						url = "http://humen.1688.com/qiye.htm";
					} else if (city.equals("��ݸ")) {
						url = "http://dongguan.1688.com/qiye.htm";
					}
					String[] keywords = keyword.split(";");
					for (String str : keywords) {
						String url1 = url + "?keywords="
								+ JsoupUtil.urlEcode(str);
						BusinessesInfoUtil.infoQiyeUrl(url1, city + ";" + str,
								pageNum);
						logger.debug(city + ";" + str + "  ��ȡ�������");
					}
				}
				response.setAttributes("keyword", keyword);
				response.setAttributes("success", "1");
			}
		} catch (Exception e) {
			logger.error("ץȡ�̼���Ϣʧ��", e);
		}
		response.setForwardId("success");
	}
}
