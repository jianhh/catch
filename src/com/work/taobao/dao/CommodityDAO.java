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
 * 商家商品信息dao
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
	 * 查询店铺基本信息
	 * 
	 * @param shopId
	 *            店铺ID
	 * @return
	 * @throws Exception
	 */
	
	public ShopTaobaoSchema getShopTaobaoById(String shopId) throws Exception {

		return this.queryByIdWithCache(Table.TB_SHOP_TAOBAO, "n_shop_id", null,
				shopId, ShopTaobaoSchema.class);
	}

	/**
	 * 查询店铺信息
	 * 
	 * @param shopId
	 *            店铺ID
	 * @return
	 * @throws Exception
	 */
	public ShopSchema getShopById(String shopId) throws Exception {

		return this.queryByIdWithCache(Table.TB_SHOP, "n_shop_id", null,
				shopId, ShopSchema.class);
	}

//	/**
//	 * 修改商家信息
//	 * 
//	 * @param schema
//	 *            商家信息
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
//	 * 修改商家信息
//	 * 
//	 * @param htmlUrl
//	 *            触店首页地址
//	 * @param shopId
//	 *            店铺ID
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
//	 * 修改商家信息
//	 * 
//	 * @param desc
//	 *            描述信息
//	 * @param shopId
//	 *            店铺ID
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
//	 * 根据店铺id查询
//	 * 
//	 * @param sellerid
//	 *            店铺id
//	 * @return
//	 * @throws Exception
//	 */
//	public ShopSchema getShopByShopId(String sellerid) throws Exception {
//
//		return this.queryByIdWithCache(Table.TB_SHOP, "n_shop_id", null,
//				sellerid, ShopSchema.class);
//	}

//	/**
//	 * 根据公司名称查询
//	 * 
//	 * @param companyName
//	 *            公司名称
//	 * @return
//	 * @throws Exception
//	 */
//	public ShopSchema getShopByShopName(String companyName) throws Exception {
//
//		return this.queryByIdWithCache(Table.TB_SHOP, "c_company_name", null,
//				companyName, ShopSchema.class);
//	}

//	/**
//	 * 根据店铺名称查询
//	 * 
//	 * @param cShopName
//	 *            店铺名称
//	 * @return
//	 * @throws Exception
//	 */
//	public ShopSchema getShopBycShopName(String cShopName) throws Exception {
//
//		return this.queryByIdWithCache(Table.TB_SHOP, "c_shop_name", null,
//				cShopName, ShopSchema.class);
//	}

	/**
	 * 增加商家商品信息
	 * 
	 * @param schema
	 *            商品信息
	 * @return
	 * @throws Exception
	 */
	public boolean addGoods(GoodsSchema schema) throws Exception {
		int n_total_sell = 200;// 转换为触店的销量
		if (StringUtils.isNotEmpty(schema.getN_third_total_sell())) {
			int n_third_total_sell = Integer.parseInt(schema
					.getN_third_total_sell());// 淘宝销量
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
	 * 删除商品信息
	 * 
	 * @param shopId
	 *            卖家ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoods(String shopId) throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoods);
		Object[] paras = { shopId };
		return this.executeBySql(sql, paras);
	}

	/**
	 * 删除商品信息
	 * 
	 * @param goodsId
	 *            商品ID
	 * @param shopId
	 *            店铺ID
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
//	 * 修改商家商品信息
//	 * 
//	 * @param goodsId
//	 *            商品ID
//	 * @param defaultPrice
//	 *            默认价格
//	 * @param totalStock
//	 *            总库存
//	 * @param price
//	 *            详情页展现的价格
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
//	 * 修改商家商品信息
//	 * 
//	 * @param goodsId
//	 *            商品ID
//	 * @param thirdTotalSell
//	 *            销量
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
	 * 修改商家商品信息
	 * 
	 * @param goodsId商品ID
	 * @param artNo货号
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
//	 * 修改商家商品信息
//	 * 
//	 * @param goodsId商品ID
//	 * @param name商品名称
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
//	 * 修改商家商品信息(运费承担方式)
//	 * 
//	 * @param goodsId
//	 *            商品ID
//	 * @param payType
//	 *            运费承担方式：1卖家，2买家， 3自取
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
//	 * 根据店铺ID查询
//	 * 
//	 * @param shopId
//	 *            店铺ID
//	 * @return
//	 * @throws Exception
//	 */
//	public List<GoodsSchema> getGoodsByshopId(String shopId) throws Exception {
//		String sql = this.getSQLById(mysqltype.getGoodsByshopId);
//		String[] paras = new String[] { shopId };
//		return this.queryListBySql(sql, GoodsSchema.class, paras);
//	}

	/**
	 * 根据店铺ID查询
	 * 
	 * @param shopId
	 *            店铺ID
	 * @return
	 * @throws Exception
	 */
	public GoodsSchema getGoodsSchemaByshopId(String shopId) throws Exception {
		String sql = this.getSQLById(mysqltype.getGoodsByshopId);
		String[] paras = new String[] { shopId };
		return this.queryObjectBySql(sql, GoodsSchema.class, paras);
	}

	/**
	 * 根据第三方平台商品ID查询
	 * 
	 * @param shopId
	 *            店铺ID
	 * @param cThirdPlatformId
	 *            第三方平台商品ID
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
//	 * 增加默认物流表
//	 * 
//	 * @param schema
//	 *            默认物流信息
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
	 * 删除默认物流信息
	 * 
	 * @param shopId
	 *            卖家ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsDefaultExpress(String shopId) throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoodsDefaultExpress);
		Object[] paras = { shopId };
		return this.executeBySql(sql, paras);
	}

	/**
	 * 删除默认物流信息
	 * 
	 * @param goodsId
	 *            商品ID
	 * @param shopId
	 *            店铺ID
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
	 * 增加商品扩展属性
	 * 
	 * @param schema
	 *            商品扩展属性信息
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
	 * 删除商品扩展属性信息
	 * 
	 * @param shopId
	 *            卖家ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsExtendsProperty(String shopId) throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoodsExtendsProperty);
		Object[] paras = { shopId };
		return this.executeBySql(sql, paras);
	}

	/**
	 * 删除商品扩展属性信息
	 * 
	 * @param goodsId
	 *            商品ID
	 * @param shopId
	 *            店铺ID
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
	 * 根据商品ID查询
	 * 
	 * @param goodsId
	 *            商品ID
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
	 * 增加商品SKU列表信息
	 * 
	 * @param schema
	 *            商品SKU列表信息
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
	 * 增加商品SKU详情信息
	 * 
	 * @param schema
	 *            商品SKU详情信息
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
	 * 修改商品SKU详情信息
	 * 
	 * @param stockNum库存
	 * @param price价格
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
	 * 删除商品SKU列表信息
	 * 
	 * @param shopId
	 *            卖家ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsSkuList(String shopId) throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoodsSkuList);
		Object[] paras = { shopId };
		return this.executeBySql(sql, paras);
	}

	/**
	 * 删除商品SKU详细信息
	 * 
	 * @param shopId
	 *            卖家ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsSkuInfo(String shopId) throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoodsSkuInfo);
		Object[] paras = { shopId };
		return this.executeBySql(sql, paras);
	}

	/**
	 * 删除商品SKU列表信息
	 * 
	 * @param goodsId
	 *            商品ID
	 * @param shopId
	 *            店铺ID
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
	 * 删除商品SKU详细信息
	 * 
	 * @param goodsId
	 *            商品ID
	 * @param shopId
	 *            店铺ID
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
	 * 根据条件查询SKU列表信息
	 * 
	 * @param goodsId
	 *            商品ID
	 * @throws Exception
	 */
	public List<GoodsSkuListSchema> getGoodsSkuListBygId(String goodsId)
			throws Exception {

		String sql = this.getSQLById(mysqltype.getGoodsSkuListBygId);
		String[] paras = new String[] { goodsId };
		return this.queryListBySql(sql, GoodsSkuListSchema.class, paras);

	}

	/**
	 * 增加商品价格区间信息
	 * 
	 * @param schema
	 *            商品价格区间信息
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
	 * 删除商品价格区间信息
	 * 
	 * @param shopId
	 *            卖家ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsPriceSection(String shopId) throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoodsPriceSection);
		Object[] paras = { shopId };
		return this.executeBySql(sql, paras);
	}

	/**
	 * 删除商品价格区间信息
	 * 
	 * @param goodsId
	 *            商品ID
	 * @param shopId
	 *            店铺ID
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
	 * 增加商品图片信息
	 * 
	 * @param schema
	 *            商品图片信息
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
	 * 删除商品图片信息
	 * 
	 * @param shopId
	 *            卖家ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsImage(String shopId) throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoodsImage);
		Object[] paras = { shopId };
		return this.executeBySql(sql, paras);
	}

	/**
	 * 删除商品图片信息
	 * 
	 * @param goodsId
	 *            卖家ID
	 * @param shopId
	 *            店铺ID
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
	 * 增加商家基本信息
	 * 
	 * @param shopId
	 *            店铺ID
	 * @param url
	 *            店铺地址
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
	 * 修改商家基本信息
	 * 
	 * @param schema
	 *            商家基本信息
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
	 * 增加商品详情图片信息
	 * 
	 * @param schema
	 *            商品详情图片信息
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
//	 * 查询详情图片
//	 * 
//	 * @param shopId
//	 *            店铺ID
//	 * @param url
//	 *            图片地址
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
//	 * 查询详情图片
//	 * 
//	 * @param shopId
//	 *            店铺ID
//	 * @param imgType
//	 *            图片类型
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
//	 * 删除商品详情图片信息
//	 * 
//	 * @param shopId
//	 *            卖家ID
//	 * @param goodsId
//	 *            商品ID
//	 * @param url
//	 *            图片地址
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
	 * 删除商品详情图片信息
	 * 
	 * @param shopId
	 *            卖家ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsInfoImage(String shopId) throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoodsInfoImage);
		Object[] paras = { shopId };
		return this.executeBySql(sql, paras);
	}

	/**
	 * 删除商品详情图片信息
	 * 
	 * @param goodsId
	 *            卖家ID
	 * @param shopId
	 *            店铺ID
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
	 * 增加标准商品类目信息
	 * 
	 * @param schema
	 *            标准商品类目信息
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
	 * 标准商品类目信息
	 * 
	 * @param catId
	 *            分类ID
	 * @return
	 * @throws Exception
	 */
	public CfgSysCategorySchema getTaobaoCfgSysCategorySchemaByCatId(String catId)
			throws Exception {

		return this.queryByIdWithCache(Table.TB_TAOBAO_CFG_SYS_CATEGORY,
				"n_category_id", null, catId, CfgSysCategorySchema.class);
	}

//	/**
//	 * 标准商品类目信息
//	 * 
//	 * @param pCatId
//	 *            父分类ID
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
	 * 增加商品上新
	 * 
	 * @param schema
	 *            商品上新信息
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
	 * 增加爬取任务
	 * 
	 * @param schema
	 *            爬取任务信息
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
	 * 修改爬取任务
	 * 
	 * @param task_id
	 *            任务ID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteCatchRecord(String task_id) throws Exception {

		String sql = this.getSQLById(mysqltype.deleteCatchRecord);
		Object[] paras = { task_id };
		return this.executeBySql(sql, paras);
	}

//	/**
//	 * 根据条件查询爬取任务信息
//	 * 
//	 * @param shopId
//	 *            店铺ID
//	 * @param status
//	 *            任务状态
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
//	 * 查询所有店铺表
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
	 * 根据shopId查询该店铺的有关配置信息列表
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	public List<ShopCfgSchema> getShopCfgList(String shopId) throws Exception{
		String sql = this.getSQLById(mysqltype.getShopCfgList);
		String[] param = new String[]{shopId};
		return this.queryListBySqlWithCache(sql, param, null, ShopCfgSchema.class);
	}

	// 组sql
	private String getSQLById(mysqltype type) throws Exception {

		return SQLCode.getInstance().getSQLStatement(
				"com.work.taobao.dao.CommodityDAO." + type.toString());
	}

}
