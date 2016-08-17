package com.work.admin.bo;

import java.util.List;

import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.page.Page;
import com.work.admin.dao.AdminDAO;
import com.work.admin.dao.AdminDAO.mysqltype;
import com.work.admin.schema.AdminSchema;
import com.work.admin.schema.User;
import com.work.admin.schema.OrderListInfo;
import com.work.admin.schema.ShopSerieSchema;
import com.work.admin.schema.SupplierListInfo;
import com.work.admin.schema.TraderListInfo;
import com.work.admin.schema.WxScanSchema;

/**
 * 商家信息bo
 * 
 * @author tangbiao
 * 
 */
public class AdminBO {

	static JLogger logger = LoggerFactory.getLogger(AdminBO.class);

	private AdminDAO adminDao;

	public AdminBO() {

		this.adminDao = AdminDAO.getInstance();
	}

	/**
	 * 通过商务人员得到数据
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 */
	public AdminSchema getAdminByName(String name) throws Exception {
		return this.adminDao.getAdminByName(name);
	}

	/**
	 * 增加商务人员
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean addAdmin(String name) throws Exception {

		return this.adminDao.addAdmin(name);
	}

	/**
	 * 得到店铺的商务人员名称通过shopId
	 * 
	 * 
	 * 
	 * @param tb_admin
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getAdminSchemaByshopId(String shopId) throws Exception {

		return this.adminDao.getAdminSchemaByshopId(shopId);
	}
	/**
	 * 统计供应商查询所有(翻页)
	 * 
	 * 
	 * 
	 * @param tb_admin
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countSupplierSort(String shopName, String name,
			String startTime, String endTime) throws Exception {

		return this.adminDao.getCountSupplier(shopName, name, startTime, endTime);
	}
	/**
	 * 供应商查询所有(翻页)
	 * 
	 * @param name
	 * @throws Exception
	 */
	public List<SupplierListInfo> getSupplierSort(String shopName, String name,
			String startTime, String endTime,int page,int pageSize,int title,String orderState) throws Exception {

	  return this.adminDao.getSupplierSort(shopName, name, startTime, endTime,page,pageSize,title,orderState);
	}

	/**
	 * 给供应商添加商务人员
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean addShopAdmin(String adminId, String shopId) throws Exception {

		return this.adminDao.addShopAdmin(adminId, shopId);
	}

	/**
	 * 得到订单数量
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
		return this.adminDao.getOrderNumByshopId(orderState, shopId);
	}

	/**
	 * 得到所有商务人员
	 * 
	 * 
	 * 
	 * @param tb_admin
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<AdminSchema> getAdminSchema() throws Exception {

		return this.adminDao.getAdminSchema();
	}
	/**
	 * 统计分销商查询所有(翻页)
	 * 
	 * 
	 * 
	 * @param tb_admin
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countTrader(String shopName, String startTime,
			String endTime, String shopId,String shopType,String supplierShopName) throws Exception {

		return this.adminDao.getCountTrader(shopName, startTime, endTime, shopId,shopType,supplierShopName);
	}
	/**
	 * 分销商查询所有(翻页)
	 * 
	 * @param name
	 * @throws Exception
	 */
	public List<TraderListInfo> getTraderSort(String shopName, String startTime,
			String endTime, String shopId,String shopType,String supplierShopName,int page,int pageSize,int title) throws Exception {

		return this.adminDao.getTraderSort(shopName, startTime, endTime, shopId,shopType,supplierShopName,page,pageSize,title);

	}
	/**
	 * 获取shop在ali的url
	 * 
	 * @param shopId
	 * @throws Exception
	 */
	public String getShopAliUrl(String shopId) throws Exception {

		return this.adminDao.getShopAliUrl(shopId);

	}
	/**
	 * 供应商界面总订单数
	 * 
	 * 
	 * 
	 * @param tb_admin
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countAllOrder() throws Exception {

		return this.adminDao.countAllOrder();
	}
	/**
	 * 供应商界面总付款订单数
	 * 
	 * 
	 * 
	 * @param tb_admin
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countAllPayOrder() throws Exception {

		return this.adminDao.countAllPayOrder();
	}
	/**
	 * 统计有多少商务绑定了的供应商的数量
	 * 
	 * 
	 * 
	 * @param tb_admin
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countAdminSupplier() throws Exception {

		return this.adminDao.countAdminSupplier();
	}
	/**
	 * 当日退款订单数
	 * 
	 * 
	 * 
	 * @param tb_admin
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countDayRefundOrder() throws Exception {

		return this.adminDao.countDayRefundOrder();
	}
	/**
	 * 
	 * 当日订单数
	 * 
	 * 
	 * @param tb_admin
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countDayOrder() throws Exception {

		return this.adminDao.countDayOrder();
	}
	/**
	 * 供应商界面当日总金额
	 * 
	 * 
	 * 
	 * @param tb_admin
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countDayMoney() throws Exception {

		return this.adminDao.countDayMoney();
	}
	/**
	 * 当日退款总金额
	 * 
	 * 
	 * 
	 * @param tb_admin
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countDayRefundMoney() throws Exception {

		return this.adminDao.countDayRefundMoney();
	}
	/**
	 * 供应商界面供应商周留存率
	 * 
	 * 
	 * 
	 * @param tb_admin
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countRetentionG(String type) throws Exception {

		return this.adminDao.countRetentionG(type);
	}
	/**
	 * 供应商界面分销商留存率
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countRetentionF(String type) throws Exception {

		return this.adminDao.countRetentionF(type);
	}
	/**
	 * 供应商界面供应商的分销商的留存率
	 * 
	 * @param
	 * 
	 * @param
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countGRetentionF(String shopId) throws Exception {

		return this.adminDao.countGRetentionF(shopId);
	}
	/**
	 * 统计订单列表
	 * 
	 * 
	 * 
	 * @param tb_admin
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countOrder(String shopName, String shopId,String shopType,String state,String startTime,String endTime) throws Exception {

		return this.adminDao.getCountOrder(shopName, shopId,shopType,state,startTime,endTime);
	}
	/**
	 * 分销商查询所有(翻页)
	 * 
	 * @param name
	 * @throws Exception
	 */
	public List<OrderListInfo> getOrderList(String shopName, String shopId,String shopType,int page,int pageSize,String state,String startTime,String endTime) throws Exception {

		return this.adminDao.getOrderList(shopName, shopId,shopType,page,pageSize,state,startTime,endTime);

	}
	/**
	 * 供应商每月日增长数
	 * 
	 * 
	 * 
	 * @param 
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ShopSerieSchema> countMDaySupplier(String type) throws Exception {

		return this.adminDao.countMDaySupplier(type);
	}
	/**
	 * 供应商的分销商一个月4周的活跃人
	 * 
	 * 
	 * 
	 * @param 
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countTraderActive(String type,String shopId) throws Exception {

		return this.adminDao.countTraderActive(type,shopId);
	}
	/**
	 * 供应商的分销商一个月4周的的分销商人数
	 * 
	 * 
	 * 
	 * @param 
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countAllTrader(String type,String shopId) throws Exception {

		return this.adminDao.countAllTrader(type,shopId);
	}
	/**
	 * 统计绑定了公众号的数量（分销商/供应商）
	 * 
	 * 
	 * 
	 * @param tb_admin
	 * 
	 * @return
	 * @throws Exception
	 */
	public String countBind(String type) throws Exception {

		return this.adminDao.countBind(type);
	}
	/**
	 * 供应商每月日增长数
	 * 
	 * 
	 * 
	 * @param 
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ShopSerieSchema> countOpenAccount() throws Exception {

		return this.adminDao.countOpenAccount();
	}
	/**
	 * 供应商每月日增长数
	 * 
	 * 
	 * 
	 * @param 
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<WxScanSchema> groupByScanScene(String startTime,String endTime) throws Exception {

		return this.adminDao.groupByScanScene(startTime,endTime);
	}
	
	/**
	 * 通过用户名和密码查询用户信息
	 * @param name
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public User getUserByNameAndPwd(String name,String password) throws Exception{			
		 return this.adminDao.getUserByNameAndPwd(name, password);		
	}
}
