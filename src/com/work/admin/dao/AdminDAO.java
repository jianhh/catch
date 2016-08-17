package com.work.admin.dao;

import java.util.List;

import com.common.db.SQLCode;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.QueryTemplate;
import com.framework.page.Page;
import com.framework.util.StringUtils;
import com.work.admin.schema.AdminSchema;
import com.work.admin.schema.OrderListInfo;
import com.work.admin.schema.ShopSerieSchema;
import com.work.admin.schema.SupplierListInfo;
import com.work.admin.schema.TraderListInfo;
import com.work.admin.schema.User;
import com.work.admin.schema.WechatPublicSchema;
import com.work.admin.schema.WxScanSchema;
import com.work.commodity.schema.GoodsSchema;

/**
 * �̼���Ʒ��Ϣdao
 * 
 * @author tangbiao
 * 
 */
public class AdminDAO extends QueryTemplate {

	static JLogger logger = LoggerFactory.getLogger(AdminDAO.class);

	private static AdminDAO instance = new AdminDAO();

	public static AdminDAO getInstance() {

		return instance;
	}

	public enum mysqltype {
		byName, getAdminByName, addAdmin, getAdminSchema, getAdminSchemaByshopId, getBindTimeByshopId, getTraderNumByshopId, getOrderNumByshopId, getFansNumByshopId, addShopAdmin, countSupplier, getSupplierSort, getSupplier, getTraderSort, getCountTrader, countSupplierSort, countAllOrder, countDayOrder, countAllPayOrder, countDayMoney, countRetentionG, countRetentionF, countGRetentionF, getOrderList, getCountOrder, countAdminSupplier, countMDaySupplier, countBind, countOpenAccount, getShopAliUrl, groupByScanScene, countDayRefundMoney, countDayRefundOrder

	}

	// ��sql
	private String getSQLById(mysqltype type) throws Exception {

		return SQLCode.getInstance().getSQLStatement(
				"com.work.admin.dao.AdminDAO." + type.toString());
	}

	/**
	 * �������Ʋ�ѯ����(��ҳ)
	 * 
	 * @param name
	 * @throws Exception
	 */
	public void getVideoSort(Page page, String name) throws Exception {

		String sql = this.getSQLById(mysqltype.byName);
		sql = sql + " where c_goods_name like '%" + name + "%'";

		page.excuteWithCache(sql, null, GoodsSchema.class, null);

	}

	/**
	 * ͨ��������Ա�õ�����
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 */
	public AdminSchema getAdminByName(String name) throws Exception {

		String sql = this.getSQLById(mysqltype.getAdminByName);
		Object[] paras = { name };
		return this.queryObjectBySql(sql, AdminSchema.class, paras);
	}

	/**
	 * ����������Ա
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean addAdmin(String name) throws Exception {

		String sql = this.getSQLById(mysqltype.addAdmin);
		Object[] paras = { name };
		return this.executeBySql(sql, paras);
	}

	/**
	 * �õ�����������Ա
	 * 
	 * 
	 * 
	 * @param tb_admin
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<AdminSchema> getAdminSchema() throws Exception {
		String sql = this.getSQLById(mysqltype.getAdminSchema);
		return this.queryListBySql(sql, AdminSchema.class, null);
	}

	/**
	 * �õ����е���ͨ��sql
	 * 
	 * 
	 * 
	 * @param tb_admin
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<SupplierListInfo> getShopSchema(String sql) throws Exception {

		return this.queryListBySql(sql, SupplierListInfo.class, null);
	}

	/**
	 * �õ����̵�������Ա����ͨ��sql
	 * 
	 * 
	 * 
	 * @param tb_admin
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getAdminSchemaByshopId(String shopId) throws Exception {
		String sql = this.getSQLById(mysqltype.getAdminSchemaByshopId);
		Object[] paras = { shopId };
		AdminSchema admin = queryObjectBySql(sql, AdminSchema.class, paras);
		if (admin == null) {
			return "";
		} else {
			return admin.getC_name();
		}

	}

	/**
	 * �õ���ʱ��ͨ��sql
	 * 
	 * 
	 * 
	 * @param tb_wechat_info
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getBindTimeByshopId(String shopId) throws Exception {
		String sql = this.getSQLById(mysqltype.getBindTimeByshopId);
		Object[] paras = { shopId };
		WechatPublicSchema wechat = queryObjectBySql(sql,
				WechatPublicSchema.class, paras);
		if (wechat == null) {
			return "";
		} else {
			return wechat.getT_bind_time();
		}
	}

	/**
	 * �õ�����������
	 * 
	 * 
	 * 
	 * @param tb_order
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getTraderNumByshopId(String shopId) throws Exception {
		String sql = this.getSQLById(mysqltype.getTraderNumByshopId);
		Object[] paras = { shopId };
		return queryStrBySql(sql, paras);
	}

	/**
	 * �õ���������
	 * 
	 * 
	 * 
	 * @param tb_trade_mappong
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getOrderNumByshopId(String orderState, String shopId)
			throws Exception {
		String sql = this.getSQLById(mysqltype.getOrderNumByshopId);
		Object[] paras = { orderState, shopId };
		return queryStrBySql(sql, paras);
	}

	/**
	 * �õ���˿����
	 * 
	 * 
	 * 
	 * @param tb_admin
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getFansNumByshopId(String shopId) throws Exception {
		String sql = this.getSQLById(mysqltype.getFansNumByshopId);
		Object[] paras = { shopId };
		return queryStrBySql(sql, paras);
	}

	/**
	 * ����Ӧ�����������Ա
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean addShopAdmin(String adminId, String shopId) throws Exception {

		String sql = this.getSQLById(mysqltype.addShopAdmin);
		Object[] paras = { adminId, shopId };
		return this.executeBySql(sql, paras);
	}

	/**
	 * ͳ�ƹ�Ӧ�̲�ѯ����(��ҳ)
	 * 
	 * @param name
	 * @throws Exception
	 */
	public String getCountSupplier(String shopName, String name,
			String startTime, String endTime) throws Exception {
		String sql = this.getSQLById(mysqltype.countSupplier);
		if (StringUtils.isNotEmpty(shopName)) {
			sql += " and a.c_shop_name like '%" + shopName + "%'";
		}
		if (StringUtils.isNotEmpty(name)) {
			if (name.equals("0")) {// δ������ĵ���
				sql += " and NOT EXISTS (SELECT 1 FROM `tb_admin_shop` z WHERE a.`n_shop_id`=z.`n_shop_id`)  ";
			} else {
				sql += " and d.n_id=" + name;
			}
		}
		if (StringUtils.isNotEmpty(startTime)) {
			sql += " and UNIX_TIMESTAMP( a.t_join_time)>=" + "UNIX_TIMESTAMP('"
					+ startTime + "')";
		}
		if (StringUtils.isNotEmpty(endTime)) {
			sql += " and UNIX_TIMESTAMP( a.t_join_time)<=" + "UNIX_TIMESTAMP('"
					+ endTime + "')";
		}
		return this.queryStrBySql(sql, null);

	}

	/**
	 * ��Ӧ�̲�ѯ����(��ҳ)
	 * 
	 * @param name
	 * @throws Exception
	 */
	public List<SupplierListInfo> getSupplierSort(String shopName, String name,
			String startTime, String endTime, int page, int pageSize,
			int title, String orderState) throws Exception {
		// sqlĬ��Ϊ������
		String sql = this.getSQLById(mysqltype.getSupplier);
		// sqlΪȫ��ʱ
		if (orderState.equals("1")) {
			sql = this.getSQLById(mysqltype.getSupplierSort);
		}
		// ��ѯ����
		if (StringUtils.isNotEmpty(shopName)) {
			sql += " and a.c_shop_name like '%" + shopName + "%'";
		}
		if (StringUtils.isNotEmpty(name)) {
			if (name.equals("0")) {// δ������ĵ���
				sql += " and NOT EXISTS (SELECT 1 FROM `tb_admin_shop` z WHERE a.`n_shop_id`=z.`n_shop_id`)  ";
			} else {
				sql += " and d.n_id=" + name;
			}
		}
		if (StringUtils.isNotEmpty(startTime)) {
			sql += " and UNIX_TIMESTAMP( a.t_join_time)>=" + "UNIX_TIMESTAMP('"
					+ startTime + "')";
		}
		if (StringUtils.isNotEmpty(endTime)) {
			sql += " and UNIX_TIMESTAMP( a.t_join_time)<=" + "UNIX_TIMESTAMP('"
					+ endTime + "')";
		}
		sql += " group by a.n_shop_id";
		// ���ݱ���������
		if (title == 0) {
			sql += " order by order_num desc";
		} else if (title == 1) {
			sql += " order by order_generation desc";
		} else if (title == 2) {
			sql += " order by order_pay desc";
		} else if (title == 3) {
			sql += " order by order_delivery desc";
		} else if (title == 4) {
			sql += " order by order_complete desc";
		} else if (title == 5) {
			sql += " order by order_closed desc";
		} else if (title == 6) {
			sql += " order by order_num desc";
		} else if (title == 7) {
			sql += " order by fans_num desc";
		} else if (title == 8) {
			sql += " order by trader_num desc";
		} else if (title == 9) {
			sql += " order by examine_num desc";
		} else if (title == 10) {
			sql += "  order by join_time desc";
		} else if (title == 11) {// ��������ʱ������
			sql += "  order by create_time desc";
		} else {
			sql += " order by order_num desc";
		}
		// ��ҳ
		sql += " limit " + (page - 1) * pageSize + "," + pageSize;
		return this.queryListBySql(sql, SupplierListInfo.class, null);
	}

	/**
	 * �����̲�ѯ����(��ҳ)
	 * 
	 * @param name
	 * @throws Exception
	 */
	public String getCountTrader(String shopName, String startTime,
			String endTime, String shopId, String shopType,
			String supplierShopName) throws Exception {
		String sql = this.getSQLById(mysqltype.getCountTrader);
		// ���ݸ�����ƴ��sql
		if (StringUtils.isNotEmpty(shopName)) {
			sql += " and `b`.`c_shop_name` like '%" + shopName + "%'";
		}
		if (supplierShopName != null && !supplierShopName.isEmpty()) {
			sql += " and `c`.`c_shop_name` like '%" + supplierShopName + "%'";
		}
		if (StringUtils.isNotEmpty(startTime)) {
			sql += " and UNIX_TIMESTAMP(`a`.`t_apply_time`)>="
					+ "UNIX_TIMESTAMP('" + startTime + "')";
		}
		if (StringUtils.isNotEmpty(endTime)) {
			sql += " and UNIX_TIMESTAMP(`a`.`t_apply_time`)<="
					+ "UNIX_TIMESTAMP('" + endTime + "')";
		}
		if (StringUtils.isNotEmpty(shopType)) {
			sql += " and `a`.`n_open`=" + shopType;
		} else {
			sql += " and `a`.`n_open`=1";
		}
		if (StringUtils.isNotEmpty(shopId)) {
			sql = this.getSQLById(mysqltype.getCountTrader)
					+ " and `a`.`n_parent_shop_id`=" + shopId;
		}
		return this.queryStrBySql(sql, null);

	}

	/**
	 * �����̲�ѯ����(��ҳ)
	 * 
	 * @param name
	 * @throws Exception
	 */
	public List<TraderListInfo> getTraderSort(String shopName,
			String startTime, String endTime, String shopId, String shopType,
			String supplierShopName, int page, int pageSize, int title)
			throws Exception {
		String sql = this.getSQLById(mysqltype.getTraderSort);
		// ���ݸ�����ƴ��sql
		if (StringUtils.isNotEmpty(shopName)) {
			sql += " and `b`.`c_shop_name` like '%" + shopName + "%'";
		}
		if (StringUtils.isNotEmpty(supplierShopName)) {
			sql += " and `c`.`c_shop_name` like '%" + supplierShopName + "%'";
		}
		if (StringUtils.isNotEmpty(startTime)) {
			sql += " and UNIX_TIMESTAMP(`a`.`t_apply_time`)>="
					+ "UNIX_TIMESTAMP('" + startTime + "')";
		}
		if (StringUtils.isNotEmpty(endTime)) {
			sql += " and UNIX_TIMESTAMP(`a`.`t_apply_time`)<="
					+ "UNIX_TIMESTAMP('" + endTime + "')";
		}
		if (StringUtils.isNotEmpty(shopType)) {
			sql += " and `a`.`n_open`=" + shopType;
		} else {
			sql += " and `a`.`n_open`=1";
		}
		if (StringUtils.isNotEmpty(shopId)) {
			sql = this.getSQLById(mysqltype.getTraderSort)
					+ " and `a`.`n_parent_shop_id`=" + shopId;
		}
		// ���ݱ���������
		if (title == 1) {
			sql += " order by arrearage_num desc";
		} else if (title == 2) {
			sql += " order by pay_num desc";
		} else if (title == 3) {
			sql += " order by shipping_num desc";
		} else if (title == 4) {
			sql += " order by end_num desc";
		} else if (title == 5) {
			sql += " order by close_num desc";
		}
		// ��ҳ
		sql += " limit " + (page - 1) * pageSize + "," + pageSize;
		return this.queryListBySql(sql, TraderListInfo.class, null);

	}

	/**
	 * �õ���������
	 * 
	 * 
	 * 
	 * @param shopId
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getShopAliUrl(String shopId) throws Exception {
		String sql = this.getSQLById(mysqltype.getShopAliUrl);
		Object[] paras = { shopId };
		return queryStrBySql(sql, paras);
	}

	/**
	 * ��Ӧ�̽����ܶ�����
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countAllOrder() throws Exception {

		String sql = this.getSQLById(mysqltype.countAllOrder);
		return this.queryStrBySql(sql, null);
	}

	/**
	 * ͳ���ж���������˵Ĺ�Ӧ�̵�����
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countAdminSupplier() throws Exception {

		String sql = this.getSQLById(mysqltype.countAdminSupplier);
		return this.queryStrBySql(sql, null);
	}

	/**
	 * ��Ӧ�̽����ܸ������
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countAllPayOrder() throws Exception {

		String sql = this.getSQLById(mysqltype.countAllPayOrder);
		return this.queryStrBySql(sql, null);
	}

	/**
	 * ���ն�����
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countDayOrder() throws Exception {

		String sql = this.getSQLById(mysqltype.countDayOrder);
		return this.queryStrBySql(sql, null);
	}

	/**
	 * �����˿����
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countDayRefundOrder() throws Exception {

		String sql = this.getSQLById(mysqltype.countDayRefundOrder);
		return this.queryStrBySql(sql, null);
	}

	/**
	 * ��Ӧ�̽��浱���ܽ��
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countDayMoney() throws Exception {

		String sql = this.getSQLById(mysqltype.countDayMoney);
		return this.queryStrBySql(sql, null);
	}

	/**
	 * �����˿��ܽ��
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countDayRefundMoney() throws Exception {

		String sql = this.getSQLById(mysqltype.countDayRefundMoney);
		return this.queryStrBySql(sql, null);
	}

	/**
	 * ��Ӧ�̽��湩Ӧ����������
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countRetentionG(String type) throws Exception {
		int time = Integer.parseInt(type);// �ڼ���
		String sql = this.getSQLById(mysqltype.countRetentionG);
		sql = "SELECT COUNT(DISTINCT n_shop_id)/(SELECT COUNT(1) all_supplier FROM tb_shop WHERE n_shop_type=1) AS supplier FROM tb_order WHERE  DATE_SUB(CURDATE(), INTERVAL "
				+ type + " WEEK) <= t_create_time";
		if (time > 1) {//
			time--;
			sql += " < DATE_SUB(CURDATE(), INTERVAL " + String.valueOf(time)
					+ " WEEK)";
		}
		sql += " AND n_shop_id=n_parent_shop_id";
		return this.queryStrBySql(sql, null);
	}

	/**
	 * ��Ӧ�̽��������������
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countRetentionF(String type) throws Exception {
		int time = Integer.parseInt(type);// �ڼ���
		String sql = this.getSQLById(mysqltype.countRetentionF);
		sql = "SELECT COUNT(DISTINCT n_shop_id)/(SELECT COUNT(1) all_supplier FROM tb_shop WHERE n_shop_type=3) AS supplier FROM tb_order WHERE  DATE_SUB(CURDATE(), INTERVAL "
				+ type + " WEEK) <= t_create_time  ";
		if (time > 1) {//
			time--;
			sql += " < DATE_SUB(CURDATE(), INTERVAL " + String.valueOf(time)
					+ " WEEK)";
		}
		sql += " AND n_shop_id !=n_parent_shop_id";
		return this.queryStrBySql(sql, null);
	}

	/**
	 * ��Ӧ�̽��湩Ӧ�̵ķ����̵�������
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countGRetentionF(String shopId) throws Exception {

		String sql = this.getSQLById(mysqltype.countGRetentionF);
		Object[] paras = { shopId };
		return this.queryStrBySql(sql, paras);
	}

	/**
	 * �����б��ѯ
	 * 
	 * @param name
	 * @throws Exception
	 */
	public String getCountOrder(String shopName, String shopId,
			String shopType, String state, String startTime, String endTime)
			throws Exception {
		String sql = this.getSQLById(mysqltype.getCountOrder);
		// ���ݸ�����ƴ��sql
		if (StringUtils.isNotEmpty(shopName)) {
			sql += " and `b`.`c_shop_name` like '%" + shopName + "%'";
		}
		if (shopType.equals("0")) {
			sql += " and to_days(`a`.`t_create_time`) =to_days(now())";
		}
		if (StringUtils.isNotEmpty(state) && !state.equals("0")) {
			if (state.equals("2")) {
				sql += " and `a`.`n_order_state`=2 OR `a`.`n_order_state`=3 OR `a`.`n_order_state`=4 OR `a`.`n_order_state`=8 and a.n_order_refund_state!=6 and a.n_order_refund_state!=7";

			} else {
				sql += " and `a`.`n_order_state`=" + state;
			}
		}
		if (StringUtils.isNotEmpty(startTime)) {
			sql += " and UNIX_TIMESTAMP(`a`.`t_pay_time`)>="
					+ "UNIX_TIMESTAMP('" + startTime + "')";
		}
		if (StringUtils.isNotEmpty(endTime)) {
			sql += " and UNIX_TIMESTAMP(`a`.`t_pay_time`)<="
					+ "UNIX_TIMESTAMP('" + endTime + "')";
		}
		if (StringUtils.isNotEmpty(shopId)) {
			sql = this.getSQLById(mysqltype.getCountOrder)
					+ " and `a`.`n_parent_shop_id`=" + shopId;
		}
		return this.queryStrBySql(sql, null);

	}

	/**
	 * �����̲�ѯ����(��ҳ)
	 * 
	 * @param name
	 * @throws Exception
	 */
	public List<OrderListInfo> getOrderList(String shopName, String shopId,
			String shopType, int page, int pageSize, String state,
			String startTime, String endTime) throws Exception {
		String sql = this.getSQLById(mysqltype.getOrderList);
		// ���ݸ�����ƴ��sql
		if (StringUtils.isNotEmpty(shopName)) {
			sql += " and `b`.`c_shop_name` like '%" + shopName + "%'";
		}
		if (shopType.equals("0")) {
			sql += " and to_days(`a`.`t_create_time`) =to_days(now()) ";
		}
		if (StringUtils.isNotEmpty(startTime)) {
			sql += " and UNIX_TIMESTAMP(`a`.`t_pay_time`)>="
					+ "UNIX_TIMESTAMP('" + startTime + "')";
		}
		if (StringUtils.isNotEmpty(endTime)) {
			sql += " and UNIX_TIMESTAMP(`a`.`t_pay_time`)<="
					+ "UNIX_TIMESTAMP('" + endTime + "')";
		}
		if (StringUtils.isNotEmpty(state) && !state.equals("0")) {
			if (state.equals("2")) {
				sql += " and (`a`.`n_order_state`=2 OR `a`.`n_order_state`=3 OR `a`.`n_order_state`=4 OR `a`.`n_order_state`=8)  and a.n_order_refund_state!=6 and a.n_order_refund_state!=7";

			} else {
				sql += " and `a`.`n_order_state`=" + state;
			}

		}
		if (StringUtils.isNotEmpty(shopId)) {
			sql = this.getSQLById(mysqltype.getOrderList)
					+ " and `a`.`n_parent_shop_id`=" + shopId;
		}
		sql += " order by t_create_time desc,t_pay_time desc ,n_shop_name desc";
		// ��ҳ
		sql += " limit " + (page - 1) * pageSize + "," + pageSize;
		return this.queryListBySql(sql, OrderListInfo.class, null);

	}

	/**
	 * ��Ӧ��/������������
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ShopSerieSchema> countMDaySupplier(String type)
			throws Exception {

		String sql = this.getSQLById(mysqltype.countMDaySupplier);
		sql += " AND n_shop_type=" + type
				+ " GROUP BY DAY(t_join_time) ORDER BY t_join_time";
		return this.queryListBySql(sql, ShopSerieSchema.class, null);

	}

	/**
	 * ��Ӧ�̵ķ�����һ����4�ܵĻ�Ծ��
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countTraderActive(String type, String shopId)
			throws Exception {

		String sql = this.getSQLById(mysqltype.countMDaySupplier);
		sql = " SELECT COUNT(DISTINCT n_shop_id) AS supplier FROM tb_order WHERE  DATE_SUB(CURDATE(), INTERVAL "
				+ type
				+ " WEEK) <= t_create_time  AND n_shop_id != n_parent_shop_id AND n_parent_shop_id ="
				+ shopId;

		return this.queryStrBySql(sql, null);

	}

	/**
	 * ��Ӧ�̵ķ�����һ����4�ܵĵķ���������
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countAllTrader(String type, String shopId) throws Exception {

		String sql = this.getSQLById(mysqltype.countMDaySupplier);
		sql = " SELECT  COUNT(1)  FROM tb_trader_mapping  WHERE tb_trader_mapping.n_open = 1 AND  DATE_SUB(CURDATE(), INTERVAL "
				+ type
				+ " WEEK) <= t_apply_time  AND n_shop_id != n_parent_shop_id AND n_parent_shop_id ="
				+ shopId;

		return this.queryStrBySql(sql, null);

	}

	/**
	 * ��Ӧ�̵ķ�����һ����4�ܵĵķ���������
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countBind(String type) throws Exception {

		String sql = this.getSQLById(mysqltype.countBind);
		sql += "   AND b.`n_shop_type`=" + type;

		return this.queryStrBySql(sql, null);

	}

	/**
	 * �󶨹��ں�������
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ShopSerieSchema> countOpenAccount() throws Exception {

		String sql = this.getSQLById(mysqltype.countOpenAccount);
		return this.queryListBySql(sql, ShopSerieSchema.class, null);

	}

	/**
	 * ���ں�ɨ���ѯ
	 * 
	 * @param startTime
	 *            ��ʼʱ��
	 * 
	 * @param endTime
	 *            ����ʱ��
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<WxScanSchema> groupByScanScene(String startTime, String endTime)
			throws Exception {

		String sql = this.getSQLById(mysqltype.groupByScanScene);
		if (StringUtils.isNotEmpty(startTime)) {
			sql += "  and  UNIX_TIMESTAMP( c_scan_time)>=" + "UNIX_TIMESTAMP('"
					+ startTime + "')";
		}
		if (StringUtils.isNotEmpty(endTime)) {
			sql += " and UNIX_TIMESTAMP( c_scan_time)<=" + "UNIX_TIMESTAMP('"
					+ endTime + "')";
		}
		sql += " GROUP BY c_scan_scene";
		return this.queryListBySql(sql, WxScanSchema.class);
	}

	/**
	 * ͨ���û����������ѯ�û���Ϣ
	 * 
	 * @param name
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public User getUserByNameAndPwd(String name, String password)
			throws Exception {
		String sql = "SELECT * FROM tb_admin_user WHERE 1=1";
		if (StringUtils.isNotEmpty(name)) {
			sql += "  and  u_name = '" + name + "'";
		}
		if (StringUtils.isNotEmpty(password)) {
			sql += " and u_password = '" + password + "'";
		}
		return this.queryObjectBySql(sql, User.class, null);
	}

	/**
	 * ��ȡ��ƷID
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getGoodsId(boolean isTruedian) throws Exception {
		String sql = "select n_number from tb_id_goods_new";
		String delSql = "delete from tb_id_goods_new where 1=1";
		if (isTruedian) {
			sql += " where n_state=10";
			delSql += " and n_state=10";
		}
		sql += " limit 0,1";
		String goodsId = queryStrBySql(sql, null);
		delSql += " and n_number=" + goodsId;
		this.executeBySql(delSql, null);
		return goodsId;
	}
}
