package com.work.commodity.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.common.db.DbSchema;
import com.common.db.SQLCode;
import com.common.log.proxy.JLogger;
import com.common.log.proxy.LoggerFactory;
import com.framework.base.QueryTemplate;
import com.framework.util.StringUtils;
import com.work.commodity.schema.CatchRecordSchema;
import com.work.commodity.schema.CfgSysCategorySchema;
import com.work.commodity.schema.CookieSchema;
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
import com.work.commodity.table.Table;
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
		addShop, updateShopById, updateShopByUrl, updateShopByDesc, addGoods, addGoodsById, updateGoods, updateGoodsSell, updateGoodsArtNo, updateGoodsName, 
		deleteGoods, deleteGoodsById, updateGoodsByLongPic, getGoodsByformId, getGoodsByshopId, updateGoodsSkuList, updateGoodsSkuInfo, getGoodsSkuInfoBygoodsId, 
		updateGoodsByPayType, addGoodsDefaultExpress, deleteGoodsDefaultExpress, deleteGoodsDefaultExpressById, addGoodsExtendsProperty, deleteGoodsExtendsProperty, 
		deleteGoodsExtendsPropertyById, addGoodsSkuList, addGoodsSkuInfo, updateGoodsSkuInfoById, deleteGoodsSkuList, deleteGoodsSkuInfo, deleteGoodsSkuListById, 
		deleteGoodsSkuInfoById, getGoodsSkuListBygId, addGoodsPriceSection, deleteGoodsPriceSection, deleteGoodsPriceSectionById, addGoodsImage, addGoodsInfoImage, 
		getGoodsInfoImageByshopId, deleteGoodsImage, deleteGoodsImageById, deleteGoodsInfoImage, deleteGoodsInfoImageByUrl, deleteGoodsInfoImageById, addShopAli, 
		updateShopAli, updateShopAliById, addCfgSysCategorySchema, addGoodsSyncRecord, addCatchRecord, deleteCatchRecord, getCatchRecordByStatus, getShopAli,
		getShopCfgList
	}

	/**
	 * 查询店铺基本信息
	 * 
	 * @param shopId
	 *            店铺ID
	 * @return
	 * @throws Exception
	 */
	public ShopAliSchema getShopAliById(String shopId) throws Exception {

		return this.queryByIdWithCache(Table.TB_SHOP_ALI, "n_shop_id", null,
				shopId, ShopAliSchema.class);
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

	/**
	 * 修改商家信息
	 * 
	 * @param schema
	 *            商家信息
	 * @return
	 * @throws Exception
	 */
	public boolean updateShopById(ShopSchema schema) throws Exception {

		String sql = this.getSQLById(mysqltype.updateShopById);
		Object[] paras = { schema.getC_shop_name(), schema.getC_company_name(),
				schema.getN_shop_type(), schema.getC_location(),
				schema.getC_contact_name(), schema.getC_contact_mobile(),
				schema.getC_contact_phone(), schema.getN_shop_id() };
		return this.executeBySql(sql, paras);
	}

	/**
	 * 修改商家信息
	 * 
	 * @param htmlUrl
	 *            触店首页地址
	 * @param shopId
	 *            店铺ID
	 * @return
	 * @throws Exception
	 */
	public boolean updateShopByUrl(String shopId, String htmlUrl)
			throws Exception {

		String sql = this.getSQLById(mysqltype.updateShopByUrl);
		Object[] paras = { htmlUrl, shopId };
		return this.executeBySql(sql, paras);
	}

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

	/**
	 * 根据店铺id查询
	 * 
	 * @param sellerid
	 *            店铺id
	 * @return
	 * @throws Exception
	 */
	public ShopSchema getShopByShopId(String sellerid) throws Exception {

		return this.queryByIdWithCache(Table.TB_SHOP, "n_shop_id", null,
				sellerid, ShopSchema.class);
	}

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
		// (CASE WHEN n_third_total_sell>10000 THEN 10000*0.1 ELSE
		// n_third_total_sell*0.1 END)+200
		int n_total_sell = 200;// 转换为触店的销量
		if (StringUtils.isNotEmpty(schema.getN_third_total_sell())) {
			int n_third_total_sell = Integer.parseInt(schema
					.getN_third_total_sell());// 阿里销量
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
					schema.getN_third_total_sell(), schema.getN_total_sell(),
					schema.getC_goods_state(), schema.getC_tp_goods_url(),
					schema.getC_art_no() };
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
					schema.getN_third_total_sell(), schema.getN_total_sell(),
					schema.getC_goods_state(), schema.getC_tp_goods_url(),
					schema.getC_art_no() };
			return this.executeBySql(sql, paras);
		}

	}

	public boolean addGoodsList(GoodsSchema goods,
			GoodsDefaultExpressSchema express,
			List<GoodsExtendsPropertySchema> propertyList,
			List<GoodsSkuInfoSchema> skuInfoList,
			List<GoodsPriceSectionSchema> sectionList,
			List<GoodsImageSchema> imageList,
			List<GoodsInfoImageSchema> infoImageList) throws Exception {
		// (CASE WHEN n_third_total_sell>10000 THEN 10000*0.1 ELSE
		// n_third_total_sell*0.1 END)+200
		int n_total_sell = 200;// 转换为触店的销量
		if (StringUtils.isNotEmpty(goods.getN_third_total_sell())) {
			int n_third_total_sell = Integer.parseInt(goods
					.getN_third_total_sell());// 阿里销量
			if (n_third_total_sell > 10000) {
				n_total_sell += 1000;
			} else {
				n_total_sell += n_third_total_sell * 0.1;
			}
		}
		goods.setN_total_sell(n_total_sell + "");
		List<DbSchema> dbList = new ArrayList<DbSchema>();
		String goodsSql = this.getSQLById(mysqltype.addGoods);
		Object[] goodsParas = { goods.getN_goods_id(), goods.getN_goods_id(),
				goods.getC_third_platform_id(),
				goods.getN_third_platform_type(), goods.getN_shop_id(),
				goods.getN_shop_id(), goods.getN_sys_cid(),
				goods.getN_sys_parent_cid(), goods.getC_goods_name(),
				goods.getN_express_pay_type(), goods.getC_goods_detail_desc(),
				goods.getC_tp_order_url(), goods.getT_create_time(),
				goods.getT_last_update_time(), goods.getN_weight(),
				goods.getN_sell_price(), goods.getC_price(),
				goods.getC_goods_unit(), goods.getN_total_stock(),
				goods.getN_third_total_sell(), goods.getN_total_sell(),
				goods.getC_goods_state(), goods.getC_tp_goods_url(),
				goods.getC_art_no() };
		DbSchema goodsdb = new DbSchema();
		goodsdb.setSql(goodsSql);
		goodsdb.setParas(goodsParas);
		dbList.add(goodsdb);// 添加商品

		String expressSql = this.getSQLById(mysqltype.addGoodsDefaultExpress);
		Object[] expressParas = { express.getN_goods_id(),
				express.getC_delivery_location(),
				express.getC_receive_location(), express.getN_price(),
				express.getN_shop_id() };
		DbSchema expressdb = new DbSchema();
		expressdb.setSql(expressSql);
		expressdb.setParas(expressParas);
		dbList.add(expressdb);// 添加物流

		String propertySql = this.getSQLById(mysqltype.addGoodsExtendsProperty);
		for (GoodsExtendsPropertySchema schema : propertyList) {
			Object[] paras = { schema.getN_goods_id(),
					schema.getC_prop_key_label(), schema.getC_prop_value(),
					schema.getN_shop_id() };
			DbSchema db = new DbSchema();
			db.setSql(propertySql);
			db.setParas(paras);
			dbList.add(db);// 添加扩展属性
		}

		String skuinfoSql = this.getSQLById(mysqltype.addGoodsSkuInfo);
		for (GoodsSkuInfoSchema schema : skuInfoList) {
			Object[] paras = { schema.getC_sku_id(), schema.getN_goods_id(),
					schema.getN_shop_id(), schema.getC_sku_list_id(),
					schema.getN_stock_num(), 0, schema.getN_price(),
					schema.getC_sku_desc(), schema.getN_price_set_flag() };
			DbSchema db = new DbSchema();
			db.setSql(skuinfoSql);
			db.setParas(paras);
			dbList.add(db);// 添加skuinfo
		}

		String priceSql = this.getSQLById(mysqltype.addGoodsPriceSection);
		for (GoodsPriceSectionSchema schema : sectionList) {
			Object[] paras = { schema.getN_goods_id(), schema.getN_num_start(),
					schema.getN_num_end(), schema.getN_price(),
					schema.getN_shop_id(), schema.getN_price_set_flag() };
			DbSchema db = new DbSchema();
			db.setSql(priceSql);
			db.setParas(paras);
			dbList.add(db);// 添加价格区间
		}

		String imageSql = this.getSQLById(mysqltype.addGoodsImage);
		for (GoodsImageSchema schema : imageList) {
			Object[] paras = { schema.getN_goods_id(),
					schema.getN_img_category(), schema.getC_img_url(),
					schema.getN_width(), schema.getN_height(),
					schema.getN_shop_id() };
			DbSchema db = new DbSchema();
			db.setSql(imageSql);
			db.setParas(paras);
			dbList.add(db);// 添加图片
		}

		String infoImageSql = this.getSQLById(mysqltype.addGoodsInfoImage);
		for (GoodsInfoImageSchema schema : infoImageList) {
			Object[] paras = { schema.getN_goods_id(), schema.getN_shop_id(),
					schema.getC_img_url(), schema.getN_img_type() };
			DbSchema db = new DbSchema();
			db.setSql(infoImageSql);
			db.setParas(paras);
			dbList.add(db);// 添加详情图片
		}
		return this.executeBySqlList(dbList);
	}

	public boolean updateGoodsList(GoodsSchema goods,
			List<GoodsExtendsPropertySchema> propertyList,
			List<GoodsSkuInfoSchema> skuInfoList,
			List<GoodsSkuInfoSchema> skuInfoUpdateList,
			List<GoodsPriceSectionSchema> sectionList,
			List<GoodsImageSchema> imageList,
			List<GoodsInfoImageSchema> infoImageList) throws Exception {
		List<DbSchema> dbList = new ArrayList<DbSchema>();

		String deletePropertysql = this
				.getSQLById(mysqltype.deleteGoodsExtendsPropertyById);
		Object[] deletePropertyparas = { goods.getN_goods_id(),
				goods.getN_shop_id() };
		DbSchema deletePropertydb = new DbSchema();
		deletePropertydb.setSql(deletePropertysql);
		deletePropertydb.setParas(deletePropertyparas);
		dbList.add(deletePropertydb);// 删除扩展属性

		String deleteImagesql = this.getSQLById(mysqltype.deleteGoodsImageById);
		Object[] deleteImageparas = { goods.getN_goods_id(),
				goods.getN_shop_id() };
		DbSchema deleteImagedb = new DbSchema();
		deleteImagedb.setSql(deleteImagesql);
		deleteImagedb.setParas(deleteImageparas);
		dbList.add(deleteImagedb);// 删除图片

		String deleteInfoImagesql = this
				.getSQLById(mysqltype.deleteGoodsInfoImageById);
		Object[] deleteInfoImageparas = { goods.getN_goods_id(),
				goods.getN_shop_id() };
		DbSchema deleteInfoImagedb = new DbSchema();
		deleteInfoImagedb.setSql(deleteInfoImagesql);
		deleteInfoImagedb.setParas(deleteInfoImageparas);
		dbList.add(deleteInfoImagedb);// 删除详情图片

		String deleteectionsql = this
				.getSQLById(mysqltype.deleteGoodsPriceSectionById);
		Object[] deleteectionparas = { goods.getN_goods_id(),
				goods.getN_shop_id() };
		DbSchema deleteectiondb = new DbSchema();
		deleteectiondb.setSql(deleteectionsql);
		deleteectiondb.setParas(deleteectionparas);
		dbList.add(deleteectiondb);// 删除价格区间

		String goodsSql = this.getSQLById(mysqltype.updateGoodsName);
		Object[] goodsParas = { goods.getC_goods_name(), goods.getN_weight(),
				goods.getN_goods_id() };
		DbSchema goodsdb = new DbSchema();
		goodsdb.setSql(goodsSql);
		goodsdb.setParas(goodsParas);
		dbList.add(goodsdb);// 修改商品

		String goodsInfosql = this.getSQLById(mysqltype.updateGoods);
		Object[] goodsInfoparas = { goods.getN_sell_price(),
				goods.getN_total_stock(), goods.getC_price(),
				goods.getC_goods_state(), goods.getC_art_no(),
				goods.getN_goods_id() };
		DbSchema goodsinfodb = new DbSchema();
		goodsinfodb.setSql(goodsInfosql);
		goodsinfodb.setParas(goodsInfoparas);
		dbList.add(goodsinfodb);// 修改商品

		String propertySql = this.getSQLById(mysqltype.addGoodsExtendsProperty);
		for (GoodsExtendsPropertySchema schema : propertyList) {
			Object[] paras = { schema.getN_goods_id(),
					schema.getC_prop_key_label(), schema.getC_prop_value(),
					schema.getN_shop_id() };
			DbSchema db = new DbSchema();
			db.setSql(propertySql);
			db.setParas(paras);
			dbList.add(db);// 添加扩展属性
		}

		String skuinfoSql = this.getSQLById(mysqltype.addGoodsSkuInfo);
		for (GoodsSkuInfoSchema schema : skuInfoList) {
			Object[] paras = { schema.getC_sku_id(), schema.getN_goods_id(),
					schema.getN_shop_id(), schema.getC_sku_list_id(),
					schema.getN_stock_num(), 0, schema.getN_price(),
					schema.getC_sku_desc(), schema.getN_price_set_flag() };
			DbSchema db = new DbSchema();
			db.setSql(skuinfoSql);
			db.setParas(paras);
			dbList.add(db);// 添加skuinfo
		}

		String skuinfoUpdateSql = this
				.getSQLById(mysqltype.updateGoodsSkuInfoById);
		for (GoodsSkuInfoSchema schema : skuInfoUpdateList) {
			Object[] paras = { schema.getN_stock_num(), schema.getN_price(),
					schema.getN_id() };
			DbSchema db = new DbSchema();
			db.setSql(skuinfoUpdateSql);
			db.setParas(paras);
			dbList.add(db);// 修改skuinfo
		}

		String priceSql = this.getSQLById(mysqltype.addGoodsPriceSection);
		for (GoodsPriceSectionSchema schema : sectionList) {
			Object[] paras = { schema.getN_goods_id(), schema.getN_num_start(),
					schema.getN_num_end(), schema.getN_price(),
					schema.getN_shop_id(), schema.getN_price_set_flag() };
			DbSchema db = new DbSchema();
			db.setSql(priceSql);
			db.setParas(paras);
			dbList.add(db);// 添加价格区间
		}

		String imageSql = this.getSQLById(mysqltype.addGoodsImage);
		for (GoodsImageSchema schema : imageList) {
			Object[] paras = { schema.getN_goods_id(),
					schema.getN_img_category(), schema.getC_img_url(),
					schema.getN_width(), schema.getN_height(),
					schema.getN_shop_id() };
			DbSchema db = new DbSchema();
			db.setSql(imageSql);
			db.setParas(paras);
			dbList.add(db);// 添加图片
		}

		String infoImageSql = this.getSQLById(mysqltype.addGoodsInfoImage);
		for (GoodsInfoImageSchema schema : infoImageList) {
			Object[] paras = { schema.getN_goods_id(), schema.getN_shop_id(),
					schema.getC_img_url(), schema.getN_img_type() };
			DbSchema db = new DbSchema();
			db.setSql(infoImageSql);
			db.setParas(paras);
			dbList.add(db);// 添加详情图片
		}
		return this.executeBySqlList(dbList);
	}

	public boolean updateGoodsInfoImageList(GoodsSchema goods,
			List<GoodsInfoImageSchema> infoImageList) throws Exception {
		List<DbSchema> dbList = new ArrayList<DbSchema>();

		String deleteInfoImagesql = this
				.getSQLById(mysqltype.deleteGoodsInfoImageById);
		Object[] deleteInfoImageparas = { goods.getN_goods_id(),
				goods.getN_shop_id() };
		DbSchema deleteInfoImagedb = new DbSchema();
		deleteInfoImagedb.setSql(deleteInfoImagesql);
		deleteInfoImagedb.setParas(deleteInfoImageparas);
		dbList.add(deleteInfoImagedb);// 删除详情图片

		String infoImageSql = this.getSQLById(mysqltype.addGoodsInfoImage);
		for (GoodsInfoImageSchema schema : infoImageList) {
			Object[] paras = { schema.getN_goods_id(), schema.getN_shop_id(),
					schema.getC_img_url(), schema.getN_img_type() };
			DbSchema db = new DbSchema();
			db.setSql(infoImageSql);
			db.setParas(paras);
			dbList.add(db);// 添加详情图片
		}
		return this.executeBySqlList(dbList);
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

	/**
	 * 修改商家商品信息
	 * 
	 * @param goodsId
	 *            商品ID
	 * @param thirdTotalSell
	 *            销量
	 * @return
	 * @throws Exception
	 */
	public boolean updateGoodsSell(String goodsId, String thirdTotalSell)
			throws Exception {
		// (CASE WHEN n_third_total_sell>10000 THEN 10000*0.1 ELSE
		// n_third_total_sell*0.1 END)+200
		int n_total_sell = 200;// 转换为触店的销量
		if (StringUtils.isNotEmpty(thirdTotalSell)) {
			int n_third_total_sell = Integer.parseInt(thirdTotalSell);// 阿里销量
			if (n_third_total_sell > 10000) {
				n_total_sell += 1000;
			} else {
				n_total_sell += n_third_total_sell * 0.1;
			}
		}
		String sql = this.getSQLById(mysqltype.updateGoodsSell);
		Object[] paras = { thirdTotalSell, n_total_sell, goodsId };
		return this.executeBySql(sql, paras);
	}

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

	/**
	 * 根据店铺ID查询
	 * 
	 * @param shopId
	 *            店铺ID
	 * @return
	 * @throws Exception
	 */
	public List<GoodsSchema> getGoodsByshopId(String shopId) throws Exception {
		String sql = this.getSQLById(mysqltype.getGoodsByshopId);
		String[] paras = new String[] { shopId };
		return this.queryListBySql(sql, GoodsSchema.class, paras);
	}

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

//	/**
//	 * 增加商品扩展属性
//	 * 
//	 * @param schema
//	 *            商品扩展属性信息
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean addGoodsExtendsProperty(GoodsExtendsPropertySchema schema)
//			throws Exception {
//
//		String sql = this.getSQLById(mysqltype.addGoodsExtendsProperty);
//		Object[] paras = { schema.getN_goods_id(),
//				schema.getC_prop_key_label(), schema.getC_prop_value(),
//				schema.getN_shop_id() };
//		return this.executeBySql(sql, paras);
//	}

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
	public boolean addGoodsSkuList(String goodsId, List<GoodsSkuListSchema> skuLists,
			Map<String, String> skuListMap) throws Exception {
		List<DbSchema> dbList = new ArrayList<DbSchema>();
		String sql = this.getSQLById(mysqltype.addGoodsSkuList);
		for (GoodsSkuListSchema schema : skuLists) {
			Object[] paras = { schema.getN_goods_id(), schema.getN_shop_id(),
					schema.getC_sku_name(), schema.getC_sku_prop(),
					schema.getN_sku_level(), schema.getC_sku_desc() };
			DbSchema db = new DbSchema();
			db.setSql(sql);
			db.setParas(paras);
			dbList.add(db);
		}
		if (skuListMap != null) {
			String updatesql = this.getSQLById(mysqltype.updateGoodsSkuList);
			String updateInfosql = this.getSQLById(mysqltype.updateGoodsSkuInfo);
			for (String str : skuListMap.keySet()) {
				Object[] paras = { skuListMap.get(str) };
				DbSchema db = new DbSchema();
				db.setSql(updatesql);
				db.setParas(paras);
				dbList.add(db);

				Object[] parasInfo = { goodsId, "%"+skuListMap.get(str)+"%" };
				DbSchema dbInfo = new DbSchema();
				dbInfo.setSql(updateInfosql);
				dbInfo.setParas(parasInfo);
				dbList.add(dbInfo);
			}
		}
		return this.executeBySqlList(dbList);
	}

//	/**
//	 * 增加商品SKU详情信息
//	 * 
//	 * @param schema
//	 *            商品SKU详情信息
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean addGoodsSkuInfo(GoodsSkuInfoSchema schema) throws Exception {
//
//		String sql = this.getSQLById(mysqltype.addGoodsSkuInfo);
//		Object[] paras = { schema.getC_sku_id(), schema.getN_goods_id(),
//				schema.getN_shop_id(), schema.getC_sku_list_id(),
//				schema.getN_stock_num(), 0, schema.getN_price(),
//				schema.getC_sku_desc() };
//		return this.executeBySql(sql, paras);
//	}

//	/**
//	 * 修改商品SKU详情信息
//	 * 
//	 * @param stockNum库存
//	 * @param price价格
//	 * @param id
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean updateGoodsSkuInfoById(String stockNum, String price,
//			String id) throws Exception {
//
//		String sql = this.getSQLById(mysqltype.updateGoodsSkuInfoById);
//		Object[] paras = { stockNum, price, id };
//		return this.executeBySql(sql, paras);
//	}

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

//	/**
//	 * 增加商品价格区间信息
//	 * 
//	 * @param schema
//	 *            商品价格区间信息
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean addGoodsPriceSection(GoodsPriceSectionSchema schema)
//			throws Exception {
//
//		String sql = this.getSQLById(mysqltype.addGoodsPriceSection);
//		Object[] paras = { schema.getN_goods_id(), schema.getN_num_start(),
//				schema.getN_num_end(), schema.getN_price(),
//				schema.getN_shop_id() };
//		return this.executeBySql(sql, paras);
//	}

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

//	/**
//	 * 
//	 * 增加商品图片信息
//	 * 
//	 * @param schema
//	 *            商品图片信息
//	 * @return
//	 * @throws Exception
//	 */
//	public boolean addGoodsImage(GoodsImageSchema schema) throws Exception {
//
//		String sql = this.getSQLById(mysqltype.addGoodsImage);
//		Object[] paras = { schema.getN_goods_id(), schema.getN_img_category(),
//				schema.getC_img_url(), schema.getN_width(),
//				schema.getN_height(), schema.getN_shop_id() };
//		return this.executeBySql(sql, paras);
//	}

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
	public boolean addShopAli(String shopId, String url) throws Exception {
		ShopAliSchema schema = this.getShopAliById(shopId);
		if (schema == null) {
			String sql = this.getSQLById(mysqltype.addShopAli);
			Object[] paras = { shopId, url };
			return this.executeBySql(sql, paras);
		} else {
			String sql = this.getSQLById(mysqltype.updateShopAliById);
			Object[] paras = { url, shopId };
			return this.executeBySql(sql, paras);
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
	public boolean updateShopAli(ShopAliSchema schema) throws Exception {

		String sql = this.getSQLById(mysqltype.updateShopAli);
		Object[] paras = { schema.getC_company_product(),
				schema.getC_company_industry(), schema.getC_company_model(),
				schema.getC_company_work(), schema.getC_company_capital(),
				schema.getC_company_founding_time(),
				schema.getC_company_address(), schema.getC_company_type(),
				schema.getC_company_person(), schema.getC_company_no(),
				schema.getC_company_way(), schema.getC_company_technology(),
				schema.getC_company_number(), schema.getC_company_area(),
				schema.getC_company_region(), schema.getC_company_custom(),
				schema.getC_company_output(), schema.getC_company_turnover(),
				schema.getC_company_exports(), schema.getC_company_brand(),
				schema.getC_company_url(), schema.getC_qualifications1(),
				schema.getC_qualifications2(), schema.getC_qualifications3(),
				schema.getC_company_desc(), schema.getC_platform_id(),
				schema.getC_ww_number(), schema.getC_trade_number(),
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

	/**
	 * 查询详情图片
	 * 
	 * @param shopId
	 *            店铺ID
	 * @param url
	 *            图片地址
	 * @return
	 * @throws Exception
	 */
	public GoodsInfoImageSchema getGoodsInfoImageByUrl(String shopId,
			String imgType, String imgName) throws Exception {
		String sql = this.getSQLById(mysqltype.getGoodsInfoImageByshopId);
		sql = sql + " and c_img_url like '%" + imgName + "%'";
		String[] paras = new String[] { shopId, imgType };
		return this.queryObjectBySql(sql, GoodsInfoImageSchema.class, paras);
	}

	/**
	 * 查询详情图片
	 * 
	 * @param shopId
	 *            店铺ID
	 * @param imgType
	 *            图片类型
	 * @return
	 * @throws Exception
	 */
	public List<GoodsInfoImageSchema> getGoodsInfoImageByshopId(String shopId,
			String imgType) throws Exception {
		String sql = this.getSQLById(mysqltype.getGoodsInfoImageByshopId);
		String[] paras = new String[] { shopId, imgType };
		return this.queryListBySql(sql, GoodsInfoImageSchema.class, paras);
	}

	/**
	 * 删除商品详情图片信息
	 * 
	 * @param shopId
	 *            卖家ID
	 * @param goodsId
	 *            商品ID
	 * @param url
	 *            图片地址
	 * @return
	 * @throws Exception
	 */
	public boolean deleteGoodsInfoImageByUrl(String shopId, String imgName)
			throws Exception {

		String sql = this.getSQLById(mysqltype.deleteGoodsInfoImage);
		sql = sql + " and c_img_url like '%" + imgName + "%' and n_img_type=1";
		Object[] paras = { shopId };
		return this.executeBySql(sql, paras);
	}

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
	public boolean addCfgSysCategorySchema(CfgSysCategorySchema schema)
			throws Exception {

		String sql = this.getSQLById(mysqltype.addCfgSysCategorySchema);
		Object[] paras = { schema.getN_category_id(), schema.getN_parent_id(),
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
	public CfgSysCategorySchema getCfgSysCategorySchemaByCatId(String catId)
			throws Exception {

		return this.queryByIdWithCache(Table.TB_CFG_SYS_CATEGORY,
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
//		return this.queryByIdWithCache(Table.TB_CFG_SYS_CATEGORY,
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
				schema.getN_goods_id(), schema.getN_num() };
		return this.executeBySql(sql, paras);
	}

	/**
	 * 商品上新总数
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getGoodsNewNum(String taskId, int num) throws Exception {
		String sql = "SELECT COUNT(1) FROM tb_goods_sync_record WHERE n_task_id="
				+ taskId + " and n_num=" + num;
		return Integer.parseInt(this.queryStrBySql(sql, null));

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

	/**
	 * 根据条件查询爬取任务信息
	 * 
	 * @param shopId
	 *            店铺ID
	 * @param status
	 *            任务状态
	 * @throws Exception
	 */
	public List<CatchRecordSchema> getCatchRecordByStatus(String status)
			throws Exception {

		String sql = this.getSQLById(mysqltype.getCatchRecordByStatus);
		String[] paras = new String[] { status };
		return this.queryListBySql(sql, CatchRecordSchema.class, paras);

	}

	/**
	 * 查询所有店铺表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ShopAliSchema> getShopAliSchema() throws Exception {

		String sql = this.getSQLById(mysqltype.getShopAli);
		return this.queryListBySql(sql, ShopAliSchema.class, null);

	}

	/**
	 * 查询所有cookie
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<CookieSchema> getCookieList() throws Exception {
		String sql = "select * from t_cookie";
		return this.queryListBySql(sql, CookieSchema.class);

	}
	
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
				"com.work.commodity.dao.CommodityDAO." + type.toString());
	}

}
