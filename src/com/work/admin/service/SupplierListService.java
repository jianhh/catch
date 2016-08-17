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
 * ��Ӧ�̲�ѯҳ
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
		// ��������
		// ��ȡǰ̨�õ�������
		String name = request.getParameter("name");
		String shopName = request.getParameter("shopName");
		// ����״̬��1:δ����;2:�Ѹ���;3:�ѷ���;4:��ǩ��;5:�ѹر�;6:����������˿�;7:������ͬ���˿�;8:�������;9:�˿����;10:�����ܾ̾��˿�;11:ɾ������;12.��Ӧ��ͬ���˿�;13.��Ӧ�ܾ̾��˿�
		String orderState = request.getParameter("orderState");//
		String startTime = request.getParameter("startTime");// ��ʼʱ��
		String endTime = request.getParameter("endTime");// ����ʱ��
		String pageNo = request.getParameter("pageNo");
		String pageSize = request.getParameter("pageSize");
		String title = request.getParameter("title");// ���ݱ�������
		String totalrows = request.getParameter("totalRows");
		String totalpages = request.getParameter("totalPages");
		// �����ҳ��ʼֵ
		int page = 1;
		int pageSizes = 10;
		int prevPageNo = 1;
		int nextPageNo = 1;
		int totalPages = 1;
		int titleNo = 0;
		int totalRows = 0;
		/*
		 * //��Ӧ����������: String retentionG="0%"; //��������������: String retentionF="0%";
		 */
		// �ܶ�����:
		String allOrder = "0";
		// �����ܸ������:
		String allPayOrder = "0";
		// �����ܶ�����:
		String dayOrder = "0";
		// �����ܽ��:
		String dayMoney = "0";
		// �������˿���:
		String dayRefundOrder = "0";
		// �������˿����:
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
				shopName = new String(shopName.getBytes("ISO-8859-1"), "UTF-8");// ��������
				logger.debug("shopName1=" + shopName);
			} else {
				shopName = "";
			}
			if (StringUtils.isNotEmpty(startTime)) {
				startTime = URLDecoder.decode(
						request.getParameter("startTime"), "utf-8");// ��������
			} else {
				startTime = "";
			}
			if (StringUtils.isNotEmpty(endTime)) {
				endTime = URLDecoder.decode(request.getParameter("endTime"),
						"utf-8");// ��������
			} else {
				endTime = "";
			}
			adminList = bo.getAdminSchema();
			if (StringUtils.isNotEmpty(totalrows)) {
				totalRows = Integer.valueOf(totalrows);
				totalPages = Integer.valueOf(totalpages);
			} else {
				// ��ȡ����
				String totalRow = bo.countSupplierSort(shopName, name,
						startTime, endTime);
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
			} else {
				nextPageNo = page;
				if (page > 1) {
					prevPageNo = page - 1;
				}
				page = totalPages;
			}
			if (totalRows > 0) {
				// ��ҳ��ѯ�б�
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
						 * //�����̵Ĺ�Ӧ�̵������� int f_retention=0; int all_tarder=0;
						 * if(StringUtils.isNotEmpty(s.getActive_user())){
						 * f_retention=Integer.parseInt(s.getActive_user()); }
						 * if(StringUtils.isNotEmpty(s.getTrader_user())){
						 * all_tarder=Integer.parseInt(s.getTrader_user()); }
						 * String retention="0%";
						 * if(f_retention==0||all_tarder==0){ }else{
						 * retention=String.valueOf((f_retention*100/all_tarder))+"%"; }
						 * s.setF_retention(retention);
						 */
						// ����ת����
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
			// ��ӵ�һ��Ҫ��ʾ������
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
			 * // ������ж���״̬������ȥ����������������ֵ if (orderState != null &&
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
			 * //����shopid�õ�������Աtb_admin_shop.tb_admin c_name
			 * s.setAdmin(dao.getAdminSchemaByshopId(shop.getN_shop_id()));
			 * //��������c_shop_name s.setShop_name(shop.getC_shop_name());
			 * //��פʱ��t_join_tim s.setJoin_time(shop.getT_join_time());
			 * //��ʱ��`tb_wechat_public` t_bind_time
			 * s.setBind_time(dao.getBindTimeByshopId(shop.getN_shop_id()));
			 * //���������� ����shopid�ҵ�`tb_trader_mapping` n_open=1
			 * n_parent_shop_id=shopId
			 * s.setTrader_num(dao.getTraderNumByshopId(shop.getN_shop_id()));
			 * //��ѯ��������n_parent_shop_id=shopId
			 * s.setOrder_num(dao.getOrderNumByshopId(shop.getN_shop_id()));
			 * //��˿����`tb_mp_attention` n_shop_id
			 * s.setFans_num(dao.getFansNumByshopId(shop.getN_shop_id()));
			 * System.out.println("s:"+s.getShop_name()); supplier.add(s); }
			 */
			// �õ�����������Ա����Ϣ
			// �����ܸ������:
			allPayOrder = bo.countAllPayOrder();
			// �ܶ�����:
			allOrder = bo.countAllOrder();
			// ���ն�����:
			dayOrder = bo.countDayOrder();
			// �����ܽ��:
			dayMoney = bo.countDayMoney();
			// �������˿���:
			dayRefundOrder = bo.countDayRefundOrder();
			// �������˿����:
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
			logger.error("��Ӧ�̲�ѯҳ����:", e);
		}
		// �����洫ֵ
		response.setAttributes("adminlist", adminList);
		response.setAttributes("supplierList", supplier);
		// �ܶ�����:
		response.setAttributes("allOrder", allOrder);
		// �����ܸ������:
		response.setAttributes("allPayOrder", allPayOrder);
		// ���ն�����:
		response.setAttributes("dayOrder", dayOrder);
		// �����ܽ��:
		response.setAttributes("dayMoney", dayMoney);
		// ���ն�����:
		response.setAttributes("dayRefundOrder", dayRefundOrder);
		// �����ܽ��:
		response.setAttributes("dayRefundMoney", dayRefundMoney);
		// ��������
		response.setAttributes("name", name);
		response.setAttributes("shopName", shopName);
		response.setAttributes("orderState", orderState);
		response.setAttributes("startTime", startTime);
		response.setAttributes("endTime", endTime);
		// ��ҳ
		response.setAttributes("page", page);
		response.setAttributes("sort", title);
		response.setAttributes("prevPageNo", prevPageNo);
		response.setAttributes("nextPageNo", nextPageNo);
		response.setAttributes("totalPages", totalPages);
		response.setAttributes("totalRows", totalRows);
		// ������
		/*
		 * //��Ӧ����������: response.setAttributes("retentionG", retentionG);
		 * //��������������: response.setAttributes("retentionF", retentionF);
		 */
		response.setForwardId("success");

	}

}
