package com.work.admin.service;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import com.aspire.dps.boprovide.exception.BOProvideException;
import com.aspire.dps.jvsc.Req;
import com.aspire.dps.jvsc.Resp;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.BaseListener;
import com.framework.util.StringUtils;
import com.work.admin.bo.AdminBO;
import com.work.admin.schema.WxScanSchema;

/**
 * 微信公众扫描
 * 
 * @author zhangwentao
 * 
 */
public class WxScanService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(WxScanService.class);

	public void doPerform(Req request, Resp response) throws BOProvideException {
		AdminBO bo = new AdminBO();
		String startTime = request.getParameter("startTime");// 开始时间
		String endTime = request.getParameter("endTime");// 结束时间
		List<WxScanSchema> wxScan=new ArrayList<WxScanSchema>();
		try {
			if (StringUtils.isNotEmpty(startTime)) {
				startTime = URLDecoder.decode(
						request.getParameter("startTime"), "utf-8");// 店铺名称
			} else {
				startTime = "";
			}
			if (StringUtils.isNotEmpty(endTime)) {
				endTime = URLDecoder.decode(request.getParameter("endTime"),
						"utf-8");// 店铺名称
			} else {
				endTime = "";
			}
			wxScan=bo.groupByScanScene(startTime, endTime);
			int i=0;
			for(WxScanSchema wSchema:wxScan){
				System.out.println(wSchema.toString()+i);
				i++;
			}
			if(wxScan.isEmpty()){
				wxScan=new ArrayList<WxScanSchema>();
			}
		} catch (Exception e) {
			logger.error("查询公众扫描错误:", e);
		}
		//搜索条件
		response.setAttributes("startTime", startTime);
		response.setAttributes("endTime", endTime);
		response.setAttributes("list", wxScan);
		response.setForwardId("success");

	}
	
}
