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
import com.work.admin.schema.OrderListInfo;

/**
 * �����б����
 * 
 * @author zhangwentao
 * 
 */
public class OrderListService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(OrderListService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		AdminBO bo = new AdminBO();
		List<OrderListInfo> order = new ArrayList<OrderListInfo>();
		String shopId = request.getParameter("shopId");
		String shopName = request.getParameter("shopName");
		String orderState = request.getParameter("orderState");
		String state= request.getParameter("state");
		String totalrows = request.getParameter("totalRows");
		String startTime= request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		// ����ҳ��λ�ú�ҳ��������С
		String pageNo = request.getParameter("pageNo");
		String pageSize = request.getParameter("pageSize");

		// �����ҳ��ʼֵ
		int page = 1;
		int pageSizes = 10;
		int prevPageNo = 1;
		int nextPageNo = 1;
		int totalPages = 1;
		int totalRows = 0;
		//int titleNo=0;
		if (StringUtils.isNotEmpty(pageNo)) {
			page = Integer.valueOf(pageNo);
		}
		if (StringUtils.isNotEmpty(pageSize)) {
			pageSizes = Integer.valueOf(pageSize);
		}
		if (StringUtils.isEmpty(orderState)) {
			orderState = "0";
		}
		try {
			// ��ҳ�����������Խ����˼���ת��
			if (StringUtils.isNotEmpty(shopName)) {
				shopName = URLDecoder.decode(request.getParameter("shopName"),
						"utf-8");}// ��������
			if (StringUtils.isEmpty(shopName)) {
				shopName = "";
			}
			// ������ͨ�������̵�id������̵�sql
			if(StringUtils.isEmpty(shopId)){
				shopId = "";
			}
			if(StringUtils.isEmpty(startTime)){
				startTime="";
			}
			if(StringUtils.isEmpty(endTime)){
				endTime="";
			}
			if (StringUtils.isNotEmpty(totalrows)) {
				totalRows = Integer.valueOf(totalrows);
				totalPages = (totalRows + pageSizes - 1) / pageSizes;
			} else {
				// ��ȡ����
				String totalRow = bo.countOrder(shopName,shopId, orderState,state,startTime,endTime);
				// ��ҳ����
				if (StringUtils.isNotEmpty(totalRow)) {
					totalRows = Integer.valueOf(totalRow);
					totalPages = (totalRows + pageSizes - 1) / pageSizes;
				}
			}
			if (page > 1) {
				prevPageNo = page - 1;
			} else {
				page = 1;
			}
			if (page < totalPages) {
				nextPageNo = page + 1;
			} else if (page == totalPages) {
				nextPageNo = page;
				if (page > 1) {
					prevPageNo = page - 1;
				}
				page = totalPages;
			}
			// ����sqlȥ�õ�����
			if(totalRows>0){
				order = bo.getOrderList(shopName, shopId,orderState, page, pageSizes,state,startTime,endTime);
			}
			// �����ѯ����
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("�����б����:"+e.getMessage());

		}
		response.setAttributes("orderList", order);
		// ��������
		response.setAttributes("shopName", shopName);
		response.setAttributes("orderState", orderState);
		response.setAttributes("state", state);
		response.setAttributes("startTime", startTime);
		response.setAttributes("endTime", endTime);
		//
		// ��ҳ
		response.setAttributes("page", page);
		response.setAttributes("prevPageNo", prevPageNo);
		response.setAttributes("nextPageNo", nextPageNo);
		response.setAttributes("totalPages", totalPages);
		response.setAttributes("totalRows", totalRows);
		response.setForwardId("success");

   }
}