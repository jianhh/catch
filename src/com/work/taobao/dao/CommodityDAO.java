package com.work.taobao.dao;

import java.util.List;

import com.common.db.SQLCode;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.QueryTemplate;
import com.framework.util.StringUtils;
import com.work.commodity.dao.CommodityDAO.mysqltype;
import com.work.commodity.schema.CatchRecordSchema;
import com.work.commodity.schema.CfgSysCategorySchema;
import com.work.commodity.schema.GoodsDefaultExpressSchema;
import com.work.commodity.schema.GoodsExtendsPropertySchema;
import com.work.commodity.schema.GoodsImageSchema;
import com.work.commodity.schema.GoodsInfoImageSchema;
import com.work.commodity.schema.GoodsPriceSectionSchema;
import com.work.commodity.schema.GoodsSchema;
import com.work.commodity.schema.GoodsSkuInfoSchema;
import com.work.commodity.schema.GoodsSkuListSchema;
import com.work.commodity.schema.GoodsSyncRecordSchema;
import com.work.commodity.schema.ShopAliSchema;
import com.work.commodity.schema.ShopCfgSchema;
import com.work.commodity.schema.ShopSchema;
import com.work.taobao.schema.ShopTaobaoSchema;
import com.work.taobao.schema.TaobaoCfgSysCategorySchema;
import com.work.taobao.table.Table;
import com.work.util.JsoupUtil;

/**
 * �̼���Ʒ��Ϣdao
 * 
 * @author tangbiao
 * 
 */
public class CommodityDAO extends QueryTemplate {

	static JLogger logger = LoggerFactory.getLogger(CommodityDAO.class);

	private static CommodityDAO instance = new CommodityDAO();

	public static CommodityDAO getInstance() {

		return instance;
	}

	public enum mysqltype {
		addShop, updateShopById, updateShopByUrl, updateShopByDesc, addGoods, addGoodsById, 
		updateGoods, updateGoodsSell, updateGoodsArtNo, updateGoodsName, deleteGoods, deleteGoodsById, 
		updateGoodsByLongPic, getGoodsByformId, getGoodsByshopId, getGoodsSkuListBygoodsId, 
		getGoodsSkuInfoBygoodsId, updateGoodsByPayType, addGoodsDefaultExpress, deleteGoodsDefaultExpress,
		deleteGoodsDefaultExpressById, addGoodsExtendsProperty, deleteGoodsExtendsProperty, 
		deleteGoodsExtendsPropertyById, addGoodsSkuList, addGoodsSkuInfo, updateGoodsSkuInfoById, 
		deleteGoodsSkuList, deleteGoodsSkuInfo, deleteGoodsSkuListById, deleteGoodsSkuInfoById,
		getGoodsSkuListBygId, addGoodsPriceSection, deleteGoodsPriceSection, deleteGoodsPriceSectionById,
		addGoodsImage, addGoodsInfoImage, getGoodsInfoImageByshopId, deleteGoodsImage,
		deleteGoodsImageById, deleteGoodsInfoImage, deleteGoodsInfoImageByUrl, deleteGoodsInfoImageById,
		addShopAli, addShopTaobao, updateShopTaobao, updateShopTaobaoById, updateShopAliById,
		addTaobaoCfgSysCategorySchema, addGoodsSyncRecord, addCatchRecord, deleteCatchRecord, 
		getCatchRecordByStatus, getShopTaobao,getShopCfgList
	}

	/**
	 * ��ѯ���̻�����Ϣ
	 * 
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	
	public ShopTaobaoSchema getShopTaobaoById(String shopId) throws Exception {

		return this.queryByIdWithCache(Table.TB_SHOP_TAOBAO, "n_shop_id", null,
				shopId, ShopTaobaoSchema.class);
	}

	/**
	 * ��ѯ������Ϣ
	 * 
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public ShopSchema getShopById(String shopId) throws Exception {

		return this.queryByIdWithCache(Table.TB_SHOP, "n_shop_id", null,
				shopId, ShopSchema.class);
	}

//	/**
//	 * �޸��̼���Ϣ
//	 * 
//	 * @param schema
//	 *            �̼���Ϣ
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean updateShopById(ShopSchema schema) throws Exception {
//
//		String sql = this.getSQLById(mysqltype.updateShopById);
//		Object[] paras = { schema.getC_shop_name(), schema.getC_company_name(),
//				schema.getN_shop_type(), schema.getC_location(),
//				schema.getC_contact_name(), schema.getC_contact_mobile(),
//				schema.getC_contact_phone(), schema.getN_shop_id() };
//		return this.executeBySql(sql, paras);
//	}

//	/**
//	 * �޸��̼���Ϣ
//	 * 
//	 * @param htmlUrl
//	 *            ������ҳ��ַ
//	 * @param shopId
//	 *            ����ID
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean updateShopByUrl(String shopId, String htmlUrl)
//			throws Exception {
//
//		String sql = this.getSQLById(mysqltype.updateShopByUrl);
//		Object[] paras = { htmlUrl, shopId };
//		return this.executeBySql(sql, paras);
//	}

//	/**
//	 * �޸��̼���Ϣ
//	 * 
//	 * @param desc
//	 *            ������Ϣ
//	 * @param shopId
//	 *            ����ID
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean updateShopByDesc(String shopId, String desc)
//			throws Exception {
//
//		String sql = this.getSQLById(mysqltype.updateShopByDesc);
//		Object[] paras = { desc, shopId };
//		return this.executeBySql(sql, paras);
//	}

//	/**
//	 * ���ݵ���id��ѯ
//	 * 
//	 * @param sellerid
//	 *            ����id
//	 * @return
//	 * @throws Exception
//	 */
//	public ShopSchema getShopByShopId(String sellerid) throws Exception {
//
//		return this.queryByIdWithCache(Table.TB_SHOP, "n_shop_id", null,
//				sellerid, ShopSchema.class);
//	}

//	/**
//	 * ���ݹ�˾���Ʋ�ѯ
//	 * 
//	 * @param companyName
//	 *            ��˾����
//	 * @return
//	 * @throws Exception
//	 */
//	public ShopSchema getShopByShopName(String companyName) throws Exception {
//
//		return this.queryByIdWithCache(Table.TB_SHOP, "c_company_name", null,
//				companyName, ShopSchema.class);
//	}

//	/**
//	 * ���ݵ������Ʋ�ѯ
//	 * 
//	 * @param cShopName
//	 *            ��������
//	 * @return
//	 * @throws Exception
//	 */
//	public ShopSchema getShopBycShopName(String cShopName) throws Exception {
//
//		return this.queryByIdWithCache(Table.TB_SHOP, "c_shop_name", null,
//				cShopName, ShopSchema.class);
//	}

	/**
	 * �����̼���Ʒ��Ϣ
	 * 
	 * @param schema
	 *            ��Ʒ��Ϣ
	 * @return
	 * @throws Exception
	 */
	public boolean addGoods(GoodsSchema schema) throws Exception {
		int n_total_sell = 200;// ת��Ϊ���������
		if (StringUtils.isNotEmpty(schema.getN_third_total_sell())) {
			int n_third_total_sell = Integer.parseInt(schema
					.getN_third_total_sell());// �Ա�����
			if (n_third_total_sell > 10000) {
				n_total_sell += 1000;
			} else {
				n_total_sell += n_third_total_sell * 0.1;
			}
		}
		schema.setN_total_sell(n_total_sell + "");
		
		if (StringUtils.isNotEmpty(schema.getN_id())) {
			String sql = this.getSQLById(mysqltype.addGoodsById);
			Object[] paras = { schema.getN_id(), schema.getN_goods_id(),
					schema.getN_goods_id(), schema.getC_third_platform_id(),
					schema.getN_third_platform_type(), schema.getN_shop_id(),
					schema.getN_shop_id(), schema.getN_sys_cid(),
					schema.getN_sys_parent_cid(), schema.getC_goods_name(),
					schema.getN_express_pay_type(),
					schema.getC_goods_detail_desc(),
					schema.getC_tp_order_url(), schema.getT_create_time(),
					schema.getT_last_update_time(), schema.getN_weight(),
					schema.getN_sell_price(), schema.getC_price(),
					schema.getC_goods_unit(), schema.getN_total_stock(),
					schema.getN_third_total_sell(),
					schema.getN_total_sell(), schema.getC_goods_state(),
					schema.getC_tp_goods_url(),schema.getC_art_no() };
			return this.executeBySql(sql, paras);
		} else {
			String sql = this.getSQLById(mysqltype.addGoods);
			Object[] paras = { schema.getN_goods_id(), schema.getN_goods_id(),
					schema.getC_third_platform_id(),
					schema.getN_third_platform_type(), schema.getN_shop_id(),
					schema.getN_shop_id(), schema.getN_sys_cid(),
					schema.getN_sys_parent_cid(), schema.getC_goods_name(),
					schema.getN_express_pay_type(),
					schema.getC_goods_detail_desc(),
					schema.getC_tp_order_url(), schema.getT_create_time(),
					schema.getT_last_update_time(), schema.getN_weight(),
					schema.getN_sell_price(), schema.getC_price(),
					schema.getC_goods_unit(), schema.getN_total_stock(),
					schema.getN_third_total_sell(),
					schema.getN_total_sell(), schema.getC_goods_state(),schema.getC_art_no()};
			return this.executeBySql(sql, paras);
		}

	}

	/**
	 * ɾ����Ʒ��Ϣ
	 * 
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoods(String shopId) throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoods);
		Object[] paras = { shopId };
		return this.executeBySql(sql, paras);
	}

	/**
	 * ɾ����Ʒ��Ϣ
	 * 
	 * @param goodsId
	 *            ��ƷID
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsById(String goodsId, String shopId)
			throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoodsById);
		Object[] paras = { goodsId, shopId };
		return this.executeBySql(sql, paras);
	}

//	/**
//	 * �޸��̼���Ʒ��Ϣ
//	 * 
//	 * @param goodsId
//	 *            ��ƷID
//	 * @param defaultPrice
//	 *            Ĭ�ϼ۸�
//	 * @param totalStock
//	 *            �ܿ��
//	 * @param price
//	 *            ����ҳչ�ֵļ۸�
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean updateGoods(String goodsId, String defaultPrice,
//			String totalStock, String price) throws Exception {
//
//		if (StringUtils.isEmpty(price)) {
//			price = JsoupUtil.priceYuan(defaultPrice);
//		}
//		int state = 1;
//		if (totalStock.equals("0")) {
//			state = 4;
//		}
//
//		String sql = this.getSQLById(mysqltype.updateGoods);
//		Object[] paras = { defaultPrice, totalStock, price, state, goodsId };
//		return this.executeBySql(sql, paras);
//	}

//	/**
//	 * �޸��̼���Ʒ��Ϣ
//	 * 
//	 * @param goodsId
//	 *            ��ƷID
//	 * @param thirdTotalSell
//	 *            ����
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean updateGoodsSell(String goodsId, String thirdTotalSell)
//			throws Exception {
//		String sql = this.getSQLById(mysqltype.updateGoodsSell);
//		Object[] paras = { thirdTotalSell, goodsId };
//		return this.executeBySql(sql, paras);
//	}

	/**
	 * �޸��̼���Ʒ��Ϣ
	 * 
	 * @param goodsId��ƷID
	 * @param artNo����
	 * @return
	 * @throws Exception
	 */
	public boolean updateGoodsArtNo(String goodsId, String artNo)
			throws Exception {
		String sql = this.getSQLById(mysqltype.updateGoodsArtNo);
		Object[] paras = { artNo, goodsId };
		return this.executeBySql(sql, paras);
	}

//	/**
//	 * �޸��̼���Ʒ��Ϣ
//	 * 
//	 * @param goodsId��ƷID
//	 * @param name��Ʒ����
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean updateGoodsName(String goodsId, String name, String weight)
//			throws Exception {
//		String sql = this.getSQLById(mysqltype.updateGoodsName);
//		Object[] paras = { name, weight, goodsId };
//		return this.executeBySql(sql, paras);
//	}

//	/**
//	 * �޸��̼���Ʒ��Ϣ(�˷ѳе���ʽ)
//	 * 
//	 * @param goodsId
//	 *            ��ƷID
//	 * @param payType
//	 *            �˷ѳе���ʽ��1���ң�2��ң� 3��ȡ
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean updateGoodsByPayType(String goodsId, String payType)
//			throws Exception {
//
//		String sql = this.getSQLById(mysqltype.updateGoodsByPayType);
//		Object[] paras = { payType, goodsId };
//		return this.executeBySql(sql, paras);
//	}

//	/**
//	 * ���ݵ���ID��ѯ
//	 * 
//	 * @param shopId
//	 *            ����ID
//	 * @return
//	 * @throws Exception
//	 */
//	public List<GoodsSchema> getGoodsByshopId(String shopId) throws Exception {
//		String sql = this.getSQLById(mysqltype.getGoodsByshopId);
//		String[] paras = new String[] { shopId };
//		return this.queryListBySql(sql, GoodsSchema.class, paras);
//	}

	/**
	 * ���ݵ���ID��ѯ
	 * 
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public GoodsSchema getGoodsSchemaByshopId(String shopId) throws Exception {
		String sql = this.getSQLById(mysqltype.getGoodsByshopId);
		String[] paras = new String[] { shopId };
		return this.queryObjectBySql(sql, GoodsSchema.class, paras);
	}

	/**
	 * ���ݵ�����ƽ̨��ƷID��ѯ
	 * 
	 * @param shopId
	 *            ����ID
	 * @param cThirdPlatformId
	 *            ������ƽ̨��ƷID
	 * @return
	 * @throws Exception
	 */
	public GoodsSchema getGoodsByformId(String shopId, String cThirdPlatformId)
			throws Exception {
		String sql = this.getSQLById(mysqltype.getGoodsByformId);
		String[] paras = new String[] { shopId, cThirdPlatformId };
		return this.queryObjectBySql(sql, GoodsSchema.class, paras);
	}

//	/**
//	 * ����Ĭ��������
//	 * 
//	 * @param schema
//	 *            Ĭ��������Ϣ
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean addGoodsDefaultExpress(GoodsDefaultExpressSchema schema)
//			throws Exception {
//
//		String sql = this.getSQLById(mysqltype.addGoodsDefaultExpress);
//		Object[] paras = { schema.getN_goods_id(),
//				schema.getC_delivery_location(),
//				schema.getC_receive_location(), schema.getN_price(),
//				schema.getN_shop_id() };
//		return this.executeBySql(sql, paras);
//	}

	/**
	 * ɾ��Ĭ��������Ϣ
	 * 
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsDefaultExpress(String shopId) throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoodsDefaultExpress);
		Object[] paras = { shopId };
		return this.executeBySql(sql, paras);
	}

	/**
	 * ɾ��Ĭ��������Ϣ
	 * 
	 * @param goodsId
	 *            ��ƷID
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsDefaultExpressById(String goodsId, String shopId)
			throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoodsDefaultExpressById);
		Object[] paras = { goodsId, shopId };
		return this.executeBySql(sql, paras);
	}

	/**
	 * ������Ʒ��չ����
	 * 
	 * @param schema
	 *            ��Ʒ��չ������Ϣ
	 * @return
	 * @throws Exception
	 */
	public boolean addGoodsExtendsProperty(GoodsExtendsPropertySchema schema)
			throws Exception {

		String sql = this.getSQLById(mysqltype.addGoodsExtendsProperty);
		Object[] paras = { schema.getN_goods_id(),
				schema.getC_prop_key_label(), schema.getC_prop_value(),
				schema.getN_shop_id() };
		return this.executeBySql(sql, paras);
	}

	/**
	 * ɾ����Ʒ��չ������Ϣ
	 * 
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsExtendsProperty(String shopId) throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoodsExtendsProperty);
		Object[] paras = { shopId };
		return this.executeBySql(sql, paras);
	}

	/**
	 * ɾ����Ʒ��չ������Ϣ
	 * 
	 * @param goodsId
	 *            ��ƷID
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsExtendsPropertyById(String goodsId, String shopId)
			throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoodsExtendsPropertyById);
		Object[] paras = { goodsId, shopId };
		return this.executeBySql(sql, paras);
	}

	/**
	 * ������ƷID��ѯ
	 * 
	 * @param goodsId
	 *            ��ƷID
	 * @return
	 * @throws Exception
	 */
	public List<GoodsSkuInfoSchema> getGoodsSkuInfoBygoodsId(String goodsId)
			throws Exception {
		String sql = this.getSQLById(mysqltype.getGoodsSkuInfoBygoodsId);
		String[] paras = new String[] { goodsId };
		return this.queryListBySql(sql, GoodsSkuInfoSchema.class, paras);
	}

	/**
	 * ������ƷSKU�б���Ϣ
	 * 
	 * @param schema
	 *            ��ƷSKU�б���Ϣ
	 * @return
	 * @throws Exception
	 */
	public boolean addGoodsSkuList(GoodsSkuListSchema schema) throws Exception {

		String sql = this.getSQLById(mysqltype.addGoodsSkuList);
		Object[] paras = { schema.getN_goods_id(), schema.getN_shop_id(),
				schema.getC_sku_name(), schema.getC_sku_prop(),
				schema.getN_sku_level(), schema.getC_sku_desc() };
		return this.executeBySql(sql, paras);
	}

	/**
	 * ������ƷSKU������Ϣ
	 * 
	 * @param schema
	 *            ��ƷSKU������Ϣ
	 * @return
	 * @throws Exception
	 */
	public boolean addGoodsSkuInfo(GoodsSkuInfoSchema schema) throws Exception {

		String sql = this.getSQLById(mysqltype.addGoodsSkuInfo);
		Object[] paras = { schema.getC_sku_id(), schema.getN_goods_id(),
				schema.getN_shop_id(), schema.getC_sku_list_id(),
				schema.getN_stock_num(), 0, schema.getN_price(),
				schema.getC_sku_desc(), schema.getN_price_set_flag() };
		return this.executeBySql(sql, paras);
	}

	/**
	 * �޸���ƷSKU������Ϣ
	 * 
	 * @param stockNum���
	 * @param price�۸�
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean updateGoodsSkuInfoById(String stockNum, String price,
			String id) throws Exception {

		String sql = this.getSQLById(mysqltype.updateGoodsSkuInfoById);
		Object[] paras = { stockNum, price, id };
		return this.executeBySql(sql, paras);
	}

	/**
	 * ɾ����ƷSKU�б���Ϣ
	 * 
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsSkuList(String shopId) throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoodsSkuList);
		Object[] paras = { shopId };
		return this.executeBySql(sql, paras);
	}

	/**
	 * ɾ����ƷSKU��ϸ��Ϣ
	 * 
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsSkuInfo(String shopId) throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoodsSkuInfo);
		Object[] paras = { shopId };
		return this.executeBySql(sql, paras);
	}

	/**
	 * ɾ����ƷSKU�б���Ϣ
	 * 
	 * @param goodsId
	 *            ��ƷID
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsSkuListById(String goodsId, String shopId)
			throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoodsSkuListById);
		Object[] paras = { goodsId, shopId };
		return this.executeBySql(sql, paras);
	}

	/**
	 * ɾ����ƷSKU��ϸ��Ϣ
	 * 
	 * @param goodsId
	 *            ��ƷID
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsSkuInfoById(String goodsId, String shopId)
			throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoodsSkuInfoById);
		Object[] paras = { goodsId, shopId };
		return this.executeBySql(sql, paras);
	}

	/**
	 * ����������ѯSKU�б���Ϣ
	 * 
	 * @param goodsId
	 *            ��ƷID
	 * @throws Exception
	 */
	public List<GoodsSkuListSchema> getGoodsSkuListBygId(String goodsId)
			throws Exception {

		String sql = this.getSQLById(mysqltype.getGoodsSkuListBygId);
		String[] paras = new String[] { goodsId };
		return this.queryListBySql(sql, GoodsSkuListSchema.class, paras);

	}

	/**
	 * ������Ʒ�۸�������Ϣ
	 * 
	 * @param schema
	 *            ��Ʒ�۸�������Ϣ
	 * @return
	 * @throws Exception
	 */
	public boolean addGoodsPriceSection(GoodsPriceSectionSchema schema)
			throws Exception {

		String sql = this.getSQLById(mysqltype.addGoodsPriceSection);
		Object[] paras = { schema.getN_goods_id(), schema.getN_num_start(),
				schema.getN_num_end(), schema.getN_price(),
				schema.getN_shop_id(), schema.getN_price_set_flag() };
		return this.executeBySql(sql, paras);
	}

	/**
	 * ɾ����Ʒ�۸�������Ϣ
	 * 
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsPriceSection(String shopId) throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoodsPriceSection);
		Object[] paras = { shopId };
		return this.executeBySql(sql, paras);
	}

	/**
	 * ɾ����Ʒ�۸�������Ϣ
	 * 
	 * @param goodsId
	 *            ��ƷID
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsPriceSectionById(String goodsId, String shopId)
			throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoodsPriceSectionById);
		Object[] paras = { goodsId, shopId };
		return this.executeBySql(sql, paras);
	}

	/**
	 * 
	 * ������ƷͼƬ��Ϣ
	 * 
	 * @param schema
	 *            ��ƷͼƬ��Ϣ
	 * @return
	 * @throws Exception
	 */
	public boolean addGoodsImage(GoodsImageSchema schema) throws Exception {

		String sql = this.getSQLById(mysqltype.addGoodsImage);
		Object[] paras = { schema.getN_goods_id(), schema.getN_img_category(),
				schema.getC_img_url(), schema.getN_width(),
				schema.getN_height(), schema.getN_shop_id() };
		return this.executeBySql(sql, paras);
	}

	/**
	 * ɾ����ƷͼƬ��Ϣ
	 * 
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsImage(String shopId) throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoodsImage);
		Object[] paras = { shopId };
		return this.executeBySql(sql, paras);
	}

	/**
	 * ɾ����ƷͼƬ��Ϣ
	 * 
	 * @param goodsId
	 *            ����ID
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsImageById(String goodsId, String shopId)
			throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoodsImageById);
		Object[] paras = { goodsId, shopId };
		return this.executeBySql(sql, paras);
	}

	/**
	 * �����̼һ�����Ϣ
	 * 
	 * @param shopId
	 *            ����ID
	 * @param url
	 *            ���̵�ַ
	 * @return
	 * @throws Exception
	 */
	public boolean addShopTaobao(String shopId, String url) throws Exception {
		ShopTaobaoSchema schema = this.getShopTaobaoById(shopId);
		if (schema == null) {
			String sql = this.getSQLById(mysqltype.addShopTaobao);
			Object[] paras = { shopId, url };
			return this.executeBySql(sql, paras);
		} else {
			return true;
//			String sql = this.getSQLById(mysqltype.updateShopTaobaoById);
//			Object[] paras = { url, shopId };
//			return this.executeBySql(sql, paras);
		}
	}

	/**
	 * �޸��̼һ�����Ϣ
	 * 
	 * @param schema
	 *            �̼һ�����Ϣ
	 * @return
	 * @throws Exception
	 */
	public boolean updateShopTaobao(ShopTaobaoSchema schema) throws Exception {

		String sql = this.getSQLById(mysqltype.updateShopTaobao);
		Object[] paras = { schema.getC_company_product(),
				schema.getC_company_industry(), schema.getC_bail_amount(),
				schema.getC_company_type(), schema.getC_credit_level(),
				schema.getC_evaluateInfo_disc(),
				schema.getC_evaluateInfo_disc_highgap(), schema.getC_evaluateInfo_send(),
				schema.getC_evaluateInfo_send_highgap(), schema.getC_evaluateInfo_taodu(),
				schema.getC_evaluateInfo_taodu_highgap(), schema.getC_fans_count(),
				schema.getC_good_rate_percentage(), schema.getC_pic_url(),
				schema.getC_taobao_shop_id(), schema.getC_shop_title(),
				schema.getC_starts(), schema.getC_user_nick(),
				schema.getC_user_num_id(), schema.getC_weitao_id(),
				schema.getN_shop_id() };
		return this.executeBySql(sql, paras);
	}

	/**
	 * 
	 * ������Ʒ����ͼƬ��Ϣ
	 * 
	 * @param schema
	 *            ��Ʒ����ͼƬ��Ϣ
	 * @return
	 * @throws Exception
	 */
	public boolean addGoodsInfoImage(GoodsInfoImageSchema schema)
			throws Exception {

		String sql = this.getSQLById(mysqltype.addGoodsInfoImage);
		Object[] paras = { schema.getN_goods_id(), schema.getN_shop_id(),
				schema.getC_img_url(), schema.getN_img_type() };
		return this.executeBySql(sql, paras);
	}
	

//	/**
//	 * ��ѯ����ͼƬ
//	 * 
//	 * @param shopId
//	 *            ����ID
//	 * @param url
//	 *            ͼƬ��ַ
//	 * @return
//	 * @throws Exception
//	 */
//	public GoodsInfoImageSchema getGoodsInfoImageByUrl(String shopId,
//			String imgType, String imgName) throws Exception {
//		String sql = this.getSQLById(mysqltype.getGoodsInfoImageByshopId);
//		sql = sql + " and c_img_url like '%" + imgName + "%'";
//		String[] paras = new String[] { shopId, imgType };
//		return this.queryObjectBySql(sql, GoodsInfoImageSchema.class, paras);
//	}

//	/**
//	 * ��ѯ����ͼƬ
//	 * 
//	 * @param shopId
//	 *            ����ID
//	 * @param imgType
//	 *            ͼƬ����
//	 * @return
//	 * @throws Exception
//	 */
//	public List<GoodsInfoImageSchema> getGoodsInfoImageByshopId(String shopId,
//			String imgType) throws Exception {
//		String sql = this.getSQLById(mysqltype.getGoodsInfoImageByshopId);
//		String[] paras = new String[] { shopId, imgType };
//		return this.queryListBySql(sql, GoodsInfoImageSchema.class, paras);
//	}

//	/**
//	 * ɾ����Ʒ����ͼƬ��Ϣ
//	 * 
//	 * @param shopId
//	 *            ����ID
//	 * @param goodsId
//	 *            ��ƷID
//	 * @param url
//	 *            ͼƬ��ַ
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean deleteGoodsInfoImageByUrl(String shopId, String imgName)
//			throws Exception {
//
//		String sql = this.getSQLById(mysqltype.deleteGoodsInfoImage);
//		sql = sql + " and c_img_url like '%" + imgName + "%' and n_img_type=1";
//		Object[] paras = { shopId };
//		return this.executeBySql(sql, paras);
//	}

	/**
	 * ɾ����Ʒ����ͼƬ��Ϣ
	 * 
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsInfoImage(String shopId) throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoodsInfoImage);
		Object[] paras = { shopId };
		return this.executeBySql(sql, paras);
	}

	/**
	 * ɾ����Ʒ����ͼƬ��Ϣ
	 * 
	 * @param goodsId
	 *            ����ID
	 * @param shopId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsInfoImageById(String goodsId, String shopId)
			throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoodsInfoImageById);
		Object[] paras = { goodsId, shopId };
		return this.executeBySql(sql, paras);
	}

	/**
	 * ���ӱ�׼��Ʒ��Ŀ��Ϣ
	 * 
	 * @param schema
	 *            ��׼��Ʒ��Ŀ��Ϣ
	 * @return
	 * @throws Exception
	 */
	public boolean addTaobaoCfgSysCategorySchema(TaobaoCfgSysCategorySchema schema)
			throws Exception {

		String sql = this.getSQLById(mysqltype.addTaobaoCfgSysCategorySchema);
		Object[] paras = { schema.getN_category_id(), schema.getN_category_id_sec(),schema.getN_parent_id(),
				schema.getN_parent_id_sec(),
				schema.getC_name(), schema.getN_level(),
				schema.getT_create_time() };
		return this.executeBySql(sql, paras);
	}

	/**
	 * ��׼��Ʒ��Ŀ��Ϣ
	 * 
	 * @param catId
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public CfgSysCategorySchema getTaobaoCfgSysCategorySchemaByCatId(String catId)
			throws Exception {

		return this.queryByIdWithCache(Table.TB_TAOBAO_CFG_SYS_CATEGORY,
				"n_category_id", null, catId, CfgSysCategorySchema.class);
	}

//	/**
//	 * ��׼��Ʒ��Ŀ��Ϣ
//	 * 
//	 * @param pCatId
//	 *            ������ID
//	 * @return
//	 * @throws Exception
//	 */
//	public CfgSysCategorySchema getCfgSysCategorySchemaByPcatId(String pCatId)
//			throws Exception {
//
//		return this.queryByIdWithCache(Table.TB_TAOBAO_CFG_SYS_CATEGORY,
//				"n_parent_id", null, pCatId, CfgSysCategorySchema.class);
//	}

	/**
	 * ������Ʒ����
	 * 
	 * @param schema
	 *            ��Ʒ������Ϣ
	 * @return
	 * @throws Exception
	 */
	public boolean addGoodsSyncRecord(GoodsSyncRecordSchema schema)
			throws Exception {

		String sql = this.getSQLById(mysqltype.addGoodsSyncRecord);
		Object[] paras = { schema.getN_shop_id(), schema.getN_task_id(),
				schema.getN_goods_id(), schema.getN_num()  };
		return this.executeBySql(sql, paras);
	}

	/**
	 * ������ȡ����
	 * 
	 * @param schema
	 *            ��ȡ������Ϣ
	 * @return
	 * @throws Exception
	 */
	public boolean addCatchRecord(CatchRecordSchema schema) throws Exception {

		String sql = this.getSQLById(mysqltype.addCatchRecord);
		Object[] paras = { schema.getN_shop_id(), schema.getN_sync_type(),
				schema.getN_task_id(), schema.getC_url(),
				schema.getT_update_time() };
		return this.executeBySql(sql, paras);
	}

	/**
	 * �޸���ȡ����
	 * 
	 * @param task_id
	 *            ����ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteCatchRecord(String task_id) throws Exception {

		String sql = this.getSQLById(mysqltype.deleteCatchRecord);
		Object[] paras = { task_id };
		return this.executeBySql(sql, paras);
	}

//	/**
//	 * ����������ѯ��ȡ������Ϣ
//	 * 
//	 * @param shopId
//	 *            ����ID
//	 * @param status
//	 *            ����״̬
//	 * @throws Exception
//	 */
//	public List<CatchRecordSchema> getCatchRecordByStatus(String status)
//			throws Exception {
//
//		String sql = this.getSQLById(mysqltype.getCatchRecordByStatus);
//		String[] paras = new String[] { status };
//		return this.queryListBySql(sql, CatchRecordSchema.class, paras);
//
//	}

//	/**
//	 * ��ѯ���е��̱�
//	 * 
//	 * @return
//	 * @throws Exception
//	 */
//	public List<ShopTaobaoSchema> getShopTaobaoSchema() throws Exception {
//
//		String sql = this.getSQLById(mysqltype.getShopTaobao);
//		return this.queryListBySql(sql, ShopTaobaoSchema.class, null);
//
//	}
	
	/**
	 * ����shopId��ѯ�õ��̵��й�������Ϣ�б�
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	public List<ShopCfgSchema> getShopCfgList(String shopId) throws Exception{
		String sql = this.getSQLById(mysqltype.getShopCfgList);
		String[] param = new String[]{shopId};
		return this.queryListBySqlWithCache(sql, param, null, ShopCfgSchema.class);
	}

	// ��sql
	private String getSQLById(mysqltype type) throws Exception {

		return SQLCode.getInstance().getSQLStatement(
				"com.work.taobao.dao.CommodityDAO." + type.toString());
	}

}
