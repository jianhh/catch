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
import com.work.admin.schema.AdminSchema;
import com.work.admin.schema.SupplierListInfo;

/**
 * 供应商查询页
 * 
 * @author zhangwentao
 * 
 */
public class SupplierListService extends BaseListener {

	private static JLogger logger = LoggerFactory
			.getLogger(SupplierListService.class);

	@Override
	public void doPerform(Req request, Resp response) throws BOProvideException {
		AdminBO bo = new AdminBO();
		List<SupplierListInfo> supplier = new ArrayList<SupplierListInfo>();
		List<AdminSchema> adminList = null;
		// 条件参数
		// 获取前台得到的数据
		String name = request.getParameter("name");
		String shopName = request.getParameter("shopName");
		// 订单状态：1:未付款;2:已付款;3:已发货;4:已签收;5:已关闭;6:申请待发货退款;7:分销商同意退款;8:交易完成;9:退款完成;10:分销商拒绝退款;11:删除订单;12.供应商同意退款;13.供应商拒绝退款
		String orderState = request.getParameter("orderState");//
		String startTime = request.getParameter("startTime");// 开始时间
		String endTime = request.getParameter("endTime");// 结束时间
		String pageNo = request.getParameter("pageNo");
		String pageSize = request.getParameter("pageSize");
		String title = request.getParameter("title");// 根据标题排序
		String totalrows = request.getParameter("totalRows");
		String totalpages = request.getParameter("totalPages");
		// 定义分页初始值
		int page = 1;
		int pageSizes = 10;
		int prevPageNo = 1;
		int nextPageNo = 1;
		int totalPages = 1;
		int titleNo = 0;
		int totalRows = 0;
		/*
		 * //供应商周留存率: String retentionG="0%"; //分销商周留存率: String retentionF="0%";
		 */
		// 总订单数:
		String allOrder = "0";
		// 当日总付款订单数:
		String allPayOrder = "0";
		// 当日总订单数:
		String dayOrder = "0";
		// 当日总金额:
		String dayMoney = "0";
		// 当日总退款金额:
		String dayRefundOrder = "0";
		// 当日总退款订单数:
		String dayRefundMoney = "0";
		if (StringUtils.isNotEmpty(pageNo)) {
			page = Integer.valueOf(pageNo);
		}
		if (StringUtils.isNotEmpty(pageSize)) {
			pageSizes = Integer.valueOf(pageSize);
		}
		if (StringUtils.isNotEmpty(title)) {
			titleNo = Integer.valueOf(title);
		}
		if (StringUtils.isEmpty(name)) {
			name = "";
		}
		if (StringUtils.isEmpty(orderState)) {
			orderState = "0";
		}
		if (StringUtils.isEmpty(title)) {
			title = "6";
		}
		try {
			if (StringUtils.isNotEmpty(shopName)) {
				logger.debug("shopName=" + shopName);
				shopName = new String(shopName.getBytes("ISO-8859-1"), "UTF-8");// 店铺名称
				logger.debug("shopName1=" + shopName);
			} else {
				shopName = "";
			}
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
			adminList = bo.getAdminSchema();
			if (StringUtils.isNotEmpty(totalrows)) {
				totalRows = Integer.valueOf(totalrows);
				totalPages = Integer.valueOf(totalpages);
			} else {
				// 获取数量
				String totalRow = bo.countSupplierSort(shopName, name,
						startTime, endTime);
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
			} else {
				nextPageNo = page;
				if (page > 1) {
					prevPageNo = page - 1;
				}
				page = totalPages;
			}
			if (totalRows > 0) {
				// 分页查询列表
				supplier = bo.getSupplierSort(shopName, name, startTime,
						endTime, page, pageSizes, titleNo, orderState);
				if (supplier.size() > 0) {
					for (SupplierListInfo s : supplier) {
						String rate = "0%";
						int a = 0;
						int b = 0;
						if (orderState.equals("1")) {
							a = Integer.valueOf(s.getMorder_pay());
							b = Integer.valueOf(s.getMorder_num());
						}
						/*
						 * //分销商的供应商的留存率 int f_retention=0; int all_tarder=0;
						 * if(StringUtils.isNotEmpty(s.getActive_user())){
						 * f_retention=Integer.parseInt(s.getActive_user()); }
						 * if(StringUtils.isNotEmpty(s.getTrader_user())){
						 * all_tarder=Integer.parseInt(s.getTrader_user()); }
						 * String retention="0%";
						 * if(f_retention==0||all_tarder==0){ }else{
						 * retention=String.valueOf((f_retention*100/all_tarder))+"%"; }
						 * s.setF_retention(retention);
						 */
						// 订单转换率
						else {
							a = Integer.valueOf(s.getOrder_pay());
							b = Integer.valueOf(s.getOrder_generation());
						}
						double conversion_rate = 0;
						if (b != 0) {
							conversion_rate = a * 1.0 / b * 100;
							BigDecimal bg = new BigDecimal(conversion_rate);
							conversion_rate = bg.setScale(2,
									BigDecimal.ROUND_HALF_UP).doubleValue();
							rate = String.valueOf(conversion_rate) + "%";
						}
						if (rate.equals("0.0%")) {
							rate = "0%";
						}
						s.setConversion_rate(rate);
					}
				}
			}
			// 添加的一行要显示的数据
			/*
			 * DecimalFormat df = new DecimalFormat("0.00");
			 * retentionG=bo.countRetentionG(); retentionG =
			 * df.format(Float.parseFloat(retentionG)*100)+"%";
			 * retentionF=bo.countRetentionF();
			 * retentionF=df.format(Float.parseFloat(retentionF)*100)+"%";
			 * if(retentionF.equals("0.0%")||retentionF.equals("1")){
			 * retentionF="0%"; }
			 */

			/*
			 * // 如果是有订单状态，这里去单独给订单数量赋值 if (orderState != null &&
			 * !orderState.isEmpty()) { for (SupplierListInfo s : shopList) { //
			 * dao.getSupplierSort(page,sql);
			 * s.setOrder_num(bo.getOrderNumByshopId(orderState, s
			 * .getShop_id())); supplier.add(s); } page.setPageInfo(supplier); }
			 */
			// List<SupplierListInfo> supplier=new
			// ArrayList<SupplierListInfo>();
			/*
			 * for(ShopSchema shop:shopList){ SupplierListInfo s=new
			 * SupplierListInfo(); s.setShop_id(shop.getN_shop_id());
			 * //根据shopid得到商务人员tb_admin_shop.tb_admin c_name
			 * s.setAdmin(dao.getAdminSchemaByshopId(shop.getN_shop_id()));
			 * //店铺名字c_shop_name s.setShop_name(shop.getC_shop_name());
			 * //入驻时间t_join_tim s.setJoin_time(shop.getT_join_time());
			 * //绑定时间`tb_wechat_public` t_bind_time
			 * s.setBind_time(dao.getBindTimeByshopId(shop.getN_shop_id()));
			 * //分销商数量 根据shopid找到`tb_trader_mapping` n_open=1
			 * n_parent_shop_id=shopId
			 * s.setTrader_num(dao.getTraderNumByshopId(shop.getN_shop_id()));
			 * //查询订单数量n_parent_shop_id=shopId
			 * s.setOrder_num(dao.getOrderNumByshopId(shop.getN_shop_id()));
			 * //粉丝数量`tb_mp_attention` n_shop_id
			 * s.setFans_num(dao.getFansNumByshopId(shop.getN_shop_id()));
			 * System.out.println("s:"+s.getShop_name()); supplier.add(s); }
			 */
			// 得到所有商务人员的信息
			// 今日总付款订单数:
			allPayOrder = bo.countAllPayOrder();
			// 总订单数:
			allOrder = bo.countAllOrder();
			// 当日订单数:
			dayOrder = bo.countDayOrder();
			// 当日总金额:
			dayMoney = bo.countDayMoney();
			// 当日总退款金额:
			dayRefundOrder = bo.countDayRefundOrder();
			// 当日总退款订单数:
			dayRefundMoney = bo.countDayRefundMoney();
			if (StringUtils.isEmpty(allOrder)) {
				allOrder = "0";
			}
			if (StringUtils.isEmpty(dayOrder)) {
				dayOrder = "0";
			}
			if (StringUtils.isEmpty(allPayOrder)) {
				allPayOrder = "0";
			}
			if (StringUtils.isEmpty(dayMoney)) {
				dayMoney = "0";
			}
			if (StringUtils.isEmpty(dayRefundOrder)) {
				dayRefundOrder = "0";
			}
			if (StringUtils.isEmpty(dayRefundMoney)) {
				dayRefundMoney = "0";
			}
			if (!dayMoney.equals("0")) {
				double aa = Double.valueOf(dayMoney).doubleValue();
				double all_apply_fee = aa / 100;
				dayMoney = String.valueOf(all_apply_fee);
			}
			if (!dayRefundMoney.equals("0")) {
				double aa = Double.valueOf(dayRefundMoney).doubleValue();
				double all_apply_fee = aa / 100;
				dayRefundMoney = String.valueOf(all_apply_fee);
			}
		} catch (Exception e) {
			logger.error("供应商查询页错误:", e);
		}
		// 给界面传值
		response.setAttributes("adminlist", adminList);
		response.setAttributes("supplierList", supplier);
		// 总订单数:
		response.setAttributes("allOrder", allOrder);
		// 今日总付款订单数:
		response.setAttributes("allPayOrder", allPayOrder);
		// 当日订单数:
		response.setAttributes("dayOrder", dayOrder);
		// 当日总金额:
		response.setAttributes("dayMoney", dayMoney);
		// 当日订单数:
		response.setAttributes("dayRefundOrder", dayRefundOrder);
		// 当日总金额:
		response.setAttributes("dayRefundMoney", dayRefundMoney);
		// 搜索条件
		response.setAttributes("name", name);
		response.setAttributes("shopName", shopName);
		response.setAttributes("orderState", orderState);
		response.setAttributes("startTime", startTime);
		response.setAttributes("endTime", endTime);
		// 分页
		response.setAttributes("page", page);
		response.setAttributes("sort", title);
		response.setAttributes("prevPageNo", prevPageNo);
		response.setAttributes("nextPageNo", nextPageNo);
		response.setAttributes("totalPages", totalPages);
		response.setAttributes("totalRows", totalRows);
		// 新增的
		/*
		 * //供应商周留存率: response.setAttributes("retentionG", retentionG);
		 * //分销商周留存率: response.setAttributes("retentionF", retentionF);
		 */
		response.setForwardId("success");

	}

}
