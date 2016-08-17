package com.work.admin.service;

import java.math.BigDecimal;
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
import com.work.admin.content.AdminContent;
import com.work.admin.schema.TraderListInfo;

/**
 * 分销商查询页
 * 
 * @author zhangwentao
 * 
 */
public class TraderListService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(TraderListService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		AdminBO bo = new AdminBO();
		List<TraderListInfo> trader = new ArrayList<TraderListInfo>();
		String shopId = request.getParameter("shopId");
		String shopName = request.getParameter("shopName");
		String startTime = request.getParameter("startTime");// 开始时间
		String endTime = request.getParameter("endTime");// 结束时间
		String shopType = request.getParameter("shopType");
		String supplierShopName = request.getParameter("supplierShopName");
		String totalrows = request.getParameter("totalRows");
		String title = request.getParameter("title");
		// 设置页面位置和页面条数大小
		String pageNo = request.getParameter("pageNo");
		String pageSize = request.getParameter("pageSize");
		String supplierUrl = "";// 供应商阿里网址

		// 定义分页初始值
		int page = 1;
		int pageSizes = 10;
		int prevPageNo = 1;
		int nextPageNo = 1;
		int totalPages = 1;
		int totalRows = 0;
		int titleNo = 0;
		if (StringUtils.isNotEmpty(pageNo)) {
			page = Integer.valueOf(pageNo);
		}
		if (StringUtils.isNotEmpty(pageSize)) {
			pageSizes = Integer.valueOf(pageSize);
		}
		if (StringUtils.isEmpty(shopType)) {
			shopType = "";
		}
		if (StringUtils.isEmpty(title)) {
			title = "0";
		}
		// 供应商绑定公众号
		String supplierBind = "0";
		// 分销商绑定公众号
		String traderBind = "0";
		// 统计有多少商务绑定了的供应商的数量
		String adminSupplier = "0%";
		try {
			// 翻页容易乱码所以进行了加密转码
			if (StringUtils.isNotEmpty(shopName) && !shopName.equals("%")) {
				// shopName =
				// URLDecoder.decode(request.getParameter("shopName"),
				// "utf-8");// 店铺名称
				shopName = new String(shopName.getBytes("ISO-8859-1"), "UTF-8");// 店铺名称
			}
			if (StringUtils.isNotEmpty(supplierShopName)
					&& !supplierShopName.equals("%")) {
				// supplierShopName = URLDecoder.decode(request
				// .getParameter("supplierShopName"), "utf-8");// 店铺名称
				supplierShopName = new String(supplierShopName
						.getBytes("ISO-8859-1"), "UTF-8");// 店铺名称
			}
			if (StringUtils.isNotEmpty(startTime)) {
				startTime = URLDecoder.decode(
						request.getParameter("startTime"), "utf-8");// 店铺名称
			} else {
				startTime = "";
			}
			if (StringUtils.isNotEmpty(title)) {
				titleNo = Integer.valueOf(title);
			}
			if (StringUtils.isNotEmpty(endTime)) {
				endTime = URLDecoder.decode(request.getParameter("endTime"),
						"utf-8");// 店铺名称
			} else {
				endTime = "";
			}
			if (StringUtils.isEmpty(shopName) || shopName.equals("%")) {
				shopName = "";
			}
			if (StringUtils.isEmpty(supplierShopName)) {
				supplierShopName = "";
			}
			// 这里是通过供销商的id查分销商的sql
			if (StringUtils.isEmpty(shopId)) {
				shopId = "";
			}
			/*
			 * if (StringUtils.isEmpty(shopName)) { if
			 * (StringUtils.isNotEmpty(shopId)) {
			 * response.setAttributes("shopId", shopId); } else { shopId = "";
			 * response.setAttributes("shopId", shopId); } }
			 */
			if (StringUtils.isNotEmpty(totalrows)) {
				totalRows = Integer.valueOf(totalrows);
				totalPages = (totalRows + pageSizes - 1) / pageSizes;
			} else {
				// 获取数量
				String totalRow = bo.countTrader(shopName, startTime, endTime,
						shopId, shopType, supplierShopName);
				// 分页设置
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
			// 根据sql去得到数据
			trader = bo.getTraderSort(shopName, startTime, endTime, shopId,
					shopType, supplierShopName, page, pageSizes, titleNo);
			// 界面查询条件
			if (StringUtils.isNotEmpty(shopId)) {
				supplierUrl = bo.getShopAliUrl(shopId);
				if (StringUtils.isEmpty(supplierUrl)) {
					supplierUrl = "";
				}
			}
			// 供应商绑定公众号
			supplierBind = bo.countBind("1");
			// 分销商绑定公众号
			traderBind = bo.countBind("3");
			// 统计有多少商务绑定了的供应商的数量
			adminSupplier = bo.countAdminSupplier();
			if (!adminSupplier.equals("0")) {
				double rate = 0;
				// 有商务人员供应商数量
				int a = Integer.parseInt(adminSupplier);
				// 总供应商数量
				int b = Integer.parseInt(bo.countSupplierSort(null, null, null,
						null));
				adminSupplier = String.valueOf((b - a) * 1.0 / b * 100);
				BigDecimal bg = new BigDecimal(adminSupplier);
				rate = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				adminSupplier = String.valueOf(rate) + "%";
				if (adminSupplier.equals("0.0%")) {
					adminSupplier = "0%";
				}
			} else {
				adminSupplier = "0%";
			}
		} catch (Exception e) {
			logger.error("分销商查询页错误:", e);

		}
		response.setAttributes("traderList", trader);
		// 供应商绑定数:
		response.setAttributes("supplierBind", supplierBind);
		// 分销商绑定数:
		response.setAttributes("traderBind", traderBind);
		// 供应商自增长率:
		response.setAttributes("adminSupplier", adminSupplier);
		// 搜索条件
		response.setAttributes("shopName", shopName);
		response.setAttributes("startTime", startTime);
		response.setAttributes("endTime", endTime);
		response.setAttributes("shopType", shopType);
		response.setAttributes("supplierShopName", supplierShopName);
		response.setAttributes("shopId", shopId);
		// 分页
		response.setAttributes("page", page);
		response.setAttributes("sort", title);
		response.setAttributes("prevPageNo", prevPageNo);
		response.setAttributes("nextPageNo", nextPageNo);
		response.setAttributes("totalPages", totalPages);
		response.setAttributes("totalRows", totalRows);
		// 单独的参数
		response.setAttributes("supplierUrl", supplierUrl);
		response.setAttributes("supplierWeUrl", AdminContent.SHOP_URL + shopId);
		response.setForwardId("success");

	}

}
