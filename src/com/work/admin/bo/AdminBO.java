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
 * �̼���Ϣbo
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
		return this.adminDao.getAdminByName(name);
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

		return this.adminDao.addAdmin(name);
	}

	/**
	 * �õ����̵�������Ա����ͨ��shopId
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
	 * ͳ�ƹ�Ӧ�̲�ѯ����(��ҳ)
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
	 * ��Ӧ�̲�ѯ����(��ҳ)
	 * 
	 * @param name
	 * @throws Exception
	 */
	public List<SupplierListInfo> getSupplierSort(String shopName, String name,
			String startTime, String endTime,int page,int pageSize,int title,String orderState) throws Exception {

	  return this.adminDao.getSupplierSort(shopName, name, startTime, endTime,page,pageSize,title,orderState);
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

		return this.adminDao.addShopAdmin(adminId, shopId);
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
		return this.adminDao.getOrderNumByshopId(orderState, shopId);
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

		return this.adminDao.getAdminSchema();
	}
	/**
	 * ͳ�Ʒ����̲�ѯ����(��ҳ)
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
	 * �����̲�ѯ����(��ҳ)
	 * 
	 * @param name
	 * @throws Exception
	 */
	public List<TraderListInfo> getTraderSort(String shopName, String startTime,
			String endTime, String shopId,String shopType,String supplierShopName,int page,int pageSize,int title) throws Exception {

		return this.adminDao.getTraderSort(shopName, startTime, endTime, shopId,shopType,supplierShopName,page,pageSize,title);

	}
	/**
	 * ��ȡshop��ali��url
	 * 
	 * @param shopId
	 * @throws Exception
	 */
	public String getShopAliUrl(String shopId) throws Exception {

		return this.adminDao.getShopAliUrl(shopId);

	}
	/**
	 * ��Ӧ�̽����ܶ�����
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
	 * ��Ӧ�̽����ܸ������
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
	 * ͳ���ж���������˵Ĺ�Ӧ�̵�����
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
	 * �����˿����
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
	 * ���ն�����
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
	 * ��Ӧ�̽��浱���ܽ��
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
	 * �����˿��ܽ��
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
	 * ��Ӧ�̽��湩Ӧ����������
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

		return this.adminDao.countRetentionF(type);
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

		return this.adminDao.countGRetentionF(shopId);
	}
	/**
	 * ͳ�ƶ����б�
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
	 * �����̲�ѯ����(��ҳ)
	 * 
	 * @param name
	 * @throws Exception
	 */
	public List<OrderListInfo> getOrderList(String shopName, String shopId,String shopType,int page,int pageSize,String state,String startTime,String endTime) throws Exception {

		return this.adminDao.getOrderList(shopName, shopId,shopType,page,pageSize,state,startTime,endTime);

	}
	/**
	 * ��Ӧ��ÿ����������
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
	 * ��Ӧ�̵ķ�����һ����4�ܵĻ�Ծ��
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
	 * ��Ӧ�̵ķ�����һ����4�ܵĵķ���������
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
	 * ͳ�ư��˹��ںŵ�������������/��Ӧ�̣�
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
	 * ��Ӧ��ÿ����������
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
	 * ��Ӧ��ÿ����������
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
	 * ͨ���û����������ѯ�û���Ϣ
	 * @param name
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public User getUserByNameAndPwd(String name,String password) throws Exception{			
		 return this.adminDao.getUserByNameAndPwd(name, password);		
	}
}
