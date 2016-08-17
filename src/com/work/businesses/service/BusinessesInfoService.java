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
 * 抓取商家信息
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
				city = "南山";
			}
			if (StringUtils.isNotEmpty(keyword)) {
				if (keyword.equals("所有商家")) {
					String url = "http://nanshan.1688.com/qiye.htm?keywords=";// 默认南山
					if (city.equals("罗湖")) {
						url = "http://luohu.1688.com/qiye.htm?keywords=";
					} else if (city.equals("福田")) {
						url = "http://futian.1688.com/qiye.htm?keywords=";
					} else if (city.equals("宝安")) {
						url = "http://baoan.1688.com/qiye.htm?keywords=";
					} else if (city.equals("龙岗")) {
						url = "http://longgang.1688.com/qiye.htm?keywords=";
					} else if (city.equals("越秀")) {
						url = "http://yuexiu.1688.com/qiye.htm?keywords=";
					} else if (city.equals("海珠")) {
						url = "http://haizhu.1688.com/qiye.htm?keywords=";
					} else if (city.equals("荔湾")) {
						url = "http://liwan.1688.com/qiye.htm?keywords=";
					} else if (city.equals("天河")) {
						url = "http://tianhe.1688.com/qiye.htm?keywords=";
					} else if (city.equals("白云")) {
						url = "http://baiyun.1688.com/qiye.htm?keywords=";
					} else if (city.equals("花都")) {
						url = "http://huadu.1688.com/qiye.htm?keywords=";
					} else if (city.equals("番禺")) {
						url = "http://panyu.1688.com/qiye.htm?keywords=";
					} else if (city.equals("虎门")) {
						url = "http://humen.1688.com/qiye.htm?keywords=";
					} else if (city.equals("东莞")) {
						url = "http://dongguan.1688.com/qiye.htm?keywords=";
					}
					BusinessesInfoUtil
							.infoQiyeUrl(url, city + ";所有商家", pageNum);
					logger.debug(city + ";所有商家爬取数据完成");
				} else {
					String url = "http://nanshan.1688.com/qiye.htm";// 默认南山
					if (city.equals("罗湖")) {
						url = "http://luohu.1688.com/qiye.htm";
					} else if (city.equals("福田")) {
						url = "http://futian.1688.com/qiye.htm";
					} else if (city.equals("宝安")) {
						url = "http://baoan.1688.com/qiye.htm";
					} else if (city.equals("龙岗")) {
						url = "http://longgang.1688.com/qiye.htm";
					} else if (city.equals("越秀")) {
						url = "http://yuexiu.1688.com/qiye.htm";
					} else if (city.equals("海珠")) {
						url = "http://haizhu.1688.com/qiye.htm";
					} else if (city.equals("荔湾")) {
						url = "http://liwan.1688.com/qiye.htm";
					} else if (city.equals("天河")) {
						url = "http://tianhe.1688.com/qiye.htm";
					} else if (city.equals("白云")) {
						url = "http://baiyun.1688.com/qiye.htm";
					} else if (city.equals("花都")) {
						url = "http://huadu.1688.com/qiye.htm";
					} else if (city.equals("番禺")) {
						url = "http://panyu.1688.com/qiye.htm";
					} else if (city.equals("虎门")) {
						url = "http://humen.1688.com/qiye.htm";
					} else if (city.equals("东莞")) {
						url = "http://dongguan.1688.com/qiye.htm";
					}
					String[] keywords = keyword.split(";");
					for (String str : keywords) {
						String url1 = url + "?keywords="
								+ JsoupUtil.urlEcode(str);
						BusinessesInfoUtil.infoQiyeUrl(url1, city + ";" + str,
								pageNum);
						logger.debug(city + ";" + str + "  爬取数据完成");
					}
				}
				response.setAttributes("keyword", keyword);
				response.setAttributes("success", "1");
			}
		} catch (Exception e) {
			logger.error("抓取商家信息失败", e);
		}
		response.setForwardId("success");
	}
}
